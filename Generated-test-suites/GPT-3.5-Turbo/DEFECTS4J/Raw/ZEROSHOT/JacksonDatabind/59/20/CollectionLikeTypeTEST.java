package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

  @Test
  public void testConstruct() {
    Class<?> rawType = null;
    TypeBindings bindings = null;
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = null;
    CollectionLikeType result = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
    assertNull(result);
  }

  @Test
  public void testConstructDeprecated() {
    Class<?> rawType = null;
    JavaType elemT = null;
    CollectionLikeType result = CollectionLikeType.construct(rawType, elemT);
    assertNull(result);
  }

  @Test
  public void testUpgradeFrom() {
    JavaType baseType = null;
    JavaType elementType = null;
    try {
      CollectionLikeType.upgradeFrom(baseType, elementType);
    } catch (IllegalArgumentException e) {
      assertEquals("Can not upgrade from an instance of null", e.getMessage());
    }
  }

  @Test
  public void test_narrow() {
    Class<?> subclass = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    JavaType result = instance._narrow(subclass);
    assertNotNull(result);
  }

  @Test
  public void testWithContentType() {
    JavaType contentType = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    JavaType result = instance.withContentType(contentType);
    assertNotNull(result);
  }

  @Test
  public void testWithTypeHandler() {
    Object h = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    CollectionLikeType result = instance.withTypeHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithContentTypeHandler() {
    Object h = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    CollectionLikeType result = instance.withContentTypeHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithValueHandler() {
    Object h = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    CollectionLikeType result = instance.withValueHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithContentValueHandler() {
    Object h = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    CollectionLikeType result = instance.withContentValueHandler(h);
    assertNotNull(result);
  }

  @Test
  public void testWithStaticTyping() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    CollectionLikeType result = instance.withStaticTyping();
    assertNotNull(result);
  }

  @Test
  public void testRefine() {
    Class<?> rawType = null;
    TypeBindings bindings = null;
    JavaType superClass = null;
    JavaType[] superInterfaces = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    JavaType result = instance.refine(rawType, bindings, superClass, superInterfaces);
    assertNotNull(result);
  }

  @Test
  public void testIsContainerType() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    boolean result = instance.isContainerType();
    assertTrue(result);
  }

  @Test
  public void testIsCollectionLikeType() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    boolean result = instance.isCollectionLikeType();
    assertTrue(result);
  }

  @Test
  public void testGetContentType() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    JavaType result = instance.getContentType();
    assertNull(result);
  }

  @Test
  public void testGetContentValueHandler() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    Object result = instance.getContentValueHandler();
    assertNull(result);
  }

  @Test
  public void testGetContentTypeHandler() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    Object result = instance.getContentTypeHandler();
    assertNull(result);
  }

  @Test
  public void testHasHandlers() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    boolean result = instance.hasHandlers();
    assertFalse(result);
  }

  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    StringBuilder result = instance.getErasedSignature(sb);
    assertNotNull(result);
  }

  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    StringBuilder result = instance.getGenericSignature(sb);
    assertNotNull(result);
  }

  @Test
  public void testBuildCanonicalName() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    String result = instance.buildCanonicalName();
    assertNotNull(result);
  }

  @Test
  public void testIsTrueCollectionType() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    boolean result = instance.isTrueCollectionType();
    assertFalse(result);
  }

  @Test
  public void testEquals() {
    Object o = null;
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    boolean result = instance.equals(o);
    assertFalse(result);
  }

  @Test
  public void testToString() {
    CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
    String result = instance.toString();
    assertNotNull(result);
  }

}