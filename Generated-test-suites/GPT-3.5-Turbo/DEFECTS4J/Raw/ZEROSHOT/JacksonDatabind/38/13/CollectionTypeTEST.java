package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> rawType = Object.class;
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
        assertEquals(elemT, collectionType.getContentType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        
        CollectionType collectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), newCollectionType.getInterfaces());
        assertEquals(contentType, newCollectionType.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withTypeHandler(h);
        
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), newCollectionType.getInterfaces());
        assertEquals(collectionType.getContentType(), newCollectionType.getContentType());
        assertEquals(h, newCollectionType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(h);
        
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), newCollectionType.getInterfaces());
        assertEquals(collectionType.getContentType().withTypeHandler(h), newCollectionType.getContentType());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withValueHandler(h);
        
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), newCollectionType.getInterfaces());
        assertEquals(collectionType.getContentType(), newCollectionType.getContentType());
        assertEquals(h, newCollectionType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withContentValueHandler(h);
        
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), newCollectionType.getInterfaces());
        assertEquals(collectionType.getContentType().withValueHandler(h), newCollectionType.getContentType());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        
        assertNotNull(newCollectionType);
        assertEquals(collectionType.getRawClass(), newCollectionType.getRawClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), newCollectionType.getInterfaces());
        assertEquals(collectionType.getContentType().withStaticTyping(), newCollectionType.getContentType());
        assertTrue(newCollectionType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        
        CollectionType collectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = (CollectionType) collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(newCollectionType);
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInterfaces, newCollectionType.getInterfaces());
        assertEquals(elementType, newCollectionType.getContentType());
    }
    
    @Test
    public void testToString() {
        Class<?> clazz = Object.class;
        
        CollectionType collectionType = new CollectionType(clazz, null, null, null, null, null, null, false);
        String expected = "[collection type; class " + clazz.getName() + ", contains null]";
        assertEquals(expected, collectionType.toString());
    }
}