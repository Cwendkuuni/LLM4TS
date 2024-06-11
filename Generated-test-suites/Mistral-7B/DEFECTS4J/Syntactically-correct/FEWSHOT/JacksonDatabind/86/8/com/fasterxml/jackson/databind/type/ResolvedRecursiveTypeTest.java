package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType refType = new SimpleType(String.class);
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);

        selfRefType.setReference(refType);

        assertEquals(refType, selfRefType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType refType1 = new SimpleType(String.class);
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);
        selfRefType.setReference(refType1);

        // This should throw an IllegalStateException
        selfRefType.setReference(new SimpleType(Integer.class));
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType refType = new SimpleType(String.class);
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);

        selfRefType.setReference(refType);

        assertEquals(refType, selfRefType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);

        assertNull(selfRefType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(String.class);
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);

        selfRefType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        selfRefType.getGenericSignature(sb);

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(String.class);
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);

        selfRefType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        selfRefType.getErasedSignature(sb);

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new SimpleType(String.class);
        JavaType refType2 = new SimpleType(String.class);

        ResolvedRecursiveType selfRefType1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType selfRefType2 = new ResolvedRecursiveType(String.class, null);

        selfRefType1.setReference(refType1);
        selfRefType2.setReference(refType2);

        assertNotEquals(selfRefType1, selfRefType2);

        selfRefType1 = new ResolvedRecursiveType(String.class, null);
        selfRefType2 = new ResolvedRecursiveType(String.class, null);
        selfRefType1.setReference(refType1);
        selfRefType2.setReference(refType1);

        assertEquals(selfRefType1, selfRefType2);
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(String.class);
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);

        selfRefType.setReference(refType);

        assertEquals("[recursive type; " + refType.getRawClass().getName() + "]", selfRefType.toString());
    }

    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(String.class, null);

        assertEquals("[recursive type; UNRESOLVED]", selfRefType.toString());
    }
}