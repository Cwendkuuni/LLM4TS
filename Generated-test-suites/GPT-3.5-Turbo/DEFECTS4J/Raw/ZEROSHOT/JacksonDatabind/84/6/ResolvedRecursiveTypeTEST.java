package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());

        try {
            resolvedRecursiveType.setReference(javaType);
            Assert.fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Trying to re-set self reference; old value = " + javaType + ", new = " + javaType, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(javaType.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(javaType.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(javaType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(null));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertNull(resolvedRecursiveType.refine(null, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals("[recursive type; " + javaType.getRawClass().getName() + "]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType1 = new JavaType();
        JavaType javaType2 = new JavaType();
        resolvedRecursiveType.setReference(javaType1);

        Assert.assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        Assert.assertFalse(resolvedRecursiveType.equals(null));
        Assert.assertFalse(resolvedRecursiveType.equals(""));
        Assert.assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(null, null)));
        
        ResolvedRecursiveType otherResolvedRecursiveType = new ResolvedRecursiveType(null, null);
        otherResolvedRecursiveType.setReference(javaType2);
        Assert.assertFalse(resolvedRecursiveType.equals(otherResolvedRecursiveType));
        
        otherResolvedRecursiveType.setReference(javaType1);
        Assert.assertTrue(resolvedRecursiveType.equals(otherResolvedRecursiveType));
    }

}