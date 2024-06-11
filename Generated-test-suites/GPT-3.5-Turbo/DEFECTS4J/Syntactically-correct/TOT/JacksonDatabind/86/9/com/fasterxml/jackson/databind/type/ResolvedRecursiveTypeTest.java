package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());

        // error scenario
        try {
            resolvedRecursiveType.setReference(ref);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected exception
        }
    }

    @Test
    public void testGetGenericSignature() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testWithContentType() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
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
        // typical use case
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        StringBuilder sb = new StringBuilder(40).append("[recursive type; UNRESOLVED]");
        assertEquals(sb.toString(), resolvedRecursiveType.toString());

        // typical use case
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        sb = new StringBuilder(40).append("[recursive type; " + ref.getRawClass().getName() + "]");
        assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        resolvedRecursiveType1.setReference(ref);
        resolvedRecursiveType2.setReference(ref);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        // should fail for unresolved reference
        assertFalse(resolvedRecursiveType1.equals(new ResolvedRecursiveType(null, null)));

        // should fail for mismatched JavaType
        assertFalse(resolvedRecursiveType1.equals(new JavaType()));
    }

}