package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType1 = new ArrowType(registry, parametersNode, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, getNativeType(UNKNOWN_TYPE), true);

    assertEqualsArrowTypes(arrowType1, arrowType2);
  }

  @Test
  public void testArrowTypeIsSubtype_SameType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(int.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeIsSubtype_DifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType1 = getNativeType(int.class);
    JSType returnType2 = getNativeType(String.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeIsSubtype_DifferentParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    Node parametersNode2 = registry.createParametersWithVarArgs(
        getNativeType(String.class), getNativeType(boolean.class));

    JSType returnType = getNativeType(int.class);
    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeIsSubtype_VariableArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    Node parametersNode2 = registry.createParametersWithVarArgs(
        getNativeType(int.class));

    JSType returnType = getNativeType(int.class);
    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType);

    // ArrowType1 is a subtype of ArrowType2 because it can accept any
    // number of int arguments, and ArrowType2 can accept at least one int
    // argument.
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtype_OptionalParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class).optional());
    Node parametersNode2 = registry.createParametersWithVarArgs(
        getNativeType(int.class));

    JSType returnType = getNativeType(int.class);
    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType);

    // ArrowType1 is a subtype of ArrowType2 because it can accept any
    // number of int arguments, and ArrowType2 can accept at most one int
    // argument.
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    Node parametersNode2 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, getNativeType(int.class));
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, getNativeType(int.class));

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testArrowTypeHasEqualParameters_TolerateUnknowns() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    Node parametersNode2 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(UNKNOWN_TYPE));

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, getNativeType(int.class));
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, getNativeType(int.class));

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
  }

  @Test
  public void testArrowTypeHasEqualParameters_NoParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(int.class));
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(int.class));

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testArrowTypeCheckEquivalenceHelper_SameTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType1 = getNativeType(int.class);
    JSType returnType2 = getNativeType(int.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testArrowTypeCheckEquivalenceHelper_DifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType1 = getNativeType(int.class);
    JSType returnType2 = getNativeType(String.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testArrowTypeEquals_SameTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType1 = getNativeType(int.class);
    JSType returnType2 = getNativeType(int.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertTrue(arrowType1.equals(arrowType2));
  }

  @Test
  public void testArrowTypeEquals_DifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType1 = getNativeType(int.class);
    JSType returnType2 = getNativeType(String.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertFalse(arrowType1.equals(arrowType2));
  }

  @Test
  public void testArrowTypeHashcode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType1 = getNativeType(int.class);
    JSType returnType2 = getNativeType(String.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    int hashcode1 = arrowType1.hashCode();
    int hashcode2 = arrowType2.hashCode();

    // The return types should have different hashcodes
    assertFalse(hashcode1 == hashcode2);

    // Changing the return types should change the hashcode
    ArrowType arrowType3 = new ArrowType(registry, parametersNode, getNativeType(boolean.class));
    assertFalse(hashcode1 == arrowType3.hashCode());

    // The parameters should not affect the hashcode if the return types are the same
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(int.class));
    assertFalse(hashcode1 == arrowType4.hashCode());
    assertFalse(hashcode2 == arrowType4.hashCode());
    assertEquals(hashcode1, arrowType4.hashCode());
  }

  @Test
  public void testArrowTypeEquals_SameParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType = getNativeType(int.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType);

    assertTrue(arrowType1.equals(arrowType2));
  }

  @Test
  public void testArrowTypeEquals_DifferentParametersButSameReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode1 = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    Node parametersNode2 = registry.createParametersWithVarArgs(
        getNativeType(long.class), getNativeType(boolean.class));
    JSType returnType = getNativeType(int.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType);

    assertFalse(arrowType1.equals(arrowType2));
  }

  @Test
  public void testArrowTypeEquals_SameParametersButDifferentReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType1 = getNativeType(int.class);
    JSType returnType2 = getNativeType(String.class);

    ArrowType arrowType1 = new ArrowType(registry, parametersNode, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode, returnType2);

    assertFalse(arrowType1.equals(arrowType2));
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        getNativeType(int.class), getNativeType(boolean.class));
    JSType returnType = getNativeType(int.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    String expectedString = "[ArrowType]\n" +
        "  Parameters: [FunctionType]\n" +
        "    Return Type: int\n" +
        "  Return Type: int\n";

    String actualString = arrowType.toString();

    assertEquals(expectedString, actualString);
  }

  private void assertEqualsArrowTypes(ArrowType arrowType1, ArrowType arrowType2) {
    assertEquals(arrowType1.getLeastSupertype(UNKNOWN_TYPE),
        arrowType2.getLeastSupertype(UNKNOWN_TYPE));
    assertEquals(arrowType1.getGreatestSubtype(UNKNOWN_TYPE),
        arrowType2.getGreatestSubtype(UNKNOWN_TYPE));
    assertEquals(arrowType1.testForEquality(UNKNOWN_TYPE),
        arrowType2.testForEquality(UNKNOWN_TYPE));
  }

  private JSType getNativeType(Class<?> clazz) {
    return JSTypeFactory.getTypeForJavaClass(clazz);
  }
}