package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(ref);
        JavaType expected = ref;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder expected = sb;
        StringBuilder actual = resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder expected = sb;
        StringBuilder actual = resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withContentType(contentType);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withTypeHandler(h);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withContentTypeHandler(h);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withValueHandler(h);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withContentValueHandler(h);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withStaticTyping();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expected = null;
        JavaType actual = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean expected = false;
        boolean actual = resolvedRecursiveType.isContainerType();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        String expected = "[recursive type; UNRESOLVED]";
        String actual = resolvedRecursiveType.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Object o = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean expected = false;
        boolean actual = resolvedRecursiveType.equals(o);
        Assert.assertEquals(expected, actual);
    }
}