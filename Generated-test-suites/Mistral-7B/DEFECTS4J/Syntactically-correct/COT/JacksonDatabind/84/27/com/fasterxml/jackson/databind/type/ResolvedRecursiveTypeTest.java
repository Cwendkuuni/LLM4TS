package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Test;

/**
 * Test cases for ResolvedRecursiveType class
 */
public class ResolvedRecursiveTypeTest {

    // Test case for constructor
    @Test
    public void testResolvedRecursiveTypeConstruction() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(type);
        assertEquals(erasedType, type.getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    // Test case for setReference method
    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);

        type.setReference(refType);
        JavaType referencedType = type.getSelfReferencedType();
        assertNotNull(referencedType);
        assertEquals(refType, referencedType);

        // Test for exception when trying to set reference multiple times
        try {
            type.setReference(refType);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Trying to re-set self reference"));
        }
    }

    // Test case for getSelfReferencedType method
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);
        type.setReference(refType);
        JavaType referencedType = type.getSelfReferencedType();
        assertNotNull(referencedType);
        assertEquals(refType, referencedType);
    }

    // Test case for getGenericSignature and getErasedSignature methods
    @Test
    public void testGetGenericSignatureAndGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);
        type.setReference(refType);

        StringBuilder sb = new StringBuilder(10);
        type.getGenericSignature(sb);
        String genericSignature = sb.toString();
        assertNotNull(genericSignature);
        assertEquals("java.lang.int[]", genericSignature);

        sb = new StringBuilder(10);
        type.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        assertNotNull(erasedSignature);
        assertEquals("java.lang.Object", erasedSignature);
    }

    // Test case for withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler, withStaticTyping methods
    @Test
    public void testWithMethods() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);
        type.setReference(refType);

        Object handler = new Object();
        type = type.withContentTypeHandler(handler);
        assertSame(handler, type.getContentTypeHandler());

        type = type.withValueHandler(handler);
        assertSame(handler, type.getValueHandler());

        type = type.withStaticTyping();
        assertTrue(type.isStaticTyping());

        type = type.withContentType(JavaType.construct(int[].class));
        assertEquals(JavaType.construct(int[].class), type.getContentType());

        type = type.withTypeHandler(handler);
        assertSame(handler, type.getTypeHandler());

        type = type.withContentValueHandler(handler);
        assertSame(handler, type.getContentValueHandler());
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        Class<?> erasedType2 = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);

        JavaType refType1 = refType;
        type1.setReference(refType1);
        JavaType refType2 = refType;
        type2.setReference(refType2);

        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type1));
        assertFalse(type1.equals(type2));
        assertFalse(type2.equals(type1));

        // Test for equality with null
        assertFalse(type1.equals(null));
        assertFalse(null.equals(type1));
    }
}