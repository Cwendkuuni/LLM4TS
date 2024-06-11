package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
  
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    try {
      arrowType1.getLeastSupertype(arrowType1);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {}
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    try {
      arrowType1.getGreatestSubtype(arrowType1);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {}
  }

  @Test
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    try {
      arrowType1.testForEquality(arrowType1);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {}
  }

  @Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    try {
      arrowType1.visit(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {}
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    assertEquals(BooleanLiteralSet.TRUE, arrowType1.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = registry.getErrorReporter();
    StaticScope<JSType> scope = registry.createScope();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    try {
      arrowType1.resolveInternal(errorReporter, scope);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {}
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    assertFalse(arrowType1.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    String expectedString = "com.google.javascript.rhino.jstype.ArrowType";
    assertEquals(expectedString, arrowType1.toStringHelper(false));
  }

}