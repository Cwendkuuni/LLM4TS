package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
  
  @Test
  public void testConstructUnsafe() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertNotNull(type);
    assertEquals(String.class, type.getRawClass());
    assertNull(type.getBindings());
    assertNull(type.getSuperClass());
    assertNull(type.getInterfaces());
    assertEquals(0, type.getExtraHash());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  @Test
  public void testConstruct() {
    SimpleType type = SimpleType.construct(Integer.class);
    assertNotNull(type);
    assertEquals(Integer.class, type.getRawClass());
    assertNotNull(type.getBindings());
    assertNull(type.getSuperClass());
    assertNull(type.getInterfaces());
    assertEquals(0, type.getExtraHash());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  @Test
  public void testNarrow() {
    SimpleType baseType = SimpleType.construct(Number.class);
    SimpleType type = (SimpleType) baseType.narrow(Integer.class);
    assertNotNull(type);
    assertEquals(Integer.class, type.getRawClass());
    assertEquals(baseType.getBindings(), type.getBindings());
    assertEquals(baseType.getSuperClass(), type.getSuperClass());
    assertEquals(baseType.getInterfaces(), type.getInterfaces());
    assertEquals(baseType.getExtraHash(), type.getExtraHash());
    assertEquals(baseType.getValueHandler(), type.getValueHandler());
    assertEquals(baseType.getTypeHandler(), type.getTypeHandler());
    assertEquals(baseType.isStatic(), type.isStatic());
  }
  
  @Test
  public void testWithContentType() {
    SimpleType baseType = SimpleType.construct(List.class);
    try {
      baseType.withContentType(SimpleType.construct(Integer.class));
      fail("UnsupportedOperationException expected");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
  
  @Test
  public void testWithContentTypeHandler() {
    SimpleType baseType = SimpleType.construct(List.class);
    try {
      baseType.withContentTypeHandler(new Object());
      fail("UnsupportedOperationException expected");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
  
  @Test
  public void testWithContentTypeValueHandler() {
    SimpleType baseType = SimpleType.construct(List.class);
    try {
      baseType.withContentValueHandler(new Object());
      fail("UnsupportedOperationException expected");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType baseType = SimpleType.construct(Number.class);
    SimpleType type = (SimpleType) baseType.withStaticTyping();
    assertNotNull(type);
    assertEquals(Number.class, type.getRawClass());
    assertEquals(baseType.getBindings(), type.getBindings());
    assertEquals(baseType.getSuperClass(), type.getSuperClass());
    assertEquals(baseType.getInterfaces(), type.getInterfaces());
    assertEquals(baseType.getExtraHash(), type.getExtraHash());
    assertEquals(baseType.getValueHandler(), type.getValueHandler());
    assertEquals(baseType.getTypeHandler(), type.getTypeHandler());
    assertTrue(type.isStatic());
  }
  
  @Test
  public void testRefine() {
    SimpleType baseType = SimpleType.construct(Number.class);
    JavaType type = baseType.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
    assertNull(type);
  }
  
  @Test
  public void testBuildCanonicalName() {
    SimpleType type = SimpleType.construct(Number.class);
    String name = type.buildCanonicalName();
    assertEquals(Number.class.getName(), name);
  }
  
  @Test
  public void testIsContainerType() {
    SimpleType type = SimpleType.construct(Number.class);
    assertFalse(type.isContainerType());
  }
  
  @Test
  public void testGetErasedSignature() {
    SimpleType type = SimpleType.construct(Number.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder signature = type.getErasedSignature(sb);
    assertNotNull(signature);
  }
  
  @Test
  public void testGetGenericSignature() {
    SimpleType type = SimpleType.construct(Number.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder signature = type.getGenericSignature(sb);
    assertNotNull(signature);
  }
  
  @Test
  public void testToString() {
    SimpleType type = SimpleType.construct(Number.class);
    String string = type.toString();
    assertNotNull(string);
    assertTrue(string.contains(Number.class.getName()));
  }
  
  @Test
  public void testEquals() {
    SimpleType type1 = SimpleType.construct(Number.class);
    SimpleType type2 = SimpleType.construct(Number.class);
    assertTrue(type1.equals(type2));
    
    SimpleType type3 = SimpleType.construct(Integer.class);
    assertFalse(type1.equals(type3));
  }
}