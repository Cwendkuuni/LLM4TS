package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());

        // Test error when trying to re-set reference
        try {
            type.setReference(ref);
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, e.getMessage());
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        JavaType newType = type.withContentType(new SimpleType(int.class));
        assertSame(type, newType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        Object handler = new Object();
        JavaType newType = type.withTypeHandler(handler);
        assertSame(type, newType);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        Object handler = new Object();
        JavaType newType = type.withContentTypeHandler(handler);
        assertSame(type, newType);
    }

    @Test
    public void testWithValueHandler() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        Object handler = new Object();
        JavaType newType = type.withValueHandler(handler);
        assertSame(type, newType);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        Object handler = new Object();
        JavaType newType = type.withContentValueHandler(handler);
        assertSame(type, newType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        JavaType newType = type.withStaticTyping();
        assertSame(type, newType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);
        type.narrow(Integer.TYPE);
    }

    @Test
    public void testRefine() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        JavaType result = type.refine(String.class, null, null, null);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(ref);

        assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", type.toString());
    }

    @Test
    public void testEquals() {
        JavaType ref1 = new SimpleType(String.class);
        JavaType ref2 = new SimpleType(String.class);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        type1.setReference(ref1);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));

        type2.setReference(ref1);
        assertTrue(type1.equals(type2));
    }
}