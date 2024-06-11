package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertEquals(String.class, collectionType.getRawClass());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isContainerType());
        assertTrue(collectionType.isCollectionLikeType());
        assertNull(collectionType.getContentValueHandler());
        assertNull(collectionType.getContentTypeHandler());
        assertFalse(collectionType.isPrimitive());
        assertFalse(collectionType.isFinal());
        assertTrue(collectionType.isAbstract());
        assertFalse(collectionType.isConcrete());
        assertFalse(collectionType.hasGenericTypes());
        assertFalse(collectionType.hasHandlers());
        assertTrue(collectionType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType = new CollectionType(String.class);
        JavaType contentType = new CollectionType(Integer.class);
        
        CollectionType result = collectionType.withContentType(contentType);
        
        assertEquals(contentType, result.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = new CollectionType(String.class);
        Object typeHandler = new Object();
        
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = new CollectionType(String.class);
        Object typeHandler = new Object();
        
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        
        assertEquals(typeHandler, result.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        CollectionType collectionType = new CollectionType(String.class);
        Object valueHandler = new Object();
        
        CollectionType result = collectionType.withValueHandler(valueHandler);
        
        assertEquals(valueHandler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = new CollectionType(String.class);
        Object valueHandler = new Object();
        
        CollectionType result = collectionType.withContentValueHandler(valueHandler);
        
        assertEquals(valueHandler, result.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(String.class);
        
        CollectionType result = collectionType.withStaticTyping();
        
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = new CollectionType(String.class);
        Class<?> rawType = Integer.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        JavaType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(rawType, result.getRawClass());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(String.class);
        
        String result = collectionType.toString();
        
        assertEquals("[collection type; class java.lang.String, contains null]", result);
    }
}