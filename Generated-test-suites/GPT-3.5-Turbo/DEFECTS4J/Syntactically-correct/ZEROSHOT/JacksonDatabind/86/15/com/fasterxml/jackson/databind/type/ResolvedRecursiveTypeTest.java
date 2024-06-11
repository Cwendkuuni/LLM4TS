package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Exercise
        resolvedRecursiveType.setReference(null);

        // Verify
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();

        // Exercise
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        // Verify
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();

        // Exercise
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        // Verify
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = null;

        // Exercise
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        // Verify
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = null;

        // Exercise
        JavaType result = resolvedRecursiveType.withTypeHandler(h);

        // Verify
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = null;

        // Exercise
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);

        // Verify
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = null;

        // Exercise
        JavaType result = resolvedRecursiveType.withValueHandler(h);

        // Verify
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = null;

        // Exercise
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);

        // Verify
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Exercise
        JavaType result = resolvedRecursiveType.withStaticTyping();

        // Verify
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = null;

        // Exercise
        JavaType result = resolvedRecursiveType._narrow(subclass);

        // Verify
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        // Exercise
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        // Verify
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Exercise
        boolean result = resolvedRecursiveType.isContainerType();

        // Verify
        assertFalse(result);
    }

    @Test
    public void testToString() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();

        // Exercise
        String result = resolvedRecursiveType.toString();

        // Verify
        assertTrue(result.contains("UNRESOLVED"));
    }

    @Test
    public void testEquals() {
        // Setup
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object o = new ResolvedRecursiveType(null, null);

        // Exercise
        boolean result = resolvedRecursiveType.equals(o);

        // Verify
        assertFalse(result);
    }
}