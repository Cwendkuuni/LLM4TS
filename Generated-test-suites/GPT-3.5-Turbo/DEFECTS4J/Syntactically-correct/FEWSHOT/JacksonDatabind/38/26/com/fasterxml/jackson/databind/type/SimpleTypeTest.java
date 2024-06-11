package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertEquals(0, type.getSuperInterfaces().length);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Map"));
        }
        try {
            SimpleType.construct(Collection.class);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Collection"));
        }
        try {
            SimpleType.construct(String[].class);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("array"));
        }
        
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertTrue(type.getSuperClass() instanceof TypeBase);
        assertEquals(0, type.getSuperInterfaces().length);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType narrowedType = (SimpleType) type._narrow(Object.class);
        assertEquals(Object.class, narrowedType.getRawClass());
        assertEquals(type.getBindings(), narrowedType.getBindings());
        assertTrue(narrowedType.getSuperClass() instanceof TypeBase);
        assertEquals(0, narrowedType.getSuperInterfaces().length);
        assertFalse(narrowedType.isContainerType());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentType(String.class);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("no content types"));
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("no content types"));
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newType = type.withValueHandler(handler);
        assertEquals(String.class, newType.getRawClass());
        assertEquals(handler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("no content types"));
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType newType = type.withStaticTyping();
        assertEquals(String.class, newType.getRawClass());
        assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = type.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        String canonicalName = type.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", result.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertTrue(type1.equals(type2));
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        String string = type.toString();
        assertEquals("[simple type, class java.lang.String]", string);
    }
}