package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isContainerType());
        assertFalse(collectionType.isArrayType());
        assertFalse(collectionType.isCollectionLikeType());
        assertFalse(collectionType.isMapLikeType());
        assertFalse(collectionType.isReferenceType());
        assertFalse(collectionType.isTypeOrSubTypeOf(Object.class));
        assertTrue(collectionType.hasHandlers());
        assertFalse(collectionType.isInterface());
        assertFalse(collectionType.hasRawClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        CollectionType collectionType = new CollectionType();

        JavaType newType = collectionType.withContentType(contentType);

        assertEquals(contentType, newType.getContentType());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = null;

        CollectionType collectionType = new CollectionType();

        CollectionType newType = collectionType.withTypeHandler(handler);

        assertEquals(handler, newType.getTypeHandler());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = null;

        CollectionType collectionType = new CollectionType();

        CollectionType newType = collectionType.withContentTypeHandler(handler);

        assertEquals(handler, newType.getContentType().getTypeHandler());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = null;

        CollectionType collectionType = new CollectionType();

        CollectionType newType = collectionType.withValueHandler(handler);

        assertEquals(handler, newType.getValueHandler());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = null;

        CollectionType collectionType = new CollectionType();

        CollectionType newType = collectionType.withContentValueHandler(handler);

        assertEquals(handler, newType.getContentType().getValueHandler());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();

        CollectionType newType = collectionType.withStaticTyping();

        assertTrue(newType.isStaticTyping());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;

        CollectionType collectionType = new CollectionType();

        CollectionType newType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(elemT, newType.getContentType());
        assertNull(newType.getValueHandler());
        assertNull(newType.getTypeHandler());
        assertFalse(newType.isContainerType());
        assertFalse(newType.isArrayType());
        assertFalse(newType.isCollectionLikeType());
        assertFalse(newType.isMapLikeType());
        assertFalse(newType.isReferenceType());
        assertFalse(newType.isTypeOrSubTypeOf(Object.class));
        assertTrue(newType.hasHandlers());
        assertFalse(newType.isInterface());
        assertFalse(newType.hasRawClass());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType();

        String toString = collectionType.toString();

        assertTrue(toString.contains(collectionType.getRawClass().getName()));
        assertTrue(toString.contains(collectionType.getContentType().toString()));
    }
}