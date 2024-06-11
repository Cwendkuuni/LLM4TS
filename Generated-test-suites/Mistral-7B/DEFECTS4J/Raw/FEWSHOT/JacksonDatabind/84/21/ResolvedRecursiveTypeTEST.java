package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();
        assertEquals("Expected and actual self-referenced types should be equal", expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = new SimpleType(String.class);
        JavaType ref2 = new SimpleType(Integer.class);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull("Expected self-referenced type to be null initially", type.getSelfReferencedType());

        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();
        assertEquals("Expected and actual self-referenced types should be equal", expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);

        StringBuilder sb = new StringBuilder(40);
        type.getGenericSignature(sb);

        String expectedString = expected.toString();
        String actualString = sb.toString();
        assertEquals("Expected and actual generic signatures should be equal", expectedString, actualString);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);

        StringBuilder sb = new StringBuilder(40);
        type.getErasedSignature(sb);

        String expectedString = expected.toString();
        String actualString = sb.toString();
        assertEquals("Expected and actual erased signatures should be equal", expectedString, actualString);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);

        JavaType ref1 = new SimpleType(String.class);
        JavaType ref2 = new SimpleType(Integer.class);

        type1.setReference(ref1);
        type2.setReference(ref2);

        assertFalse("Two instances with different self-referenced types should not be equal", type1.equals(type2));

        JavaType equalRef = new SimpleType(String.class);
        type1.setReference(equalRef);
        type2.setReference(equalRef);

        assertTrue("Two instances with equal self-referenced types should be equal", type1.equals(type2));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull("Expected string representation of unresolved type to be null", type.toString());

        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);

        String expected = "[recursive type; java.lang.String]";
        String actual = type.toString();
        assertEquals("Expected and actual string representations should be equal", expected, actual);
    }
}