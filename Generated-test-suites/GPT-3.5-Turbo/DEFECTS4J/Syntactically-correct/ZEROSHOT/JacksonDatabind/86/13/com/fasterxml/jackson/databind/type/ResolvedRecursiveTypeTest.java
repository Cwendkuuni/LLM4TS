package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        assertNull(rrt.getSelfReferencedType());

        JavaType ref = new JavaType();
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());

        try {
            rrt.setReference(ref);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = "+ref+", new = "+ref, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getGenericSignature(sb);

        assertSame(sb, result);
        // TODO: Add assertion for the values returned by rrt.getGenericSignature()
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getErasedSignature(sb);

        assertSame(sb, result);
        // TODO: Add assertion for the values returned by rrt.getErasedSignature()
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType result = rrt.withContentType(null);

        assertSame(rrt, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType result = rrt.withTypeHandler(null);

        assertSame(rrt, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType result = rrt.withContentTypeHandler(null);

        assertSame(rrt, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType result = rrt.withValueHandler(null);

        assertSame(rrt, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType result = rrt.withContentValueHandler(null);

        assertSame(rrt, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType result = rrt.withStaticTyping();

        assertSame(rrt, result);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType subclass = new JavaType();
        JavaType result = rrt._narrow(subclass);

        assertSame(rrt, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = rrt.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        boolean result = rrt.isContainerType();

        assertFalse(result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        String result = rrt.toString();

        assertTrue(result.startsWith("[recursive type; "));
        // TODO: Add assertion for the specific contents of the string
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(null, null);
        rrt1.setReference(new JavaType());
        rrt2.setReference(new JavaType());

        assertFalse(rrt1.equals(null));
        assertFalse(rrt1.equals(new Object()));
        assertFalse(rrt1.equals(rrt2));

        rrt2.setReference(rrt1.getSelfReferencedType());
        assertTrue(rrt1.equals(rrt2));
    }
}