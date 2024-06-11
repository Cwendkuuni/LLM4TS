package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.Visitor.VisitorType;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    JSType arrayType = mock(JSType.class);
    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;

    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(arrayType)).thenReturn(true);

    assertSame(arrayType, visitor.caseObjectType(objectType));

    ObjectType notArrayType = mock(ObjectType.class);
    when(notArrayType.isSubtype(arrayType)).thenReturn(false);

    assertNull(visitor.caseObjectType(notArrayType));
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    JSType arrayType = mock(JSType.class);
    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;

    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(arrayType)).thenReturn(true);

    assertNull(visitor.caseObjectType(objectType));

    ObjectType notArrayType = mock(ObjectType.class);
    when(notArrayType.isSubtype(arrayType)).thenReturn(false);

    assertSame(notArrayType, visitor.caseObjectType(notArrayType));
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;

    ObjectType objectType = mock(ObjectType.class);

    FunctionType functionType = mock(FunctionType.class);
    when(objectType.isSubtype(functionType)).thenReturn(true);

    assertSame(objectType, visitor.caseObjectType(objectType));

    assertSame(objectType, visitor.caseFunctionType(functionType));
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;

    ObjectType objectType = mock(ObjectType.class);

    FunctionType functionType = mock(FunctionType.class);
    when(objectType.isSubtype(functionType)).thenReturn(true);

    assertNull(visitor.caseObjectType(objectType));

    assertNull(visitor.caseFunctionType(functionType));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    Node condition = mock(Node.class);
    Node callee = mock(Node.class);
    Node param = mock(Node.class);

    when(callee.getFirstChild()).thenReturn(mock(Node.class));
    when(callee.getLastChild()).thenReturn(param);
    when(condition.getFirstChild()).thenReturn(callee);
    when(condition.getLastChild()).thenReturn(mock(Node.class));
    when(condition.getType()).thenReturn(CALL);
    when(condition.getChildCount()).thenReturn(2);

    JSType type = mock(JSType.class);

    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(type)).thenReturn(true);

    when(param.isQualifiedName()).thenReturn(true);
    when(param.getType()).thenReturn(null);

    when(getTypeIfRefinable(param, null)).thenReturn(objectType);

    when(callee.getFirstChild().getType()).thenReturn(NAME);
    when(callee.getFirstChild().getString()).thenReturn("goog");
    when(callee.getLastChild().getType()).thenReturn(STRING);

    Function<TypeRestriction, JSType> restriction = interpreter.restricters.get("isObject");

    FlowScope informedScope = mock(FlowScope.class);

    ClosureReverseAbstractInterpreter.TypeRestriction typeRestriction = new ClosureReverseAbstractInterpreter.TypeRestriction(type, true);

    Node restrictedParameter = mock(Node.class);

    interpreter.declareNameInScope(informedScope, restrictedParameter, type);

    when(restriction.apply(typeRestriction)).thenReturn(objectType);

    FlowScope result = interpreter.restrictParameter(param, type, null, restriction, true);

    assertSame(informedScope, result);
  }
}