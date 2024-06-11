import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.com.google.javascript.rhino.NodeOperations;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.jstype.Visitor.VisitorType;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static JSTypeRegistry typeRegistry;
  private static ClosureReverseAbstractInterpreter interpreter;
  private static Node testNode;
  private static FlowScope blindScope;

  @org.junit.BeforeClass
  public static void setUp() {
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    testNode = NodeOperations.createBlock(null);
    blindScope = new FlowScope(null);
  }

  @org.junit.Test
  public void testIsDef_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isDef",
        NodeOperations.createString("someVariable"));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(OBJECT_TYPE);
    JSType actualType = informedScope.getVariable("someVariable")
        .getType();
    assertEquals("Variable type should be Object when isDef is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsDef_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isDef",
        NodeOperations.createNull());
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someVariable")
        .getType();
    assertNull("Variable type should be null when isDef is false", expectedType);
    assertNull("Variable type should be null when isDef is false", actualType);
  }

  @org.junit.Test
  public void testIsDefAndNotNull_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isDefAndNotNull",
        NodeOperations.createString("someVariable"));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(OBJECT_TYPE);
    JSType actualType = informedScope.getVariable("someVariable")
        .getType();
    assertEquals("Variable type should be Object when isDefAndNotNull is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsDefAndNotNull_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isDefAndNotNull",
        NodeOperations.createNull());
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someVariable")
        .getType();
    assertNull("Variable type should be null when isDefAndNotNull is false",
        expectedType);
    assertNull("Variable type should be null when isDefAndNotNull is false",
        actualType);
  }

  @org.junit.Test
  public void testIsString_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isString",
        NodeOperations.createString("someString"));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(STRING);
    JSType actualType = informedScope.getVariable("someString")
        .getType();
    assertEquals("Variable type should be String when isString is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsString_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isString",
        NodeOperations.createNumber(42));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someString")
        .getType();
    assertNull("Variable type should be null when isString is false", expectedType);
    assertNull("Variable type should be null when isString is false", actualType);
  }

  @org.junit.Test
  public void testIsBoolean_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isBoolean",
        NodeOperations.createBoolean(true));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(BOOLEAN);
    JSType actualType = informedScope.getVariable("someBoolean")
        .getType();
    assertEquals("Variable type should be Boolean when isBoolean is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsBoolean_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isBoolean",
        NodeOperations.createNumber(42));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someBoolean")
        .getType();
    assertNull("Variable type should be null when isBoolean is false", expectedType);
    assertNull("Variable type should be null when isBoolean is false", actualType);
  }

  @org.junit.Test
  public void testIsNumber_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isNumber",
        NodeOperations.createNumber(42));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(NUMBER);
    JSType actualType = informedScope.getVariable("someNumber")
        .getType();
    assertEquals("Variable type should be Number when isNumber is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsNumber_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isNumber",
        NodeOperations.createString("someString"));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someNumber")
        .getType();
    assertNull("Variable type should be null when isNumber is false", expectedType);
    assertNull("Variable type should be null when isNumber is false", actualType);
  }

  @org.junit.Test
  public void testIsFunction_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isFunction",
        NodeOperations.createFunction(testNode, null, null, false));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(FUNCTION_TYPE);
    JSType actualType = informedScope.getVariable("someFunction")
        .getType();
    assertEquals("Variable type should be Function when isFunction is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsFunction_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isFunction",
        NodeOperations.createString("someString"));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someFunction")
        .getType();
    assertNull("Variable type should be null when isFunction is false", expectedType);
    assertNull("Variable type should be null when isFunction is false", actualType);
  }

  @org.junit.Test
  public void testIsArray_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isArray",
        NodeOperations.createArray(new Object() {}.getClass().getName()));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(ARRAY_TYPE);
    JSType actualType = informedScope.getVariable("someArray")
        .getType();
    assertEquals("Variable type should be Array when isArray is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsArray_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isArray",
        NodeOperations.createObject(ImmutableMap.of("name", "object")))
        .getFirstChild();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someArray")
        .getType();
    assertNull("Variable type should be null when isArray is false", expectedType);
    assertNull("Variable type should be null when isArray is false", actualType);
  }

  @org.junit.Test
  public void testIsObject_True() {
    Node condition = NodeOperations.createCall(testNode, "goog.isObject",
        NodeOperations.createObject(ImmutableMap.of("name", "object")));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedType = getNativeType(OBJECT_TYPE);
    JSType actualType = informedScope.getVariable("someObject")
        .getType();
    assertEquals("Variable type should be Object when isObject is true",
        expectedType, actualType);
  }

  @org.junit.Test
  public void testIsObject_False() {
    Node condition = NodeOperations.createCall(testNode, "goog.isObject",
        NodeOperations.createNumber(42));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someObject")
        .getType();
    assertNull("Variable type should be null when isObject is false", expectedType);
    assertNull("Variable type should be null when isObject is false", actualType);
  }

  private static JSType getNativeType(JSTypeType type) {
    return typeRegistry.getType(type);
  }
}