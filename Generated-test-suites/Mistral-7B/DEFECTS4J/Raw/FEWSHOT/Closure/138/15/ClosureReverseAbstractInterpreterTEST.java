package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;

  @Before
  public void setup() {
    convention = mock(CodingConvention.class);
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testRestrictToArray() {
    JSType originalType = getMockType(ObjectType.class, "Array<number>");
    when(typeRegistry.getTypeFromName("Array")).thenReturn(getNativeType(ARRAY_TYPE));

    JSType expectedType = getNativeType(ARRAY_TYPE);
    JSType result = interpreter.restrictToArrayVisitor.caseObjectType(
        (ObjectType) originalType.visit(interpreter.restrictToArrayVisitor));

    assertSame(expectedType, result);
  }

  @Test
  public void testRestrictToNotArray() {
    JSType originalType = getMockType(ObjectType.class, "Object");

    JSType expectedType = getMockType(ObjectType.class, "Object");
    JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(
        (ObjectType) originalType.visit(interpreter.restrictToNotArrayVisitor));

    assertSame(expectedType, result);
  }

  @Test
  public void testRestrictToObject() {
    JSType originalType = getMockType(FunctionType.class, "() -> void");

    JSType expectedType = getNativeType(OBJECT_TYPE);
    JSType result = interpreter.restrictToObjectVisitor.caseFunctionType(
        (FunctionType) originalType.visit(interpreter.restrictToObjectVisitor));

    assertSame(expectedType, result);
  }

  @Test
  public void testRestrictToNotObject() {
    JSType originalType = getMockType(ObjectType.class, "Object");

    JSType expectedType = null;
    JSType result = interpreter.restrictToNotObjectVisitor.caseObjectType(
        (ObjectType) originalType.visit(interpreter.restrictToNotObjectVisitor));

    assertNull(result);
  }

  @Test
  public void testRestrictIsDef() {
    JSType originalType = getMockType(ObjectType.class, "Object");

    JSType expectedType = getRestrictedWithoutUndefined(originalType);

    Function<TypeRestriction, JSType> restricter = interpreter.restricters.get(
        "isDef");
    JSType result = restricter.apply(new TypeRestriction(originalType, true));

    assertSame(expectedType, result);
  }

  @Test
  public void testRestrictIsNotNull() {
    JSType originalType = getMockType(ObjectType.class, "Object");

    JSType expectedType = getNativeType(NO_OBJECT_TYPE);

    Function<TypeRestriction, JSType> restricter = interpreter.restricters.get(
        "isNull");
    JSType result = restricter.apply(new TypeRestriction(originalType, false));

    assertSame(expectedType, result);
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    JSType originalType = getMockType(ObjectType.class, "Object");

    JSType expectedType = getRestrictedWithoutUndefined(
        getRestrictedWithoutNull(originalType));

    Function<TypeRestriction, JSType> restricter = interpreter.restricters.get(
        "isDefAndNotNull");
    JSType result = restricter.apply(new TypeRestriction(originalType, true));

    assertSame(expectedType, result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = createMockNode(CALL, 2,
        createMockNode(GETPROP, "goog", "isDef"),
        createMockNode(STRING, "someVariable"));

    FlowScope blindScope = mock(FlowScope.class);
    boolean outcome = true;

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);

    // There should be no difference between the original and informed scopes.
    assertSame(blindScope, informedScope);
  }

  @Test
  public void testRestrictParameter() {
    Node parameter = createMockNode(NAME, "someVariable");
    JSType originalType = getMockType(ObjectType.class, "Object");
    FlowScope blindScope = mock(FlowScope.class);
    Function<TypeRestriction, JSType> restricter = mock(Function.class);
    when(restricter.apply(any(TypeRestriction.class))).thenReturn(
        getMockType(ObjectType.class, "Object"));

    interpreter.restricters.put("isDef", restricter);

    JSType expectedType = getMockType(ObjectType.class, "Object");
    interpreter.restrictParameter(parameter, originalType, blindScope, restricter,
        true);

    // The informed scope should contain the restricted type.
    FlowScope informedScope = mock(FlowScope.class);
    declareNameInScope(informedScope, parameter, expectedType);
    verify(blindScope).createChildFlowScope();
    verify(blindScope).declareNameInScope(eq(parameter), eq(expectedType));
  }
}