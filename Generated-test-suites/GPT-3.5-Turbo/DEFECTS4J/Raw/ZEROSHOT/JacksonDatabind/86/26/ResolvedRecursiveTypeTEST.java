package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
        assertNull(recursiveType.getSelfReferencedType());

        JavaType ref = new JavaType(null, null, false, null, null);
        recursiveType.setReference(ref);

        assertEquals(ref, recursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType ref1 = new JavaType(null, null, false, null, null);
        JavaType ref2 = new JavaType(null, null, false, null, null);

        recursiveType.setReference(ref1);
        recursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType ref = new JavaType(null, null, false, null, null);
        recursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = recursiveType.getGenericSignature(sb);

        assertSame(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType ref = new JavaType(null, null, false, null, null);
        recursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = recursiveType.getErasedSignature(sb);

        assertSame(sb, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType result = recursiveType.withTypeHandler(null);

        assertSame(recursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType result = recursiveType.withContentTypeHandler(null);

        assertSame(recursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType result = recursiveType.withValueHandler(null);

        assertSame(recursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType result = recursiveType.withContentValueHandler(null);

        assertSame(recursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType result = recursiveType.withStaticTyping();

        assertSame(recursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        JavaType rawType = new JavaType(null, null, false, null, null);
        JavaType superClass = new JavaType(null, null, false, null, null);
        JavaType[] superInterfaces = new JavaType[0];

        JavaType result = recursiveType.refine(rawType, null, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        assertFalse(recursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        String result = recursiveType.toString();

        assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void testEquals_sameObject() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        assertTrue(recursiveType.equals(recursiveType));
    }

    @Test
    public void testEquals_null() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);

        assertFalse(recursiveType.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
        Object obj = new Object();

        assertFalse(recursiveType.equals(obj));
    }

    @Test
    public void testEquals_sameReferenceType() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);

        assertTrue(recursiveType1.equals(recursiveType2));
    }

    @Test
    public void testEquals_differentReferenceType() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);

        JavaType ref1 = new JavaType(null, null, false, null, null);
        JavaType ref2 = new JavaType(null, null, false, null, null);

        recursiveType1.setReference(ref1);
        recursiveType2.setReference(ref2);

        assertFalse(recursiveType1.equals(recursiveType2));
    }

    @Test
    public void testEquals_sameReferencedType() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);

        JavaType ref = new JavaType(null, null, false, null, null);

        recursiveType1.setReference(ref);
        recursiveType2.setReference(ref);

        assertTrue(recursiveType1.equals(recursiveType2));
    }

    @Test
    public void testEquals_differentReferencedType() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);

        JavaType ref1 = new JavaType(null, null, false, null, null);
        JavaType ref2 = new JavaType(null, null, false, null, null);

        recursiveType1.setReference(ref1);
        recursiveType2.setReference(ref2);

        assertFalse(recursiveType1.equals(recursiveType2));
    }
}