package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
  
    private ResolvedRecursiveType resolvedRecursiveType;
  
    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test
    public void testSetReference() {
        // Test setting reference with non-null value
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());

        // Test setting reference with null value
        resolvedRecursiveType.setReference(null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        assertSame(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        assertSame(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(h);

        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);

        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(h);

        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);

        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();

        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        JavaType result = resolvedRecursiveType._narrow(subclass);

        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

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
        String result = resolvedRecursiveType.toString();

        assertTrue(result.contains("recursive type"));
        assertTrue(result.contains("UNRESOLVED"));
    }

    @Test
    public void testEquals() {
        Object o = new ResolvedRecursiveType(null, null);

        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(null, null)));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(new JavaType(), null)));
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(null, new TypeBindings())));
        assertTrue(resolvedRecursiveType.equals(o));
    }
}