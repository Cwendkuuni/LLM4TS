package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());

        // Error scenario - trying to set self reference multiple times
        try {
            resolvedRecursiveType.setReference(javaType);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Success
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);

        // Typical case
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
        assertEquals("", result.toString());

        // Null reference case
        resolvedRecursiveType.setReference(null);
        result = resolvedRecursiveType.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
        assertEquals("", result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);

        // Typical case
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
        assertEquals("", result.toString());

        // Null reference case
        resolvedRecursiveType.setReference(null);
        result = resolvedRecursiveType.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
        assertEquals("", result.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertNotNull(result);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Typical case
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        String result = resolvedRecursiveType.toString();
        assertNotNull(result);
        assertEquals("[recursive type; " + javaType.getRawClass().getName() + "]", result);

        // Unresolved reference case
        resolvedRecursiveType.setReference(null);
        result = resolvedRecursiveType.toString();
        assertNotNull(result);
        assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType1.setReference(javaType);
        resolvedRecursiveType2.setReference(javaType);

        // Typical case - same classes and same self-referenced types
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        // Different classes
        assertFalse(resolvedRecursiveType1.equals(javaType));

        // Different self-referenced types
        resolvedRecursiveType2.setReference(new JavaType());
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        // Unresolved self-referenced types
        resolvedRecursiveType1.setReference(null);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}