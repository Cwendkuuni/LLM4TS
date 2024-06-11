
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertNotNull(simpleType);
    assertEquals(String.class, simpleType.getRawClass());
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
    SimpleType.construct(String[].class);
  }

  @Test
  public void testWithTypeHandler() {
    Object typeHandler = new Object();
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType typeWithHandler = simpleType.withTypeHandler(typeHandler);
    assertEquals(typeHandler, typeWithHandler.getTypeHandler());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(String.class);
    simpleType.withContentType(String.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    simpleType.withContentTypeHandler(new Object());
  }

  @Test
  public void testWithDefaultValueHandler() {
    Object defaultValueHandler = new Object();
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType typeWithDefaultHandler = simpleType.withValueHandler(defaultValueHandler);
    assertEquals(defaultValueHandler, typeWithDefaultHandler.getValueHandler());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    simpleType.withContentValueHandler(new Object());
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(String.class);
    assertFalse(simpleType.isStaticTyping());
    SimpleType staticType = simpleType.withStaticTyping();
    assertTrue(staticType.isStaticTyping());
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(String.class);
    assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
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
    assertEquals("Ljava/lang/String;", sb.toString());
  }

  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(String.class);
    assertEquals("[simple type, class java.lang.String]", simpleType.toString());
  }

  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = new SimpleType(String.class);
    assertTrue(simpleType1.equals(simpleType2));
  }
}
