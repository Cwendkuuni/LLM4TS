package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.typecheck.TypeCheck;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;
import java.util.function.Function;

/**
 * Tests for {@link ClosureReverseAbstractInterpreter} class
 */
public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;
  private CodingConvention convention;
  private Function<Node, Type> nodeToType;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    convention = mock(CodingConvention.class);
    nodeToType = NodeOperations.getType;
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    Node condition = createCallNode("goog.isDef", Arrays.asList(createStringNode("foo")));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;

    when(typeRegistry.createUnionType(JSTypeNative.NUMBER_STRING_BOOLEAN, JSTypeNative.NULL_VOID))
        .thenReturn(expectedType);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = createCallNode("goog.isDef", Arrays.asList(createStringNode("foo")));
    when(typeRegistry.createUnionType(JSTypeNative.NUMBER_STRING_BOOLEAN, JSTypeNative.NULL_VOID))
        .thenReturn(JSTypeNative.VOID_TYPE);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_true() {
    Node condition = createCallNode("goog.isNull", Arrays.asList(createNumberNode(1)));
    JSType expectedType = JSTypeNative.NULL_TYPE;

    when(typeRegistry.getNativeType(JSTypeNative.NULL_VOID))
        .thenReturn(JSTypeNative.NULL_TYPE);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
    Node condition = createCallNode("goog.isNull", Arrays.asList(createNumberNode(1)));
    JSType expectedType = JSTypeNative.VOID_TYPE;

    when(typeRegistry.getNativeType(JSTypeNative.NULL_VOID))
        .thenReturn(expectedType);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    Node condition = createCallNode("goog.isDefAndNotNull", Arrays.asList(createStringNode("foo")));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;

    when(typeRegistry.createUnionType(JSTypeNative.NUMBER_STRING_BOOLEAN, JSTypeNative.NULL_VOID))
        .thenReturn(JSTypeNative.BOOLEAN_TYPE);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
    Node condition = createCallNode("goog.isDefAndNotNull", Arrays.asList(createStringNode("foo")));
    when(typeRegistry.createUnionType(JSTypeNative.NUMBER_STRING_BOOLEAN, JSTypeNative.NULL_VOID))
        .thenReturn(JSTypeNative.VOID_TYPE);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    Node condition = createCallNode("goog.isArray", Arrays.asList(createStringNode("[]")));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;

    when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE))
        .thenReturn(expectedType);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = createCallNode("goog.isArray", Arrays.asList(createStringNode("[]")));
    when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE))
        .thenReturn(JSTypeNative.VOID_TYPE);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    Node condition = createCallNode("goog.isObject", Arrays.asList(createStringNode("{}")));
    JSType expectedType = JSTypeNative.OBJECT_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
    Node condition = createCallNode("goog.isObject", Arrays.asList(createStringNode("{}")));
    JSType expectedType = JSTypeNative.VOID_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
    Node condition = createCallNode("goog.isFunction", Arrays.asList(createStringNode("function(){}")));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction_false() {
    Node condition = createCallNode("goog.isFunction", Arrays.asList(createStringNode("function(){}")));
    JSType expectedType = JSTypeNative.VOID_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString() {
    Node condition = createCallNode("goog.isString", Arrays.asList(createStringNode("'foo'")));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString_false() {
    Node condition = createCallNode("goog.isString", Arrays.asList(createStringNode("'foo'")));
    JSType expectedType = JSTypeNative.VOID_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
    Node condition = createCallNode("goog.isBoolean", Arrays.asList(createStringNode("true")));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean_false() {
    Node condition = createCallNode("goog.isBoolean", Arrays.asList(createStringNode("true")));
    JSType expectedType = JSTypeNative.VOID_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
    Node condition = createCallNode("goog.isNumber", Arrays.asList(createStringNode("1")));
    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(expectedType, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber_false() {
    Node condition = createCallNode("goog.isNumber", Arrays.asList(createStringNode("1")));
    JSType expectedType = JSTypeNative.VOID_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(result.getType());
  }

  private Node createCallNode(String calleeName, List<Node> arguments) {
    CallExpression call = new CallExpression();
    Node callee = new NameExpression();
    callee.setName(calleeName);
    call.setCallee(callee);
    call.setArguments(arguments);
    return call;
  }

  private Node createStringNode(String value) {
    StringLiteral expression = new StringLiteral();
    expression.setValue(value);
    return expression;
  }

  private Node createNumberNode(int value) {
    NumberLiteral expression = new NumberLiteral();
    expression.setValue(value);
    return expression;
  }
}