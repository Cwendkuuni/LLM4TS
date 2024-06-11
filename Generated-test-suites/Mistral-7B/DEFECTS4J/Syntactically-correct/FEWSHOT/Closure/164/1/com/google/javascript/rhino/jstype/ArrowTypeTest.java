package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  JSTypeRegistry registry;
  ErrorReporter errorReporter;
  ArrowType arrowType;
  ArrowType arrowType1;
  ArrowType arrowType2;
  ArrowType arrowType3;
  ArrowType arrowType4;
  ArrowType arrowType5;
  ArrowType arrowTypeInferred;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    registry = new JSTypeRegistry(errorReporter);

    JSType intType = JSTypeNative.getIntInstance(registry);
    JSType stringType = JSTypeNative.getStringInstance(registry);
    JSType booleanType = JSTypeNative.getBooleanInstance(registry);

    arrowType = new ArrowType(registry, null, intType, false);
    arrowType1 = new ArrowType(registry, null, stringType, false);
    arrowType2 = new ArrowType(registry, null, booleanType, false);
    arrowType3 = new ArrowType(registry, null, intType, true);
    arrowType4 = new ArrowType(registry, null, null, false);
    arrowType5 = new ArrowType(registry, null, null, true);
    arrowTypeInferred = new ArrowType(registry, null, null, true);
  }

  @Test
  public void testIsSubtype() {
    assertTrue(arrowType.isSubtype(arrowType1));
    assertTrue(arrowType.isSubtype(arrowType2));
    assertTrue(arrowType1.isSubtype(arrowType));
    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType2.isSubtype(arrowType));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType2.isSubtype(arrowType2));
    assertTrue(arrowType3.isSubtype(arrowType));
    assertTrue(arrowType3.isSubtype(arrowType1));
    assertFalse(arrowType3.isSubtype(arrowType2));
    assertFalse(arrowType4.isSubtype(arrowType));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertFalse(arrowType4.isSubtype(arrowType2));
    assertFalse(arrowType4.isSubtype(arrowType3));
    assertFalse(arrowType5.isSubtype(arrowType));
    assertFalse(arrowType5.isSubtype(arrowType1));
    assertFalse(arrowType5.isSubtype(arrowType2));
    assertFalse(arrowType5.isSubtype(arrowType3));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType11 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType12 = new ArrowType(registry, null, intType, false);

    assertTrue(arrowType.hasEqualParameters(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType));
    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType11));
    assertTrue(arrowType1.hasEqualParameters(arrowType12));
    assertFalse(arrowType.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
    assertFalse(arrowType3.hasEqualParameters(arrowType));
    assertFalse(arrowType3.hasEqualParameters(arrowType1));
    assertFalse(arrowType3.hasEqualParameters(arrowType2));
    assertFalse(arrowType3.hasEqualParameters(arrowType3));
  }

  @Test
  public void testEquivalence() {
    ArrowType arrowType11 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType12 = new ArrowType(registry, null, intType, false);

    assertTrue(arrowType.isEquivalentTo(arrowType));
    assertTrue(arrowType.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType));
    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType11));
    assertTrue(arrowType1.isEquivalentTo(arrowType12));
    assertFalse(arrowType.isEquivalentTo(arrowType2));
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
    assertFalse(arrowType3.isEquivalentTo(arrowType));
    assertFalse(arrowType3.isEquivalentTo(arrowType1));
    assertFalse(arrowType3.isEquivalentTo(arrowType2));
    assertFalse(arrowType3.isEquivalentTo(arrowType3));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType11 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType12 = new ArrowType(registry, null, intType, false);

    int hashCode = arrowType.hashCode();
    assertEquals(hashCode, arrowType.hashCode());
    assertEquals(hashCode, arrowType1.hashCode());
    assertEquals(hashCode, arrowType11.hashCode());
    assertEquals(hashCode, arrowType12.hashCode());

    arrowType.returnType = stringType;
    assertNotEquals(hashCode, arrowType.hashCode());

    arrowType.parameters = new Node(registry.createIdentifier("x"), intType);
    assertNotEquals(hashCode, arrowType.hashCode());

    arrowType.returnType = null;
    arrowType.parameters = null;
    assertEquals(hashCode, arrowType.hashCode());
  }

  @Test
  public void testLeastSupertype() {
    throw new TestCaseNotFoundException("Unsupported operation: ArrowType.getLeastSupertype(JSType)");
  }

  @Test
  public void testGreatestSubtype() {
    throw new TestCaseNotFoundException("Unsupported operation: ArrowType.getGreatestSubtype(JSType)");
  }

  @Test
  public void testVisit() {
    throw new TestCaseNotFoundException("Unsupported operation: ArrowType.visit(Visitor<T>)");
  }

  @Test
  public void testToStringHelper() {
    assertEquals("ArrowType(returnType=int, hasUnknownParamsOrReturn=false)", arrowType.toStringHelper(false));
  }

  @Test
  public void testResolveInternal() {
    arrowType.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertNotNull(arrowType.returnType);

    arrowType4.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertNull(arrowType4.returnType);

    arrowType5.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertTrue(arrowType5.returnTypeInferred);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertTrue(arrowType3.hasUnknownParamsOrReturn());
    assertTrue(arrowType4.hasUnknownParamsOrReturn());
    assertTrue(arrowType5.hasUnknownParamsOrReturn());
  }
}