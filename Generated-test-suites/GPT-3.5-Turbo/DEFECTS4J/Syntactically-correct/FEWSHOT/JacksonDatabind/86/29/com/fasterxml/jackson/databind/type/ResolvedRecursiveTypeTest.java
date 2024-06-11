package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType javaType = new JavaType(null, null, null, null, null, null);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());

        try {
            resolvedRecursiveType.setReference(javaType);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + javaType + ", new = " + javaType, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType(null, null, null, null, null, null);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertSame(sb, result);
        assertEquals(result.toString(), resolvedRecursiveType.getSelfReferencedType().getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType(null, null, null, null, null, null);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertSame(sb, result);
        assertEquals(result.toString(), resolvedRecursiveType.getSelfReferencedType().getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(null, null, null, null, null, null);

        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        assertSame(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        assertSame(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        assertSame(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;

        assertSame(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        StringBuilder sb = new StringBuilder(40).append("[recursive type; UNRESOLVED");
        assertEquals(sb.toString(), resolvedRecursiveType.toString());

        JavaType javaType = new JavaType(null, null, null, null, null, null);
        resolvedRecursiveType.setReference(javaType);

        sb = new StringBuilder(40).append("[recursive type; ").append(javaType.getRawClass().getName());
        assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(null, null);

        JavaType javaType1 = new JavaType(null, null, null, null, null, null);
        JavaType javaType2 = new JavaType(null, null, null, null, null, null);
        resolvedRecursiveType1.setReference(javaType1);
        resolvedRecursiveType2.setReference(javaType2);
        resolvedRecursiveType3.setReference(javaType1);

        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}