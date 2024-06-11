
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType();
        resolvedType.setReference(ref);
        assertEquals(ref, resolvedType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = resolvedType.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = resolvedType.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new JavaType();
        JavaType result = resolvedType.withContentType(contentType);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withTypeHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withContentTypeHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withValueHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withContentValueHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType result = resolvedType.withStaticTyping();
        assertEquals(resolvedType, result);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> subclass = String.class;
        JavaType result = resolvedType._narrow(subclass);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(resolvedType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, new TypeBindings());
        String result = resolvedType.toString();
        assertTrue(result.contains("UNRESOLVED"));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType();
        resolvedType.setReference(ref);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedType2.setReference(ref);
        assertTrue(resolvedType.equals(resolvedType2));
    }
}
