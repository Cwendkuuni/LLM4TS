package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference_ValidReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertSame(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_MultipleCalls() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertNotNull(result);
        assertSame(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertNotNull(result);
        assertSame(sb, result);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType result = type.withContentType(contentType);
        assertSame(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType result = type.withStaticTyping();
        assertSame(type, result);
    }

    @Test
    @Deprecated
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        JavaType result = type._narrow(subclass);
        assertSame(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString_UnresolvedReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, type.toString());
    }

    @Test
    public void testToString_ResolvedReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        ref.setRawClass(String.class);
        type.setReference(ref);
        String expected = "[recursive type; java.lang.String]";
        assertEquals(expected, type.toString());
    }

    @Test
    public void testEquals_EqualObjects() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        JavaType ref2 = new JavaType();
        type2.setReference(ref2);

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEquals_SameObject() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);

        assertTrue(type.equals(type));
    }

    @Test
    public void testEquals_Null() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);

        assertFalse(type.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);

        assertFalse(type.equals("string"));
    }

    @Test
    public void testEquals_UnresolvedReference() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEquals_DifferentReference() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        JavaType ref2 = new JavaType();
        ref2.setRawClass(String.class);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }
}