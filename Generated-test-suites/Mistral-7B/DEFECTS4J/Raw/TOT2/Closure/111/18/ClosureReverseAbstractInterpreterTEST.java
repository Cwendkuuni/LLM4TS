package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.TypeOf;
import com.google.javascript.rhino.jstype.TypeOperations;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), mock(JSTypeRegistry.class));
    Node conditionNode = NodeOperations.newCallNode("goog.isDef",
        NodeOperations.newQualifiedNameNode("someObj"), null);
    Node blindScope = NodeOperations.newObjectNode();
    JSType expectedType = TypeOf.OBJECT;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true).getScopeType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), mock(JSTypeRegistry.class));
    Node conditionNode = NodeOperations.newCallNode("goog.isNull",
        NodeOperations.newNullNode(), null);
    Node blindScope = NodeOperations.newObjectNode();
    JSType expectedType = TypeOf.VOID;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true).getScopeType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), mock(JSTypeRegistry.class));
    Node conditionNode = NodeOperations.newCallNode("goog.isDefAndNotNull",
        NodeOperations.newQualifiedNameNode("someObj"), null);
    Node blindScope = NodeOperations.newObjectNode();
    JSType expectedType = null;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true).getScopeType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsString() {
    // ...
  }

  @Test
  public void testIsBoolean() {
    // ...
  }

  @Test
  public void testIsNumber() {
    // ...
  }

  @Test
  public void testIsFunction() {
    // ...
  }

  @Test
  public void testIsArray() {
    // ...
  }

  @Test
  public void testIsObject() {
    // ...
  }

  @Test
  public void testIsDefWithNullCondition() {
    // ...
  }

  @Test
  public void testIsDefWithNullObject() {
    // ...
  }

  @Test
  public void testIsDefWithUndefinedObject() {
    // ...
  }

  @Test
  public void testIsDefWithFunctionType() {
    // ...
  }

  @Test
  public void testIsDefWithArrayType() {
    // ...
  }

  // ...
}