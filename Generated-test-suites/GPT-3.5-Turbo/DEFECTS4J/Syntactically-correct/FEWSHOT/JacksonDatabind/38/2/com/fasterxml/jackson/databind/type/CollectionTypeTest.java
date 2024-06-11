package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {
   
    @Test
    public void testConstruct() {
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getInterfaces());
        assertNull(collectionType.getElementType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withContentType(contentType);
        
        assertNotNull(updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(contentType, updatedType.getElementType());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withTypeHandler(h);
        
        assertNotNull(updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType(), updatedType.getElementType());
        assertEquals(h, updatedType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withContentTypeHandler(h);
        
        assertNotNull(updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withTypeHandler(h), updatedType.getElementType());
        assertEquals(collectionType.getTypeHandler(), updatedType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withValueHandler(h);
        
        assertNotNull(updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType(), updatedType.getElementType());
        assertEquals(h, updatedType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withContentValueHandler(h);
        
        assertNotNull(updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withValueHandler(h), updatedType.getElementType());
        assertEquals(collectionType.getValueHandler(), updatedType.getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withStaticTyping();
        
        assertNotNull(updatedType);
        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withStaticTyping(), updatedType.getElementType());
        assertEquals(true, updatedType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;
        
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(updatedType);
        assertEquals(rawType, updatedType.getRawClass());
        assertEquals(bindings, updatedType.getBindings());
        assertEquals(superClass, updatedType.getSuperClass());
        assertEquals(superInterfaces, updatedType.getInterfaces());
        assertEquals(collectionType.getElementType(), updatedType.getElementType());
    }
    
    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(List.class, null, null, null, null, null, null, false);
        
        assertEquals("[collection type; class java.util.List, contains null]", collectionType.toString());
    }
    
}