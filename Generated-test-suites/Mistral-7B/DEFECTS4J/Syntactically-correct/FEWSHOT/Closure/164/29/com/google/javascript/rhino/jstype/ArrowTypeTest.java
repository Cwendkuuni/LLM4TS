package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with null parameters and returnType
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));

    // Test with different return types
    ArrowType arrowType3 = new ArrowType(registry, null, registry.getBooleanType());
    ArrowType arrowType4 = new ArrowType(registry, null, registry.getIntType());
    assertFalse(arrowType3.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType3));

    // Test with different parameter types
    ArrowType arrowType5 = new ArrowType(registry, registry.getNumberType(), null);
    ArrowType arrowType6 = new ArrowType(registry, registry.getStringType(), null);
    assertFalse(arrowType5.isSubtype(arrowType6));
    assertFalse(arrowType6.isSubtype(arrowType5));

    // Test with same return type and different parameters
    ArrowType arrowType7 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());
    ArrowType arrowType8 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getStringType()), registry.getNumberType());
    assertFalse(arrowType7.isSubtype(arrowType8));
    assertFalse(arrowType8.isSubtype(arrowType7));

    // Test with same return type and same parameters
    ArrowType arrowType9 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());
    ArrowType arrowType10 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());
    assertTrue(arrowType9.isSubtype(arrowType10));
    assertTrue(arrowType10.isSubtype(arrowType9));

    // Test with a subtype as a parameter
    ArrowType arrowType11 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), arrowType3);
    assertTrue(arrowType3.isSubtype(arrowType11));
    assertTrue(arrowType11.isSubtype(arrowType3));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());
    ArrowType arrowType2 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());
    ArrowType arrowType3 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getStringType()), registry.getNumberType());
    ArrowType arrowType4 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getBooleanType());
    ArrowType arrowType5 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), arrowType3);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));
    assertTrue(arrowType5.hasEqualParameters(arrowType1));
  }

  @Test
  public void testEqualsAndHashcode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    ArrowType arrowType3 = new ArrowType(registry, null, registry.getNumberType());

    ArrowType arrowType4 = new ArrowType(registry, null, registry.getNumberType());
    ArrowType arrowType5 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());

    ArrowType arrowType6 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());
    ArrowType arrowType7 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType(), true);

    assertEquals(arrowType1, arrowType1);
    assertEquals(arrowType1, arrowType2);
    assertNotEquals(arrowType1, arrowType3);
    assertNotEquals(arrowType1, arrowType4);
    assertEquals(arrowType3, arrowType4);
    assertEquals(arrowType5, arrowType6);
    assertNotEquals(arrowType5, arrowType7);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();
    int hashCode3 = arrowType3.hashCode();
    int hashCode4 = arrowType4.hashCode();
    int hashCode5 = arrowType5.hashCode();
    int hashCode6 = arrowType6.hashCode();
    int hashCode7 = arrowType7.hashCode();

    assertEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, hashCode3);
    assertEquals(hashCode3, hashCode4);
    assertEquals(hashCode5, hashCode6);
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, registry.getBooleanType());
    ArrowType arrowType3 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());

    ArrowType arrowType4 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getBooleanType());
    ArrowType arrowType5 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getBooleanType(), true);

    assertThrows(UnsupportedOperationException.class, () -> arrowType1.getLeastSupertype(arrowType2));
    assertThrows(UnsupportedOperationException.class, () -> arrowType2.getLeastSupertype(arrowType1));
    assertThrows(UnsupportedOperationException.class, () -> arrowType3.getLeastSupertype(arrowType4));
    assertThrows(UnsupportedOperationException.class, () -> arrowType4.getLeastSupertype(arrowType3));
    assertEquals(arrowType4, arrowType5.getLeastSupertype(arrowType5));
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, registry.getBooleanType());
    ArrowType arrowType3 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());

    ArrowType arrowType4 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getBooleanType());
    ArrowType arrowType5 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getBooleanType(), true);

    assertThrows(UnsupportedOperationException.class, () -> arrowType1.getGreatestSubtype(arrowType2));
    assertThrows(UnsupportedOperationException.class, () -> arrowType2.getGreatestSubtype(arrowType1));
    assertThrows(UnsupportedOperationException.class, () -> arrowType3.getGreatestSubtype(arrowType4));
    assertThrows(UnsupportedOperationException.class, () -> arrowType4.getGreatestSubtype(arrowType3));
    assertNull(arrowType4.getGreatestSubtype(arrowType5));
    assertNull(arrowType5.getGreatestSubtype(arrowType4));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, registry.getBooleanType());
    ArrowType arrowType3 = new ArrowType(registry, registry.getParametersWithVarArgs(registry.getNumberType()), registry.getNumberType());

    assertEquals("ArrowType(null, null)", arrowType1.toString());
    assertEquals("ArrowType(null, Boolean)", arrowType2.toString());
    assertEquals("ArrowType(ParametersWithVarArgs(Number[]), Number)", arrowType3.toString());
  }
}