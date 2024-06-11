package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new JavaType(String.class);
        type.setReference(ref);

        JavaType referencedType = type.getSelfReferencedType();
        assertEquals(ref.getClass(), referencedType.getClass());
        assertEquals(ref, referencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new JavaType(String.class);
        type.setReference(ref);

        JavaType referencedType = type.getSelfReferencedType();
        assertEquals(ref.getClass(), referencedType.getClass());
        assertEquals(ref, referencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new JavaType(String.class);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);

        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new JavaType(String.class);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);

        String expected = ref.getRawClass().getName();
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);

        JavaType ref1 = new JavaType(String.class);
        type1.setReference(ref1);

        Class<?> erasedType2 = Object.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);

        JavaType ref2 = new JavaType(Integer.class);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));

        Class<?> erasedType3 = Object.class;
        TypeBindings bindings3 = new TypeBindings();
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType3, bindings3);

        JavaType ref3 = ref1;
        type3.setReference(ref3);

        assertTrue(type1.equals(type3));
    }
}