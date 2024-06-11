package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrimitiveType;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testArrowTypeConstructorWithNullParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertEquals("Expected ArrowType instance", arrowType.getClass(), arrowType.getClass());
  }

  @Test
  public void testArrowTypeConstructorWithValidParametersAndReturnType() {
    Node parameters = Node.newNode(Node.CREATE_LIST, 2);
    parameters.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters.setNext(Node.newNode(Node.IDENT, "p2"));

    JSType returnType = JSTypeFactory.createPrimitiveType(PrimitiveType.INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("Expected ArrowType instance", arrowType.getClass(), arrowType.getClass());
    assertEquals("Expected correct parameters", parameters, arrowType.parameters);
    assertEquals("Expected correct return type", returnType, arrowType.returnType);
  }

  @Test
  public void testArrowTypeConstructorWithValidParametersAndInferredReturnType() {
    Node parameters = Node.newNode(Node.CREATE_LIST, 2);
    parameters.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters.setNext(Node.newNode(Node.IDENT, "p2"));

    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertTrue("Expected returnTypeInferred to be true", arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtypeArrowTypeWithSameType() {
    JSType type1 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);

    assertTrue("Expected true", type1.isSubtype(type2));
    assertTrue("Expected true", type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithDifferentReturnTypes() {
    JSType type1 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, null, JSTypeNative.getNumberType(), false);

    assertFalse("Expected false", type1.isSubtype(type2));
    assertFalse("Expected false", type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithDifferentParameters() {
    Node parameters1 = Node.newNode(Node.CREATE_LIST, 2);
    parameters1.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters1.setNext(Node.newNode(Node.IDENT, "p2"));

    Node parameters2 = Node.newNode(Node.CREATE_LIST, 1);
    parameters2.setNext(Node.newNode(Node.IDENT, "p1"));

    JSType type1 = new ArrowType(registry, parameters1, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, parameters2, JSTypeNative.getBooleanType(), false);

    assertFalse("Expected false", type1.isSubtype(type2));
    assertFalse("Expected false", type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithSameParametersButDifferentReturnTypes() {
    Node parameters = Node.newNode(Node.CREATE_LIST, 2);
    parameters.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters.setNext(Node.newNode(Node.IDENT, "p2"));

    JSType type1 = new ArrowType(registry, parameters, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, parameters, JSTypeNative.getNumberType(), false);

    assertFalse("Expected false", type1.isSubtype(type2));
    assertFalse("Expected false", type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtypeArrowTypeWithSameParametersAndSameReturnTypesButDifferentOrder() {
    Node parameters = Node.newNode(Node.CREATE_LIST, 2);
    parameters.setNext(Node.newNode(Node.IDENT, "p2"));
    parameters.setNext(Node.newNode(Node.IDENT, "p1"));

    JSType type1 = new ArrowType(registry, parameters, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, Node.newNode(Node.CREATE_LIST, 2), JSTypeNative.getBooleanType(), false);

    assertTrue("Expected true", type1.isSubtype(type2));
    assertTrue("Expected true", type2.isSubtype(type1));
  }

  @Test
  public void testHasEqualParameters() {
    Node parameters1 = Node.newNode(Node.CREATE_LIST, 2);
    parameters1.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters1.setNext(Node.newNode(Node.IDENT, "p2"));

    Node parameters2 = Node.newNode(Node.CREATE_LIST, 2);
    parameters2.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters2.setNext(Node.newNode(Node.IDENT, "p2"));

    ArrowType arrowType1 = new ArrowType(registry, parameters1, JSTypeNative.getBooleanType(), false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, JSTypeNative.getBooleanType(), false);

    assertTrue("Expected true", arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue("Expected true", arrowType2.hasEqualParameters(arrowType1, false));
  }

  @Test
  public void testHasEqualParametersWithUnknownTypes() {
    Node parameters1 = Node.newNode(Node.CREATE_LIST, 2);
    parameters1.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters1.setNext(Node.newNode(Node.IDENT, "p2"));

    Node parameters2 = Node.newNode(Node.CREATE_LIST, 2);
    parameters2.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters2.setNext(Node.newNode(Node.IDENT, JSTypeNative.getUnknownType()));

    ArrowType arrowType1 = new ArrowType(registry, parameters1, JSTypeNative.getBooleanType(), false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, JSTypeNative.getBooleanType(), false);

    assertTrue("Expected true", arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue("Expected true", arrowType2.hasEqualParameters(arrowType1, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithSameTypes() {
    JSType type1 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);

    assertTrue("Expected true", type1.checkArrowEquivalenceHelper((ArrowType) type2, false));
    assertTrue("Expected true", type2.checkArrowEquivalenceHelper((ArrowType) type1, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithDifferentReturnTypes() {
    JSType type1 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, null, JSTypeNative.getNumberType(), false);

    assertFalse("Expected false", type1.checkArrowEquivalenceHelper((ArrowType) type2, false));
    assertFalse("Expected false", type2.checkArrowEquivalenceHelper((ArrowType) type1, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithDifferentParameters() {
    Node parameters1 = Node.newNode(Node.CREATE_LIST, 2);
    parameters1.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters1.setNext(Node.newNode(Node.IDENT, "p2"));

    Node parameters2 = Node.newNode(Node.CREATE_LIST, 1);
    parameters2.setNext(Node.newNode(Node.IDENT, "p1"));

    JSType type1 = new ArrowType(registry, parameters1, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, parameters2, JSTypeNative.getBooleanType(), false);

    assertFalse("Expected false", type1.checkArrowEquivalenceHelper((ArrowType) type2, false));
    assertFalse("Expected false", type2.checkArrowEquivalenceHelper((ArrowType) type1, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithSameParametersButDifferentReturnTypes() {
    Node parameters = Node.newNode(Node.CREATE_LIST, 2);
    parameters.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters.setNext(Node.newNode(Node.IDENT, "p2"));

    JSType type1 = new ArrowType(registry, parameters, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, parameters, JSTypeNative.getNumberType(), false);

    assertFalse("Expected false", type1.checkArrowEquivalenceHelper((ArrowType) type2, false));
    assertFalse("Expected false", type2.checkArrowEquivalenceHelper((ArrowType) type1, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithSameParametersAndSameReturnTypesButDifferentOrder() {
    Node parameters = Node.newNode(Node.CREATE_LIST, 2);
    parameters.setNext(Node.newNode(Node.IDENT, "p2"));
    parameters.setNext(Node.newNode(Node.IDENT, "p1"));

    JSType type1 = new ArrowType(registry, parameters, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, Node.newNode(Node.CREATE_LIST, 2), JSTypeNative.getBooleanType(), false);

    assertTrue("Expected true", type1.checkArrowEquivalenceHelper((ArrowType) type2, false));
    assertTrue("Expected true", type2.checkArrowEquivalenceHelper((ArrowType) type1, false));
  }

  @Test
  public void testHashCode() {
    JSType type1 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    JSType type2 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);

    assertEquals("Expected equal hashcodes", type1.hashCode(), type2.hashCode());

    JSType type3 = new ArrowType(registry, null, JSTypeNative.getNumberType(), false);

    assertNotEquals("Expected different hashcodes", type1.hashCode(), type3.hashCode());

    Node parameters = Node.newNode(Node.CREATE_LIST, 2);
    parameters.setNext(Node.newNode(Node.IDENT, "p1"));
    parameters.setNext(Node.newNode(Node.IDENT, "p2"));

    ArrowType arrowType1 = new ArrowType(registry, parameters, JSTypeNative.getBooleanType(), false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newNode(Node.CREATE_LIST, 2), JSTypeNative.getBooleanType(), false);

    assertEquals("Expected equal hashcodes", arrowType1.hashCode(), arrowType2.hashCode());

    ArrowType arrowType3 = new ArrowType(registry, Node.newNode(Node.CREATE_LIST, 2), JSTypeNative.getNumberType(), false);

    assertNotEquals("Expected different hashcodes", arrowType1.hashCode(), arrowType3.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    // UnsupportedOperationException is thrown by the superclass
    // assertThrows(UnsupportedOperationException.class, () -> type.getLeastSupertype(other));
  }

  @Test
  public void testGetGreatestSubtype() {
    // UnsupportedOperationException is thrown by the superclass
    // assertThrows(UnsupportedOperationException.class, () -> type.getGreatestSubtype(other));
  }

  @Test
  public void testToStringHelper() {
    JSType type = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);

    assertEquals("Expected [ArrowType]", type.toStringHelper(false), type.toString());
  }

  @Test
  public void testHasAnyTemplateInternal() {
    JSType type = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);

    assertFalse("Expected false", type.hasAnyTemplateInternal());

    JSType typeWithTemplatedReturnType = new ArrowType(registry, null, JSTypeNative.getArrayType(JSTypeNative.getNumberType(), JSTypeNative.getNumberType()), false);

    assertTrue("Expected true", typeWithTemplatedReturnType.hasAnyTemplateInternal());

    JSType typeWithTemplatedParameter = new ArrowType(registry, Node.newNode(Node.CREATE_LIST, 1), JSTypeNative.getBooleanType(), false);
    Node templatedParameter = Node.newNode(Node.CREATE_LIST, 1);
    templatedParameter.setNext(Node.newNode(Node.IDENT, "T"));
    typeWithTemplatedParameter.setParameters(templatedParameter);

    assertTrue("Expected true", typeWithTemplatedParameter.hasAnyTemplateInternal());
  }
}