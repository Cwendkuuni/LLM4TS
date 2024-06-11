package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Tests for the ResolvedRecursiveType class
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        JavaType referencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref.getClass(), referencedType.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref1);
        JavaType ref2 = new JavaType(Long.class);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        JavaType referencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref.getClass(), referencedType.getClass());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "[L" + ref.getRawClass().getName() + ";]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = ref.getRawClass().getName();
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new JavaType(Integer.class);
        ResolvedRecursiveType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = Integer.class;
        ResolvedRecursiveType result = resolvedRecursiveType._narrow(subclass);
        assertNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = Integer.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        ResolvedRecursiveType result = resolvedRecursiveType.refine(rawType, newBindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.toString(sb);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = String.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new JavaType(Integer.class);
        resolvedRecursiveType1.setReference(ref1);

        Class<?> erasedType2 = String.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new JavaType(Integer.class);
        resolvedRecursiveType2.setReference(ref2);

        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        assertEquals(resolvedRecursiveType1, resolvedRecursiveType1);
    }
}