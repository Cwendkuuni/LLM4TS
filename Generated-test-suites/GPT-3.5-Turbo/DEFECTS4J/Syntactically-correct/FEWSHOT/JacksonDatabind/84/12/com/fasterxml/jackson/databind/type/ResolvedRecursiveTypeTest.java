package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(ref);

        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());

        try {
            resolvedRecursiveType.setReference(ref);
        } catch (IllegalStateException ex) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, ex.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(ref);

        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(ref);

        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new ResolvedRecursiveType(Object.class, null);
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
    public void test_narrow() {
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(Object.class, null));
        String result = resolvedRecursiveType.toString();

        assertTrue(result.contains("recursive type"));
    }

    @Test
    public void testEquals() {
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(Object.class, null));
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, null);
        other.setReference(resolvedRecursiveType.getSelfReferencedType());

        assertTrue(resolvedRecursiveType.equals(other));
    }
}