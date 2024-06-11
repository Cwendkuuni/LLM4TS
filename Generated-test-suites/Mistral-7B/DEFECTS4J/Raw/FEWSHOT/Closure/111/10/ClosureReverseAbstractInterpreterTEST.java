package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    TypeRestriction trueTypeRestriction = new TypeRestriction(new ObjectType(typeRegistry), true);
    JSType expectedTrueResult = getNativeType(Boolean.TYPE);
    JSType actualTrueResult = interpreter.getRestrictedWithoutUndefined(trueTypeRestriction.type);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseTypeRestriction = new TypeRestriction(null, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedWithoutUndefined(falseTypeRestriction.type);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    TypeRestriction trueTypeRestriction = new TypeRestriction(new ObjectType(typeRegistry), true);
    JSType expectedTrueResult = getNativeType(Object.class);
    JSType actualTrueResult = interpreter.getRestrictedWithoutNull(trueTypeRestriction.type);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseTypeRestriction = new TypeRestriction(null, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedWithoutNull(falseTypeRestriction.type);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    TypeRestriction trueTypeRestriction = new TypeRestriction(new ObjectType(typeRegistry), true);
    JSType expectedTrueResult = getNativeType(Object.class);
    JSType actualTrueResult = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(trueTypeRestriction.type));
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseTypeRestriction = new TypeRestriction(null, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(falseTypeRestriction.type));
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    TypeRestriction trueTypeRestriction = new TypeRestriction(String.class, true);
    JSType expectedTrueResult = String.class;
    JSType actualTrueResult = interpreter.getRestrictedByTypeOfResult(
        trueTypeRestriction.type, "string", true);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseTypeRestriction = new TypeRestriction(Integer.class, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedByTypeOfResult(
        falseTypeRestriction.type, "string", true);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    TypeRestriction trueTypeRestriction = new TypeRestriction(Boolean.TYPE, true);
    JSType expectedTrueResult = Boolean.TYPE;
    JSType actualTrueResult = interpreter.getRestrictedByTypeOfResult(
        trueTypeRestriction.type, "boolean", true);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseTypeRestriction = new TypeRestriction(String.class, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedByTypeOfResult(
        falseTypeRestriction.type, "boolean", true);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    TypeRestriction trueTypeRestriction = new TypeRestriction(Number.class, true);
    JSType expectedTrueResult = Number.class;
    JSType actualTrueResult = interpreter.getRestrictedByTypeOfResult(
        trueTypeRestriction.type, "number", true);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseTypeRestriction = new TypeRestriction(String.class, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedByTypeOfResult(
        falseTypeRestriction.type, "number", true);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    TypeRestriction trueTypeRestriction = new TypeRestriction(new FunctionType(null, null), true);
    JSType expectedTrueResult = FunctionType.create(null, null);
    JSType actualTrueResult = interpreter.getRestrictedByTypeOfResult(
        trueTypeRestriction.type, "function", true);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseTypeRestriction = new TypeRestriction(String.class, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedByTypeOfResult(
        falseTypeRestriction.type, "function", true);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // null case
    TypeRestriction nullTypeRestriction = new TypeRestriction(null, true);
    JSType expectedNullResult = null;
    JSType actualNullResult = interpreter.getRestrictedByTypeOfResult(
        nullTypeRestriction.type, "array", true);
    assertNull(actualNullResult);
    assertEquals(expectedNullResult, actualNullResult);

    // true case
    TypeRestriction trueArrayTypeRestriction = new TypeRestriction(Array.class, true);
    JSType expectedTrueResult = Array.class;
    JSType actualTrueResult = interpreter.getRestrictedByTypeOfResult(
        trueArrayTypeRestriction.type, "array", true);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseArrayTypeRestriction = new TypeRestriction(String.class, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedByTypeOfResult(
        falseArrayTypeRestriction.type, "array", true);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // null case
    TypeRestriction nullTypeRestriction = new TypeRestriction(null, true);
    JSType expectedNullResult = null;
    JSType actualNullResult = interpreter.getRestrictedByTypeOfResult(
        nullTypeRestriction.type, "object", true);
    assertNull(actualNullResult);
    assertEquals(expectedNullResult, actualNullResult);

    // true case
    TypeRestriction trueObjectTypeRestriction = new TypeRestriction(String.class, true);
    JSType expectedTrueResult = ObjectType.create(String.class);
    JSType actualTrueResult = interpreter.getRestrictedByTypeOfResult(
        trueObjectTypeRestriction.type, "object", true);
    assertNotNull(actualTrueResult);
    assertEquals(expectedTrueResult, actualTrueResult);

    // false case
    TypeRestriction falseObjectTypeRestriction = new TypeRestriction(Number.class, true);
    JSType expectedFalseResult = null;
    JSType actualFalseResult = interpreter.getRestrictedByTypeOfResult(
        falseObjectTypeRestriction.type, "object", true);
    assertNull(actualFalseResult);
    assertEquals(expectedFalseResult, actualFalseResult);
  }

  private JSType getNativeType(Class<?> clazz) {
    return typeRegistry.getNativeType(clazz);
  }

  private JSType getNativeType(int typeCode) {
    return JSTypeRegistry.getNativeType(typeRegistry, typeCode);
  }
}