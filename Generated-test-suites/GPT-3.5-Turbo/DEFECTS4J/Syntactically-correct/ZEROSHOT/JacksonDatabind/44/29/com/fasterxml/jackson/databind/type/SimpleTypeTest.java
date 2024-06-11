package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
    assertEquals(Object.class, simpleType.getRawClass());
  }

  @Test
  public void testConstruct() {
    SimpleType simpleType = SimpleType.construct(Object.class);
    assertEquals(Object.class, simpleType.getRawClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithMap() {
    SimpleType.construct(Map.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithCollection() {
    SimpleType.construct(Collection.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithArray() {
    SimpleType.construct(Array.class);
  }

  @Test
  public void testNarrow() {
    SimpleType simpleType = new SimpleType(Object.class);
    SimpleType narrowedType = simpleType._narrow(String.class);
    assertEquals(String.class, narrowedType.getRawClass());
    assertNotEquals(simpleType, narrowedType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNarrowWithInvalidSubclass() {
    SimpleType simpleType = new SimpleType(Object.class);
    simpleType._narrow(List.class);
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(Object.class);
    JavaType refinedType = simpleType.refine(String.class, null, null, null);
    assertNull(refinedType);
  }

  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(Object.class);
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
    assertNotNull(erasedSignature);
  }

  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder genericSignature = simpleType.getGenericSignature(sb);
    assertNotNull(genericSignature);
  }

  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(Object.class);
    String result = simpleType.toString();
    assertNotNull(result);
  }

  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(Object.class);
    SimpleType simpleType2 = new SimpleType(Object.class);
    assertTrue(simpleType1.equals(simpleType2));

    SimpleType simpleType3 = new SimpleType(String.class);
    assertFalse(simpleType1.equals(simpleType3));
  }
}