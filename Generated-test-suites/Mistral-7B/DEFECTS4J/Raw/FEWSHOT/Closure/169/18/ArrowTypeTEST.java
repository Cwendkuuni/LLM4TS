package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(UNKNOWN_TYPE);

    // Test with null inputs
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = null;
    assertFalse(arrowType1.isSubtype(arrowType2));

    arrowType2 = new ArrowType(registry, null, null);
    assertFalse(arrowType1.isSubtype(arrowType2));

    // Test with same type
    ArrowType arrowType3 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType1));

    // Test with different return types
    ArrowType arrowType4 = new ArrowType(registry, parameters, getNativeType(INT_TYPE));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));

    // Test with different parameter types
    Node parameter1 = registry.createParameterNode("p1", INT_TYPE);
    Node parameter2 = registry.createParameterNode("p2", STRING_TYPE);
    ArrowType arrowType5 = new ArrowType(registry, new NodeList(parameter1, parameter2), returnType);
    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType1));

    // Test with same parameter types but different order
    ArrowType arrowType6 = new ArrowType(registry, new NodeList(parameter2, parameter1), returnType);
    assertTrue(arrowType1.isSubtype(arrowType6));
    assertTrue(arrowType6.isSubtype(arrowType1));

    // Test with optional parameters
    Node optionalParameter = registry.createOptionalParameterNode("p3", getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType7 = new ArrowType(registry, new NodeList(parameters, optionalParameter), returnType);
    assertTrue(arrowType1.isSubtype(arrowType7));
    assertTrue(arrowType7.isSubtype(arrowType1));

    // Test with varargs
    Node varargsParameter = registry.createVarargsParameterNode("p4", getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType8 = new ArrowType(registry, new NodeList(parameters, varargsParameter), returnType);
    assertTrue(arrowType1.isSubtype(arrowType8));
    assertTrue(arrowType8.isSubtype(arrowType1));

    // Test with unknown types
    ArrowType arrowType9 = new ArrowType(registry, parameters, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType10 = new ArrowType(registry, new NodeList(parameters), returnType);
    assertFalse(arrowType1.isSubtype(arrowType9));
    assertFalse(arrowType9.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType10));
    assertTrue(arrowType10.isSubtype(arrowType1));

    // Test with inferred return types
    ArrowType arrowType11 = new ArrowType(registry, parameters, null, true);
    ArrowType arrowType12 = new ArrowType(registry, parameters, INT_TYPE, true);
    assertFalse(arrowType11.isSubtype(arrowType12));
    assertFalse(arrowType12.isSubtype(arrowType11));

    ErrorReporter errorReporter = new ErrorReporter();
    ArrowType arrowType13 = new ArrowType(registry, parameters, null, true).resolveInternal(errorReporter, null);
    ArrowType arrowType14 = new ArrowType(registry, parameters, INT_TYPE, true).resolveInternal(errorReporter, null);
    assertFalse(arrowType11.isSubtype(arrowType13));
    assertFalse(arrowType13.isSubtype(arrowType11));
    assertTrue(arrowType11.isSubtype(arrowType14));
    assertTrue(arrowType14.isSubtype(arrowType11));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    Node parameter1 = registry.createParameterNode("p1", INT_TYPE);
    Node parameter2 = registry.createParameterNode("p2", STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
    ArrowType arrowType2 = new ArrowType(registry, parameters1, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));

    ArrowType arrowType3 = new ArrowType(registry, new NodeList(parameter1, parameter2), null);
    ArrowType arrowType4 = new ArrowType(registry, new NodeList(parameter2, parameter1), null);
    assertTrue(arrowType3.hasEqualParameters(arrowType4, false));
    assertTrue(arrowType3.hasEqualParameters(arrowType4, true));

    ArrowType arrowType5 = new ArrowType(registry, parameters1, INT_TYPE);
    assertFalse(arrowType1.hasEqualParameters(arrowType5, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType5, true));

    ArrowType arrowType6 = new ArrowType(registry, new NodeList(parameters1, new NodeList(parameter1, parameter2)), null);
    assertTrue(arrowType1.hasEqualParameters(arrowType6, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType6, true));

    ArrowType arrowType7 = new ArrowType(registry, new NodeList(parameters1, new NodeList(new NodeList(parameter1), parameter2)), null);
    assertFalse(arrowType1.hasEqualParameters(arrowType7, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType7, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    Node parameter1 = registry.createParameterNode("p1", INT_TYPE);
    Node parameter2 = registry.createParameterNode("p2", STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
    ArrowType arrowType2 = new ArrowType(registry, parameters1, null);
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));

    ArrowType arrowType3 = new ArrowType(registry, new NodeList(parameter1, parameter2), null);
    ArrowType arrowType4 = new ArrowType(registry, new NodeList(parameter2, parameter1), null);
    assertTrue(arrowType3.checkArrowEquivalenceHelper(arrowType4, false));
    assertTrue(arrowType3.checkArrowEquivalenceHelper(arrowType4, true));

    ArrowType arrowType5 = new ArrowType(registry, parameters1, INT_TYPE);
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType5, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType5, true));

    ArrowType arrowType6 = new ArrowType(registry, new NodeList(parameters1, new NodeList(parameter1, parameter2)), null);
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType6, false));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType6, true));

    ArrowType arrowType7 = new ArrowType(registry, new NodeList(parameters1, new NodeList(new NodeList(parameter1), parameter2)), null);
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType7, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType7, true));
  }
}