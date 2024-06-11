package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testRefine() {
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(CollectionType.class, bindings, superClass, superInterfaces, elementType, valueHandler, typeHandler, asStatic);
        
        Class<?> rawType = CollectionType.class;
        CollectionType refinedType = (CollectionType) collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(rawType, refinedType.getClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces, refinedType.getSuperInterfaces());
        assertEquals(elementType, refinedType.getElementType());
        assertEquals(valueHandler, refinedType.getValueHandler());
        assertEquals(typeHandler, refinedType.getTypeHandler());
        assertEquals(asStatic, refinedType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withContentValueHandler(handler);
        
        assertEquals(collectionType.getClass(), newCollectionType.getClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newCollectionType.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), newCollectionType.getElementType());
        assertEquals(collectionType.getValueHandler(), newCollectionType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newCollectionType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newCollectionType.isStatic());
        assertEquals(handler, newCollectionType.getElementType().getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        
        assertEquals(collectionType.getClass(), newCollectionType.getClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newCollectionType.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), newCollectionType.getElementType());
        assertEquals(collectionType.getValueHandler(), newCollectionType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newCollectionType.getTypeHandler());
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        CollectionType newCollectionType = collectionType.withContentValueHandler(handler);
        
        assertEquals(collectionType.getClass(), newCollectionType.getClass());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newCollectionType.getSuperInterfaces());
        assertEquals(collectionType.getElementType(), newCollectionType.getElementType());
        assertEquals(collectionType.getValueHandler(), newCollectionType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newCollectionType.getTypeHandler());
        assertEquals(handler, newCollectionType.getElementType().getContentValueHandler());
    }
}