package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newObject();
    JSType returnType = JSTypeNative.INT;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("Expected ArrowType, got " + arrowType.getClass().getName(),
        ArrowType.class, arrowType.getClass());
  }

  @Test
  public void testArrowTypeIsNullArguments() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertEquals("Unexpected returnType, got " + arrowType.returnType,
        JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
    assertNull("Unexpected parameters, got " + arrowType.parameters, arrowType.parameters);
  }

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray(2);
    JSType intType = JSTypeNative.INT;
    JSType stringType = JSTypeNative.STRING;
    JSType returnType = JSTypeNative.BOOLEAN;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("Unexpected returnType, got " + arrowType.returnType,
        returnType, arrowType.returnType);
    assertEquals("Unexpected number of parameters, got " + arrowType.parameters.getLength(),
        2, arrowType.parameters.getLength());

    Node firstParam = arrowType.parameters.getFirstChild();
    assertEquals("Unexpected type of first parameter, got " + firstParam.getJSType(),
        intType, firstParam.getJSType());

    Node secondParam = arrowType.parameters.getChild(1);
    assertEquals("Unexpected type of second parameter, got " + secondParam.getJSType(),
        stringType, secondParam.getJSType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithInvalidParametersType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newObject();
    JSType returnType = JSTypeNative.INT;
    new ArrowType(registry, parameters, returnType, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithInvalidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray(2);
    JSType returnType = null;
    new ArrowType(registry, parameters, returnType);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newArray(1);
    JSType intType1 = JSTypeNative.INT;
    JSType returnType1 = JSTypeNative.BOOLEAN;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = Node.newArray(1);
    JSType intType2 = JSTypeNative.INT;
    JSType returnType2 = JSTypeNative.INT;
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue("Expected subtype relationship, got " +
        arrowType1.isSubtype(arrowType2), arrowType1.isSubtype(arrowType2));

    // This relationship should not hold
    ArrowType arrowType3 = new ArrowType(registry, parameters1, intType1);
    assertFalse("Expected non-subtype relationship, got " +
        arrowType1.isSubtype(arrowType3), arrowType1.isSubtype(arrowType3));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newArray(1).setVarArgs(false);
    JSType intType1 = JSTypeNative.INT;
    Node param1 = parameters1.getFirstChild();
    param1.setJSType(intType1);

    Node parameters2 = Node.newArray(1).setVarArgs(false);
    JSType intType2 = JSTypeNative.INT;
    Node param2 = parameters2.getFirstChild();
    param2.setJSType(intType2);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, intType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, intType2);

    assertTrue("Expected equal parameter lists, got " +
        arrowType1.hasEqualParameters(arrowType2), arrowType1.hasEqualParameters(arrowType2));

    // This relationship should not hold
    Node varArgParameters = Node.newArray(1).setVarArgs(true);
    ArrowType arrowType3 = new ArrowType(registry, parameters1, intType1);
    assertFalse("Expected non-equal parameter lists, got " +
        arrowType1.hasEqualParameters(arrowType3), arrowType1.hasEqualParameters(arrowType3));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newArray(1).setVarArgs(false);
    JSType intType1 = JSTypeNative.INT;
    Node param1 = parameters1.getFirstChild();
    param1.setJSType(intType1);

    Node parameters2 = Node.newArray(1).setVarArgs(false);
    JSType intType2 = JSTypeNative.INT;
    Node param2 = parameters2.getFirstChild();
    param2.setJSType(intType2);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, intType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, intType2);

    assertTrue("Expected equivalent types, got " +
        arrowType1.isEquivalentTo(arrowType2), arrowType1.isEquivalentTo(arrowType2));

    // This relationship should not hold
    ArrowType arrowType3 = new ArrowType(registry, parameters1, JSTypeNative.BOOLEAN);
    assertFalse("Expected non-equivalent types, got " +
        arrowType1.isEquivalentTo(arrowType3), arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testArrowTypeGetLeastSupertype() {
    throw new UnsupportedOperationException("getLeastSupertype method not implemented");
  }

  @Test
  public void testArrowTypeGetGreatestSubtype() {
    throw new UnsupportedOperationException("getGreatestSubtype method not implemented");
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray(1).setVarArgs(false);
    JSType intType = JSTypeNative.INT;
    JSType returnType = JSTypeNative.BOOLEAN;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("Unexpected toString result, got " + arrowType.toString(),
        "(function(int) {return boolean;})", arrowType.toString());
  }

  @Test
  public void testArrowTypeResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray(1).setVarArgs(false);
    JSType intType = JSTypeNative.INT;
    JSType stringType = JSTypeNative.STRING;
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test that unresolved types are not changed
    assertNull("Unexpected resolved return type, got " + arrowType.returnType, arrowType.returnType);
    assertNull("Unexpected resolved parameters, got " + arrowType.parameters, arrowType.parameters);

    // Test that resolved types are updated
    JSTypeRegistry scope = new JSTypeRegistry();
    JSType intTypeResolved = intType.resolve(registry, scope);
    JSType stringTypeResolved = stringType.resolve(registry, scope);
    JSType returnTypeResolved = JSTypeNative.BOOLEAN;
    JSType intTypeVarArgs = JSTypeNative.INT_VAR_ARGS;

    arrowType.resolveInternal(registry.getErrorReporter(), scope);
    assertEquals("Unexpected resolved return type, got " + arrowType.returnType, returnTypeResolved, arrowType.returnType);
    assertEquals("Unexpected resolved parameters, got " + arrowType.parameters.getLength(), 1, arrowType.parameters.getLength());
    Node param = arrowType.parameters.getFirstChild();
    assertEquals("Unexpected resolved type of first parameter, got " + param.getJSType(), intTypeResolved, param.getJSType());
    assertTrue("Unexpected var_args parameter, got " + param.isVarArgs(), !param.isVarArgs());

    // Test that unknown types are replaced with UNKNOWN_TYPE
    Node unknownParameters = Node.newObject();
    JSType unknownReturnType = null;
    ArrowType unknownArrowType = new ArrowType(registry, unknownParameters, unknownReturnType);
    assertEquals("Unexpected resolved return type, got " + unknownArrowType.returnType, JSTypeNative.UNKNOWN_TYPE, unknownArrowType.returnType);
    assertNotNull("Unexpected resolved parameters, got " + unknownArrowType.parameters, unknownArrowType.parameters);
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newObject();
    JSType returnType = JSTypeNative.INT;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test that the arrow type is not considered to have unknown params or return if all types are known
    assertFalse("Expected non-unknown type, got " + arrowType.hasUnknownParamsOrReturn(), arrowType.hasUnknownParamsOrReturn());

    // Test that the arrow type is considered to have unknown params or return if any type is unknown
    Node unknownParameter = Node.newObject().setJSType(null);
    Node unknownParametersWithUnknownParam = Node.newArray(1).setVarArgs(false).setJSType(unknownParameter);
    ArrowType arrowTypeWithUnknownParam = new ArrowType(registry, unknownParametersWithUnknownParam, returnType);
    assertTrue("Expected unknown type, got " + arrowTypeWithUnknownParam.hasUnknownParamsOrReturn(), arrowTypeWithUnknownParam.hasUnknownParamsOrReturn());

    // Test that the arrow type is considered to have unknown return type if the return type is unknown
    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, parameters, null);
    assertTrue("Expected unknown type, got " + arrowTypeWithUnknownReturn.hasUnknownParamsOrReturn(), arrowTypeWithUnknownReturn.hasUnknownParamsOrReturn());
  }
}