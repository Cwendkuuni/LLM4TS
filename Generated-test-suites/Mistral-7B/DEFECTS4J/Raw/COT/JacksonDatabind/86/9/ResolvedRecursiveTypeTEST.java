package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

/**
 * Test class for ResolvedRecursiveType
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertSame(erasedType, resolvedRecursiveType.getRawClass());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());

        // Test exception when trying to set reference multiple times
        assertThrows(IllegalStateException.class, () -> resolvedRecursiveType.setReference(ref));
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        assertNull(sb.toString());

        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder expectedSb = new StringBuilder("[recursive type; ");
        ref.getGenericSignature(expectedSb);
        expectedSb.append("]");
        assertSame(expectedSb.toString(), resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        assertNull(sb.toString());

        JavaType ref = MockJavaType.getMockType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder expectedSb = new StringBuilder(ref.getRawClass().getName());
        assertSame(expectedSb.toString(), resolvedRecursiveType.getErasedSignature(sb).toString());
    }

    // Add other test methods for remaining overridden methods with similar structure

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = MockJavaType.getMockType();
        resolvedRecursiveType.withContentType(contentType);
    }

    // Add test methods for other overridden methods with similar structure, including testing for exceptions

}