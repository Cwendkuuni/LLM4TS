package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClosureReverseAbstractInterpreterTest {
  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;
  private Visitor<JSType> restrictToArrayVisitor;
  private Visitor<JSType> restrictToNotArrayVisitor;
  private Visitor<JSType> restrictToObjectVisitor;
  private Visitor<JSType> restrictToNotObjectVisitor;

  @Before
  public void setup() {
    this.convention = mock(CodingConvention.class);
    this.typeRegistry = mock(JSTypeRegistry.class);
    this.interpreter = new ClosureReverseAbstractInterpreter(this.convention, this.typeRegistry);

    this.restrictToArrayVisitor = this.interpreter.restrictToArrayVisitor;
    this.restrictToNotArrayVisitor = this.interpreter.restrictToNotArrayVisitor;
    this.restrictToObjectVisitor = this.interpreter.restrictToObjectVisitor;
    this.restrictToNotObjectVisitor = this.interpreter.restrictToNotObjectVisitor;

    when(this.typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(JSTypeNative.ARRAY_TYPE);
    when(this.typeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(JSTypeNative.NO_OBJECT_TYPE);
    when(this.typeRegistry.getNativeType(JSTypeNative.NULL_TYPE)).thenReturn(JSTypeNative.NULL_TYPE);
    when(this.typeRegistry.getNativeType(JSTypeNative.NULL_VOID)).thenReturn(JSTypeNative.NULL_VOID);
    when(this.typeRegistry.getNativeType(JSTypeNative.NUMBER_STRING_BOOLEAN)).thenReturn(JSTypeNative.NUMBER_STRING_BOOLEAN);
    when(this.typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(JSTypeNative.OBJECT_TYPE);
    when(this.typeRegistry.getNativeType(JSTypeNative.VOID_TYPE)).thenReturn(JSTypeNative.VOID_TYPE);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    JSType type = JSTypeNative.NUMBER_STRING_BOOLEAN;
    Node condition = createCallNode("goog.isDef", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = true;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.BOOLEAN);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    JSType type = JSTypeNative.NUMBER_STRING_BOOLEAN;
    Node condition = createCallNode("goog.isDef", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = false;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.VOID_TYPE);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
    JSType type = JSTypeNative.NUMBER_STRING_BOOLEAN;
    Node condition = createCallNode("goog.isNull", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = true;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.BOOLEAN);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
    JSType type = JSTypeNative.NUMBER_STRING_BOOLEAN;
    Node condition = createCallNode("goog.isNull", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = false;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.NULL_TYPE);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    JSType type = JSTypeNative.NUMBER_STRING_BOOLEAN;
    Node condition = createCallNode("goog.isDefAndNotNull", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = true;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.BOOLEAN);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
    JSType type = JSTypeNative.NUMBER_STRING_BOOLEAN;
    Node condition = createCallNode("goog.isDefAndNotNull", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = false;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.NULL_VOID);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
    Node condition = createCallNode("goog.isArray", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = true;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.BOOLEAN);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
    Node condition = createCallNode("goog.isArray", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = false;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.NULL_TYPE);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
    Node condition = createCallNode("goog.isObject", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = true;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = type;
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
    Node condition = createCallNode("goog.isObject", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = false;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.NULL_VOID);
    assertEquals(expectedType, informedScope.getTopType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_nonMatchingCondition() {
    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
    Node condition = createCallNode("goog.isString", type, null);

    FlowScope blindScope = createBlindFlowScope();
    boolean outcome = true;

    FlowScope informedScope = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    JSType expectedType = getNativeType(JSTypeNative.NULL_VOID);
    assertEquals(expectedType, informedScope.getTopType());
  }

  private Node createCallNode(String calleeName, JSType type, Node[] args) {
    Node callee = new Node_Call(null);
    callee.setClassName("goog");
    callee.setPropertyName(calleeName);

    callee.setFirstChild(new Node_Name(null, calleeName));
    callee.setLastChild(new Node_NewArray(null, args.length));

    for (Node arg : args) {
      callee.getLastChild().addChild(arg);
    }

    callee.setType(new ObjectType(type));
    return callee;
  }

  private FlowScope createBlindFlowScope() {
    return new FlowScope(this.convention, this.typeRegistry) {
      @Override
      public void declareNameInScope(String name, JSType type) {
        // No-op
      }

      @Override
      public void declareNameInScope(String name, JSType type, int index) {
        // No-op
      }

      @Override
      public void declareNameInScope(String name, JSType type, Node node) {
        // No-op
      }

      @Override
      public void declareNameInScope(String name, Node node) {
        // No-op
      }
    };
  }

  private JSType getNativeType(int nativeTypeCode) {
    return this.typeRegistry.getNativeType(nativeTypeCode);
  }

  private JSType getNativeType(String nativeTypeName) {
    return this.typeRegistry.getNativeType(nativeTypeName);
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedTypeName, boolean outcome) {
    return this.interpreter.restrictToObjectVisitor.caseObjectType(
        new ObjectType(type)).isSubtype(getNativeType(expectedTypeName)) ?
        getNativeType(expectedTypeName) : null;
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    return this.interpreter.restrictToNotObjectVisitor.caseAllType().getGreatestSubtype(type);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    return this.interpreter.restrictToNotArrayVisitor.caseTopType(type);
  }
}

class Node_Call extends Node {
  // Empty constructor
}

class Node_Name extends Node {
  // Empty constructor
}

class Node_NewArray extends Node {
  // Empty constructor
}