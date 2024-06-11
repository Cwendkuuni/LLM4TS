package com.example; // replace with your package name

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeReference<Object>() {}.getType();
        type.setReference(ref);
        JavaType result = type.getSelfReferencedType();
        assertSame(ref, result);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeReference<Object>() {}.getType();
        type.setReference(ref);
        JavaType result = type.getSelfReferencedType();
        assertSame(ref, result);
    }

    @Test
    public void testGetGenericSignature() {
        // Add this test case for getGenericSignature method
    }

    @Test
    public void testGetErasedSignature() {
        // Add this test case for getErasedSignature method
    }

    @Test
    public void testNullJavaTypeReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertThrows(NullPointerException.class, () -> type.setReference(null));
    }

    @Test
    public void testMultipleReferenceSet() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new TypeReference<Object>() {}.getType();
        JavaType ref2 = new TypeReference<Object>() {}.getType();
        type.setReference(ref1);
        assertThrows(IllegalStateException.class, () -> type.setReference(ref2));
    }

    @Test
    public void testNullReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testInvalidClassParameter() {
        Class<?> erasedType = new Object() {}.getClass(); // An invalid class
        TypeBindings bindings = new TypeBindings();
        assertThrows(IllegalArgumentException.class, () -> new ResolvedRecursiveType(erasedType, bindings));
    }

    @Test
    public void testInvalidTypeBindingsParameter() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = null; // An invalid TypeBindings
        assertThrows(IllegalArgumentException.class, () -> new ResolvedRecursiveType(erasedType, bindings));
    }
}