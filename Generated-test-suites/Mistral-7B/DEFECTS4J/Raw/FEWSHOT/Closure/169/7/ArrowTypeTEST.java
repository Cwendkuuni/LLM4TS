package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;
import com.google.javascript.rhino.StaticScope;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ErrorReporter t;
  private Scope scope;

  @Before
  public void setUp() {
    t = new ErrorReporter();
    scope = new StaticScope<>(new JSTypeRegistry(), t);
  }

  @Test
  public void testIsSubtype_SameType() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope));
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope));

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypes() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope));
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), null, new StringType(scope));

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentParameterTypes() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new IntType(scope)), new IntType(scope));
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new StringType(scope)), new IntType(scope));

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_SameParameterAndReturnTypes() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope));
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope));

    ArrowType arrowType3 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope), false);
    ArrowType arrowType4 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope), true);

    assertTrue(arrowType1.isSubtype(arrowType3));
    assertTrue(arrowType1.isSubtype(arrowType4));
    assertTrue(arrowType3.isSubtype(arrowType1));
    assertTrue(arrowType4.isSubtype(arrowType1));

    assertTrue(arrowType2.isSubtype(arrowType3));
    assertTrue(arrowType2.isSubtype(arrowType4));
    assertTrue(arrowType3.isSubtype(arrowType2));
    assertTrue(arrowType4.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_DifferentInferredReturnTypes() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope), false);
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope), true);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_NullTypes() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters_SameTypes() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new IntType(scope)), new IntType(scope));
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new IntType(scope)), new IntType(scope));

    ArrowType arrowType3 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new IntType(scope)), new IntType(scope), false);
    ArrowType arrowType4 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new IntType(scope)), new IntType(scope), true);

    ArrowType arrowType5 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new StringType(scope)), new IntType(scope));

    assertTrue(arrowType1.hasEqualParameters(arrowType1, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType5, true));

    assertTrue(arrowType3.hasEqualParameters(arrowType1, true));
    assertTrue(arrowType3.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType3.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType3.hasEqualParameters(arrowType5, true));

    assertTrue(arrowType4.hasEqualParameters(arrowType1, true));
    assertTrue(arrowType4.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType4.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType4.hasEqualParameters(arrowType5, true));
  }

  @Test
  public void testHasEqualParameters_DifferentTypes() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new IntType(scope)), new IntType(scope));
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new StringType(scope)), new IntType(scope));

    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType2.hasEqualParameters(arrowType1, true));
  }

  @Test
  public void testHasEqualParameters_NullTypesOrEmptyParams() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, true));

    ArrowType arrowType3 = new ArrowType(null, null, null);
    ArrowType arrowType4 = new ArrowType(null, null, null);

    ArrowType arrowType5 = new ArrowType(null, null, null);
    ArrowType arrowType6 = new ArrowType(new ArrayLiteral(scope), null, null);

    ArrowType arrowType7 = new ArrowType(null, null, null, false);
    ArrowType arrowType8 = new ArrowType(null, null, null, true);

    assertTrue(arrowType3.hasEqualParameters(arrowType5, true));
    assertTrue(arrowType3.hasEqualParameters(arrowType6, true));
    assertTrue(arrowType3.hasEqualParameters(arrowType7, true));
    assertTrue(arrowType3.hasEqualParameters(arrowType8, true));

    assertTrue(arrowType5.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType5.hasEqualParameters(arrowType6, true));
    assertTrue(arrowType5.hasEqualParameters(arrowType7, true));
    assertTrue(arrowType5.hasEqualParameters(arrowType8, true));

    assertTrue(arrowType6.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType6.hasEqualParameters(arrowType5, true));
    assertTrue(arrowType6.hasEqualParameters(arrowType7, true));
    assertTrue(arrowType6.hasEqualParameters(arrowType8, true));

    assertTrue(arrowType7.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType7.hasEqualParameters(arrowType5, true));
    assertTrue(arrowType7.hasEqualParameters(arrowType6, true));
    assertTrue(arrowType7.hasEqualParameters(arrowType8, true));

    assertTrue(arrowType8.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType8.hasEqualParameters(arrowType5, true));
    assertTrue(arrowType8.hasEqualParameters(arrowType6, true));
    assertTrue(arrowType8.hasEqualParameters(arrowType7, true));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope));
    ArrowType arrowType2 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope));
    ArrowType arrowType3 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope), true);
    ArrowType arrowType4 =
        new ArrowType(new JSTypeRegistry(), null, new IntType(scope), false);

    ArrowType arrowType5 =
        new ArrowType(new JSTypeRegistry(), new ArrayLiteral(scope, new IntType(scope)), new IntType(scope));

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    assertEquals(arrowType1.hashCode(), arrowType3.hashCode());
    assertEquals(arrowType1.hashCode(), arrowType4.hashCode());
    assertNotEquals(arrowType1.hashCode(), arrowType5.hashCode());
    assertNotEquals(arrowType2.hashCode(), arrowType3.hashCode());
    assertNotEquals(arrowType2.hashCode(), arrowType4.hashCode());
    assertNotEquals(arrowType2.hashCode(), arrowType5.hashCode());
    assertNotEquals(arrowType3.hashCode(), arrowType4.hashCode());
    assertNotEquals(arrowType3.hashCode(), arrowType5.hashCode());
    assertNotEquals(arrowType4.hashCode(), arrowType5.hashCode());
  }
}