package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    // Test constructor
    @Test
    public void testConstruct() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    // Test constructor with bindings
    @Test
    public void testConstructWithBindings() {
        JavaType elemType = new SimpleType(String.class);
        TypeBindings bindings = new TypeBindings();
        bindings.put("elementType", elemType);
        JavaType collectionType = CollectionType.construct(List.class, bindings, null, null, elemType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(elemType, collectionType.getBindings().get("elementType"));
    }

    // Test constructor with bindings and refined type
    @Test
    public void testConstructWithRefinedType() {
        JavaType elemType = new SimpleType(String.class);
        JavaType refinedType = new SimpleType(ArrayList.class);
        refinedType = CollectionType.construct(refinedType.getRawClass(), null, null, null, elemType);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(elemType, collectionType.getContentType().getContentType());
        assertEquals(refinedType, collectionType.getContentType());
    }

    // Test deprecated constructor
    @Test
    public void testConstructDeprecated() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, elemType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType subclass = ArrayList.class;
        JavaType narrowedType = collectionType._narrow(subclass);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(elemType, narrowedType.getContentType());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    // Test withContentType
    @Test
    public void testWithContentType() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType newContentType = new SimpleType(Integer.class);
        JavaType newCollectionType = collectionType.withContentType(newContentType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withTypeHandler
    @Test
    public void testWithTypeHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType newCollectionType = collectionType.withTypeHandler(handler);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getTypeHandler());
        assertNull(newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withContentTypeHandler
    @Test
    public void testWithContentTypeHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType newContentType = new SimpleType(Integer.class);
        JavaType newCollectionType = collectionType.withContentTypeHandler(handler);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getTypeHandler());
        assertEquals(elemType, newCollectionType.getContentType().getContentType());
        assertNull(newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withValueHandler
    @Test
    public void testWithValueHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType newCollectionType = collectionType.withValueHandler(handler);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withContentValueHandler
    @Test
    public void testWithContentValueHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType newContentType = new SimpleType(Integer.class);
        JavaType newCollectionType = collectionType.withContentValueHandler(handler);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getValueHandler());
        assertEquals(elemType, newCollectionType.getContentType().getContentType());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    // Test withStaticTyping
    @Test
    public void testWithStaticTyping() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        JavaType newCollectionType = collectionType.withStaticTyping();
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertTrue(newCollectionType.isStatic());
    }

    // Test refine
    @Test
    public void testRefine() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        bindings.put("superInterfaces", new Class[]{Collection.class});
        JavaType refinedType = collectionType.refine(rawType, bindings, null, null);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(elemType, refinedType.getContentType());
        assertArrayEquals(new Class[]{Collection.class}, refinedType.getSuperInterfaces());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }
}