package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

/**
 * Test for ClosureReverseAbstractInterpreter class.
 */
public class ClosureReverseAbstractInterpreterTest {

  @Mock
  private JSTypeRegistry typeRegistry;
  @Mock
  private ObjectType objectType;
  @Mock
  private FunctionType functionType;
  @Mock
  private Node node;
  @Mock
  private CodingConvention codingConvention;

  @InjectMocks
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testIsDef() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction = new TypeRestriction(expected, true);
    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isDef"), true).getFlowScope().getScope();

    assertEquals(expected, result);
  }

  @Test
  public void testIsDefFalse() {
    JSType expected = null;
    TypeRestriction typeRestriction = new TypeRestriction(expected, false);
    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isDef"), false).getFlowScope().getScope();

    assertNull(result);
  }

  @Test
  public void testIsDefNullType() {
    JSType expected = null;
    TypeRestriction typeRestriction = new TypeRestriction(expected, true);
    when(typeRegistry.getObjectType(ArgumentMatchers.anyString())).thenReturn(objectType);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isDef"), true).getFlowScope().getScope();

    assertNull(result);
  }

  @Test
  public void testIsNull() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction = new TypeRestriction(expected, false);
    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isNull"), false).getFlowScope().getScope();

    assertEquals(expected, result);
  }

  @Test
  public void testIsNullTrue() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction = new TypeRestriction(expected, true);
    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isNull"), true).getFlowScope().getScope();

    assertNull(result);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction1 = new TypeRestriction(expected, true);
    TypeRestriction typeRestriction2 = new TypeRestriction(expected, false);

    JSType result1 = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isDefAndNotNull"), true).getFlowScope().getScope();

    assertEquals(expected, result1);

    JSType result2 = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isDefAndNotNull"), false).getFlowScope().getScope();

    assertNull(result2);
  }

  @Test
  public void testIsString() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction = new TypeRestriction(expected, true);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isString"), true).getFlowScope().getScope();

    assertEquals(expected, result);
  }

  @Test
  public void testIsStringFalse() {
    JSType expected = null;
    TypeRestriction typeRestriction = new TypeRestriction(expected, false);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isString"), false).getFlowScope().getScope();

    assertNull(result);
  }

  @Test
  public void testIsBoolean() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction = new TypeRestriction(expected, true);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isBoolean"), true).getFlowScope().getScope();

    assertEquals(expected, result);
  }

  @Test
  public void testIsBooleanFalse() {
    JSType expected = null;
    TypeRestriction typeRestriction = new TypeRestriction(expected, false);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isBoolean"), false).getFlowScope().getScope();

    assertNull(result);
  }

  @Test
  public void testIsNumber() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction = new TypeRestriction(expected, true);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isNumber"), true).getFlowScope().getScope();

    assertEquals(expected, result);
  }

  @Test
  public void testIsNumberFalse() {
    JSType expected = null;
    TypeRestriction typeRestriction = new TypeRestriction(expected, false);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isNumber"), false).getFlowScope().getScope();

    assertNull(result);
  }

  @Test
  public void testIsFunction() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);
    TypeRestriction typeRestriction = new TypeRestriction(expected, true);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isFunction"), true).getFlowScope().getScope();

    assertEquals(expected, result);
  }

  @Test
  public void testIsFunctionFalse() {
    JSType expected = null;
    TypeRestriction typeRestriction = new TypeRestriction(expected, false);

    JSType result = interpreter.restrictParameter(node, expected, null,
        interpreter.restricters.get("isFunction"), false).getFlowScope().getScope();

    assertNull(result);
  }

  @Test
  public void testIsArray() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);

    when(typeRegistry.getObjectType(ArgumentMatchers.eq("Array")))
        .thenReturn(objectType);

    Function<TypeRestriction, JSType> visitor = arg -> arg.outcome ?
        interpreter.restrictToArrayVisitor : interpreter.restrictToNotArrayVisitor;

    JSType result = expected.visit(visitor);

    if (expected != null) {
      assertEquals(interpreter.getNativeType(ARRAY_TYPE), result);
    } else {
      assertNull(result);
    }
  }

  @Test
  public void testIsObject() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);

    Function<TypeRestriction, JSType> visitor = arg -> arg.outcome ?
        interpreter.restrictToObjectVisitor : interpreter.restrictToNotObjectVisitor;

    JSType result = expected.visit(visitor);

    if (expected != null) {
      assertEquals(interpreter.getNativeType(OBJECT_TYPE), result);
    } else {
      assertNull(result);
    }
  }

  @Test(expected = AssertionError.class)
  public void testUnknownFunctionCall() {
    JSType expected = interpreter.getTypeIfRefinable(node, null);

    when(typeRegistry.getObjectType(ArgumentMatchers.anyString()))
        .thenReturn(functionType);

    Function<TypeRestriction, JSType> visitor = arg -> arg.outcome ?
        interpreter.restrictToObjectVisitor : interpreter.restrictToNotObjectVisitor;

    JSType result = expected.visit(visitor);

    assertNull(result);
  }
}