package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

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
        
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }
    
    @Test
    public void testWithContentType() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        JavaType contentType = null;
        
        JavaType newCollectionType = collectionType.withContentType(contentType);
        
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(contentType, newCollectionType.getElementType());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object h = null;
        
        CollectionType newCollectionType = collectionType.withTypeHandler(h);
        
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(elemT, newCollectionType.getElementType());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertEquals(h, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStatic());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object h = null;
        
        CollectionType newCollectionType = collectionType.withContentTypeHandler(h);
        
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(elemT.withTypeHandler(h), newCollectionType.getElementType());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object h = null;
        
        CollectionType newCollectionType = collectionType.withContentValueHandler(h);
        
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(elemT.withValueHandler(h), newCollectionType.getElementType());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertEquals(asStatic, newCollectionType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        CollectionType newCollectionType = collectionType.withStaticTyping();
        
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(elemT.withStaticTyping(), newCollectionType.getElementType());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertTrue(newCollectionType.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Class<?> newRawType = null;
        TypeBindings newBindings = null;
        JavaType newSuperClass = null;
        JavaType[] newSuperInts = null;
        
        JavaType refinedType = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInts);
        
        assertEquals(newRawType, refinedType.getRawClass());
        assertEquals(newBindings, refinedType.getBindings());
        assertEquals(newSuperClass, refinedType.getSuperClass());
        assertEquals(newSuperInts, refinedType.getInterfaces());
        assertEquals(elemT, refinedType.getElementType());
        assertEquals(valueHandler, refinedType.getValueHandler());
        assertEquals(typeHandler, refinedType.getTypeHandler());
        assertEquals(asStatic, refinedType.isStatic());
    }
    
    @Test
    public void testToString() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        String expectedString = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        
        assertEquals(expectedString, collectionType.toString());
    }
}