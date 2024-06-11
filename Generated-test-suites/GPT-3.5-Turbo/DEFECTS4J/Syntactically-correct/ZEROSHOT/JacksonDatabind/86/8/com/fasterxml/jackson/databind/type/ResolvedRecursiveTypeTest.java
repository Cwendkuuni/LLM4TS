
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType reference = new JavaType();
        resolvedRecursiveType.setReference(reference);

        Assert.assertEquals(reference, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();

        resolvedRecursiveType.getGenericSignature(sb);

        Assert.assertEquals(expectedResult.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();

        resolvedRecursiveType.getErasedSignature(sb);

        Assert.assertEquals(expectedResult.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType expectedResult = resolvedRecursiveType;

        JavaType result = resolvedRecursiveType.withContentType(contentType);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType expectedResult = resolvedRecursiveType;

        JavaType result = resolvedRecursiveType.withTypeHandler(h);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType expectedResult = resolvedRecursiveType;

        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType expectedResult = resolvedRecursiveType;

        JavaType result = resolvedRecursiveType.withValueHandler(h);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType expectedResult = resolvedRecursiveType;

        JavaType result = resolvedRecursiveType.withContentValueHandler(h);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType expectedResult = resolvedRecursiveType;

        JavaType result = resolvedRecursiveType.withStaticTyping();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = new Class<?>();
        JavaType expectedResult = resolvedRecursiveType;

        JavaType result = resolvedRecursiveType.narrow(subclass);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType expectedResult = null;

        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean expectedResult = false;

        boolean result = resolvedRecursiveType.isContainerType();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        String expectedResult = "[recursive type; UNRESOLVED]";

        String result = resolvedRecursiveType.toString();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object o = new Object();
        boolean expectedResult = false;

        boolean result = resolvedRecursiveType.equals(o);

        Assert.assertEquals(expectedResult, result);
    }
}
