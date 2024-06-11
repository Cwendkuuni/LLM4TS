package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  @Test
  public void testRestrictToArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isArray", null);
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restrictToArrayVisitor.caseTopType(expectedType);
    assertNotNull(actualType);
    assertNotNull(actualType.isArrayType());
  }

  @Test
  public void testRestrictToNotArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isObject", null);
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restrictToNotArrayVisitor.caseObjectType(expectedType);
    assertNotNull(actualType);
    assertNull(actualType.isArrayType());
  }

  @Test
  public void testRestrictToObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isObject", null);
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restrictToObjectVisitor.caseObjectType(expectedType);
    assertNotNull(actualType);
    assertNotNull(actualType.isObjectType());
  }

  @Test
  public void testRestrictToNotObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isObject", null);
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restrictToNotObjectVisitor.caseObjectType(expectedType);
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node1 = NodeOperations.createCall(NAME, "goog.isDef", NodeOperations.createName("foo"));
    JSType expectedType1 = interpreter.getType(node1, null);
    JSType actualType1 = interpreter.restricters.get("isDef").apply(new TypeRestriction(expectedType1, true));
    assertNotNull(actualType1);
    assertNotNull(actualType1.isObjectType());

    Node node2 = NodeOperations.createCall(NAME, "goog.isDef", NodeOperations.createName("foo"));
    JSType expectedType2 = interpreter.getType(node2, null);
    JSType actualType2 = interpreter.restricters.get("isDef").apply(new TypeRestriction(expectedType2, false));
    assertNull(actualType2);
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node1 = NodeOperations.createCall(NAME, "goog.isDef", NodeOperations.createName("foo"));
    Node node2 = NodeOperations.createCall(NAME, "goog.isNotNull", NodeOperations.createName("foo"));
    Node node3 = NodeOperations.createBinaryOperator(NAME, "&&", node1, node2);

    JSType expectedType = interpreter.getType(node3, null);
    JSType actualType = interpreter.restricters.get("isDefAndNotNull").apply(new TypeRestriction(expectedType, true));
    assertNotNull(actualType);
    assertNotNull(actualType.isObjectType());
  }

  @Test
  public void testRestrictIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isString", NodeOperations.createName("foo"));
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restricters.get("isString").apply(new TypeRestriction(expectedType, true));
    assertNotNull(actualType);
    assertNotNull(actualType.isStringType());
  }

  @Test
  public void testRestrictIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isBoolean", NodeOperations.createName("foo"));
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restricters.get("isBoolean").apply(new TypeRestriction(expectedType, true));
    assertNotNull(actualType);
    assertNotNull(actualType.isBooleanType());
  }

  @Test
  public void testRestrictIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isNumber", NodeOperations.createName("foo"));
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restricters.get("isNumber").apply(new TypeRestriction(expectedType, true));
    assertNotNull(actualType);
    assertNotNull(actualType.isNumberType());
  }

  @Test
  public void testRestrictIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isFunction", NodeOperations.createName("foo"));
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restricters.get("isFunction").apply(new TypeRestriction(expectedType, true));
    assertNotNull(actualType);
    assertNotNull(actualType.isFunctionType());
  }

  @Test
  public void testRestrictIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isArray", NodeOperations.createName("foo"));
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restrictToArrayVisitor.caseTopType(expectedType);
    assertNotNull(actualType);
    assertNotNull(actualType.isArrayType());
  }

  @Test
  public void testRestrictIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node node = NodeOperations.createCall(NAME, "goog.isObject", NodeOperations.createName("foo"));
    JSType expectedType = interpreter.getType(node, null);
    JSType actualType = interpreter.restrictToObjectVisitor.caseObjectType(expectedType);
    assertNotNull(actualType);
    assertNotNull(actualType.isObjectType());
  }
}