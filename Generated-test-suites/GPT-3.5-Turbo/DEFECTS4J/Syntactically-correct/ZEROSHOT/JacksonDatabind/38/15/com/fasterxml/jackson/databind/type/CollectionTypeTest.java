package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        // Set up a CollectionType instance for testing
        collectionType = new CollectionType(Object.class, null, null, null, new JavaType(), null, null, false);
    }
    
    @Test
    public void testConstruct() {
        // Test the construct method
        CollectionType constructedType = CollectionType.construct(Object.class, null, null, null, new JavaType());
        assertEquals(Object.class, constructedType.getClass());
        assertNull(constructedType.getBindings());
        assertNull(constructedType.getSuperClass());
        assertNull(constructedType.getSuperInterfaces());
        assertEquals(Object.class, constructedType.getElementType());
        assertNull(constructedType.getValueHandler());
        assertNull(constructedType.getTypeHandler());
        assertFalse(constructedType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        // Test the withContentType method
        JavaType contentType = new JavaType();
        CollectionType result = collectionType.withContentType(contentType);
        assertEquals(collectionType.getClass(), result.getClass());
        assertEquals(contentType, result.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        // Test the withTypeHandler method
        Object typeHandler = new Object();
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        assertEquals(collectionType.getClass(), result.getClass());
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test the withContentTypeHandler method
        Object typeHandler = new Object();
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        assertEquals(collectionType.getClass(), result.getClass());
        assertEquals(typeHandler, result.getElementType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Test the withValueHandler method
        Object valueHandler = new Object();
        CollectionType result = collectionType.withValueHandler(valueHandler);
        assertEquals(collectionType.getClass(), result.getClass());
        assertEquals(valueHandler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test the withContentValueHandler method
        Object valueHandler = new Object();
        CollectionType result = collectionType.withContentValueHandler(valueHandler);
        assertEquals(collectionType.getClass(), result.getClass());
        assertEquals(valueHandler, result.getElementType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Test the withStaticTyping method
        CollectionType result = collectionType.withStaticTyping();
        assertEquals(collectionType.getClass(), result.getClass());
        assertTrue(result.isAsStatic());
    }

    @Test
    public void testRefine() {
        // Test the refine method
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new JavaType();
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, result.getClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertEquals(superInterfaces, result.getSuperInterfaces());
        assertEquals(elementType, result.getElementType());
        assertEquals(valueHandler, result.getValueHandler());
        assertEquals(typeHandler, result.getTypeHandler());
        assertEquals(asStatic, result.isAsStatic());
    }

    @Test
    public void testToString() {
        // Test the toString method
        String expected = "[collection type; class java.lang.Object, contains null]";
        assertEquals(expected, collectionType.toString());
    }
}