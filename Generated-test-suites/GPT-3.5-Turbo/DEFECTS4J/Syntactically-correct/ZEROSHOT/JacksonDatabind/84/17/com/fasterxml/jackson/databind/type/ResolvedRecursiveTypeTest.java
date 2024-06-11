package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType javaType1 = new JavaType();
        JavaType javaType2 = new JavaType();
        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        // TODO: Add assertion
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        // TODO: Add assertion
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = Class.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertTrue(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsWithNullReference() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Object obj = new Object();
        assertFalse(resolvedRecursiveType.equals(obj));
    }

    @Test
    public void testEqualsWithDifferentReferenceType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Class.class, new TypeBindings());
        other.setReference(new JavaType());
        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsWithSameReferenceType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        other.setReference(javaType);
        assertTrue(resolvedRecursiveType.equals(other));
    }

}