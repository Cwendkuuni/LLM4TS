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
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.TypeChecker;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private static final JSTypeNative ARRAY_TYPE = new JSTypeNative(JSType.ARRAY_TYPE);
  private static final JSTypeNative NO_OBJECT_TYPE = new JSTypeNative(JSType.NO_OBJECT_TYPE);
  private static final JSTypeNative NULL_TYPE = new JSTypeNative(JSType.NULL_TYPE);
  private static final JSTypeNative OBJECT_TYPE = new JSTypeNative(JSType.OBJECT_TYPE);
  private static final JSTypeNative UNDEFINED_TYPE = new JSTypeNative(JSType.UNDEFINED);

  private static class RestrictByTrueTypeOfResultVisitor
      extends Visitor<JSType> {
    @Override
    protected JSType caseTopType(JSType topType) {
      return topType;
    }

    public abstract JSType caseObjectType(ObjectType type);
  }

  private static class RestrictByFalseTypeOfResultVisitor
      extends RestrictByTrueTypeOfResultVisitor {
    @Override
    public JSType caseObjectType(ObjectType type) {
      return null;
    }
  }

  private static class TypeRestriction {
    private final JSType type;
    private final boolean outcome;

    private TypeRestriction(JSType type, boolean outcome) {
      this.type = type;
      this.outcome = outcome;
    }
  }

  private static class DummyTypeRegistry implements JSTypeRegistry {
    @Override
    public JSType getNativeType(JSType type) {
      switch (type) {
        case ARRAY_TYPE: return ARRAY_TYPE;
        case NO_OBJECT_TYPE: return NO_OBJECT_TYPE;
        case NULL_TYPE: return NULL_TYPE;
        case OBJECT_TYPE: return OBJECT_TYPE;
        default: return UNDEFINED_TYPE;
      }
    }

    @Override
    public FunctionType getFunctionType(Node node, int argsLength) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ObjectType getObjectType(String name, Map<String, JSType> properties) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addType(JSType type) {
      // Do nothing
    }
  }

  private static class DummyTypeChecker implements TypeChecker {
    @Override
    public JSType checkType(Node node, JSType type, boolean isOptional) {
      return type;
    }

    @Override
    public JSType checkType(Node node, JSType type) {
      return type;
    }
  }

  private ClosureReverseAbstractInterpreter interpreter;
  private DummyTypeRegistry typeRegistry;
  private DummyTypeChecker typeChecker;

  @Before
  public void setup() {
    typeRegistry = new DummyTypeRegistry();
    typeChecker = new DummyTypeChecker();
    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType arrayType = typeRegistry.getNativeType(JSType.ARRAY_TYPE);
    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(arrayType)).thenReturn(true);

    RestrictByTrueTypeOfResultVisitor visitor =
        new RestrictByTrueTypeOfResultVisitor() {
          @Override
          public JSType caseObjectType(ObjectType type) {
            return arrayType;
          }
        };

    JSType result = interpreter.new Visitor<JSType>() {
      @Override
      public JSType visitObjectType(ObjectType type) {
        return type.visit(visitor);
      }
    }.visitObjectType(objectType);

    assertSame(arrayType, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSType arrayType = typeRegistry.getNativeType(JSType.ARRAY_TYPE);
    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(arrayType)).thenReturn(false);

    RestrictByFalseTypeOfResultVisitor visitor =
        new RestrictByFalseTypeOfResultVisitor() {};

    JSType result = interpreter.new Visitor<JSType>() {
      @Override
      public JSType visitObjectType(ObjectType type) {
        return type.visit(visitor);
      }
    }.visitObjectType(objectType);

    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ObjectType objectType = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);
    when(objectType.isSubtype(FUNCTION_TYPE)).thenReturn(false);

    RestrictByTrueTypeOfResultVisitor visitor =
        new RestrictByTrueTypeOfResultVisitor() {
          @Override
          protected JSType caseTopType(JSType topType) {
            return NO_OBJECT_TYPE;
          }

          @Override
          public JSType caseObjectType(ObjectType type) {
            return type;
          }

          @Override
          public JSType caseFunctionType(FunctionType type) {
            return type;
          }
        };

    JSType result = interpreter.new Visitor<JSType>() {
      @Override
      public JSType visitObjectType(ObjectType type) {
        return type.visit(visitor);
      }

      @Override
      public JSType visitFunctionType(FunctionType type) {
        return type.visit(visitor);
      }
    }.visitObjectType(objectType);

    assertSame(objectType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ObjectType objectType = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);

    RestrictByFalseTypeOfResultVisitor visitor =
        new RestrictByFalseTypeOfResultVisitor() {};

    JSType result = interpreter.new Visitor<JSType>() {
      @Override
      public JSType visitObjectType(ObjectType type) {
        return type.visit(visitor);
      }

      @Override
      public JSType visitFunctionType(FunctionType type) {
        return type.visit(visitor);
      }
    }.visitObjectType(objectType);

    assertNull(result);

    result = interpreter.new Visitor<JSType>() {
      @Override
      public JSType visitObjectType(ObjectType type) {
        return type.visit(visitor);
      }

      @Override
      public JSType visitFunctionType(FunctionType type) {
        return type.visit(visitor);
      }
    }.visitFunctionType(functionType);

    assertNull(result);
  }

  @Test
  public void testRestrictParameterWithBooleanOutcome() {
    Node parameter = mock(Node.class);
    JSType type = null;

    Function<TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isDef");

    FlowScope informed = interpreter.restrictParameter(
        parameter, type, mock(FlowScope.class), restriction, true);

    assertSame(interpreter.getTypeIfRefinable(parameter, mock(FlowScope.class)),
        informed.getTypeInScope("parameter"));
  }

  @Test
  public void testRestrictParameterWithFalseOutcome() {
    Node parameter = mock(Node.class);
    JSType type = mock(JSType.class);

    Function<TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isDef");

    FlowScope informed = interpreter.restrictParameter(
        parameter, type, mock(FlowScope.class), restriction, false);

    assertSame(mock(FlowScope.class), informed);
  }

  @Test
  public void testGetPreciserScopeWithTrueOutcome() {
    Node condition = mock(Node.class);
    FlowScope blindScope = mock(FlowScope.class);

    when(condition.getType()).thenReturn(CALL);
    when(condition.getChildCount()).thenReturn(2);
    Node callee = mock(Node.class);
    Node param = mock(Node.class);

    when(callee.getType()).thenReturn(GETPROP);
    when(callee.getFirstChild().getType()).thenReturn(NAME);
    when(callee.getFirstChild().getString()).thenReturn("goog");
    when(callee.getLastChild().getType()).thenReturn(STRING);
    when(callee.getLastChild().getString()).thenReturn("isDef");
    when(param.isQualifiedName()).thenReturn(true);

    JSType paramType = mock(JSType.class);
    when(typeRegistry.getTypeIfRefinable(param, blindScope)).thenReturn(paramType);

    Function<TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isDef");

    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    assertSame(informed, interpreter.restrictParameter(
        param, paramType, blindScope, restriction, true));
  }

  @Test
  public void testGetPreciserScopeWithFalseOutcome() {
    Node condition = mock(Node.class);
    FlowScope blindScope = mock(FlowScope.class);

    when(condition.getType()).thenReturn(CALL);
    when(condition.getChildCount()).thenReturn(2);
    Node callee = mock(Node.class);
    Node param = mock(Node.class);

    when(callee.getType()).thenReturn(GETPROP);
    when(callee.getFirstChild().getType()).thenReturn(NAME);
    when(callee.getFirstChild().getString()).thenReturn("goog");
    when(callee.getLastChild().getType()).thenReturn(STRING);
    when(callee.getLastChild().getString()).thenReturn("isDef");
    when(param.isQualifiedName()).thenReturn(true);

    JSType paramType = mock(JSType.class);
    when(typeRegistry.getTypeIfRefinable(param, blindScope)).thenReturn(paramType);

    Function<TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isDef");

    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertSame(blindScope, informed);
  }
}