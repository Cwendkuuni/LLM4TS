
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withTypeHandler(h);
        
        assertNotNull(result);
        assertEquals(h, result.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentTypeHandler(h);
        
        assertNotNull(result);
        assertEquals(h, result.getElementType().getTypeHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withStaticTyping();
        
        assertNotNull(result);
        assertTrue(result.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInterfaces, result.getInterfaces());
        assertEquals(elemT, result.getElementType());
    }
    
    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        String result = collectionType.toString();
        
        assertNotNull(result);
        assertTrue(result.contains(collectionType.getClass().getName()));
    }
}
