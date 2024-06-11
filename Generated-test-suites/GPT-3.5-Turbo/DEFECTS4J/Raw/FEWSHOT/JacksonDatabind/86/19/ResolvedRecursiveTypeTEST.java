package com.fasterxml.jackson.databind.type;

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
        ResolvedRecursiveType ref = new ResolvedRecursiveType(null, null);

        assertNull(resolvedRecursiveType.getSelfReferencedType());

        resolvedRecursiveType.setReference(ref);

        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());

        try {
            resolvedRecursiveType.setReference(ref);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException ex) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, ex.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder(40);
        JavaType ref = new JavaType(null, null, null);

        resolvedRecursiveType.setReference(ref);

        assertEquals(ref.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder(40);
        JavaType ref = new JavaType(null, null, null);

        resolvedRecursiveType.setReference(ref);

        assertEquals(ref.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(null, null, null);

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;

        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(40);
        JavaType ref = new JavaType(null, null, null);

        resolvedRecursiveType.setReference(ref);

        assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", resolvedRecursiveType.toString());

        resolvedRecursiveType.setReference(null);

        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());

    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType sameType = new ResolvedRecursiveType(null, null);

        JavaType ref = new JavaType(null, null, null);

        resolvedRecursiveType.setReference(ref);
        otherType.setReference(ref);
        sameType.setReference(ref);

        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));

        assertFalse(resolvedRecursiveType.equals(null));

        assertFalse(resolvedRecursiveType.equals(new Object()));

        assertFalse(resolvedRecursiveType.equals(otherType));

        assertTrue(resolvedRecursiveType.equals(sameType));

    }
}