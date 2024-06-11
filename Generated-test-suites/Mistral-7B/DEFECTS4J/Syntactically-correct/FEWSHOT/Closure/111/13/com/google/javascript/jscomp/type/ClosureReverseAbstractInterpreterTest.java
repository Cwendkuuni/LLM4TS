package com.google.javascript.jscomp.type;

import static com.google.javascript.jscomp.test.JSTypematchers.isTypeOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.test.TypeMock;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;
  private JSType NULL_TYPE;
  private JSType ARRAY_TYPE;
  private JSType NUMBER_STRING_BOOLEAN;
  private JSType OBJECT_TYPE;
  private JSType VOID_TYPE;
  private JSType NO_OBJECT_TYPE;
  private JSType NULL_VOID;

  @Before
  public void setUp() {
    this.convention = new CodingConvention() {
      @Override
      public void checkSourceFile(Node tree, String sourceFileName) {}

      @Override
      public void checkSourceUnit(Node tree) {}

      @Override
      public void checkSourceRoots(Node tree) {}
    };

    this.typeRegistry = new JSTypeRegistry();
    this.interpreter = new ClosureReverseAbstractInterpreter(this.convention, this.typeRegistry);
    this.NULL_TYPE = this.typeRegistry.createNullType();
    this.ARRAY_TYPE = this.typeRegistry.createArrayType(this.typeRegistry.createNullType());
    this.NUMBER_STRING_BOOLEAN = this.typeRegistry.createUnionType(
        this.typeRegistry.createNumberType(), this.typeRegistry.createStringType(), this.typeRegistry.createBooleanType());
    this.OBJECT_TYPE = this.typeRegistry.createObjectType(ImmutableMap.of());
    this.VOID_TYPE = this.typeRegistry.createVoidType();
    this.NO_OBJECT_TYPE = this.typeRegistry.createNoObjectType();
    this.NULL_VOID = this.typeRegistry.createUnionType(this.NULL_TYPE, this.VOID_TYPE);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType arrayType = this.typeRegistry.createArrayType(this.typeRegistry.createNullType());
    JSType expectedType = arrayType;

    JSType result = this.interpreter.restrictToArrayVisitor.visit(expectedType);
    assertThat(result, isTypeOf(expectedType));
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ObjectType expectedType = this.typeRegistry.createObjectType(ImmutableMap.of());
    JSType result = this.interpreter.restrictToNotArrayVisitor.visit(expectedType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ObjectType expectedType = this.typeRegistry.createObjectType(ImmutableMap.of());
    JSType result = this.interpreter.restrictToObjectVisitor.visit(expectedType);
    assertThat(result, isTypeOf(expectedType));
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSType expectedType = this.typeRegistry.createNullType();
    JSType result = this.interpreter.restrictToNotObjectVisitor.visit(this.OBJECT_TYPE);
    assertNull(result);
  }

  @Test
  public void testRestrictFunctionType() {
    FunctionType expectedType = this.typeRegistry.createFunctionType(this.OBJECT_TYPE, this.ARRAY_TYPE);
    JSType result = this.interpreter.restrictToObjectVisitor.visit(expectedType);
    assertThat(result, isTypeOf(expectedType));
  }

  @Test
  public void testRestrictByTrueTypeOfResultVisitor() {
    JSType expectedType = this.OBJECT_TYPE;
    TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);
    JSType result = new RestrictByTrueTypeOfResultVisitor().caseObjectType(typeRestriction).get();
    assertThat(result, isTypeOf(expectedType));
  }

  @Test
  public void testRestrictByFalseTypeOfResultVisitor() {
    JSType expectedType = this.VOID_TYPE;
    TypeRestriction typeRestriction = new TypeRestriction(expectedType, false);
    JSType result = new RestrictByFalseTypeOfResultVisitor().caseObjectType(typeRestriction).get();
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = TypeMock.createCallNode("goog.isDef", TypeMock.createNode("foo"), TypeMock.createNullNode());
    Node blindScope = TypeMock.createScopeNode();
    FlowScope result = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertNull(result);

    condition = TypeMock.createCallNode("goog.isDef", TypeMock.createNode("foo"), TypeMock.createBooleanNode(true));
    result = this.interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = this.OBJECT_TYPE;
    JSType actualType = result.getDeclaredNameType("foo");
    assertThat(actualType, isTypeOf(expectedType));
  }

  @Test
  public void testRestrictParameter() {
    Node parameter = TypeMock.createNameNode("foo");
    JSType type = this.ARRAY_TYPE;
    FlowScope blindScope = TypeMock.createScopeNode();
    Function<TypeRestriction, JSType> restriction = this.interpreter.restricters.get("isArray");

    FlowScope result = this.interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
    expectedType = this.ARRAY_TYPE;
    JSType actualType = result.getDeclaredNameType("foo");
    assertThat(actualType, isTypeOf(expectedType));

    type = null;
    result = this.interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
    assertNull(result);

    type = this.OBJECT_TYPE;
    restriction = this.interpreter.restricters.get("isObject");
    result = this.interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
    expectedType = this.OBJECT_TYPE;
    actualType = result.getDeclaredNameType("foo");
    assertThat(actualType, isTypeOf(expectedType));

    restriction = this.interpreter.restricters.get("isNull");
    result = this.interpreter.restrictParameter(parameter, type, blindScope, restriction, false);
    assertNull(result);
  }
}