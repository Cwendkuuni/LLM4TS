package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.TypeRestriction;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType arrayType = getNativeType(ARRAY_TYPE);
    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(arrayType)).thenReturn(true);

    JSType result = interpreter.restrictToArrayVisitor.visit(objectType);
    assertSame(arrayType, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ObjectType objectType = mock(ObjectType.class);
    JSType arrayType = getNativeType(ARRAY_TYPE);
    when(objectType.isSubtype(arrayType)).thenReturn(false);

    JSType result = interpreter.restrictToNotArrayVisitor.visit(objectType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ObjectType objectType = mock(ObjectType.class);
    JSType nullType = getNativeType(NULL_TYPE);
    JSType functionType = mock(FunctionType.class);

    when(objectType.isSubtype(nullType)).thenReturn(false);
    when(objectType.isSubtype(functionType)).thenReturn(false);

    JSType result = interpreter.restrictToObjectVisitor.visit(objectType);
    assertSame(getNativeType(NO_OBJECT_TYPE), result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ObjectType objectType = mock(ObjectType.class);
    JSType allType = interpreter.createUnionType(
        getNativeType(NUMBER_STRING_BOOLEAN), getNativeType(NULL_VOID));

    when(objectType.isSubtype(getNativeType(ARRAY_TYPE))).thenReturn(false);
    when(objectType.isSubtype(getNativeType(FUNCTION_TYPE))).thenReturn(false);

    JSType result = interpreter.restrictToNotObjectVisitor.visit(objectType);
    assertSame(allType, result);
  }

  @Test
  public void testRestrictParameterWithBooleanOutcome() {
    Node parameter = mock(Node.class);
    JSType type = getNativeType(OBJECT_TYPE);
    JSType restrictedType = getNativeType(NUMBER_STRING_BOOLEAN);

    Function<TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isBoolean");

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.restrictParameter(
        parameter, type, blindScope, restriction, true);

    assertSame(restrictedType, result.getLocalType(parameter));
  }

  @Test
  public void testRestrictParameterWithFalseOutcome() {
    Node parameter = mock(Node.class);
    JSType type = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedType = getNativeType(NULL_VOID);

    Function<TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isNull");

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.restrictParameter(
        parameter, type, blindScope, restriction, false);

    assertSame(restrictedType, result.getLocalType(parameter));
  }
}