package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructors() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertEquals(String.class, rrt.getRawClass());
        assertNull(rrt.getBindings());
        assertNull(rrt.getKeyType());
        assertNull(rrt.getContentType());
        assertEquals(0, rrt.getValueHandler());
        assertEquals(0, rrt.getTypeHandler());

        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(Integer.class, null);
        assertEquals(Integer.class, rrt2.getRawClass());
        assertNull(rrt2.getBindings());
        assertNull(rrt2.getKeyType());
        assertNull(rrt2.getContentType());
        assertEquals(0, rrt2.getValueHandler());
        assertEquals(0, rrt2.getTypeHandler());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertNull(rrt.getSelfReferencedType());

        JavaType ref = JavaType.from(Class.class);
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType ref = JavaType.from(Class.class);
        rrt.setReference(ref);
        rrt.setReference(ref);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt.withContentType(null);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt.withTypeHandler(null);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt.withContentTypeHandler(null);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt.withValueHandler(null);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt.withContentValueHandler(null);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt.withStaticTyping();
        assertEquals(rrt, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt._narrow(String.class);
        assertEquals(rrt, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        JavaType result = rrt.refine(String.class, null, null, null);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        assertFalse(rrt.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        String result = rrt.toString();
        assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(String.class, null);
        assertTrue(rrt.equals(rrt2));

        JavaType ref = JavaType.from(Class.class);
        rrt.setReference(ref);
        assertFalse(rrt.equals(rrt2));

        rrt2.setReference(ref);
        assertTrue(rrt.equals(rrt2));
    }
}