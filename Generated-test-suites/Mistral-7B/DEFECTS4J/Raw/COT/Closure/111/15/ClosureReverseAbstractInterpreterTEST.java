package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
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
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setup() {
    convention = mock(CodingConvention.class);
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testIsDef_true() {
    Node condition = Node.newCall("goog.isDef", Node.newQualifiedName("someObject"));
    JSType expectedType = interpreter.getType(condition, null);
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isDef"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDef_false() {
    Node condition = Node.newCall("goog.isDef", Node.newQualifiedName("someObject"));
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isDef"), false).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNull_true() {
    Node condition = Node.newCall("goog.isDefAndNotNull", Node.newQualifiedName("someObject"));
    JSType expectedType = interpreter.getType(condition, null);
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isDefAndNotNull"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNull_false() {
    Node condition = Node.newCall("goog.isDefAndNotNull", Node.newQualifiedName("someObject"));
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isDefAndNotNull"), false).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsString_true() {
    Node condition = Node.newCall("goog.isString", Node.newString("someString"));
    JSType expectedType = JSTypeNative.STRING_TYPE;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isString"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsString_false() {
    Node condition = Node.newCall("goog.isString", Node.newString("someString"));
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isString"), false).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsBoolean_true() {
    Node condition = Node.newCall("goog.isBoolean", Node.newBoolean(true));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isBoolean"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsBoolean_false() {
    Node condition = Node.newCall("goog.isBoolean", Node.newBoolean(true));
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isBoolean"), false).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsNumber_true() {
    Node condition = Node.newCall("goog.isNumber", Node.newNumber(1));
    JSType expectedType = JSTypeNative.NUMBER_TYPE;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isNumber"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsNumber_false() {
    Node condition = Node.newCall("goog.isNumber", Node.newNumber(1));
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isNumber"), false).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsFunction_true() {
    Node condition = Node.newCall("goog.isFunction", Node.newCall("someFunction"));
    JSType expectedType = new FunctionType(JSTypeNative.VOID_TYPE, new ObjectType[0]);
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isFunction"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsFunction_false() {
    Node condition = Node.newCall("goog.isFunction", Node.newCall("someFunction"));
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isFunction"), false).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsArray_true() {
    Node condition = Node.newCall("goog.isArray", Node.newArray());
    JSType expectedType = JSTypeNative.ARRAY_TYPE;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isArray"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsArray_false() {
    Node condition = Node.newCall("goog.isArray", Node.newArray());
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isArray"), false).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsObject_true() {
    Node condition = Node.newCall("goog.isObject", Node.newObject());
    JSType expectedType = JSTypeNative.OBJECT_TYPE;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), expectedType, null, interpreter.restricters.get("isObject"), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsObject_false() {
    Node condition = Node.newCall("goog.isObject", Node.newObject());
    JSType expectedType = null;
    JSType actualType = interpreter.restrictParameter(
        condition.getLastChild(), null, null, interpreter.restricters.get("isObject"), false).getType();

    assertEquals(expectedType, actualType);
  }
}