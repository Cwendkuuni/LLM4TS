package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertEquals(String.class, simpleType.getRawClass());
  }

  @Test
  public void testConstruct() {
    SimpleType simpleType = SimpleType.construct(String.class);
    assertEquals(String.class, simpleType.getRawClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithMap() {
    SimpleType.construct(Map.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithCollections() {
    SimpleType.construct(Collections.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithArray() {
    SimpleType.construct(Array.class);
  }

  @Test
  public void testNarrow() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType narrowed = simpleType.narrow(CharSequence.class);
    assertEquals(CharSequence.class, narrowed.getRawClass());
  }

  @Test
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentType(String.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
    }
  }

  @Test
  public void testWithValueHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType withValueHandler = simpleType.withValueHandler("handler");
    assertEquals("handler", withValueHandler.getValueHandler());
  }

  @Test
  public void testWithContentValueHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentValueHandler("handler");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
    }
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType staticTyping = simpleType.withStaticTyping();
    assertTrue(staticTyping.isStatic());
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(String.class);
    JavaType refined = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
    assertNull(refined);
  }

  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(String.class);
    assertFalse(simpleType.isContainerType());
  }


  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder sb = new StringBuilder();
    simpleType.getErasedSignature(sb);
    assertEquals("Ljava/lang/String;", sb.toString());
  }

  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder sb = new StringBuilder();
    simpleType.getGenericSignature(sb);
    assertEquals("Ljava/lang/String;;", sb.toString());
  }

  @Test
  public void testEqualsAndHashCode() {
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = new SimpleType(String.class);
    assertTrue(simpleType1.equals(simpleType2) && simpleType2.equals(simpleType1));
    assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
  }

}