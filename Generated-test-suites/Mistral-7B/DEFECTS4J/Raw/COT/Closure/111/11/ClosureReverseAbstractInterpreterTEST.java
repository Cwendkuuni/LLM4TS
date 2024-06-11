package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

/**
 * Tests for ClosureReverseAbstractInterpreter class
 */
public class ClosureReverseAbstractInterpreterTest {

  

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isDef", Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createObjectType("String");
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected 'String' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isNull", Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createNullType();
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(JSType.class);
    assertEquals("Expected 'null' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isDefAndNotNull",
        Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createObjectType("String");
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected 'String' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isString",
        Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createObjectType("String");
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected 'String' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isBoolean",
        Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createBooleanType();
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(JSType.class);
    assertEquals("Expected 'boolean' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isNumber",
        Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createNumberType();
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(JSType.class);
    assertEquals("Expected 'number' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isFunction",
        Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createFunctionType(
        ImmutableMap.of(), typeRegistry.createVoidType());
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(FunctionType.class);
    assertEquals("Expected 'function' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isArray", Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createArrayType();
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected 'Array' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true.asInstanceOf(ObjectType.class)
            .getBaseType());

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node conditionNode = Node.newCall(null, "goog.isObject", Node.newName("obj"));

    FlowScope blindScope = new FlowScope();

    // Test when outcome is true
    JSType expectedResultType_true = typeRegistry.createObjectType("TestObject");
    FlowScope informedScope_true = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);
    Type actualResultType_true = informedScope_true.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected 'TestObject' but was: " + actualResultType_true,
        expectedResultType_true, actualResultType_true);

    // Test when outcome is false
    JSType expectedResultType_false = null;
    FlowScope informedScope_false = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);
    Type actualResultType_false = informedScope_false.getVariable("obj")
        .getType().asInstanceOf(ObjectType.class);
    assertEquals("Expected null but was: " + actualResultType_false,
        expectedResultType_false, actualResultType_false);
  }

  
}