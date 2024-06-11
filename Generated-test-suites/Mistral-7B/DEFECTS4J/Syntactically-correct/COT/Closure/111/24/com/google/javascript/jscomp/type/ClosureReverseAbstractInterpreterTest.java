package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor.VisitorType;
import com.google.javascript.rhino.jstype.Visitor.VisitorWithResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

/**
 * Test for {@link ClosureReverseAbstractInterpreter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
  private static final CodingConvention codingConvention = Mockito.mock(CodingConvention.class);

  private ClosureReverseAbstractInterpreter interpreter;
  private Map<String, Function<TypeRestriction, JSType>> restricters;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    restricters = interpreter.restricters;
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_True_IsDef() {
    Node condition = Mockito.mock(Node.class);
    Node parameter = Mockito.mock(Node.class);
    JSType paramType = Mockito.mock(JSType.class);
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Mockito.when(condition.getChildCount()).thenReturn(2);
    Mockito.when(condition.getFirstChild()).thenReturn(left);
    Mockito.when(condition.getLastChild()).thenReturn(right);
    Mockito.when(left.isName()).thenReturn(true);
    Mockito.when(left.getString()).thenReturn("goog");
    Mockito.when(right.isString()).thenReturn(true);
    Mockito.when(right.getString()).thenReturn("isDef");
    Mockito.when(getTypeIfRefinable(parameter, Mockito.mock(FlowScope.class))).thenReturn(paramType);
    Mockito.when(left.isGetProp()).thenReturn(true);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);

    Function<TypeRestriction, JSType> restricter = restricters.get("isDef");
    JSType expectedResult = restricter.apply(new TypeRestriction(paramType, true));
    assertSame(expectedResult, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_False_IsDef() {
    Node condition = Mockito.mock(Node.class);
    Node parameter = Mockito.mock(Node.class);
    JSType paramType = Mockito.mock(JSType.class);
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Mockito.when(condition.getChildCount()).thenReturn(2);
    Mockito.when(condition.getFirstChild()).thenReturn(left);
    Mockito.when(condition.getLastChild()).thenReturn(right);
    Mockito.when(left.isName()).thenReturn(true);
    Mockito.when(left.getString()).thenReturn("goog");
    Mockito.when(right.isString()).thenReturn(true);
    Mockito.when(right.getString()).thenReturn("isDef");
    Mockito.when(getTypeIfRefinable(parameter, Mockito.mock(FlowScope.class))).thenReturn(paramType);
    Mockito.when(left.isGetProp()).thenReturn(true);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);

    assertNull(result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_True_IsString() {
    Node condition = Mockito.mock(Node.class);
    Node parameter = Mockito.mock(Node.class);
    JSType paramType = Mockito.mock(JSType.class);
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Mockito.when(condition.getChildCount()).thenReturn(2);
    Mockito.when(condition.getFirstChild()).thenReturn(left);
    Mockito.when(condition.getLastChild()).thenReturn(right);
    Mockito.when(left.isName()).thenReturn(true);
    Mockito.when(left.getString()).thenReturn("goog");
    Mockito.when(right.isString()).thenReturn(true);
    Mockito.when(right.getString()).thenReturn("isString");
    Mockito.when(getTypeIfRefinable(parameter, Mockito.mock(FlowScope.class))).thenReturn(paramType);
    Mockito.when(left.isGetProp()).thenReturn(true);

    Function<TypeRestriction, JSType> restricter = restricters.get("isString");
    JSType expectedResult = restricter.apply(new TypeRestriction(paramType, true));
    Mockito.when(restricter.apply(Mockito.any())).thenReturn(expectedResult);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);

    assertSame(expectedResult, result.getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_False_IsString() {
    Node condition = Mockito.mock(Node.class);
    Node parameter = Mockito.mock(Node.class);
    JSType paramType = Mockito.mock(JSType.class);
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Mockito.when(condition.getChildCount()).thenReturn(2);
    Mockito.when(condition.getFirstChild()).thenReturn(left);
    Mockito.when(condition.getLastChild()).thenReturn(right);
    Mockito.when(left.isName()).thenReturn(true);
    Mockito.when(left.getString()).thenReturn("goog");
    Mockito.when(right.isString()).thenReturn(true);
    Mockito.when(right.getString()).thenReturn("isString");
    Mockito.when(getTypeIfRefinable(parameter, Mockito.mock(FlowScope.class))).thenReturn(paramType);
    Mockito.when(left.isGetProp()).thenReturn(true);

    Function<TypeRestriction, JSType> restricter = restricters.get("isString");
    JSType expectedResult = restricter.apply(new TypeRestriction(paramType, false));
    Mockito.when(restricter.apply(Mockito.any())).thenReturn(expectedResult);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);

    assertSame(expectedResult, result.getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetPreciserScopeKnowingConditionOutcome_InvalidFunction() {
    Node condition = Mockito.mock(Node.class);
    Node parameter = Mockito.mock(Node.class);
    JSType paramType = Mockito.mock(JSType.class);
    Node left = Mockito.mock(Node.class);
    Node right = Mockito.mock(Node.class);
    Mockito.when(condition.getChildCount()).thenReturn(2);
    Mockito.when(condition.getFirstChild()).thenReturn(left);
    Mockito.when(condition.getLastChild()).thenReturn(right);
    Mockito.when(left.isName()).thenReturn(true);
    Mockito.when(left.getString()).thenReturn("goog");
    Mockito.when(right.isString()).thenReturn(true);
    Mockito.when(right.getString()).thenReturn("invalidFunction");
    Mockito.when(getTypeIfRefinable(parameter, Mockito.mock(FlowScope.class))).thenReturn(paramType);
    Mockito.when(left.isGetProp()).thenReturn(true);

    interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
  }

  private JSType getNativeType(VisitorType type) {
    if (type == ARRAY_TYPE) {
      return JSTypeNative.ARRAY_TYPE;
    } else if (type == FUNCTION_TYPE) {
      return JSTypeNative.FUNCTION_TYPE;
    } else if (type == NO_OBJECT_TYPE) {
      return JSTypeNative.NO_OBJECT_TYPE;
    } else if (type == NULL_TYPE) {
      return JSTypeNative.NULL_TYPE;
    } else if (type == NULL_VOID) {
      return JSTypeNative.NULL_VOID;
    } else if (type == NUMBER_STRING_BOOLEAN) {
      return JSTypeNative.NUMBER_STRING_BOOLEAN;
    } else if (type == OBJECT_TYPE) {
      return JSTypeNative.OBJECT_TYPE;
    } else if (type == VOID_TYPE) {
      return JSTypeNative.VOID_TYPE;
    }

    throw new UnsupportedOperationException();
  }

  private JSType getTypeIfRefinable(Node node, FlowScope scope) {
    Visitor visitor = new FunctionWithResultVisitor() {
      @Override
      public JSType visitObjectType(ObjectType type, VisitorWithResult next) {
        return interpreter.getType(node, scope, type, next);
      }

      @Override
      public JSType visitFunctionType(FunctionType type, VisitorWithResult next) {
        return interpreter.getType(node, scope, type, next);
      }
    };

    return visitor.visit(scope.getType(node), null);
  }

  private static class FunctionWithResultVisitor extends Visitor {
    @Override
    public JSType visit(JSType type, VisitorWithResult next) {
      if (type instanceof ObjectType || type instanceof FunctionType) {
        return type;
      }

      next.visit(type, null);
      return null;
    }
  }
}