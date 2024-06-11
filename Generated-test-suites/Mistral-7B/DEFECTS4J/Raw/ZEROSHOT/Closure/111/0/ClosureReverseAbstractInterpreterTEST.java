package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectTypeFactory;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private ObjectTypeFactory objectTypeFactory;

  @Before
  public void setUp() {
    typeRegistry = new JSTypeRegistry();
    objectTypeFactory = typeRegistry.getFactory();
    JSTypeRegistry registry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention() {};
    interpreter = new ClosureReverseAbstractInterpreter(convention, registry);
  }

  @Test
  public void testRestrictIsDef() {
    JSType type = typeRegistry.createUnionType(
        typeRegistry.createStringType(), typeRegistry.createNumberType());
    assertNull(interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isDef"), false));
    assertSame(typeRegistry.createNumberType(), interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isDef"), true));
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    JSType type = typeRegistry.createUnionType(
        typeRegistry.createStringType(), typeRegistry.createNumberType());
    JSType result = interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isDefAndNotNull"), false);
    assertSame(typeRegistry.createVoidType(), result);

    result = interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isDefAndNotNull"), true);
    assertSame(typeRegistry.createStringType().getGreatestSubtype(typeRegistry.createNumberType()), result);
  }

  @Test
  public void testRestrictIsString() {
    JSType type = typeRegistry.createStringType();
    assertSame(type, interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isString"), true));
    assertNull(interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isString"), false));
  }

  @Test
  public void testRestrictIsBoolean() {
    JSType type = typeRegistry.createBooleanType();
    assertSame(type, interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isBoolean"), true));
    assertNull(interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isBoolean"), false));
  }

  @Test
  public void testRestrictIsNumber() {
    JSType type = typeRegistry.createNumberType();
    assertSame(type, interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isNumber"), true));
    assertNull(interpreter.restrictParameter(
        Node.newName("parameter"), type, null, interpreter.restricters.get("isNumber"), false));
  }

  @Test
  public void testRestrictIsFunction() {
    FunctionType functionType = objectTypeFactory.createFunctionType(
        typeRegistry.createStringType(), null, null);
    assertSame(functionType, interpreter.restrictParameter(
        Node.newName("parameter"), functionType, null, interpreter.restricters.get("isFunction"), true));
    assertNull(interpreter.restrictParameter(
        Node.newName("parameter"), functionType, null, interpreter.restricters.get("isFunction"), false));
  }

  @Test
  public void testRestrictIsArray() {
    JSType arrayType = typeRegistry.createArrayType(typeRegistry.createNumberType());
    assertNull(interpreter.restrictParameter(
        Node.newName("parameter"), arrayType, null, interpreter.restricters.get("isArray"), false));
    assertSame(arrayType, interpreter.restrictParameter(
        Node.newName("parameter"), arrayType, null, interpreter.restricters.get("isArray"), true));
  }

  @Test
  public void testRestrictIsObject() {
    ObjectType objectType = objectTypeFactory.createObjectType();
    assertNull(interpreter.restrictParameter(
        Node.newName("parameter"), objectType, null, interpreter.restricters.get("isObject"), false));
    assertSame(objectType, interpreter.restrictParameter(
        Node.newName("parameter"), objectType, null, interpreter.restricters.get("isObject"), true));
  }
}