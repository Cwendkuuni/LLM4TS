import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createPropertyName;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.VOID_TYPE;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private final JSTypeRegistry typeRegistry;
  private final CodingConvention convention;
  private final ClosureReverseAbstractInterpreter interpreter;

  public ClosureReverseAbstractInterpreterTest() {
    typeRegistry = new JSTypeRegistry();
    convention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @org.junit.Test
  public void testIsDef() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isDef"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertTrue(expectedType != null);
    assertTrue(expectedType.isSubtype(interpreter.getNativeType(NUMBER_STRING_BOOLEAN)));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isDef"),
        createIdentifier("test"),
        createBoolean(false));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);
  }

  @org.junit.Test
  public void testIsDefAndNotNull() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isDefAndNotNull"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);

    Node test = createObjectLiteral();
    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isDefAndNotNull"),
        test);
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertTrue(expectedType != null);
    assertTrue(expectedType.isSubtype(interpreter.getNativeType(NULL_VOID)));
  }

  @org.junit.Test
  public void testIsFunction() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isFunction"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    if (expectedType != null) {
      FunctionType functionType = (FunctionType) expectedType;
      assertTrue(functionType.getParameterList().isEmpty());
      assertTrue(functionType.getReturnType().isSubtype(VOID_TYPE));
    }

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isFunction"),
        createIdentifier("test"),
        createBoolean(false));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);
  }

  @org.junit.Test
  public void testIsArray() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isArray"),
        createArrayLiteral());
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertTrue(expectedType != null);
    assertTrue(expectedType.isSubtype(interpreter.getNativeType(ARRAY_TYPE)));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isArray"),
        createBoolean(false),
        createIdentifier("test"));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);
  }

  @org.junit.Test
  public void testIsObject() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isObject"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    if (expectedType != null) {
      ObjectType objectType = (ObjectType) expectedType;
      assertTrue(objectType.getDeclaredMembers().isEmpty());
    }

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isObject"),
        createIdentifier("test"),
        createBoolean(false));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);
  }

  @org.junit.Test
  public void testIsString() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isString"),
        createStringLiteral("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertTrue(expectedType != null);
    assertTrue(expectedType.isSubtype(interpreter.getNativeType(JSType.STRING)));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isString"),
        createStringLiteral("test"),
        createBoolean(false));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);
  }

  @org.junit.Test
  public void testIsBoolean() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isBoolean"),
        createBoolean(true));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertTrue(expectedType != null);
    assertTrue(expectedType.isSubtype(interpreter.getNativeType(JSType.BOOLEAN)));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isBoolean"),
        createBoolean(false));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);
  }

  @org.junit.Test
  public void testIsNull() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isNull"),
        createNull());
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertTrue(expectedType != null);
    assertTrue(expectedType.isSubtype(interpreter.getNativeType(NULL_TYPE)));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isNull"),
        createIdentifier("test"));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    assertNull(expectedType);
  }

  @org.junit.Test
  public void testRestrictToArray() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isArray"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    JSType resultType = interpreter.restrictToArray(expectedType);

    assertTrue(expectedType != null);
    assertTrue(resultType != null);
    assertTrue(resultType.isSubtype(interpreter.getNativeType(ARRAY_TYPE)));
    assertEquals(expectedType, resultType.getGreatestSubtype(expectedType));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isArray"),
        createBoolean(false),
        createIdentifier("test"));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    resultType = interpreter.restrictToArray(expectedType);

    assertNull(resultType);
  }

  @org.junit.Test
  public void testRestrictToObject() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isObject"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    JSType resultType = interpreter.restrictToObject(expectedType);

    assertTrue(expectedType != null);
    assertTrue(resultType != null);
    assertTrue(resultType.isSubtype(interpreter.getNativeType(JSType.OBJECT)));
    assertEquals(expectedType, resultType.getGreatestSubtype(expectedType));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isObject"),
        createIdentifier("test"),
        createBoolean(false));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    resultType = interpreter.restrictToObject(expectedType);

    assertNull(resultType);
  }

  @org.junit.Test
  public void testRestrictToNotArray() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isArray"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    JSType resultType = interpreter.restrictToNotArray(expectedType);

    assertTrue(expectedType != null);
    assertTrue(resultType != null);
    assertTrue(resultType.isNotSubtype(interpreter.getNativeType(ARRAY_TYPE)));
    assertEquals(expectedType, resultType.getGreatestSubtype(expectedType));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isArray"),
        createBoolean(false),
        createIdentifier("test"));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    resultType = interpreter.restrictToNotArray(expectedType);

    assertNull(resultType);
  }

  @org.junit.Test
  public void testRestrictToNotObject() {
    Node condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isObject"),
        createIdentifier("test"));
    JSType expectedType = interpreter.getType(condition, VOID_TYPE, null);

    JSType resultType = interpreter.restrictToNotObject(expectedType);

    assertTrue(expectedType != null);
    assertTrue(resultType != null);
    assertTrue(resultType.isSubtype(interpreter.getNativeType(JSType.VOID)));
    assertEquals(expectedType, resultType.getGreatestSubtype(expectedType));

    condition = createCallExpression(
        createIdentifier("goog"),
        createPropertyName("isObject"),
        createIdentifier("test"),
        createBoolean(false));
    expectedType = interpreter.getType(condition, VOID_TYPE, null);

    resultType = interpreter.restrictToNotObject(expectedType);

    assertNull(resultType);
  }

  private Node createCallExpression(Node... nodes) {
    Node node = createCallExpression();
    for (Node n : nodes) {
      node.appendChild(n);
    }
    return node;
  }

  private Node createCallExpression() {
    return new CallExpression();
  }

  private Node createArrayLiteral() {
    return new ArrayLiteral();
  }

  private Node createObjectLiteral() {
    return new ObjectLiteral();
  }

  private Node createIdentifier(String name) {
    return new Identifier(name);
  }

  private Node createPropertyName(String name) {
    return new PropertyName(name);
  }

  private Node createStringLiteral(String value) {
    return new StringLiteral(value);
  }

  private Node createNull() {
    return new NullLiteral();
  }

  private Node createBoolean(boolean value) {
    return new BooleanLiteral(value);
  }
}