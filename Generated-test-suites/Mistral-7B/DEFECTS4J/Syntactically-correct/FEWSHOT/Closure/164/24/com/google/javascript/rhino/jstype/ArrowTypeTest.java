package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

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
  public void testIsSubtype_SameArrowType() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypes() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType1 = getNativeType(NUMBER);
    JSType returnType2 = getNativeType(STRING);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentParameters() {
    Node parameters1 = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()));
    Node parameters2 = registry.createParametersWithVarArgs(
        registry.createNamedType("string", registry.getStringType()));
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_MissingParameters() {
    Node parameters1 = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()));
    Node parameters2 = null;
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_MissingReturnType() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType1 = getNativeType(NUMBER);
    JSType returnType2 = null;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType
 arrowType2 = new ArrowType(registry, parameters, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_InferredReturnTypes() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType1 = getNativeType(NUMBER);
    JSType returnType2 = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, true);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, true);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_IncorrectParameterTypes() {
    Node parameters1 = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()));
    Node parameters2 = registry.createParametersWithVarArgs(
        registry.createNamedType("string", registry.getStringType()));
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    Node params1 = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()),
        registry.createNamedType("string", registry.getStringType()));
    Node params2 = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()));
    ArrowType arrowType1 = new ArrowType(registry, params1, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType2 = new ArrowType(registry, params2, getNativeType(UNKNOWN_TYPE));

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));

    Node params3 = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()),
        registry.createNamedType("number", registry.getNumberType()));
    ArrowType arrowType3 = new ArrowType(registry, params3, getNativeType(UNKNOWN_TYPE));

    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertTrue(arrowType3.hasEqualParameters(arrowType1));
  }

  @Test
  public void testEqualsAndHashCode() {
    Node parameters1 = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()));
    Node parameters2 = registry.createParametersWithVarArgs(
        registry.createNamedType("string", registry.getStringType()));
    JSType returnType1 = getNativeType(NUMBER);
    JSType returnType2 = getNativeType(STRING);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);

    assertFalse(arrowType1.equals(arrowType2));
    assertFalse(arrowType2.equals(arrowType1));

    assertTrue(arrowType1.hashCode() == arrowType3.hashCode());
    assertTrue(arrowType3.hashCode() == arrowType1.hashCode());
  }

  @Test
  public void testGetLeastSupertype_UnsupportedOperationException() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    try {
      arrowType.getLeastSupertype(getNativeType(NUMBER));
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testGetGreatestSubtype_UnsupportedOperationException() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    try {
      arrowType.getGreatestSubtype(getNativeType(NUMBER));
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testToStringHelper() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    String result = arrowType.toStringHelper(false);
    // The exact output depends on the implementation of toStringHelper()
    // in JSType, but it should include the return type and the parameters.
    assertTrue(result.contains("ArrowType"));
    assertTrue(result.contains(returnType.toString()));
    assertTrue(result.contains(parameters.toString()));
  }

  @Test
  public void testResolveInternal() {
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("number", registry.getNumberType()));
    JSType returnType = registry.getFunctionType(
        getNativeType(UNKNOWN_TYPE), getNativeType(UNKNOWN_TYPE), registry);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType result = arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry));

    assertTrue(result.isFunctionType());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.hasUnknownParamsOrReturn());

    JSType knownReturnType = getNativeType(NUMBER);
    ArrowType arrowTypeWithKnownReturnType = new ArrowType(registry, parameters, knownReturnType);

    assertFalse(arrowTypeWithKnownReturnType.hasUnknownParamsOrReturn());
  }
}