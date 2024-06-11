package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        JavaType elemT = new JavaTypeMock();
        CollectionType collectionType = CollectionType.construct(Object.class, null, null, null, elemT);
        assertEquals(Object.class, collectionType.getRawClass());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaTypeMock();
        CollectionType collectionType = new CollectionTypeMock();
        CollectionType result = collectionType.withContentType(contentType);
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionTypeMock();
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionTypeMock();
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, result.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionTypeMock();
        CollectionType result = collectionType.withValueHandler(valueHandler);
        assertEquals(valueHandler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionTypeMock();
        CollectionType result = collectionType.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, result.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionTypeMock();
        CollectionType result = collectionType.withStaticTyping();
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = new JavaTypeMock();

        CollectionType collectionType = new CollectionTypeMock();
        CollectionType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, result.getRawClass());
        assertEquals(elemT, result.getContentType());
    }
}