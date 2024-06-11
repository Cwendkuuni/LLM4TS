package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType ref = new SimpleType(String.class);

        type.setReference(ref);
        assertNotNull(type.getSelfReferencedType());
        assertEquals(ref.getClass(), type.getSelfReferencedType().getClass());
        assertEquals(ref, type.getSelfReferencedType());

        try {
            type.setReference(ref);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        assertNotNull(type.getSelfReferencedType());
        assertEquals(ref, type.getSelfReferencedType());

        ResolvedRecursiveType otherType = new ResolvedRecursiveType(Integer.TYPE, null);
        assertNull(otherType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType = new SimpleType(String[].class);
        type.setReference(refType);

        StringBuilder expected = new StringBuilder();
        type.getGenericSignature(expected);
        assertEquals("[Ljava.lang.Object;", expected.toString());

        type = new ResolvedRecursiveType(String.class, null);
        refType = new SimpleType(Integer[].class);
        type.setReference(refType);

        expected.setLength(0);
        type.getGenericSignature(expected);
        assertEquals("[Ljava.lang.Object;", expected.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType = new SimpleType(String[].class);
        type.setReference(refType);

        StringBuilder expected = new StringBuilder();
        type.getErasedSignature(expected);
        assertEquals("[Ljava.lang.Object;", expected.toString());

        type = new ResolvedRecursiveType(String.class, null);
        refType = new SimpleType(Integer[].class);
        type.setReference(refType);

        expected.setLength(0);
        type.getErasedSignature(expected);
        assertEquals("Ljava/lang/Object;", expected.toString());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType = new SimpleType(String.class);
        type.setReference(refType);

        assertEquals("[recursive type; [Ljava.lang.String;]", type.toString());

        type = new ResolvedRecursiveType(String.class, null);
        refType = new SimpleType(Integer.class);
        type.setReference(refType);

        assertEquals("[recursive type; Ljava/lang/Integer;]", type.toString());

        type = new ResolvedRecursiveType(Integer.TYPE, null);
        assertNotNull(type.toString());
        assertFalse(type.toString().contains("UNRESOLVED"));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType1 = new SimpleType(String.class);
        type1.setReference(refType1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType2 = new SimpleType(String.class);
        type2.setReference(refType2);

        assertFalse(type1.equals(type2));

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType3 = new SimpleType(String.class);
        type3.setReference(refType3);

        type1.setReference(refType3);

        assertTrue(type1.equals(type3));
        assertTrue(type3.equals(type1));
    }
}