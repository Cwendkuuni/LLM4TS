package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        resolvedRecursiveType.setReference(javaType);

        JavaType referencedType = resolvedRecursiveType.getSelfReferencedType();

        assertSame(javaType, referencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType1 = mock(JavaType.class);
        JavaType javaType2 = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType referencedType = resolvedRecursiveType.getSelfReferencedType();

        assertSame(javaType, referencedType);
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = mock(TypeBindings.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType referencedType = resolvedRecursiveType.getSelfReferencedType();

        assertNull(referencedType);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = mock(JavaType.class);
        StringBuilder sb = new StringBuilder();

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(javaType);

        resolvedRecursiveType.getGenericSignature(sb);

        javaType.getGenericSignature(sb);

        String result = sb.toString();

        assertSame("expected getGenericSignature to be called on self-referenced type", result, javaType.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = mock(JavaType.class);
        StringBuilder sb = new StringBuilder();

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(javaType);

        resolvedRecursiveType.getErasedSignature(sb);

        javaType.getErasedSignature(sb);

        String result = sb.toString();

        assertSame("expected getErasedSignature to be called on self-referenced type", result, javaType.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);

        resolvedRecursiveType.withContentType(javaType);

        JavaType result = resolvedRecursiveType.getSelfReferencedType();

        assertSame(javaType, result);
    }

    @Test
    public void testToString() {
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(javaType);

        String result = resolvedRecursiveType.toString();

        String expected = "[recursive type; " + javaType.getRawClass().getName() + "]";

        assertSame(expected, result);
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = mock(JavaType.class);
        JavaType javaType2 = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);

        resolvedRecursiveType1.setReference(javaType1);
        resolvedRecursiveType2.setReference(javaType2);

        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        when(javaType1.equals(javaType2)).thenReturn(true);

        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsSelf() {
        JavaType javaType = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);

        resolvedRecursiveType.setReference(javaType);

        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
}