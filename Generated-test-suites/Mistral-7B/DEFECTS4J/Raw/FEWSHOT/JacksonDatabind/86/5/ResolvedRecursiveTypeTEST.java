package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = new SimpleType(String.class);

        type.setReference(reference);
        assertSame(reference, type.getSelfReferencedType());

        try {
            type.setReference(reference);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(null, type.getSelfReferencedType());

        JavaType reference = new SimpleType(String.class);
        type.setReference(reference);
        assertEquals(reference, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("", sb.toString());

        JavaType reference = new SimpleType(String.class);
        type.setReference(reference);
        sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals(reference.getTypeName(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("java.lang.Object", sb.toString());

        JavaType reference = new SimpleType(String.class);
        type.setReference(reference);
        sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals(reference.getRawClass().getName(), sb.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type1.equals(type2));

        JavaType reference = new SimpleType(String.class);
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
        type3.setReference(reference);
        ResolvedRecursiveType type4 = new ResolvedRecursiveType(Object.class, null);
        type4.setReference(reference);
        assertTrue(type3.equals(type4));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());

        JavaType reference = new SimpleType(String.class);
        type.setReference(reference);
        assertEquals("[recursive type; [" + reference.getRawClass().getName() + "]", type.toString());
    }
}