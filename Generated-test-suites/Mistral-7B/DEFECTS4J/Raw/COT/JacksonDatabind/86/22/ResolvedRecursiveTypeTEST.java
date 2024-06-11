package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * JUnit test cases for ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    /**
     * Test setReference method.
     */
    @Test
    public void testSetReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeReference<List<String>>(){}.getType();
        resolvedRecursiveType.setReference(ref);
        JavaType getSelfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref, getSelfReferencedType);
    }

    /**
     * Test getSelfReferencedType method.
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeReference<List<String>>(){}.getType();
        resolvedRecursiveType.setReference(ref);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref, selfReferencedType);
    }

    /**
     * Test getGenericSignature method.
     */
    @Test
    public void testGetGenericSignature() {
        Class<List<List<String>>> erasedType = List.class.getGenericType();
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String genericSignature = sb.toString();
        String expected = "java.util.List<java.util.List<java.lang.String>>";
        assertEquals(expected, genericSignature);
    }

    /**
     * Test getErasedSignature method.
     */
    @Test
    public void testGetErasedSignature() {
        Class<List<List<String>>> erasedType = List.class.getGenericType();
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        String expected = "java.util.List<java.lang.Object>";
        assertEquals(expected, erasedSignature);
    }

    /**
     * Test withContentType method.
     */
    @Test
    public void testWithContentType() {
        Class<List<List<String>>> erasedType = List.class.getGenericType();
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType withContentType = javaType.withContentType(new TypeReference<List<String>>(){}.getType());
        assertEquals(javaType, withContentType);
    }

    /**
     * Test setReference with null.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithNull() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
    }

    /**
     * Test setReference with same reference multiple times.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithSameReferenceMultipleTimes() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeReference<List<String>>(){}.getType();
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Class<List<List<String>>> erasedType1 = List.class.getGenericType();
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new TypeReference<List<String>>(){}.getType();
        resolvedRecursiveType1.setReference(ref1);

        Class<List<List<String>>> erasedType2 = List.class.getGenericType();
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new TypeReference<List<String>>(){}.getType();
        resolvedRecursiveType2.setReference(ref2);

        Class<List<List<String>>> erasedType3 = List.class.getGenericType();
        TypeBindings bindings3 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType3, bindings3);
        JavaType ref3 = new TypeReference<List<String>>(){}.getType();
        resolvedRecursiveType3.setReference(ref3);

        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Class<List<List<String>>> erasedType = List.class.getGenericType();
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeReference<List<String>>(){}.getType();
        resolvedRecursiveType.setReference(ref);
        String toString = resolvedRecursiveType.toString();
        String expected = "[recursive type; [java.util.List<java.util.List<java.lang.String]>; UNRESOLVED]";
        assertEquals(expected, toString);
    }

}