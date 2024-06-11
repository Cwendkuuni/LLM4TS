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
    }
    
    @Test
    public void testWithContentType() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        JavaType contentType = null;
        JavaType result = collectionType.withContentType(contentType);
        
        assertEquals(collectionType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        Object h = null;
        CollectionType result = collectionType.withTypeHandler(h);
        
        assertNotNull(result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        Object h = null;
        CollectionType result = collectionType.withContentTypeHandler(h);
        
        assertNotNull(result);
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        Object h = null;
        CollectionType result = collectionType.withValueHandler(h);
        
        assertNotNull(result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        Object h = null;
        CollectionType result = collectionType.withContentValueHandler(h);
        
        assertNotNull(result);
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        CollectionType result = collectionType.withStaticTyping();
        
        assertNotNull(result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        Class<?> rawType2 = null;
        TypeBindings bindings2 = null;
        JavaType superClass2 = null;
        JavaType[] superInterfaces = null;
        JavaType elemT2 = null;
        JavaType result = collectionType.refine(rawType2, bindings2, superClass2, superInterfaces);
        
        assertNotNull(result);
    }
    
    @Test
    public void testToString() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        String result = collectionType.toString();
        
        assertNotNull(result);
    }
}