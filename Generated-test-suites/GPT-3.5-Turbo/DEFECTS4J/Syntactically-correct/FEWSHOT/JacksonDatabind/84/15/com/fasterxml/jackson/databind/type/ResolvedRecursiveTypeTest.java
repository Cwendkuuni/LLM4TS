package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, resolvedRecursiveType.getErasedType());
        assertNull(resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getReferencedType());
        assertNull(resolvedRecursiveType.getSuperClass());
        assertNull(resolvedRecursiveType.getSuperInterfaces());
        assertEquals(0, resolvedRecursiveType.getModifiers());
        assertFalse(resolvedRecursiveType.isEnumType());
        assertFalse(resolvedRecursiveType.isInterface());
        assertFalse(resolvedRecursiveType.isPrimitive());
        assertFalse(resolvedRecursiveType.isFinal());
        assertFalse(resolvedRecursiveType.isGeneric());
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = null;
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object h = null;
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object h = null;
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object h = null;
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object h = null;
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = Object.class;
        TypeBindings refineBindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = resolvedRecursiveType.refine(rawType, refineBindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        String toString = "[recursive type; UNRESOLVED]";
        assertEquals(toString, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = null;
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}