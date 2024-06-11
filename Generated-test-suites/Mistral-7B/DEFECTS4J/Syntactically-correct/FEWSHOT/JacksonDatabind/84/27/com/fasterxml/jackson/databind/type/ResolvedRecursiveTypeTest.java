package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        JavaType selfRef = type.getSelfReferencedType();
        assertNotNull(selfRef);
        assertEquals(String.class, selfRef.getRawClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = new JavaType(String.class);
        type.setReference(ref1);
        JavaType ref2 = new JavaType(Integer.class);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.getSelfReferencedType());

        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        JavaType selfRef = type.getSelfReferencedType();
        assertNotNull(selfRef);
        assertEquals(String.class, selfRef.getRawClass());
    }

    @Test
    public void testGetGenericSignature() {
        // Test with a resolved self-reference
        ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
        JavaType ref = new JavaType(List.class);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);

        assertEquals("java.util.List<T>", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        // Test with a resolved self-reference
        ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
        JavaType ref = new JavaType(List.class);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);

        assertEquals("[Ljava.lang.Object;", sb.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        assertNotNull(type1);
        assertNotNull(type2);
        assertFalse(type1.equals(type2));

        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        type1.setReference(ref1);
        type2.setReference(ref2);
        assertFalse(type1.equals(type2));

        JavaType ref3 = new JavaType(String.class);
        type1.setReference(ref3);
        JavaType selfRef1 = type1.getSelfReferencedType();
        JavaType selfRef2 = type2.getSelfReferencedType();
        assertEquals(String.class, selfRef1.getRawClass());
        assertEquals(String.class, selfRef2.getRawClass());
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());

        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        assertEquals("[recursive type; [Ljava.lang.String;]", type.toString());
    }
}