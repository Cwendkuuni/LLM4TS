package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }
    
    @Test
    public void testWithContentType() {
        CollectionType collectionType = null;
        JavaType contentType = null;
        CollectionType updatedType = collectionType.withContentType(contentType);
        assertNotNull(updatedType);
        assertNotEquals(collectionType, updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(contentType, updatedType.getElementType());
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = null;
        Object handler = null;
        CollectionType updatedType = collectionType.withTypeHandler(handler);
        assertNotNull(updatedType);
        assertNotEquals(collectionType, updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType(), updatedType.getElementType());
        assertEquals(handler, updatedType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = null;
        Object handler = null;
        CollectionType updatedType = collectionType.withContentTypeHandler(handler);
        assertNotNull(updatedType);
        assertNotEquals(collectionType, updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withTypeHandler(handler), updatedType.getElementType());
        assertEquals(collectionType.getTypeHandler(), updatedType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        CollectionType collectionType = null;
        Object handler = null;
        CollectionType updatedType = collectionType.withValueHandler(handler);
        assertNotNull(updatedType);
        assertNotEquals(collectionType, updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType(), updatedType.getElementType());
        assertEquals(handler, updatedType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = null;
        Object handler = null;
        CollectionType updatedType = collectionType.withContentValueHandler(handler);
        assertNotNull(updatedType);
        assertNotEquals(collectionType, updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withValueHandler(handler), updatedType.getElementType());
        assertEquals(collectionType.getValueHandler(), updatedType.getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = null;
        CollectionType updatedType = collectionType.withStaticTyping();
        assertNotNull(updatedType);
        assertNotEquals(collectionType, updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withStaticTyping(), updatedType.getElementType());
        assertTrue(updatedType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        CollectionType collectionType = null;
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType refinedType = collectionType.refine(rawType, bindings, superClass, superInts);
        assertNotNull(refinedType);
        assertNotEquals(collectionType, refinedType);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInts, refinedType.getInterfaces());
        assertEquals(elemT, refinedType.getElementType());
    }
    
    @Test
    public void testToString() {
        CollectionType collectionType = null;
        String expected = "[collection type; class "+collectionType.getRawClass().getName()+", contains "+collectionType.getElementType()+"]";
        assertEquals(expected, collectionType.toString());
    }
}