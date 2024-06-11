package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType referencedType = new JavaType(Integer.class);
        type.setReference(referencedType);
        JavaType selfReferencedType = type.getSelfReferencedType();
        assertNotNull(selfReferencedType);
        assertEquals(referencedType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType referencedType1 = new JavaType(Integer.class);
        type.setReference(referencedType1);
        try {
            JavaType referencedType2 = new JavaType(Long.class);
            type.setReference(referencedType2);
        } finally {
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType referencedType = new JavaType(Integer.class);
        type.setReference(referencedType);
        JavaType selfReferencedType = type.getSelfReferencedType();
        assertNotNull(selfReferencedType);
        assertEquals(referencedType, selfReferencedType);
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertNull(sb.toString());

        JavaType referencedType = new JavaType(Integer.class);
        type.setReference(referencedType);
        sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/Integer;", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertNull(sb.toString());

        JavaType referencedType = new JavaType(Integer.class);
        type.setReference(referencedType);
        sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());

        JavaType referencedType = new JavaType(Integer.class);
        type.setReference(referencedType);
        assertEquals("[recursive type; [Ljava/lang/Integer;]", type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        assertFalse(type1.equals(type2));

        JavaType referencedType = new JavaType(Integer.class);
        type1.setReference(referencedType);
        type2.setReference(referencedType);
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsDifferentClasses() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, null);
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertFalse(type.equals(null));
    }
}