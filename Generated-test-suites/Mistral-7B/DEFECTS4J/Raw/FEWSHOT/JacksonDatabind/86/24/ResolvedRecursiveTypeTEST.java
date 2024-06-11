package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType = new SimpleType(String.class);

        type1.setReference(refType);
        JavaType refType1 = type1.getSelfReferencedType();

        assertNotNull(refType1);
        assertEquals(refType, refType1);

        try {
            type1.setReference(refType1);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        assertNull(type.getSelfReferencedType());

        JavaType refType = new SimpleType(String.class);
        type.setReference(refType);
        JavaType selfRefType = type.getSelfReferencedType();

        assertNotNull(selfRefType);
        assertEquals(refType, selfRefType);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);

        assertEquals("", sb.toString());

        JavaType refType = new SimpleType(String.class);
        type.setReference(refType);
        sb.setLength(0);
        type.getGenericSignature(sb);

        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);

        assertEquals("", sb.toString());

        JavaType refType = new SimpleType(String.class);
        type.setReference(refType);
        sb.setLength(0);
        type.getErasedSignature(sb);

        assertEquals("java.lang.Object", sb.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.TYPE, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.TYPE, null);

        assertFalse(type1.equals(type2));

        JavaType refType = new SimpleType(String.class);
        type1.setReference(refType);
        type2.setReference(refType);

        assertTrue(type1.equals(type2));

        JavaType otherRefType = new SimpleType(Integer.class);
        type1.setReference(otherRefType);
        type2.setReference(refType);

        assertFalse(type1.equals(type2));
    }
}