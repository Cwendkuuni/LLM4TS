package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeNative;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Given
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = new Node();
    conditionNode.setType(JSTypeNative.BOOLEAN_TYPE);

    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();

    // Case 1: isDef
    Node isDefCondition = new Node();
    isDefCondition.setFirstChild(new Node("goog"));
    isDefCondition.setLastChild(new Node("isDef"));
    Node isDefParameter = new Node();
    isDefParameter.setType(JSTypeNative.OBJECT_TYPE);
    Node isDefType = interpreter.getType(isDefCondition, blindScope).getReturnType();
    TypeRestriction isDefRestriction = new TypeRestriction(isDefType, true);

    assertEquals(JSTypeNative.getBooleanType(), interpreter.restrictParameter(
        isDefParameter, isDefType, blindScope, interpreter.restricters.get("isDef"), true).getPreciseType());

    // Case 2: isDef, null
    Node isDefNullCondition = new Node();
    isDefNullCondition.setFirstChild(new Node("goog"));
    isDefNullCondition.setLastChild(new Node("isDef"));
    Node isDefNullParameter = new Node();
    Node isDefNullType = JSTypeNative.NULL_TYPE;
    TypeRestriction isDefNullRestriction = new TypeRestriction(isDefNullType, false);

    assertNull(interpreter.restrictParameter(
        isDefNullParameter, isDefNullType, blindScope, interpreter.restricters.get("isDef"), false).getPreciseType());

    // Case 3: isDefAndNotNull
    Node isDefAndNotNullCondition = new Node();
    isDefAndNotNullCondition.setFirstChild(new Node("goog"));
    isDefAndNotNullCondition.setLastChild(new Node("isDefAndNotNull"));
    Node isDefAndNotNullParameter = new Node();
    Node isDefAndNotNullType = JSTypeNative.BOOLEAN_TYPE;
    TypeRestriction isDefAndNotNullRestriction = new TypeRestriction(isDefAndNotNullType, true);

    assertFalse(interpreter.restrictParameter(
        isDefAndNotNullParameter, isDefAndNotNullType, blindScope, interpreter.restricters.get("isDefAndNotNull"), false).getPreciseType().isBoolean());

    // Case 4: isString
    Node isStringCondition = new Node();
    isStringCondition.setFirstChild(new Node("goog"));
    isStringCondition.setLastChild(new Node("isString"));
    Node isStringParameter = new Node();
    Node isStringType = JSTypeNative.STRING_TYPE;
    TypeRestriction isStringRestriction = new TypeRestriction(isStringType, true);

    assertEquals(isStringType, interpreter.restrictParameter(
        isStringParameter, isStringType, blindScope, interpreter.restricters.get("isString"), true).getPreciseType());

    // Case 5: isString, null
    Node isStringNullCondition = new Node();
    isStringNullCondition.setFirstChild(new Node("goog"));
    isStringNullCondition.setLastChild(new Node("isString"));
    Node isStringNullParameter = new Node();
    Node isStringNullType = JSTypeNative.NULL_TYPE;
    TypeRestriction isStringNullRestriction = new TypeRestriction(isStringNullType, false);

    assertNull(interpreter.restrictParameter(
        isStringNullParameter, isStringNullType, blindScope, interpreter.restricters.get("isString"), false).getPreciseType());

    // Case 6: isArray
    Node isArrayCondition = new Node();
    isArrayCondition.setFirstChild(new Node("goog"));
    isArrayCondition.setLastChild(new Node("isArray"));
    Node isArrayParameter = new Node();
    Node isArrayType = JSTypeNative.ARRAY_TYPE;
    TypeRestriction isArrayRestriction = new TypeRestriction(isArrayType, true);

    assertEquals(isArrayType, interpreter.restrictParameter(
        isArrayParameter, isArrayType, blindScope, interpreter.restricters.get("isArray"), true).getPreciseType());

    // Case 7: isArray, null
    Node isArrayNullCondition = new Node();
    isArrayNullCondition.setFirstChild(new Node("goog"));
    isArrayNullCondition.setLastChild(new Node("isArray"));
    Node isArrayNullParameter = new Node();
    Node isArrayNullType = JSTypeNative.NULL_TYPE;
    TypeRestriction isArrayNullRestriction = new TypeRestriction(isArrayNullType, false);

    assertNull(interpreter.restrictParameter(
        isArrayNullParameter, isArrayNullType, blindScope, interpreter.restricters.get("isArray"), false).getPreciseType());

    // Case 8: isObject
    Node isObjectCondition = new Node();
    isObjectCondition.setFirstChild(new Node("goog"));
    isObjectCondition.setLastChild(new Node("isObject"));
    Node isObjectParameter = new Node();
    Node isObjectType = JSTypeNative.OBJECT_TYPE;
    TypeRestriction isObjectRestriction = new TypeRestriction(isObjectType, true);

    assertEquals(isObjectType, interpreter.restrictParameter(
        isObjectParameter, isObjectType, blindScope, interpreter.restricters.get("isObject"), true).getPreciseType());

    // Case 9: isObject, null
    Node isObjectNullCondition = new Node();
    isObjectNullCondition.setFirstChild(new Node("goog"));
    isObjectNullCondition.setLastChild(new Node("isObject"));
    Node isObjectNullParameter = new Node();
    Node isObjectNullType = JSTypeNative.NULL_TYPE;
    TypeRestriction isObjectNullRestriction = new TypeRestriction(isObjectNullType, false);

    assertNull(interpreter.restrictParameter(
        isObjectNullParameter, isObjectNullType, blindScope, interpreter.restricters.get("isObject"), false).getPreciseType());

    // Case 10: isFunction
    Node isFunctionCondition = new Node();
    isFunctionCondition.setFirstChild(new Node("goog"));
    isFunctionCondition.setLastChild(new Node("isFunction"));
    Node isFunctionParameter = new Node();
    Node isFunctionType = JSTypeNative.FUNCTION_TYPE;
    TypeRestriction isFunctionRestriction = new TypeRestriction(isFunctionType, true);

    assertEquals(isFunctionType, interpreter.restrictParameter(
        isFunctionParameter, isFunctionType, blindScope, interpreter.restricters.get("isFunction"), true).getPreciseType());

    // Case 11: isFunction, null
    Node isFunctionNullCondition = new Node();
    isFunctionNullCondition.setFirstChild(new Node("goog"));
    isFunctionNullCondition.setLastChild(new Node("isFunction"));
    Node isFunctionNullParameter = new Node();
    Node isFunctionNullType = JSTypeNative.NULL_TYPE;
    TypeRestriction isFunctionNullRestriction = new TypeRestriction(isFunctionNullType, false);

    assertNull(interpreter.restrictParameter(
        isFunctionNullParameter, isFunctionNullType, blindScope, interpreter.restricters.get("isFunction"), false).getPreciseType());
  }
}

class TypeRestriction {
  private final JSType type;
  private final boolean outcome;

  TypeRestriction(JSType type, boolean outcome) {
    this.type = type;
    this.outcome = outcome;
  }
}