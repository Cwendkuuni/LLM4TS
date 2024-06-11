package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test identity
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    assertTrue(arrowType1.isSubtype(arrowType1));

    // Test empty arrow types
    ArrowType arrowType2 = new ArrowType(registry, null, null, false);
    ArrowType arrowType3 = new ArrowType(registry, null, null, false);
    assertTrue(arrowType2.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType2));

    // Test arrow types with different return types
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(String.class), false);
    assertFalse(arrowType4.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType4));

    // Test arrow types with different and inferred return types
    ArrowType arrowType6 = new ArrowType(registry, null, getNativeType(int.class), true);
    ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(String.class), true);
    assertTrue(arrowType6.isSubtype(arrowType7));
    assertTrue(arrowType7.isSubtype(arrowType6));

    // Test arrow types with different parameter lists
    ArrowType arrowType8 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType9 = new ArrowType(registry, null, getNativeType(double.class), false);
    assertFalse(arrowType8.isSubtype(arrowType9));
    assertFalse(arrowType9.isSubtype(arrowType8));

    // Test arrow types with different and optional parameters
    ArrowType arrowType10 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType11 = new ArrowType(registry, null, getNativeType(int.class), true);
    assertTrue(arrowType10.isSubtype(arrowType11));
    assertFalse(arrowType11.isSubtype(arrowType10));

    // Test arrow types with varargs
    ArrowType arrowType12 = new ArrowType(registry, getNativeType(int[].class), null, false);
    ArrowType arrowType13 = new ArrowType(registry, null, null, false);
    assertFalse(arrowType12.isSubtype(arrowType13));
    assertFalse(arrowType13.isSubtype(arrowType12));

    // Test arrow types with nested arrow types
    ArrowType arrowType14 = new ArrowType(registry, null, arrowType12, false);
    ArrowType arrowType15 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType16 = new ArrowType(registry, null, arrowType15, false);
    assertFalse(arrowType14.isSubtype(arrowType16));
    assertFalse(arrowType16.isSubtype(arrowType14));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test identity
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    assertTrue(arrowType1.hasEqualParameters(arrowType1, true));

    // Test empty arrow types
    ArrowType arrowType2 = new ArrowType(registry, null, null, false);
    ArrowType arrowType3 = new ArrowType(registry, null, null, false);
    assertTrue(arrowType2.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType3.hasEqualParameters(arrowType2, true));

    // Test arrow types with different return types
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(String.class), false);
    assertFalse(arrowType4.hasEqualParameters(arrowType5, true));
    assertFalse(arrowType5.hasEqualParameters(arrowType4, true));

    // Test arrow types with different but tolerated unknown types
    ArrowType arrowType6 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(Object.class), false);
    assertTrue(arrowType6.hasEqualParameters(arrowType7, true));
    assertTrue(arrowType7.hasEqualParameters(arrowType6, true));

    // Test arrow types with no arguments
    ArrowType arrowType8 = new ArrowType(registry, null, null, false);
    ArrowType arrowType9 = new ArrowType(registry, null, null, false);
    assertTrue(arrowType8.hasEqualParameters(arrowType9, true));
    assertTrue(arrowType9.hasEqualParameters(arrowType8, true));

    // Test arrow types with single arguments
    ArrowType arrowType10 = new ArrowType(registry, getNativeType(int.class), null, false);
    ArrowType arrowType11 = new ArrowType(registry, getNativeType(double.class), null, false);
    assertFalse(arrowType10.hasEqualParameters(arrowType11, true));
    assertFalse(arrowType11.hasEqualParameters(arrowType10, true));

    // Test arrow types with optional arguments
    ArrowType arrowType12 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType13 = new ArrowType(registry, null, getNativeType(int.class), true);
    assertTrue(arrowType12.hasEqualParameters(arrowType13, true));
    assertTrue(arrowType13.hasEqualParameters(arrowType12, true));

    // Test arrow types with varargs
    ArrowType arrowType14 = new ArrowType(registry, getNativeType(int[].class), null, false);
    ArrowType arrowType15 = new ArrowType(registry, null, null, false);
    assertFalse(arrowType14.hasEqualParameters(arrowType15, true));
    assertFalse(arrowType15.hasEqualParameters(arrowType14, true));

    // Test arrow types with nested arrow types
    ArrowType arrowType16 = new ArrowType(registry, null, arrowType14, false);
    ArrowType arrowType17 = new ArrowType(registry, null, getNativeType(int.class), false);
    ArrowType arrowType18 = new ArrowType(registry, null, arrowType17, false);
    assertFalse(arrowType16.hasEqualParameters(arrowType18, true));
    assertFalse(arrowType18.hasEqualParameters(arrowType16, true));
  }

  private JSType getNativeType(Class<?> clazz) {
    if (clazz == null) {
      return null;
    }
    return JSTypeFactory.getInstance().getNativeType(clazz);
  }
}