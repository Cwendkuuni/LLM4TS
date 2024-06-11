package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType refType = new ClassType(ResolvedRecursiveType.class);
        resolvedRecursiveType.setReference(refType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(refType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType refType = new ClassType(ResolvedRecursiveType.class);
        resolvedRecursiveType.setReference(refType);
        resolvedRecursiveType.setReference(new ArrayType(Integer.TYPE, 1));
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        assertEquals("", sb.toString());

        JavaType refType = new ClassType(ResolvedRecursiveType.class);
        resolvedRecursiveType.setReference(refType);
        sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        assertEquals("[? extends java.lang.Object]", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        assertEquals("", sb.toString());

        JavaType refType = new ClassType(ResolvedRecursiveType.class);
        resolvedRecursiveType.setReference(refType);
        sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        assertEquals("java.lang.Object", sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType contentType = new ClassType(String.class);
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        Class<?> subclass = String.class;
        JavaType result = resolvedRecursiveType.narrow(subclass);
        assertSame(resolvedRecursiveType, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        Class<?> rawType = Integer[].class;
        TypeBindings bindings = new SimpleTypeBindings();
        JavaType superClass = new ClassType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        fail("Expected exception: UnsupportedOperationException");
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.TYPE, null);
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());

        JavaType refType = new ClassType(ResolvedRecursiveType.class);
        resolvedRecursiveType.setReference(refType);
        assertEquals("[recursive type; java.lang.Class[com.fasterxml.jackson.databind.type.ResolvedRecursiveType]@0]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Integer.TYPE, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Integer.TYPE, null);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType refType = new ClassType(ResolvedRecursiveType.class);
        resolvedRecursiveType1.setReference(refType);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Integer.TYPE, null);
        resolvedRecursiveType3.setReference(refType);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}