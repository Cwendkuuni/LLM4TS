package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.TypeRestriction;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistryImpl;
import com.google.javascript.rhino.jstype.ObjectTypeImpl;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    CodingConvention convention = mock(CodingConvention.class);
    typeRegistry = new JSTypeRegistryImpl();
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);
    ObjectType arrayObjectType = new ObjectTypeImpl(arrayType, "Array");
    ObjectType nonArrayObjectType = new ObjectTypeImpl(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE), "NonArrayObject");

    JSType result = interpreter.getRestrictedType(arrayObjectType, false);
    assertNull(result);

    result = interpreter.getRestrictedType(arrayObjectType, true);
    assertNotNull(result);
    assertSame(arrayType, result);

    result = interpreter.getRestrictedType(nonArrayObjectType, false);
    assertNull(result);

    result = interpreter.getRestrictedType(nonArrayObjectType, true);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ObjectType arrayObjectType = new ObjectTypeImpl(JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE), "Array");
    ObjectType nonArrayObjectType = new ObjectTypeImpl(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE), "NonArrayObject");

    JSType result = interpreter.getRestrictedType(arrayObjectType, false);
    assertNull(result);

    result = interpreter.getRestrictedType(arrayObjectType, true);
    assertSame(JSTypeNative.getNativeType(JSTypeNative.VOID_TYPE), result);

    result = interpreter.getRestrictedType(nonArrayObjectType, false);
    assertNotNull(result);
    assertSame(nonArrayObjectType, result);

    result = interpreter.getRestrictedType(nonArrayObjectType, true);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ObjectType objectType = new ObjectTypeImpl(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE), "Object");
    FunctionType functionType = new FunctionType(JSTypeNative.getNativeType(JSTypeNative.VOID_TYPE),
        new ObjectTypeImpl(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE), "Function"), null, null);

    JSType result = interpreter.getRestrictedType(objectType, false);
    assertNull(result);

    result = interpreter.getRestrictedType(objectType, true);
    assertNotNull(result);
    assertSame(objectType, result);

    result = interpreter.getRestrictedType(functionType, false);
    assertNull(result);

    result = interpreter.getRestrictedType(functionType, true);
    assertNotNull(result);
    assertSame(functionType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ObjectType objectType = new ObjectTypeImpl(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE), "Object");
    FunctionType functionType = new FunctionType(JSTypeNative.getNativeType(JSTypeNative.VOID_TYPE),
        new ObjectTypeImpl(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE), "Function"), null, null);

    JSType result = interpreter.getRestrictedType(objectType, false);
    assertNull(result);

    result = interpreter.getRestrictedType(objectType, true);
    assertSame(JSTypeNative.getNativeType(JSTypeNative.NULL_VOID), result);

    result = interpreter.getRestrictedType(functionType, false);
    assertNull(result);

    result = interpreter.getRestrictedType(functionType, true);
    assertSame(JSTypeNative.getNativeType(JSTypeNative.NULL_VOID), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    JSType type = JSTypeNative.getNativeType(JSTypeNative.NUMBER_TYPE);
    Function<TypeRestriction, JSType> isNumberRestriction =
        interpreter.restricters.get("isNumber");

    FlowScope blindScope = createBlindScope();
    Node condition = createConditionNode("goog.isNumber(x)");
    Node parameter = createParameterNode("x");

    FlowScope informedScope = interpreter.restrictParameter(parameter, type, blindScope, isNumberRestriction, true);
    assertNotNull(informedScope);

    JSType informedType = informedScope.getScopeVariable(parameter.getName()).getType();
    assertNotNull(informedType);
    assertSame(JSTypeNative.getNativeType(JSTypeNative.NUMBER_TYPE), informedType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeFalse() {
    JSType type = JSTypeNative.getNativeType(JSTypeNative.NUMBER_TYPE);
    Function<TypeRestriction, JSType> isNumberRestriction =
        interpreter.restricters.get("isNumber");

    FlowScope blindScope = createBlindScope();
    Node condition = createConditionNode("goog.isNumber(x)");
    Node parameter = createParameterNode("x");

    FlowScope informedScope = interpreter.restrictParameter(parameter, type, blindScope, isNumberRestriction, false);
    assertNull(informedScope);
  }

  private Node createConditionNode(String expression) {
    Node left = Node.newName("goog");
    Node callee = Node.newCall(left, expression);
    Node right = Node.newString("x");
    return Node.newCall(callee, right);
  }

  private Node createParameterNode(String name) {
    return Node.newName(name);
  }

  private FlowScope createBlindScope() {
    return new FlowScopeImpl(typeRegistry);
  }
}