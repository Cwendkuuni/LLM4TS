package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ErrorReporter errorReporter;
  private StaticScope<JSType> staticScope;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    staticScope = new StaticScope<>(errorReporter);
    registry = new JSTypeRegistry();
  }

  @Test
  public void testIsSubtype() {
    // Create test cases with known types
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(Number.class), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(Boolean.class), false);
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(Object.class), false);

    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(String.class), true);

    // Test the base case
    assertTrue(arrowType1.isSubtype(arrowType1));

    // Test covariant relationship
    assertTrue(arrowType1.isSubtype(arrowType3));
    assertTrue(arrowType1.isSubtype(arrowType4));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType3.isSubtype(arrowType1));
    assertTrue(arrowType4.isSubtype(arrowType1));

    // Test contravariant relationship
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    // Test covariant relationship with unknown types
    ArrowType arrowType6 = new ArrowType(registry, null, null, false);
    ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(String.class), false);
    assertTrue(arrowType6.isSubtype(arrowType7));

    // Test contravariant relationship with unknown types
    assertFalse(arrowType7.isSubtype(arrowType6));

    // Test with null types
    ArrowType arrowType8 = new ArrowType(registry, null, null, false);
    ArrowType arrowType9 = new ArrowType(registry, null, null, false);
    assertTrue(arrowType8.isSubtype(arrowType9));
    assertTrue(arrowType9.isSubtype(arrowType8));

    // Test inferred return types
    ArrowType arrowType10 = new ArrowType(registry, null, null, true);
    ArrowType arrowType11 = new ArrowType(registry, null, getNativeType(String.class), false);
    assertTrue(arrowType11.isSubtype(arrowType10));
    assertFalse(arrowType10.isSubtype(arrowType11));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(Number.class), false);
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(Object.class), false);
    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(Number[].class), false);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));

    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));
    assertFalse(arrowType1.hasEqualParameters(arrowType5));
  }

  @Test
  public void testIsEquivalentTo() {
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(Number.class), false);
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(Object.class), false);
    ArrowType arrowType5 = new ArrowType(registry, null, null, false);

    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));

    assertFalse(arrowType1.isEquivalentTo(arrowType3));
    assertFalse(arrowType1.isEquivalentTo(arrowType4));
    assertFalse(arrowType1.isEquivalentTo(arrowType5));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(String.class), false);
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(Number.class), false);
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(Object.class), false);
    ArrowType arrowType5 = new ArrowType(registry, null, null, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();

    assertEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, arrowType2.hashCode());
    assertNotEquals(hashCode1, arrowType3.hashCode());
    assertNotEquals(hashCode1, arrowType4.hashCode());
    assertNotEquals(hashCode1, arrowType5.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testVisit() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testToString() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    String expected = "ArrowType[inferred return type, inferred parameters]";
    assertEquals(expected, arrowType.toString());

    Node parametersNode = registry.createList(registry.createParameter("a", getNativeType(String.class)),
        registry.createParameter("b", getNativeType(Number.class)), null);
    ArrowType arrowTypeWithParams = new ArrowType(registry, parametersNode, null, false);
    expected = "ArrowType[parameters: [Parameter[a: String], Parameter[b: Number]], inferred return type]";
    assertEquals(expected, arrowTypeWithParams.toString());

    ArrowType arrowTypeWithKnownReturn = new ArrowType(registry, null, getNativeType(Boolean.class), false);
    expected = "ArrowType[inferred parameters, Boolean]";
    assertEquals(expected, arrowTypeWithKnownReturn.toString());
  }

  @Test
  public void testToStringForAnnotations() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    String expected = "ArrowType";
    assertEquals(expected, arrowType.toStringHelper(true));
  }

  @Test
  public void testResolve() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    // Test with null parameters and return type
    ArrowType resolvedArrowType = arrowType.resolve(errorReporter, staticScope);
    assertEquals(arrowType, resolvedArrowType);

    // Test with resolved parameters and return type
    Node parametersNode = registry.createList(registry.createParameter("a", getNativeType(String.class)),
        registry.createParameter("b", getNativeType(Number.class)), null);
    ArrowType arrowTypeWithParams = new ArrowType(registry, parametersNode, null, false);
    ArrowType resolvedArrowTypeWithParams = arrowTypeWithParams.resolve(errorReporter, staticScope);
    JSType expectedTypeWithParams = registry.createFunctionType(getNativeType(Object.class),
        registry.createList(getNativeType(String.class), getNativeType(Number.class), getNativeType(Object[].class)),
        getNativeType(Object.class), null);
    assertEquals(expectedTypeWithParams, resolvedArrowTypeWithParams.returnType);

    // Test with inferred return type
    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, null, null, true);
    ArrowType resolvedArrowTypeWithInferredReturn = arrowTypeWithInferredReturn.resolve(errorReporter, staticScope);
    assertEquals(getNativeType(Object.class), resolvedArrowTypeWithInferredReturn.returnType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    assertFalse(arrowType.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownParams = new ArrowType(registry, null, null, false);
    arrowTypeWithUnknownParams.parameters = registry.createList(registry.createParameter("a", null), null);
    assertTrue(arrowTypeWithUnknownParams.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, null, null, false);
    arrowTypeWithUnknownReturn.returnType = null;
    assertTrue(arrowTypeWithUnknownReturn.hasUnknownParamsOrReturn());
  }

  private JSType getNativeType(Class<?> clazz) {
    return registry.getNativeType(JSTypeNative.getTypeFromJava(clazz));
  }

  private <T> T safeResolve(JSType type, ErrorReporter errorReporter, StaticScope<JSType> scope) {
    try {
      return type.resolve(errorReporter, scope);
    } catch (Error e) {
      errorReporter.reportError(e.getMessage(), e.getStackTrace());
      return null;
    }
  }
}