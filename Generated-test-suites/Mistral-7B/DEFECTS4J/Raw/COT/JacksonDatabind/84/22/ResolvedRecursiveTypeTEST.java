package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test cases for ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);

        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref1 = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref1);

        JavaType ref2 = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);

        StringBuilder expected = new StringBuilder("[Ljava/lang/Object;");
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);

        StringBuilder expected = new StringBuilder("Ljava/lang/Object;");
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    // Add other test cases for the remaining methods with similar structure
    // ...
}