package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    convention = mock(CodingConvention.class);
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    Node condition = createNode("goog.isArray(arr)");
    JSType arrType = createType(ObjectType.create("Array<number>"));
    JSType arrTypeResult = interpreter.restrictParameter(createNode("arr"), arrType, createBlindScope(), interpreter.restrictToArrayVisitor, true).getDeclaredType();
    assertNotNull(arrTypeResult);
    assertNotNull(arrTypeResult.asArrayType());
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    Node condition = createNode("goog.isArray(str)");
    JSType strType = createType(ObjectType.create("String"));
    JSType strTypeResult = interpreter.restrictParameter(createNode("str"), strType, createBlindScope(), interpreter.restrictToNotArrayVisitor, true).getDeclaredType();
    assertNotNull(strTypeResult);
    assertNull(strTypeResult.asArrayType());
  }

  @Test
  public void testRestrictToObjectVisitor() {
    Node condition = createNode("goog.isObject(obj)");
    JSType objType = createType(ObjectType.create("Object"));
    JSType objTypeResult = interpreter.restrictParameter(createNode("obj"), objType, createBlindScope(), interpreter.restrictToObjectVisitor, true).getDeclaredType();
    assertNotNull(objTypeResult);
    assertNotNull(objTypeResult.isObjectType());
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    Node condition = createNode("goog.isObject(num)");
    JSType numType = createType(NumberType.create(1.0));
    JSType numTypeResult = interpreter.restrictParameter(createNode("num"), numType, createBlindScope(), interpreter.restrictToNotObjectVisitor, true).getDeclaredType();
    assertNull(numTypeResult);
  }

  @Test
  public void testIsDef() {
    Node condition = createNode("goog.isDef(var)");
    JSType varType = createType(ObjectType.create("String"));
    FlowScope informed = interpreter.restrictParameter(createNode("var"), varType, createBlindScope(), interpreter.restricters.get("isDef"), true);
    JSType resultType = informed.getDeclaredType();
    assertNotNull(resultType);
    if (varType != null) {
      assertNotNull(resultType.getGreatestSubtype(varType));
    } else {
      assertNotNull(resultType.isSubtype(JSTypeRegistry.getJavalangNullType()));
    }
  }

  @Test
  public void testIsNotNull() {
    Node condition = createNode("goog.isDef(var) && var != null");
    JSType varType = createType(ObjectType.create("String"));
    FlowScope informed = interpreter.restrictParameter(createNode("var"), varType, createBlindScope(), interpreter.restricters.get("isDefAndNotNull"), false);
    JSType resultType = informed.getDeclaredType();
    assertNotNull(resultType);
    assertNotNull(resultType.isSubtype(JSTypeRegistry.getJavalangNullType().getGreatestSubtype(varType)));
  }

  @Test
  public void testIsString() {
    Node condition = createNode("goog.isString(str)");
    JSType strType = createType(ObjectType.create("String"));
    FlowScope informed = interpreter.restrictParameter(createNode("str"), strType, createBlindScope(), interpreter.restricters.get("isString"), true);
    JSType resultType = informed.getDeclaredType();
    assertNotNull(resultType);
    assertNotNull(resultType.isStringType());
  }

  @Test
  public void testIsBoolean() {
    Node condition = createNode("goog.isBoolean(bool)");
    JSType boolType = createType(BooleanType.create(true));
    FlowScope informed = interpreter.restrictParameter(createNode("bool"), boolType, createBlindScope(), interpreter.restricters.get("isBoolean"), true);
    JSType resultType = informed.getDeclaredType();
    assertNotNull(resultType);
    assertNotNull(resultType.isBooleanType());
  }

  @Test
  public void testIsNumber() {
    Node condition = createNode("goog.isNumber(num)");
    JSType numType = createType(NumberType.create(1.0));
    FlowScope informed = interpreter.restrictParameter(createNode("num"), numType, createBlindScope(), interpreter.restricters.get("isNumber"), true);
    JSType resultType = informed.getDeclaredType();
    assertNotNull(resultType);
    assertNotNull(resultType.isNumberType());
  }

  @Test
  public void testIsFunction() {
    Node condition = createNode("goog.isFunction(func)");
    FunctionType funcType = FunctionType.create(ObjectType.create("Object"), VoidType.INSTANCE);
    FlowScope informed = interpreter.restrictParameter(createNode("func"), funcType, createBlindScope(), interpreter.restricters.get("isFunction"), true);
    JSType resultType = informed.getDeclaredType();
    assertNotNull(resultType);
    assertNotNull(resultType.isFunctionType());
  }

  private Node createNode(String expression) {
    return Node.newCall(createContext(), createName("goog"), createName(expression));
  }

  private Node createName(String name) {
    return Node.newName(name);
  }

  private FlowScope createBlindScope() {
    return new BlindFlowScope();
  }

  private JSType createType(JSType type) {
    when(typeRegistry.createType(type)).thenReturn(type);
    return type;
  }

  private Context createContext() {
    Context context = new Context();
    when(typeRegistry.lookupType(context, ObjectType.CLASS_NAME)).thenReturn(ObjectType.INSTANCE);
    when(typeRegistry.lookupType(context, FunctionType.CLASS_NAME)).thenReturn(FunctionType.INSTANCE);
    when(typeRegistry.lookupType(context, ArrayType.CLASS_NAME)).thenReturn(ARRAY_TYPE);
    when(typeRegistry.lookupType(context, ObjectType.NAME)).thenReturn(ObjectType.INSTANCE);
    when(typeRegistry.lookupType(context, VoidType.CLASS_NAME)).thenReturn(VoidType.INSTANCE);
    when(typeRegistry.lookupType(context, BooleanType.CLASS_NAME)).thenReturn(BooleanType.INSTANCE);
    when(typeRegistry.lookupType(context, NumberType.CLASS_NAME)).thenReturn(NumberType.INSTANCE);
    return context;
  }
}

class BlindFlowScope extends FlowScope {
  @Override
  public FlowScope createChildFlowScope() {
    return this;
  }
}