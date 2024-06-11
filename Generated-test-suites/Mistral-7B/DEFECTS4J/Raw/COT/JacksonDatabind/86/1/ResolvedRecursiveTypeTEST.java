package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * JUnit tests for ResolvedRecursiveType class
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSetReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType(String.class);
        resolvedRecursiveType.setReference(javaType);
        ResolvedRecursiveType expected = new ResolvedRecursiveType(erasedType, bindings).setReference(javaType);
        assertEquals(expected.getSelfReferencedType(), resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceIllegalStateException() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType(String.class);
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.setReference(javaType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType(String.class);
        resolvedRecursiveType.setReference(javaType);
        JavaType result = resolvedRecursiveType.getSelfReferencedType();
        assertSame(javaType, result);
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder expected = new StringBuilder("[recursive type; ");
        StringBuilder result = resolvedRecursiveType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder expected = new StringBuilder("[recursive type; ");
        StringBuilder result = resolvedRecursiveType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testWithContentType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType(Integer.class);
        ResolvedRecursiveType result = resolvedRecursiveType.withContentType(javaType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        // deprecated method, not tested
    }

    @Test
    public void testRefine() {
        // not implemented method, not tested
    }

    @Test
    public void testIsContainerType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
        JavaType javaType = new SimpleType(String.class);
        resolvedRecursiveType.setReference(javaType);
        expected = "[recursive type; " + javaType.getRawClass().getName() + "]";
        result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType1.equals(null));
        JavaType javaType = new SimpleType(String.class);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType, bindings).setReference(javaType);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType1)); // should not be equal to itself!
        resolvedRecursiveType2.setReference(javaType);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}