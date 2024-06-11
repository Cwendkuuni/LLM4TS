
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class CollectionTypeTest {

  @Test
  public void testConstruct() {
    // Test if CollectionType is properly constructed
    TypeBindings bindings = new TypeBindings();
    JavaType superClass = null;
    JavaType[] superInts = new JavaType[0];
    JavaType elemT = new JavaType();
    Object valueHandler = null;
    Object typeHandler = null;

    CollectionType collectionType = CollectionType.construct(
        CollectionType.class, bindings, superClass, superInts, elemT);

    assertNotNull(collectionType);
    assertEquals(CollectionType.class, collectionType.getRawClass());
    assertEquals(bindings, collectionType.getBindings());
    assertEquals(superClass, collectionType.getSuperClass());
    assertArrayEquals(superInts, collectionType.getInterfaces());
    assertNotNull(collectionType.getElementType());
    assertEquals(valueHandler, collectionType.getValueHandler());
    assertEquals(typeHandler, collectionType.getTypeHandler());
    assertFalse(collectionType.isContainerType());
  }

  @Test
  public void testWithContentType() {
    // Test if withContentType sets the content type properly and returns new instance
    JavaType contentType = new JavaType();
    CollectionType collectionType = new CollectionType(CollectionType.class);
    CollectionType newCollectionType = collectionType.withContentType(contentType);

    assertNotSame(collectionType, newCollectionType);
    assertEquals(contentType, newCollectionType.getContentType());
  }

  @Test
  public void testWithTypeHandler() {
    // Test if withTypeHandler sets the type handler properly and returns new instance
    Object typeHandler = new Object();
    CollectionType collectionType = new CollectionType(CollectionType.class);
    CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);

    assertNotSame(collectionType, newCollectionType);
    assertEquals(typeHandler, newCollectionType.getTypeHandler());
  }

  @Test
  public void testWithContentTypeHandler() {
    // Test if withContentTypeHandler sets the content type handler properly and returns new instance
    Object contentTypeHandler = new Object();
    CollectionType collectionType = new CollectionType(CollectionType.class);
    CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);

    assertNotSame(collectionType, newCollectionType);
    assertEquals(contentTypeHandler, newCollectionType.getContentTypeHandler());
  }

  @Test
  public void testWithValueHandler() {
    // Test if withValueHandler sets the value handler properly and returns new instance
    Object valueHandler = new Object();
    CollectionType collectionType = new CollectionType(CollectionType.class);
    CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);

    assertNotSame(collectionType, newCollectionType);
    assertEquals(valueHandler, newCollectionType.getValueHandler());
  }

  @Test
  public void testWithContentValueHandler() {
    // Test if withContentValueHandler sets the content value handler properly and returns new instance
    Object contentValueHandler = new Object();
    CollectionType collectionType = new CollectionType(CollectionType.class);
    CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);

    assertNotSame(collectionType, newCollectionType);
    assertEquals(contentValueHandler, newCollectionType.getContentValueHandler());
  }

  @Test
  public void testWithStaticTyping() {
    // Test if withStaticTyping sets the static typing flag properly and returns new instance
    CollectionType collectionType = new CollectionType(CollectionType.class);
    CollectionType newCollectionType = collectionType.withStaticTyping();

    assertNotSame(collectionType, newCollectionType);
    assertTrue(newCollectionType.isStatic());
  }

  @Test
  public void testRefine() {
    // Test if refine updates the collection type and returns new instance
    TypeBindings bindings = new TypeBindings();
    JavaType superClass = null;
    JavaType[] superInts = new JavaType[0];
    JavaType elemT = new JavaType();

    CollectionType collectionType = new CollectionType(CollectionType.class);
    CollectionType newCollectionType = collectionType.refine(CollectionType.class, bindings, superClass, superInts);

    assertNotSame(collectionType, newCollectionType);
    assertEquals(CollectionType.class, newCollectionType.getRawClass());
    assertEquals(bindings, newCollectionType.getBindings());
    assertEquals(superClass, newCollectionType.getSuperClass());
    assertArrayEquals(superInts, newCollectionType.getInterfaces());
    assertEquals(elemT, newCollectionType.getElementType());
  }

  @Test
  public void testToString() {
    CollectionType collectionType = new CollectionType(CollectionType.class);
    String expected = "[collection type; class com.fasterxml.jackson.databind.type.CollectionType, contains com.fasterxml.jackson.databind.type.JavaType]";
    assertEquals(expected, collectionType.toString());
  }
}

