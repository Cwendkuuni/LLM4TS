package com.google.javascript.jscomp.type;

import static com.google.javascript.jscomp.testing.TypeCheckerTestUtil.createType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.testing.TypeCheckerTestUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = TypeCheckerTestUtil.getTypeRegistry();
  private static final CodingConvention convention = TypeCheckerTestUtil.getDefaultCodingConvention();

  private static final ClosureReverseAbstractInterpreter interpreter =
      new ClosureReverseAbstractInterpreter(convention, typeRegistry);

  @Test
  public void testRestrictToArray() {
    Node node = Node.newCall("goog.isDef", Node.newQualifiedName("myArray"), null);
    JSType originalType = createType(node, typeRegistry);
    JSType restrictedType = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restrictToArrayVisitor, true).getType();
    assertEquals(typeRegistry.getNativeType(ARRAY_TYPE), restrictedType);
  }

  @Test
  public void testRestrictToNotArray() {
    Node node = Node.newCall("goog.isDef", Node.newQualifiedName("myObject"), null);
    JSType originalType = createType(node, typeRegistry);
    JSType restrictedType = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restrictToArrayVisitor, false).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictToObject() {
    Node node = Node.newCall("goog.isDef", Node.newQualifiedName("myFunction"), null);
    JSType originalType = createType(node, typeRegistry);
    JSType restrictedType = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restrictToObjectVisitor, true).getType();
    assertEquals(typeRegistry.getNativeType(OBJECT_TYPE), restrictedType);
  }

  @Test
  public void testRestrictToNotObject() {
    Node node = Node.newCall("goog.isDef", Node.newLiteral(1), null);
    JSType originalType = createType(node, typeRegistry);
    JSType restrictedType = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restrictToObjectVisitor, false).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testIsDef() {
    Node node = Node.newCall("goog.isDef", Node.newQualifiedName("myArray"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restrictors.get("isDef"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(VOID_TYPE).getGreatestSubtype(typeRegistry.getNativeType(ARRAY_TYPE)), restrictedType);
  }

  @Test
  public void testIsDefFalse() {
    Node node = Node.newCall("goog.isDef", Node.newQualifiedName("myNull"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isDef"), false);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(NULL_VOID), restrictedType);
  }

  @Test
  public void testIsDefAndNotNull() {
    Node node = Node.newCall("goog.isDefAndNotNull", Node.newQualifiedName("myObject"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isDefAndNotNull"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(OBJECT_TYPE), restrictedType);
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    Node node = Node.newCall("goog.isDefAndNotNull", Node.newQualifiedName("myNull"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isDefAndNotNull"), false);
    JSType restrictedType = scope.getType();
    assertNull(restrictedType);
  }

  @Test
  public void testIsString() {
    Node node = Node.newCall("goog.isString", Node.newStringLiteral("hello"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isString"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(STRING_TYPE), restrictedType);
  }

  @Test
  public void testIsStringFalse() {
    Node node = Node.newCall("goog.isString", Node.newQualifiedName("myObject"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isString"), false);
    JSType restrictedType = scope.getType();
    assertNull(restrictedType);
  }

  @Test
  public void testIsBoolean() {
    Node node = Node.newCall("goog.isBoolean", Node.newBooleanLiteral(true), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isBoolean"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(BOOLEAN_TYPE), restrictedType);
  }

  @Test
  public void testIsBooleanFalse() {
    Node node = Node.newCall("goog.isBoolean", Node.newNumberLiteral(1), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isBoolean"), false);
    JSType restrictedType = scope.getType();
    assertNull(restrictedType);
  }

  @Test
  public void testIsNumber() {
    Node node = Node.newCall("goog.isNumber", Node.newNumberLiteral(1), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isNumber"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(NUMBER_TYPE), restrictedType);
  }

  @Test
  public void testIsNumberFalse() {
    Node node = Node.newCall("goog.isNumber", Node.newStringLiteral("hello"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isNumber"), false);
    JSType restrictedType = scope.getType();
    assertNull(restrictedType);
  }

  @Test
  public void testIsFunction() {
    Node node = Node.newCall("goog.isFunction", Node.newName("myFunction"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isFunction"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(FUNCTION_TYPE), restrictedType);
  }

  @Test
  public void testIsFunctionFalse() {
    Node node = Node.newCall("goog.isFunction", Node.newQualifiedName("myObject"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isFunction"), false);
    JSType restrictedType = scope.getType();
    assertNull(restrictedType);
  }

  @Test
  public void testIsArray() {
    Node node = Node.newCall("goog.isArray", Node.newQualifiedName("myArray"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isArray"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(ARRAY_TYPE), restrictedType);
  }

  @Test
  public void testIsArrayFalse() {
    Node node = Node.newCall("goog.isArray", Node.newQualifiedName("myObject"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isArray"), false);
    JSType restrictedType = scope.getType();
    assertNull(restrictedType);
  }

  @Test
  public void testIsObject() {
    Node node = Node.newCall("goog.isObject", Node.newQualifiedName("myObject"), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isObject"), true);
    JSType restrictedType = scope.getType();
    assertEquals(getNativeType(OBJECT_TYPE), restrictedType);
  }

  @Test
  public void testIsObjectFalse() {
    Node node = Node.newCall("goog.isObject", Node.newNumberLiteral(1), null);
    JSType originalType = createType(node, typeRegistry);
    FlowScope scope = interpreter.restrictParameter(
        node.getLastChild(), originalType, null, interpreter.restricters.get("isObject"), false);
    JSType restrictedType = scope.getType();
    assertNull(restrictedType);
  }

  private static JSType getNativeType(JSType type) {
    return typeRegistry.getNativeType(type.getTypeName());
  }
}