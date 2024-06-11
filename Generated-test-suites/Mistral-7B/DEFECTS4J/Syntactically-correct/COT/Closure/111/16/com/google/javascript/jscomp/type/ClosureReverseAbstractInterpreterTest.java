package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(2, "goog.isDef", Node.newString("a"));
    FlowScope blindScope = new FlowScope();
    JSType expectedType = typeRegistry.createTopType();
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    assertEquals(expectedType, actualScope.getDeclaredType("a"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(2, "goog.isDef", Node.newString("a"));
    FlowScope blindScope = new FlowScope();
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(actualScope.getDeclaredType("a"));
  }

  // Add test cases for other methods: isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction, isArray, isObject

  @Test
  public void testRestrictParameter_isDef_true() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node parameter = Node.newName("p");
    JSType type = new ObjectType("Object");
    FlowScope blindScope = new FlowScope();

    Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isDef");

    FlowScope actualScope = interpreter.restrictParameter(
        parameter, type, blindScope, restriction, true);

    JSType expectedType = typeRegistry.createUnionType(
        typeRegistry.getNativeType(JSTypeNative.NULL_TYPE),
        typeRegistry.getNativeType(JSTypeNative.VOID_TYPE));
    assertEquals(expectedType, actualScope.getDeclaredType("p"));
  }

  // Add test cases for other restricters: isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction, isArray, isObject

  @Test
  public void testRestrictParameter_isDef_false() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node parameter = Node.newName("p");
    JSType type = new ObjectType("Object");
    FlowScope blindScope = new FlowScope();

    Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isDef");

    FlowScope actualScope = interpreter.restrictParameter(
        parameter, type, blindScope, restriction, false);

    assertNull(actualScope.getDeclaredType("p"));
  }
}