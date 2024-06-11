package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.test.RedefineAllGlobalFunctions;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(RedefineAllGlobalFunctions.class)
public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final ClosureReverseAbstractInterpreter INTERPRETER =
      new ClosureReverseAbstractInterpreter(
          CodingConvention.NONE, TYPE_REGISTRY);

  @Test
  public void testRestrictIsDef() {
    JSType type = TYPE_REGISTRY.createType(String.class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = INTERPRETER.getRestrictedWithoutUndefined(type);
    JSType actual = INTERPRETER.restricters.get("isDef").apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsDefNull() {
    JSType type = TYPE_REGISTRY.createType(String.class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isDef").apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    JSType type = TYPE_REGISTRY.createType(String.class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = INTERPRETER.getRestrictedWithoutUndefined(
        INTERPRETER.getRestrictedWithoutNull(type));
    JSType actual = INTERPRETER.restricters.get("isDefAndNotNull")
        .apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsString() {
    JSType type = TYPE_REGISTRY.createType(String.class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = type;
    JSType actual = INTERPRETER.restricters.get("isString").apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsStringFalse() {
    JSType type = TYPE_REGISTRY.createType(String.class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isString").apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsBoolean() {
    JSType type = TYPE_REGISTRY.createType(Boolean.class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = type;
    JSType actual = INTERPRETER.restricters
        .get("isBoolean").apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsBooleanFalse() {
    JSType type = TYPE_REGISTRY.createType(Boolean.class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isBoolean").apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsNumber() {
    JSType type = TYPE_REGISTRY.createType(Number.class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = type;
    JSType actual = INTERPRETER.restricters.get("isNumber").apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsNumberFalse() {
    JSType type = TYPE_REGISTRY.createType(Number.class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isNumber").apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsFunction() {
    JSType type = TYPE_REGISTRY.createType(Function.class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = type;
    JSType actual = INTERPRETER.restricters.get("isFunction").apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsFunctionFalse() {
    JSType type = TYPE_REGISTRY.createType(Function.class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isFunction").apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsArrayTrue() {
    JSType type = TYPE_REGISTRY.createType(Integer[].class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = ARRAY_TYPE;
    JSType actual = INTERPRETER.restricters.get("isArray").apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsArrayFalse() {
    JSType type = TYPE_REGISTRY.createType(Integer[].class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isArray").apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsObjectTrue() {
    JSType type = TYPE_REGISTRY.createType(Object.class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = OBJECT_TYPE;
    JSType actual = INTERPRETER.restricters.get("isObject").apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsObjectFalse() {
    JSType type = TYPE_REGISTRY.createType(Object.class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isObject").apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsObjectFunctionTrue() {
    JSType type = TYPE_REGISTRY.createFunctionType(new FunctionType(new ObjectType(Function.class), new ObjectType(Object.class)));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = OBJECT_TYPE;
    JSType actual = INTERPRETER.restricters.get("isObject")
        .apply(restriction);
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsObjectFunctionFalse() {
    JSType type = TYPE_REGISTRY.createFunctionType(new FunctionType(new ObjectType(Function.class), new ObjectType(Object.class)));
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = INTERPRETER.restricters.get("isObject")
        .apply(restriction);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType type = TYPE_REGISTRY.createType(Object.class);
    JSType arrayType = TYPE_REGISTRY.createType(Integer[].class);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType actual = type.visit(INTERPRETER.restrictToArrayVisitor);
    JSType expected = arrayType;
    assertNotNull(expected);
    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictToArrayVisitorNull() {
    JSType type = TYPE_REGISTRY.createType(Object.class);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType actual = type.visit(INTERPRETER.restrictToArrayVisitor);
    JSType expected = null;
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSType type = TYPE_REGISTRY.createType(Integer[].class);
    JSType expected = null;
    JSType actual = type.visit(INTERPRETER.restrictToNotArrayVisitor);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictToNotArrayVisitorObject() {
    JSType type = TYPE_REGISTRY.createType(Object.class);
    JSType expected = null;
    JSType actual = type.visit(INTERPRETER.restrictToNotArrayVisitor);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSType type = TYPE_REGISTRY.createType(Function.class);
    JSType expected = null;
    JSType actual = type.visit(INTERPRETER.restrictToNotObjectVisitor);
    assertNull(expected);
    assertNull(actual);
  }

  @Test
  public void testRestrictToNotObjectVisitorArray() {
    JSType type = TYPE_REGISTRY.createType(Integer[].class);
    JSType expected = null;
    JSType actual = type.visit(INTERPRETER.restrictToNotObjectVisitor);
    assertNull(expected);
    assertNull(actual);
  }
}