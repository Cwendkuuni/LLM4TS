package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testClosureReverseAbstractInterpreter() {
    // Given
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // When

    // Test isDef method
    JSType expectedType1 = JSTypeFactory.getNativeType(Boolean.TYPE);
    JSType actualType1 = interpreter.restrictParameter(
        new Node(), expectedType1, null, interpreter.restricters.get("isDef"), true);
    assertTrue(expectedType1.equals(actualType1));

    JSType expectedType2 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType2 = interpreter.restrictParameter(
        new Node(), null, null, interpreter.restricters.get("isDef"), false);
    assertNull(actualType2);

    // Test isNull method
    JSType expectedType3 = JSTypeFactory.getNativeType(NullType.NULL);
    JSType actualType3 = interpreter.restrictParameter(
        new Node(), expectedType3, null, interpreter.restricters.get("isNull"), true);
    assertTrue(expectedType3.equals(actualType3));

    JSType expectedType4 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType4 = interpreter.restrictParameter(
        new Node(), null, null, interpreter.restricters.get("isNull"), false);
    assertNull(actualType4);

    // Test isDefAndNotNull method
    JSType expectedType5 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType5 = interpreter.restrictParameter(
        new Node(), null, null, interpreter.restricters.get("isDefAndNotNull"), false);
    assertNull(actualType5);

    JSType expectedType6 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType6 = interpreter.restrictParameter(
        new Node(), null, null, interpreter.restricters.get("isDefAndNotNull"), true);
    assertNull(actualType6);

    JSType expectedType7 = JSTypeFactory.getNativeType(Boolean.TYPE);
    JSType actualType7 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(Number.class), null, interpreter.restricters.get("isDefAndNotNull"), true);
    assertTrue(expectedType7.equals(actualType7));

    // Test isString method
    JSType expectedType8 = JSTypeFactory.getNativeType(String.class);
    JSType actualType8 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(String.class), null, interpreter.restricters.get("isString"), true);
    assertTrue(expectedType8.equals(actualType8));

    JSType expectedType9 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType9 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(Number.class), null, interpreter.restricters.get("isString"), false);
    assertNull(actualType9);

    // Test isBoolean method
    JSType expectedType10 = JSTypeFactory.getNativeType(Boolean.TYPE);
    JSType actualType10 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(Boolean.TYPE), null, interpreter.restricters.get("isBoolean"), true);
    assertTrue(expectedType10.equals(actualType10));

    JSType expectedType11 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType11 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(Number.class), null, interpreter.restricters.get("isBoolean"), false);
    assertNull(actualType11);

    // Test isNumber method
    JSType expectedType12 = JSTypeFactory.getNativeType(Number.TYPE);
    JSType actualType12 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(Number.TYPE), null, interpreter.restricters.get("isNumber"), true);
    assertTrue(expectedType12.equals(actualType12));

    JSType expectedType13 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType13 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(String.class), null, interpreter.restricters.get("isNumber"), false);
    assertNull(actualType13);

    // Test isFunction method
    JSType expectedType14 = JSTypeFactory.getNativeType(FunctionType.FUNCTION_TYPE);
    JSType actualType14 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(FunctionType.FUNCTION_TYPE), null, interpreter.restricters.get("isFunction"), true);
    assertTrue(expectedType14.equals(actualType14));

    JSType expectedType15 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType15 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(ObjectType.OBJECT_TYPE), null, interpreter.restricters.get("isFunction"), false);
    assertNull(actualType15);

    // Test isArray method
    JSType expectedType16 = JSTypeFactory.getNativeType(ArrayType.ARRAY_TYPE);
    JSType actualType16 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(ArrayType.ARRAY_TYPE), null, interpreter.restricters.get("isArray"), true);
    assertTrue(expectedType16.equals(actualType16));

    JSType expectedType17 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType17 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(ObjectType.OBJECT_TYPE), null, interpreter.restricters.get("isArray"), false);
    assertNull(actualType17);

    // Test isObject method
    JSType expectedType18 = JSTypeFactory.getNativeType(ObjectType.OBJECT_TYPE);
    JSType actualType18 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(ObjectType.OBJECT_TYPE), null, interpreter.restricters.get("isObject"), true);
    assertTrue(expectedType18.equals(actualType18));

    JSType expectedType19 = JSTypeFactory.getNativeType(Void.TYPE);
    JSType actualType19 = interpreter.restrictParameter(
        new Node(), JSTypeFactory.getNativeType(NumberType.NUMBER_TYPE), null, interpreter.restricters.get("isObject"), false);
    assertNull(actualType19);
  }
}