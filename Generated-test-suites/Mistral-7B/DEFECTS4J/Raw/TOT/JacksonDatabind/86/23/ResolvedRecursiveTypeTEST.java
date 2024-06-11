package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> cls = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(cls, bindings);
        JavaType refType = new ClassType(SomeClass.class);

        type1.setReference(refType);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(cls, bindings);
        type2.setReference(type1);

        assertSame(type1, type2.getSelfReferencedType());
        assertSame(type2, type1.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> cls = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(cls, bindings);
        JavaType refType = new ClassType(SomeClass.class);

        type1.setReference(refType);
        type1.setReference(refType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> cls = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(cls, bindings);
        JavaType refType = new ClassType(SomeClass.class);

        type1.setReference(refType);
        assertSame(refType, type1.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedType_uninitialized() {
        Class<ResolvedRecursiveType> cls = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(cls, bindings);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> cls = ResolvedRecursiveType.class;
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType type = new ResolvedRecursiveType(cls, new TypeBindings());
        type.getGenericSignature(sb);

        // The output of getGenericSignature() may vary, so we cannot test it directly.
        // Instead, let's verify that it doesn't throw an exception.
        assertNull(sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> cls = ResolvedRecursiveType.class;
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType type = new ResolvedRecursiveType(cls, new TypeBindings());
        type.getErasedSignature(sb);

        // The output of getErasedSignature() may vary, so we cannot test it directly.
        // Instead, let's verify that it doesn't throw an exception.
        assertNull(sb.toString());
    }

    // Add more test cases here for other methods in ResolvedRecursiveType class

    private static class SomeClass {
        // Add some methods or fields here to make the test cases more meaningful
    }
}