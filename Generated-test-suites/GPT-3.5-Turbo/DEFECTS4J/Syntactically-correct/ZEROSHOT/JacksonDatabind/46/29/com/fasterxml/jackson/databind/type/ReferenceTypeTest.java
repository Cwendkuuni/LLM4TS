package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

  @Test
  public void testConstruct() {
    ReferenceType referenceType = ReferenceType.construct(null, null, null, null);
    assertNotNull(referenceType);
  }

  @Test
  public void testWithTypeHandler() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(referenceType, referenceType.withTypeHandler(null));
  }

  @Test
  public void testWithContentTypeHandler() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(referenceType, referenceType.withContentTypeHandler(null));
  }

  @Test
  public void testWithValueHandler() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(referenceType, referenceType.withValueHandler(null));
  }

  @Test
  public void testWithContentValueHandler() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(referenceType, referenceType.withContentValueHandler(null));
  }

  @Test
  public void testWithStaticTyping() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(referenceType, referenceType.withStaticTyping());
  }

  @Test
  public void testBuildCanonicalName() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertNotNull(referenceType.buildCanonicalName());
  }

  @Test
  public void testNarrow() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(referenceType, referenceType._narrow(null));
  }

  @Test
  public void testGetReferencedType() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertNotNull(referenceType.getReferencedType());
  }

  @Test
  public void testIsReferenceType() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertTrue(referenceType.isReferenceType());
  }

  @Test
  public void testContainedTypeCount() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(1, referenceType.containedTypeCount());
  }

  @Test
  public void testContainedType() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(null, referenceType.containedType(0));
  }

  @Test
  public void testContainedTypeName() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(null, referenceType.containedTypeName(0));
  }

  @Test
  public void testGetParameterSource() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertEquals(null, referenceType.getParameterSource());
  }

  @Test
  public void testGetErasedSignature() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertNotNull(referenceType.getErasedSignature(null));
  }

  @Test
  public void testGetGenericSignature() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertNotNull(referenceType.getGenericSignature(null));
  }

  @Test
  public void testToString() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertNotNull(referenceType.toString());
  }

  @Test
  public void testEquals() {
    ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
    assertFalse(referenceType.equals(null));
    assertTrue(referenceType.equals(referenceType));
  }
}