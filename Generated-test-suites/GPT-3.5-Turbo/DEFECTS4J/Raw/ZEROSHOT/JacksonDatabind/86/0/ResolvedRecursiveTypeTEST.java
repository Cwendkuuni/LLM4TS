package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null);
        resolvedRecursiveType.setReference(ref);

        JavaType actual = resolvedRecursiveType.getSelfReferencedType();

        Assert.assertEquals(ref, actual);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();

        StringBuilder actual = resolvedRecursiveType.getGenericSignature(sb);

        Assert.assertEquals(sb, actual);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();

        StringBuilder actual = resolvedRecursiveType.getErasedSignature(sb);

        Assert.assertEquals(sb, actual);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(null, null, null, null);

        JavaType actual = resolvedRecursiveType.withContentType(contentType);

        Assert.assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();

        JavaType actual = resolvedRecursiveType.withTypeHandler(h);

        Assert.assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();

        JavaType actual = resolvedRecursiveType.withContentTypeHandler(h);

        Assert.assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();

        JavaType actual = resolvedRecursiveType.withValueHandler(h);

        Assert.assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();

        JavaType actual = resolvedRecursiveType.withContentValueHandler(h);

        Assert.assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        JavaType actual = resolvedRecursiveType.withStaticTyping();

        Assert.assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = new Class<?>();

        JavaType actual = resolvedRecursiveType._narrow(subclass);

        Assert.assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(null, null, null, null);
        JavaType[] superInterfaces = new JavaType[0];

        JavaType actual = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        Assert.assertNull(actual);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        boolean actual = resolvedRecursiveType.isContainerType();

        Assert.assertFalse(actual);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(new JavaType(new Class<?>(), null, null, null));
        String expected = "[recursive type; java.lang.Class]";

        String actual = resolvedRecursiveType.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(new JavaType(new Class<?>(), null, null, null));
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        other.setReference(new JavaType(new Class<?>(), null, null, null));

        boolean actual = resolvedRecursiveType.equals(other);

        Assert.assertTrue(actual);
    }

    @Test
    public void testEquals_sameReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(new JavaType(new Class<?>(), null, null, null));

        boolean actual = resolvedRecursiveType.equals(resolvedRecursiveType);

        Assert.assertTrue(actual);
    }

    @Test
    public void testEquals_null() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(new JavaType(new Class<?>(), null, null, null));

        boolean actual = resolvedRecursiveType.equals(null);

        Assert.assertFalse(actual);
    }
}