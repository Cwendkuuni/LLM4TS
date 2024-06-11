package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new SimpleType(Integer.class);

        type.setReference(refType);
        assertSame(refType, type.getSelfReferencedType());
        assertTrue(type.toString().contains("Integer"));

        try {
            type.setReference(refType);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);

        assertSame(type1, type1);
        assertSame(type1, type2);

        JavaType refType = new SimpleType(Integer.class);
        type1.setReference(refType);

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, null);
        type3.setReference(refType);

        assertSame(type1, type3);

        JavaType refType2 = new SimpleType(Long.class);
        type1.setReference(refType2);

        assertNotSame(type1, type3);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);

        assertTrue(sb.toString().equals("[]"));

        JavaType refType = new SimpleType(Integer.class);
        type.setReference(refType);

        sb.setLength(0);
        type.getGenericSignature(sb);

        assertTrue(sb.toString().equals(refType.getGenericSignature(sb).toString()));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);

        assertTrue(sb.toString().equals("Ljava/lang/String;"));

        JavaType refType = new SimpleType(Integer.class);
        type.setReference(refType);

        sb.setLength(0);
        type.getErasedSignature(sb);

        assertTrue(sb.toString().equals(refType.getErasedSignature(sb).toString()));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertTrue(type.toString().contains("UNRESOLVED"));

        JavaType refType = new SimpleType(Integer.class);
        type.setReference(refType);

        assertTrue(type.toString().contains("Integer"));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testEqualsNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());

        assertNotSame(type, null);
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());
    }
}