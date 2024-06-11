package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        assertEquals(String.class, resolvedRecursiveType.getRawClass());
        assertEquals(typeBindings, resolvedRecursiveType.typeBindings);
        assertNull(resolvedRecursiveType.getContentType());
    }

    @Test
    public void testSetReference() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);

        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new JavaType();
        referencedType.setGenericSignature(sb);
        resolvedRecursiveType.setReference(referencedType);

        StringBuilder expectedSignature = new StringBuilder();
        referencedType.getGenericSignature(expectedSignature);

        StringBuilder actualSignature = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actualSignature);

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetErasedSignature() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new JavaType();
        referencedType.setErasedSignature(sb);
        resolvedRecursiveType.setReference(referencedType);

        StringBuilder expectedSignature = new StringBuilder();
        referencedType.getErasedSignature(expectedSignature);

        StringBuilder actualSignature = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(actualSignature);

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testWithContentType() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, result);
        assertNull(result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
        assertNull(result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
        assertNull(result.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
        assertNull(result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
        assertNull(result.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        JavaType result = resolvedRecursiveType.withStaticTyping();

        assertEquals(resolvedRecursiveType, result);
        assertTrue(result.hasRawClass(String.class));
    }

    @Test
    public void testNarrow() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        Class<?> clazz = String.class;
        JavaType result = resolvedRecursiveType.narrow(clazz);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        Class<?> rawType = String.class;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedRecursiveType.refine(rawType, typeBindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, typeBindings);

        assertNotNull(resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, typeBindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, typeBindings);

        JavaType referencedType = new JavaType();
        resolvedRecursiveType1.setReference(referencedType);
        resolvedRecursiveType2.setReference(referencedType);

        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}