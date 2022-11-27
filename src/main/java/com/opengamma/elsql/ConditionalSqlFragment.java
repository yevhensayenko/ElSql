/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.elsql;

/**
 * Representation of conditional SQL fragment.
 */
abstract class ConditionalSqlFragment extends ContainerSqlFragment {

  /**
   * The variable.
   */
  private final String _variable;
  /**
   * The value to match against.
   */
  private final String _matchValue;
  /**
   * The comparison sign. Either '=' or '!='.
   */
  private final String _comparisonSign;

  /**
   * Creates an instance.
   * 
   * @param variable  the variable to determine whether to include the AND on, not null
   * @param comparisonSign  either '=' or '!='
   * @param matchValue  the value to match, null to match on existence
   */
  ConditionalSqlFragment(String variable, String comparisonSign,  String matchValue) {
    _variable = extractVariableName(variable);
    _comparisonSign = comparisonSign;
    _matchValue = matchValue;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the variable.
   * 
   * @return the variable, not null
   */
  String getVariable() {
    return _variable;
  }

  /**
   * Gets the match value.
   * 
   * @return the match value, not null
   */
  String getMatchValue() {
    return _matchValue;
  }

  //-------------------------------------------------------------------------
  boolean isMatch(SqlParams params, int[] loopIndex) {
    String var = applyLoopIndex(_variable, loopIndex);
    if (params.contains(var) == false) {
      return false;
    }
    Object value = params.get(var);
    if (value == null) {
      return false;
    }
    if (_matchValue != null) {
      if ("!=".equals(_comparisonSign)) {
        return !_matchValue.equalsIgnoreCase(value.toString());
      }
      return _matchValue.equalsIgnoreCase(value.toString());
    }
    if (value instanceof Boolean) {
      return ((Boolean) value).booleanValue();
    }
    return true;
  }

  boolean endsWith(StringBuilder buf, String match) {
    String str = (buf.length() >= match.length() ? buf.substring(buf.length() - match.length()) : "");
    return str.equals(match);
  }

  //-------------------------------------------------------------------------
  @Override
  public String toString() {
    return getClass().getSimpleName() + ":" + _variable + " " + getFragments();
  }

}
