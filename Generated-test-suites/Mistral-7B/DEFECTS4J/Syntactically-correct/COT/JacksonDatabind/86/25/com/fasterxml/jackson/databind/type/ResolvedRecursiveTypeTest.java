package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);

        resolvedRecursiveType.setReference(ref);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceIllegalStateException() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);

        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new ClassType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);

        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref, selfReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new ClassType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new ClassType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = ref.getRawClass().getName();
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new ClassType(String.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);

        JavaType contentType = new ArrayType(IntegerType.getInstance());

        ResolvedRecursiveType result = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, result);
    }

    // Add other test methods for the remaining methods in the ResolvedRecursiveType class

}