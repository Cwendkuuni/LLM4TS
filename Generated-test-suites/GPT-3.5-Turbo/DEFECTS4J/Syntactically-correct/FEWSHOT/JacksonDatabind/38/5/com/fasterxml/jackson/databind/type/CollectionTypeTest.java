package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[2];
        JavaType elemT = new JavaType();
        
        CollectionType type = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInts, type.getSuperInterfaces());
        assertEquals(elemT, type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }

    @Test
    public void testCollectionTypeWithContentType() {
        JavaType contentType = new JavaType();
        CollectionType original = new CollectionType();
        CollectionType modified = original.withContentType(contentType);
        
        assertEquals(original.getRawClass(), modified.getRawClass());
        assertEquals(original.getBindings(), modified.getBindings());
        assertEquals(original.getSuperClass(), modified.getSuperClass());
        assertEquals(original.getSuperInterfaces(), modified.getSuperInterfaces());
        assertEquals(contentType, modified.getContentType());
        assertEquals(original.getValueHandler(), modified.getValueHandler());
        assertEquals(original.getTypeHandler(), modified.getTypeHandler());
        assertEquals(original.isStatic(), modified.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType original = new CollectionType();
        CollectionType modified = original.withStaticTyping();
        
        assertEquals(original.getRawClass(), modified.getRawClass());
        assertEquals(original.getBindings(), modified.getBindings());
        assertEquals(original.getSuperClass(), modified.getSuperClass());
        assertEquals(original.getSuperInterfaces(), modified.getSuperInterfaces());
        assertEquals(original.getContentType(), modified.getContentType());
        assertEquals(original.getValueHandler(), modified.getValueHandler());
        assertEquals(original.getTypeHandler(), modified.getTypeHandler());
        assertTrue(modified.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[2];
        JavaType elemT = new JavaType();
        
        CollectionType original = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType[] superInterfaces = new JavaType[2];
        CollectionType modified = original.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(rawType, modified.getRawClass());
        assertEquals(bindings, modified.getBindings());
        assertEquals(superClass, modified.getSuperClass());
        assertEquals(superInterfaces, modified.getSuperInterfaces());
        assertEquals(elemT, modified.getContentType());
        assertNull(modified.getValueHandler());
        assertNull(modified.getTypeHandler());
        assertFalse(modified.isStatic());
    }

    @Test
    public void testToString() {
        CollectionType type = new CollectionType();
        String expected = "[collection type; class "+ type.getRawClass().getName() +", contains "+ type.getContentType() +"]";
        assertEquals(expected, type.toString());
    }
}