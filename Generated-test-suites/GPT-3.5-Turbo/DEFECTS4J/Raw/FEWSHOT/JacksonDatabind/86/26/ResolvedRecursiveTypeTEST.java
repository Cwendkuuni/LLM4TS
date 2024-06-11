package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertEquals(bindings, resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getContentType());
        assertNull(resolvedRecursiveType.getKeyType());
        assertEquals(0, resolvedRecursiveType.getContentInclusion());
        assertNull(resolvedRecursiveType.getValueHandler());
        assertNull(resolvedRecursiveType.getKeyHandler());
        assertEquals(false, resolvedRecursiveType.hasHandlers());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        
        assertTrue(sb.length() > 0);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        
        assertTrue(sb.length() > 0);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(null, result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        boolean result = resolvedRecursiveType.isContainerType();
        
        assertEquals(false, result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        String result = resolvedRecursiveType.toString();
        
        assertTrue(result.contains("recursive type"));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType1.setReference(ref);
        resolvedRecursiveType2.setReference(ref);
        
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        assertEquals(true, result);
    }
}