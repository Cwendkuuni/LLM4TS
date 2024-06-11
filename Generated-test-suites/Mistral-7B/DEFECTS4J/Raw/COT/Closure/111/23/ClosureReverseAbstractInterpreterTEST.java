package com.example.test; // Replace this with the actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClosureReverseAbstractInterpreterTest {

  @Mock
  private CodingConvention codingConvention;
  @Mock
  private JSTypeRegistry typeRegistry;

  @InjectMocks
  private ClosureReverseAbstractInterpreter interpreter;

  @Test
  public void testIsDef_true_returnsRestrictedType() {
    JSType inputType = new FunctionType(ObjectType.createNull(), ObjectType.createNull());
    JSType expectedType = JSTypeNative.OBJECT_TYPE;
    when(typeRegistry.createObjectType(ImmutableMap.of("type", "String")))
        .thenReturn(JSTypeNative.STRING_TYPE);

    JSType result = interpreter.getRestrictedWithoutUndefined(inputType);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsDef_false_returnsNull() {
    JSType inputType = JSTypeNative.VOID_TYPE;

    JSType result = interpreter.getRestrictedWithoutUndefined(inputType);

    assertNull(result);
  }

  @Test
  public void testIsNull_true_returnsRestrictedType() {
    JSType inputType = JSTypeNative.NUMBER_TYPE;
    JSType expectedType = JSTypeNative.NULL_TYPE;

    JSType result = interpreter.getRestrictedWithoutNull(inputType);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsNull_false_returnsNull() {
    JSType inputType = JSTypeNative.BOOLEAN_TYPE;

    JSType result = interpreter.getRestrictedWithoutNull(inputType);

    assertNull(result);
  }

  @Test
  public void testIsDefAndNotNull_true_returnsRestrictedType() {
    JSType inputType = JSTypeNative.ARRAY_TYPE;
    JSType expectedType = JSTypeNative.OBJECT_TYPE;

    JSType result = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(inputType));

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsDefAndNotNull_false_returnsNull() {
    JSType inputType = JSTypeNative.BOOLEAN_TYPE;

    JSType result = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(inputType));

    assertNull(result);
  }

  @Test
  public void testIsString_true_returnsRestrictedType() {
    JSType inputType = JSTypeNative.OBJECT_TYPE;
    when(typeRegistry.createObjectType(ImmutableMap.of("type", "String")))
        .thenReturn(JSTypeNative.STRING_TYPE);

    JSType expectedType = JSTypeNative.STRING_TYPE;
    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "String", true);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsString_false_returnsNull() {
    JSType inputType = JSTypeNative.NUMBER_TYPE;

    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "String", false);

    assertNull(result);
  }

  @Test
  public void testIsBoolean_true_returnsRestrictedType() {
    JSType inputType = JSTypeNative.OBJECT_TYPE;
    when(typeRegistry.createObjectType(ImmutableMap.of("type", "Boolean")))
        .thenReturn(JSTypeNative.BOOLEAN_TYPE);

    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;
    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "Boolean", true);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsBoolean_false_returnsNull() {
    JSType inputType = JSTypeNative.NUMBER_TYPE;

    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "Boolean", false);

    assertNull(result);
  }

  @Test
  public void testIsNumber_true_returnsRestrictedType() {
    JSType inputType = JSTypeNative.OBJECT_TYPE;
    when(typeRegistry.createObjectType(ImmutableMap.of("type", "Number")))
        .thenReturn(JSTypeNative.NUMBER_TYPE);

    JSType expectedType = JSTypeNative.NUMBER_TYPE;
    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "Number", true);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsNumber_false_returnsNull() {
    JSType inputType = JSTypeNative.NUMBER_STRING_BOOLEAN;

    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "Number", false);

    assertNull(result);
  }

  @Test
  public void testIsFunction_true_returnsRestrictedType() {
    JSType inputType = JSTypeNative.OBJECT_TYPE;
    when(typeRegistry.createObjectType(ImmutableMap.of("type", "Function")))
        .thenReturn(new FunctionType(ObjectType.createNull(), ObjectType.createNull()));

    JSType expectedType = new FunctionType(ObjectType.createNull(), ObjectType.createNull());
    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "Function", true);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsFunction_false_returnsNull() {
    JSType inputType = JSTypeNative.NUMBER_TYPE;

    JSType result = interpreter.getRestrictedByTypeOfResult(inputType, "Function", false);

    assertNull(result);
  }

  @Test
  public void testIsArray_true_returnsRestrictedType() {
    JSType inputType = null;
    boolean outcome = true;

    JSType expectedType = JSTypeNative.ARRAY_TYPE;
    JSType result = interpreter.getTypeIfRefinable(
        new Node() { /* Empty node with isCall() == false */ }, null)
        .visit(interpreter.restrictToArrayVisitor);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsArray_false_returnsNull() {
    JSType inputType = null;
    boolean outcome = false;

    JSType result = interpreter.getTypeIfRefinable(
        new Node() { /* Empty node with isCall() == false */ }, null)
        .visit(interpreter.restrictToNotArrayVisitor);

    assertNull(result);
  }

  @Test
  public void testIsObject_true_returnsRestrictedType() {
    JSType inputType = null;
    boolean outcome = true;

    JSType expectedType = JSTypeNative.OBJECT_TYPE;
    JSType result = interpreter.getTypeIfRefinable(
        new Node() { /* Empty node with isCall() == false */ }, null)
        .visit(interpreter.restrictToObjectVisitor);

    assertEquals(expectedType, result);
  }

  @Test
  public void testIsObject_false_returnsNull() {
    JSType inputType = new FunctionType(ObjectType.createNull(), ObjectType.createNull());

    JSType result = interpreter.getTypeIfRefinable(
        new Node() { /* Empty node with isCall() == false */ }, null)
        .visit(interpreter.restrictToNotObjectVisitor);

    assertNull(result);
  }
}