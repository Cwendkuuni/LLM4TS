package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for ResolvedRecursiveType class
 */
public class ResolvedRecursiveTypeTest {

    // Test case for constructor
    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Assert.assertNotNull(resolvedRecursiveType);
    }

    // Test case for setReference method
    @Test
    public void testSetReference() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(int.class);

        resolvedRecursiveType.setReference(refType);
        JavaType expected = refType;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        Assert.assertEquals(expected, actual);

        try {
            resolvedRecursiveType.setReference(refType); // verify that method can't be called twice
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Trying to re-set self reference; old value = " + refType + ", new = " + refType, e.getMessage());
        }
    }

    // Test case for getSelfReferencedType method
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(int.class);
        resolvedRecursiveType.setReference(refType);

        JavaType expected = refType;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        Assert.assertEquals(expected, actual);
    }

    // Test case for getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(refType);

        StringBuilder expected = new StringBuilder("[Ljava.lang.int;");
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actual);

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    // Test case for getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(refType);

        StringBuilder expected = new StringBuilder("Ljava/lang/Object;[Ljava/lang/Object;");
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(actual);

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        Class<?> erasedType1 = ResolvedRecursiveType.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType refType1 = new SimpleType(int[].class);
        resolvedRecursiveType1.setReference(refType1);

        Class<?> erasedType2 = ResolvedRecursiveType.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType refType2 = new SimpleType(int[].class);
        resolvedRecursiveType2.setReference(refType2);

        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType1, bindings1);

        Assert.assertFalse(resolvedRecursiveType1.equals(null));
        Assert.assertFalse(resolvedRecursiveType1.equals(new Object()));
        Assert.assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));

        JavaType sameRefType = refType1;
        ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedRecursiveType4.setReference(sameRefType);

        Assert.assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType4));

        JavaType differentRefType = new SimpleType(String[].class);
        ResolvedRecursiveType resolvedRecursiveType5 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedRecursiveType5.setReference(differentRefType);

        Assert.assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType5));
    }
}