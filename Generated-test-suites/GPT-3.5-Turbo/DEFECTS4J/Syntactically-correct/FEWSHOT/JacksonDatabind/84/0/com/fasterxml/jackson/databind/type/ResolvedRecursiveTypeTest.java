package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType referenceType = null;
        type.setReference(referenceType);
        assertEquals(referenceType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = null;
        JavaType expected = type;
        JavaType result = type.withContentType(contentType);
        assertEquals(expected, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = null;
        JavaType expected = type;
        JavaType result = type.withTypeHandler(handler);
        assertEquals(expected, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = null;
        JavaType expected = type;
        JavaType result = type.withContentTypeHandler(handler);
        assertEquals(expected, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = null;
        JavaType expected = type;
        JavaType result = type.withValueHandler(handler);
        assertEquals(expected, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = null;
        JavaType expected = type;
        JavaType result = type.withContentValueHandler(handler);
        assertEquals(expected, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType expected = type;
        JavaType result = type.withStaticTyping();
        assertEquals(expected, result);
    }

    @Deprecated
    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = null;
        JavaType expected = type;
        JavaType result = type._narrow(subclass);
        assertEquals(expected, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType expected = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(expected, result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        boolean expected = false;
        boolean result = type.isContainerType();
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder expectedBuilder = new StringBuilder();
        expectedBuilder.append("[recursive type; UNRESOLVED");
        String expected = expectedBuilder.toString();
        String result = type.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object obj = null;
        boolean expected = false;
        boolean result = type.equals(obj);
        assertEquals(expected, result);
    }
}