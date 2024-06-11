package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType referencedType = new ArrayType(String.class, 1);
        selfRefType.setReference(referencedType);
        assertNotNull(selfRefType.getSelfReferencedType());
        assertEquals(referencedType, selfRefType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType referencedType = new ArrayType(String.class, 1);
        selfRefType.setReference(referencedType);
        selfRefType.setReference(new ArrayType(String.class, 1));
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType referencedType = new ArrayType(String.class, 1);
        selfRefType.setReference(referencedType);
        assertNotNull(selfRefType.getSelfReferencedType());
        assertEquals(referencedType, selfRefType.getSelfReferencedType());
        assertNull(new ResolvedRecursiveType(int[].class, bindings).getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType referencedType = new ArrayType(String.class, 1);
        selfRefType.setReference(referencedType);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = new StringBuilder();
        selfRefType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType referencedType = new ArrayType(String.class, 1);
        selfRefType.setReference(referencedType);
        StringBuilder expected = new StringBuilder("[Ljava.lang.Object;");
        StringBuilder actual = new StringBuilder();
        selfRefType.getErasedSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = String[].class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType selfRefType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType referencedType1 = new ArrayType(String.class, 1);
        selfRefType1.setReference(referencedType1);

        Class<?> erasedType2 = String[].class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType selfRefType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType referencedType2 = new ArrayType(String.class, 1);
        selfRefType2.setReference(referencedType2);

        Class<?> erasedType3 = Integer[].class;
        TypeBindings bindings3 = new TypeBindings();
        ResolvedRecursiveType selfRefType3 = new ResolvedRecursiveType(erasedType3, bindings3);

        assertTrue(selfRefType1.equals(selfRefType1));
        assertTrue(selfRefType1.equals(selfRefType1.getClass().cast(selfRefType2.getSelfReferencedType())));
        assertFalse(selfRefType1.equals(selfRefType3));
        assertFalse(selfRefType1.equals(null));
    }
}