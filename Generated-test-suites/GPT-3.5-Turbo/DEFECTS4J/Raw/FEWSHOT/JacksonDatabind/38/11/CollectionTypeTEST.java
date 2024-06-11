package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        // Test construct method
        // Create a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Verify the result
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testConstructInvalidRawType() {
        // Test construct method with invalid rawType
        // Create an invalid rawType and an elemT
        Class<?> rawType = null;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        // Execute the method
        try {
            com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verify the exception message
            assertEquals("Raw type for CollectionType cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        // Test withContentType method
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Create a contentType
        JavaType contentType = com.fasterxml.jackson.databind.JavaType.emptyType();
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withContentType(contentType);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithContentTypeSameContent() {
        // Test withContentType method with the same contentType
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Create a contentType same as the elemT
        JavaType contentType = elemT;
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withContentType(contentType);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(elemT, result.getContentType());
    }

    @Test
    public void testWithContentTypeNull() {
        // Test withContentType method with null contentType
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Set contentType to null
        JavaType contentType = null;
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withContentType(contentType);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertNull(result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        // Test withTypeHandler method
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Create a typeHandler
        Object typeHandler = new Object();
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withTypeHandler(typeHandler);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(elemT, result.getContentType());
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test withContentTypeHandler method
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Create a contentTypeHandler
        Object contentTypeHandler = new Object();
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withContentTypeHandler(contentTypeHandler);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(elemT.withTypeHandler(contentTypeHandler), result.getContentType());
        assertEquals(contentTypeHandler, result.getContentType().getTypeHandler());
        assertNull(result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerNull() {
        // Test withContentTypeHandler method with null contentTypeHandler
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Set contentTypeHandler to null
        Object contentTypeHandler = null;
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withContentTypeHandler(contentTypeHandler);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(elemT, result.getContentType());
        assertNull(result.getContentType().getTypeHandler());
        assertNull(result.getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test withContentValueHandler method
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Create a contentValueHandler
        Object contentValueHandler = new Object();
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withContentValueHandler(contentValueHandler);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(elemT.withValueHandler(contentValueHandler), result.getContentType());
        assertEquals(contentValueHandler, result.getContentType().getValueHandler());
        assertNull(result.getTypeHandler());
    }

    @Test
    public void testWithContentValueHandlerNull() {
        // Test withContentValueHandler method with null contentValueHandler
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        JavaType elemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType.construct(rawType, elemT);
        // Set contentValueHandler to null
        Object contentValueHandler = null;
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withContentValueHandler(contentValueHandler);
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(elemT, result.getContentType());
        assertNull(result.getContentType().getValueHandler());
        assertNull(result.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Test withStaticTyping method
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType
                .construct(rawType, com.fasterxml.jackson.databind.type.TypeBindings.emptyBindings(), null, new com.fasterxml.jackson.databind.JavaType[] {}, com.fasterxml.jackson.databind.JavaType.emptyType());
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = collectionType.withStaticTyping();
        // Verify the result
        assertEquals(rawType, result.getRawClass());
        assertEquals(true, result.isStatic());
    }

    @Test
    public void testRefine() {
        // Test refine method
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType
                .construct(rawType, com.fasterxml.jackson.databind.type.TypeBindings.emptyBindings(), null, new com.fasterxml.jackson.databind.JavaType[] {}, com.fasterxml.jackson.databind.JavaType.emptyType());
        // Create new parameters
        Class<?> newRawType = java.util.LinkedHashSet.class;
        com.fasterxml.jackson.databind.type.TypeBindings newBindings = new com.fasterxml.jackson.databind.type.TypeBindings();
        com.fasterxml.jackson.databind.JavaType newSuperClass = null;
        com.fasterxml.jackson.databind.JavaType[] newSuperInterfaces = new com.fasterxml.jackson.databind.JavaType[] {};
        com.fasterxml.jackson.databind.JavaType newElemT = com.fasterxml.jackson.databind.JavaType.emptyType();
        // Execute the method
        com.fasterxml.jackson.databind.type.CollectionType result = (com.fasterxml.jackson.databind.type.CollectionType) collectionType
                .refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
        // Verify the result
        assertEquals(newRawType, result.getRawClass());
        assertEquals(newElemT, result.getContentType());
    }

    @Test
    public void testToString() {
        // Test toString method
        // Create a collectionType with a rawType and an elemT
        Class<?> rawType = java.util.ArrayList.class;
        com.fasterxml.jackson.databind.type.CollectionType collectionType = com.fasterxml.jackson.databind.type.CollectionType
                .construct(rawType, com.fasterxml.jackson.databind.type.TypeBindings.emptyBindings(), null, new com.fasterxml.jackson.databind.JavaType[] {}, com.fasterxml.jackson.databind.JavaType.emptyType());
        // Execute the method
        String result = collectionType.toString();
        // Verify the result
        assertEquals("[collection type; class " + rawType.getName() + ", contains " + com.fasterxml.jackson.databind.JavaType.emptyType() + "]", result);
    }
}