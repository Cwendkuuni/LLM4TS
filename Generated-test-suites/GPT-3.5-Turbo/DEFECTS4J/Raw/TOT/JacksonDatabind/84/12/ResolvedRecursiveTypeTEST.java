package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType referencedType = recursiveType.getSelfReferencedType();

        assertNull(referencedType);

        JavaType newReferencedType = new JavaType();
        recursiveType.setReference(newReferencedType);

        referencedType = recursiveType.getSelfReferencedType();

        assertNotNull(referencedType);
        assertEquals(newReferencedType, referencedType);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = recursiveType.getGenericSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = recursiveType.getErasedSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new JavaType();
        JavaType result = recursiveType.withContentType(contentType);

        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = recursiveType.withTypeHandler(handler);

        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = recursiveType.withContentTypeHandler(handler);

        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = recursiveType.withValueHandler(handler);

        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = recursiveType.withContentValueHandler(handler);

        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType result = recursiveType.withStaticTyping();

        assertEquals(recursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Class<?> subclass = String.class;
        JavaType result = recursiveType._narrow(subclass);

        assertEquals(recursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = recursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        boolean isContainerType = recursiveType.isContainerType();

        assertFalse(isContainerType);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        String toString = recursiveType.toString();

        assertEquals("[recursive type; UNRESOLVED]", toString);
    }

    @Test
    public void testEquals_sameObject() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);

        assertTrue(recursiveType.equals(recursiveType));
    }

    @Test
    public void testEquals_nullObject() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);

        assertFalse(recursiveType.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);

        assertFalse(recursiveType.equals(new Object()));
    }

    @Test
    public void testEquals_differentReferencedType() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Integer.class, null);

        assertFalse(recursiveType1.equals(recursiveType2));
    }

    @Test
    public void testEquals_sameReferencedType() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(String.class, null);

        assertTrue(recursiveType1.equals(recursiveType2));
    }

}