package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = new Class<>();
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = new Class<>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        boolean result = resolvedRecursiveType.isContainerType();
        assertFalse(result);
    }

    @Test
    public void testToString() {
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(other));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(null, null)));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(new Class<>(), null)));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(null, new TypeBindings())));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(new Class<>(), new TypeBindings())));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(null, new TypeBindings().addBinding("key", new JavaType()))));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(new Class<>(), new TypeBindings().addBinding("key", new JavaType()))));
        resolvedRecursiveType.setReference(new JavaType());
        other.setReference(new JavaType());
        assertTrue(resolvedRecursiveType.equals(other));
    }
}