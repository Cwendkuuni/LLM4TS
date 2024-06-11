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
        JavaType ref = new SimpleType("java.lang.String");
        type.setReference(ref);

        JavaType selfRef = type.getSelfReferencedType();
        assertTrue(selfRef instanceof ResolvedRecursiveType);
        assertSame(ref, selfRef.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref1 = new SimpleType("java.lang.String");
        JavaType ref2 = new SimpleType("java.util.List");

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType("java.lang.String");
        type.setReference(ref);

        JavaType selfRef = type.getSelfReferencedType();
        assertTrue(selfRef instanceof ResolvedRecursiveType);
        assertSame(ref, selfRef.getSelfReferencedType());

        assertNull(new ResolvedRecursiveType(String.class, null).getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
        JavaType ref = new SimpleType("java.lang.String");
        type.setReference(ref);

        StringBuilder sb = new StringBuilder(128);
        type.getGenericSignature(sb);

        assertTrue(sb.toString().startsWith("[Ljava.lang.String;[recursive type; "));
        assertTrue(sb.toString().endsWith("java.lang.String]"));

        assertNull(new ResolvedRecursiveType(String.class, null).getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, null);
        JavaType ref = new SimpleType("java.lang.String");
        type.setReference(ref);

        StringBuilder sb = new StringBuilder(128);
        type.getErasedSignature(sb);

        assertTrue(sb.toString().equals("[Ljava.lang.Object;"));

        assertNull(new ResolvedRecursiveType(String.class, null).getErasedSignature(sb));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType("java.lang.String");

        type1.setReference(ref);
        type2.setReference(ref);

        assertTrue(type1.equals(type2));

        assertFalse(type1.equals(new SimpleType("java.lang.Integer")));
        assertFalse(type1.equals(null));

        JavaType ref1 = new SimpleType("java.lang.String");
        JavaType ref2 = new SimpleType("java.util.List");
        type1.setReference(ref1);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType("java.lang.String");

        type.setReference(ref);

        String str = type.toString();
        assertTrue(str.startsWith("[recursive type; "));
        assertTrue(str.endsWith("java.lang.String]"));

        assertNull(new ResolvedRecursiveType(String.class, null).toString());
    }
}