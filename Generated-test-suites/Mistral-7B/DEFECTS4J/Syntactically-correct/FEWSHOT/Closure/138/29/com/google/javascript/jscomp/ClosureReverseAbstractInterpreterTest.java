package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictIsDef() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = JSTypeNative.getBooleanType();
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isDef"), true).getType();
    assertEquals(type, restrictedType);

    type = JSTypeNative.getUndefinedType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isDef"), false).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = JSTypeNative.getBooleanType();
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isNull"), true).getType();
    assertNull(restrictedType);

    type = JSTypeNative.getNullType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isNull"), false).getType();
    assertEquals(type, restrictedType);
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = JSTypeNative.getBooleanType();
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isDefAndNotNull"), true).getType();
    assertNull(restrictedType);

    type = JSTypeNative.getNullType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isDefAndNotNull"), false).getType();
    assertNull(restrictedType);

    restrictedType = interpreter.restrictParameter(
        null, JSTypeNative.getBooleanType(), null, interpreter.restricters.get("isDefAndNotNull"), true).getType();
    assertEquals(JSTypeNative.getBooleanType(), restrictedType);
  }

  @Test
  public void testRestrictIsString() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = JSTypeNative.getBooleanType();
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isString"), true).getType();
    assertNull(restrictedType);

    type = JSTypeNative.getStringType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isString"), false).getType();
    assertEquals(type, restrictedType);
  }

  @Test
  public void testRestrictIsBoolean() {
    CodingConvention
        convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = JSTypeNative.getBooleanType();
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isBoolean"), true).getType();
    assertEquals(type, restrictedType);

    type = JSTypeNative.getNumberType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isBoolean"), false).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsNumber() {
    CodingConvention
        convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = JSTypeNative.getNumberType();
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isNumber"), true).getType();
    assertEquals(type, restrictedType);

    type = JSTypeNative.getBooleanType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isNumber"), false).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsFunction() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    FunctionType functionType = new FunctionType(OBJECT_TYPE, JSTypeNative.getVoidType(), new Object[] {
      JSTypeNative.getBooleanType(),
      JSTypeNative.getObjectType()
    });

    JSType type = functionType;
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isFunction"), true).getType();
    assertEquals(type, restrictedType);

    type = JSTypeNative.getNumberType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isFunction"), false).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsArray() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = JSTypeNative.getBooleanType();
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isArray"), true).getType();
    assertNull(restrictedType);

    type = JSTypeNative.getArrayType(JSTypeNative.getNumberType());
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isArray"), false).getType();
    assertEquals(type, restrictedType);
  }

  @Test
  public void testRestrictIsObject() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    ObjectType objectType = new ObjectType("Object", new Object[] {});

    JSType type = objectType;
    JSType restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isObject"), true).getType();
    assertEquals(type, restrictedType);

    type = JSTypeNative.getNumberType();
    restrictedType = interpreter.restrictParameter(
        null, type, null, interpreter.restricters.get("isObject"), false).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = new Node(CALL, null, new Node[] {
      new Node(GETPROP, null, new Node[] {
        new Node(NAME, "goog"),
        new Node(STRING, "isDef")
      }),
      new Node(STRING, "foo")
    });

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, blindScope, true);

    assertEquals(JSTypeNative.getBooleanType(), informedScope.getVariable("foo").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = new Node(CALL, null, new Node[] {
      new Node(GETPROP, null, new Node[] {
        new Node(NAME, "goog"),
        new Node(STRING, "isDef")
      }),
      new Node(STRING, "foo")
    });

    FlowScope blindScope = new FlowScope();
    assertNull(interpreter.getPreciserScopeKnowingConditionOutcome(node, blindScope, false));
  }
}