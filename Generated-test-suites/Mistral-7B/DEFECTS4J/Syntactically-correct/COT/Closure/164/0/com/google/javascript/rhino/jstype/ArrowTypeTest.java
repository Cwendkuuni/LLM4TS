package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.UnknownType;

public class ArrowTypeTest {

  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter() {};
    scope = new StaticScope<JSType>(JSTypeNative.getRegistry());
  }

  @Test
  public void testBasicArrowTypeConstruction() {
    // ArrowType with no parameters and an inferred return type
    Node parameters = Node.NULL;
    JSType returnType = JSTypeNative.VOID;
    ArrowType arrowType = new ArrowType(JSTypeRegistry.INSTANCE, parameters, returnType, true);

    assertEquals(JSType.ARROW, arrowType.getType());
    assertNull(arrowType.parameters);
    assertEquals(JSTypeNative.VOID, arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);

    // ArrowType with no parameters and an explicit return type
    parameters = Node.NULL;
    returnType = new IntType(JSTypeRegistry.INSTANCE);
    arrowType = new ArrowType(JSTypeRegistry.INSTANCE, parameters, returnType, false);

    assertEquals(JSType.ARROW, arrowType.getType());
    assertNull(arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);

    // ArrowType with multiple parameters and an inferred return type
    Node param1 = Node.newIdentifier("p1");
    Node param2 = Node.newIdentifier("p2");
    Node paramList = Node.newList(param1, param2);
    parameters = Node.newObject(JSTypeRegistry.INSTANCE.getParametersWithVarArgs(new ArrayType(JSTypeRegistry.INSTANCE, JSTypeNative.ANY)));
    parameters.setFirstChild(paramList);
    JSType paramType1 = new IntType(JSTypeRegistry.INSTANCE);
    JSType paramType2 = new StringType(JSTypeRegistry.INSTANCE);
    parameters.setChild(0, param1.setJSType(paramType1));
    parameters.setChild(1, param2.setJSType(paramType2));

    returnType = JSTypeNative.VOID;
    arrowType = new ArrowType(JSTypeRegistry.INSTANCE, parameters, returnType, true);

    assertEquals(JSType.ARROW, arrowType.getType());
    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);

    // ArrowType with multiple parameters and an explicit return type
    returnType = new IntType(JSTypeRegistry.INSTANCE);
    arrowType = new ArrowType(JSTypeRegistry.INSTANCE, parameters, returnType, false);

    assertEquals(JSType.ARROW, arrowType.getType());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype() {
    // Test cases for ArrowType <: ArrowType

    // ArrowType(Void -> Void) <: ArrowType(Int -> Int)
    Node voidParams = Node.NULL;
    JSType voidReturn = JSTypeNative.VOID;
    ArrowType arrow1 = new ArrowType(JSTypeRegistry.INSTANCE, voidParams, voidReturn, true);

    Node intParams = Node.newList(new IntType(JSTypeRegistry.INSTANCE));
    Node intReturn = new IntType(JSTypeRegistry.INSTANCE);
    ArrowType arrow2 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, intReturn, false);

    assertTrue(arrow1.isSubtype(arrow2));

    // ArrowType(Int -> Int) <: ArrowType(Int -> Int)
    assertSame(arrow2, arrow2.isSubtype(arrow2));

    // Test cases for ArrowType <: FunctionType

    // ArrowType(Int -> Int) <: FunctionType(Int, Int)
    Node intFunctionParams = Node.newList(new IntType(JSTypeRegistry.INSTANCE), new IntType(JSTypeRegistry.INSTANCE));
    Node intFunctionReturn = new IntType(JSTypeRegistry.INSTANCE);
    FunctionType functionType = new FunctionType(JSTypeRegistry.INSTANCE, intFunctionParams, intFunctionReturn);

    assertTrue(arrow2.isSubtype(functionType));

    // FunctionType(Int, Int) <: ArrowType(Int -> Int)
    FunctionType functionTypeReverse = new FunctionType(JSTypeRegistry.INSTANCE, intReturn, new ArrayType(JSTypeRegistry.INSTANCE, intFunctionParams.get(0).getType()));

    assertTrue(functionTypeReverse.isSubtype(arrow2));

    // Test cases for ArrowType <: ObjectType

    // ArrowType(Int -> Int) <: ObjectType
    Node objectTypeParams = Node.NULL;
    Node objectTypeReturn = Node.newObject(JSTypeRegistry.INSTANCE.getObjectType(JSTypeRegistry.INSTANCE, "Object", null));
    ObjectType objectType = new ObjectType(JSTypeRegistry.INSTANCE, objectTypeParams, objectTypeReturn);

    assertFalse(arrow2.isSubtype(objectType));

    // ArrowType(Int -> Int) <: ObjectType(Int)
    Node objectTypeIntParam = Node.newIdentifier("Int");
    Node objectTypeIntReturn = JSTypeNative.getNativeType(JSTypeRegistry.INSTANCE, "Int");
    ObjectType objectTypeInt = new ObjectType(JSTypeRegistry.INSTANCE, objectTypeIntParam, objectTypeIntReturn);

    assertFalse(arrow2.isSubtype(objectTypeInt));

    // Test cases for ArrowType <: ArrowType with different return types

    // ArrowType(Int -> Int) <: ArrowType(Int -> String)
    Node stringReturn = new StringType(JSTypeRegistry.INSTANCE);
    ArrowType arrow3 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, stringReturn, false);

    assertFalse(arrow2.isSubtype(arrow3));

    // ArrowType(Int -> Int) <: ArrowType(String -> Int)
    Node stringParams = Node.newIdentifier("String");
    Node stringParamType = new StringType(JSTypeRegistry.INSTANCE);
    ArrowType arrow4 = new ArrowType(JSTypeRegistry.INSTANCE, Node.newObject(JSTypeRegistry.INSTANCE.getParametersWithVarArgs(new ArrayType(JSTypeRegistry.INSTANCE, stringParamType))), intReturn, false);

    assertFalse(arrow2.isSubtype(arrow4));

    // Test cases for ArrowType <: ArrowType with different parameter lists

    // ArrowType(Int -> Int) <: ArrowType(Int, Int -> Int)
    Node intIntParams = Node.newList(new IntType(JSTypeRegistry.INSTANCE), Node.newObject(JSTypeRegistry.INSTANCE.getParametersWithVarArgs(new ArrayType(JSTypeRegistry.INSTANCE, intReturn.getType()))));
    ArrowType arrow5 = new ArrowType(JSTypeRegistry.INSTANCE, intIntParams, intReturn, false);

    assertFalse(arrow2.isSubtype(arrow5));

    // ArrowType(Int -> Int) <: ArrowType(Int, Int, Int -> Int)
    Node intIntIntParams = Node.newList(new IntType(JSTypeRegistry.INSTANCE), new IntType(JSTypeRegistry.INSTANCE), Node.newObject(JSTypeRegistry.INSTANCE.getParametersWithVarArgs(new ArrayType(JSTypeRegistry.INSTANCE, intReturn.getType()))));
    ArrowType arrow6 = new ArrowType(JSTypeRegistry.INSTANCE, intIntIntParams, intReturn, false);

    assertFalse(arrow2.isSubtype(arrow6));
  }

  @Test
  public void testHasEqualParameters() {
    Node intParams = Node.newList(new IntType(JSTypeRegistry.INSTANCE));
    Node stringParams = Node.newList(new StringType(JSTypeRegistry.INSTANCE));
    Node voidParams = Node.NULL;

    ArrowType arrow1 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, JSTypeNative.VOID, true);
    ArrowType arrow2 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, JSTypeNative.VOID, true);
    ArrowType arrow3 = new ArrowType(JSTypeRegistry.INSTANCE, stringParams, JSTypeNative.VOID, true);
    ArrowType arrow4 = new ArrowType(JSTypeRegistry.INSTANCE, voidParams, JSTypeNative.VOID, true);

    assertTrue(arrow1.hasEqualParameters(arrow2));
    assertFalse(arrow1.hasEqualParameters(arrow3));
    assertFalse(arrow1.hasEqualParameters(arrow4));

    // Testing with varargs
    Node intParamsVarargs = Node.newObject(JSTypeRegistry.INSTANCE.getParametersWithVarArgs(new ArrayType(JSTypeRegistry.INSTANCE, intReturn.getType())));
    ArrowType arrow5 = new ArrowType(JSTypeRegistry.INSTANCE, intParamsVarargs, intReturn, false);
    ArrowType arrow6 = new ArrowType(JSTypeRegistry.INSTANCE, intParamsVarargs, intReturn, false);

    assertTrue(arrow5.hasEqualParameters(arrow6));
  }

  @Test
  public void testEqualsAndHashCode() {
    Node intParams = Node.newList(new IntType(JSTypeRegistry.INSTANCE));
    Node stringParams = Node.newList(new StringType(JSTypeRegistry.INSTANCE));
    Node voidParams = Node.NULL;
    JSType voidReturn = JSTypeNative.VOID;
    JSType intReturn = new IntType(JSTypeRegistry.INSTANCE);

    ArrowType arrow1 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, voidReturn, true);
    ArrowType arrow2 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, voidReturn, true);
    ArrowType arrow3 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, stringReturn, false);
    ArrowType arrow4 = new ArrowType(JSTypeRegistry.INSTANCE, voidParams, voidReturn, true);

    assertEquals(arrow1, arrow1);
    assertEquals(arrow1, arrow2);

    // Testing with different return types
    ArrowType arrow5 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, intReturn, false);
    assertNotEquals(arrow1, arrow5);

    // Testing with different parameter lists
    ArrowType arrow6 = new ArrowType(JSTypeRegistry.INSTANCE, stringParams, voidReturn, true);
    assertNotEquals(arrow1, arrow6);

    // Testing with varargs
    Node intParamsVarargs = Node.newObject(JSTypeRegistry.INSTANCE.getParametersWithVarArgs(new ArrayType(JSTypeRegistry.INSTANCE, intReturn.getType())));
    ArrowType arrow7 = new ArrowType(JSTypeRegistry.INSTANCE, intParamsVarargs, intReturn, false);
    ArrowType arrow8 = new ArrowType(JSTypeRegistry.INSTANCE, intParamsVarargs, intReturn, false);

    assertEquals(arrow7, arrow8);

    ArrowType arrow9 = new ArrowType(JSTypeRegistry.INSTANCE, intParamsVarargs, intReturn, false);
    ArrowType arrow10 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, intReturn, false);

    assertNotEquals(arrow7, arrow9);
    assertNotEquals(arrow7, arrow10);

    // Testing null and unknown types
    ArrowType arrow11 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, UnknownType.INSTANCE, true);
    assertNotEquals(arrow1, arrow11);

    ArrowType arrow12 = new ArrowType(JSTypeRegistry.INSTANCE, intParams, null, true);
    assertNotEquals(arrow1, arrow12);
  }
}