package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType(null, null, null);
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_ThrowsIllegalStateException() {
        JavaType existingRef = new JavaType(null, null, null);
        JavaType newRef = new JavaType(null, null, null);
        resolvedRecursiveType.setReference(existingRef);
        resolvedRecursiveType.setReference(newRef);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new JavaType(null, null, null);
        resolvedRecursiveType.setReference(ref);
        ref.getGenericSignature(expected);
        resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new JavaType(null, null, null);
        resolvedRecursiveType.setReference(ref);
        ref.getErasedSignature(expected);
        resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(null, null, null);
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = new JavaType(null, null, null);
        JavaType[] superInterfaces = new JavaType[]{};
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        boolean result = resolvedRecursiveType.isContainerType();
        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Object o = new ResolvedRecursiveType(null, null);
        boolean result = resolvedRecursiveType.equals(o);
        Assert.assertFalse(result);
    }
}