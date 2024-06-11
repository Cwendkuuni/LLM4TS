package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(ref);

        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        Assert.assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        Assert.assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withTypeHandler(h);

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withValueHandler(h);

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType._narrow(subclass);

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean result = resolvedRecursiveType.isContainerType();

        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        String result = resolvedRecursiveType.toString();

        StringBuilder expected = new StringBuilder(40)
                .append("[recursive type; UNRESOLVED");

        Assert.assertEquals(expected.toString(), result);
    }

    @Test
    public void testEquals_sameObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean result = resolvedRecursiveType.equals(resolvedRecursiveType);

        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_nullObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean result = resolvedRecursiveType.equals(null);

        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_differentClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean result = resolvedRecursiveType.equals(new Object());

        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_differentReferencedType() {
        JavaType ref1 = null;
        JavaType ref2 = null;
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType2.setReference(ref2);

        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_sameReferencedType() {
        JavaType ref = null;
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(ref);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType2.setReference(ref);

        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_differentSelfReferencedType() {
        JavaType ref1 = null;
        JavaType ref2 = null;
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType2.setReference(ref2);

        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_sameSelfReferencedType() {
        JavaType ref = null;
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(ref);
        ResolvedRecursiveType resolvedRecursiveType2 = resolvedRecursiveType1;

        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        Assert.assertTrue(result);
    }
}