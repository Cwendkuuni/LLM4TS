package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.Field;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Field referencedTypeField = resolvedRecursiveType.getClass().getDeclaredField("_referencedType");
        referencedTypeField.setAccessible(true);
        Assert.assertNull(referencedTypeField.get(resolvedRecursiveType)); // initial value is null

        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(javaType, referencedTypeField.get(resolvedRecursiveType)); // value is set correctly
    }

    @Test
    public void testGetSelfReferencedType() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Field referencedTypeField = resolvedRecursiveType.getClass().getDeclaredField("_referencedType");
        referencedTypeField.setAccessible(true);

        JavaType javaType = new JavaType(Object.class);
        referencedTypeField.set(resolvedRecursiveType, javaType);
        Assert.assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType()); // value is returned correctly
    }

    @Test
    public void testGetGenericSignature() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Field referencedTypeField = resolvedRecursiveType.getClass().getDeclaredField("_referencedType");
        referencedTypeField.setAccessible(true);

        JavaType javaType = new JavaType(Object.class);
        referencedTypeField.set(resolvedRecursiveType, javaType);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(javaType.getGenericSignature(sb).toString(), resolvedRecursiveType.getGenericSignature(sb).toString()); // values are equal
    }

    @Test
    public void testGetErasedSignature() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Field referencedTypeField = resolvedRecursiveType.getClass().getDeclaredField("_referencedType");
        referencedTypeField.setAccessible(true);

        JavaType javaType = new JavaType(Object.class);
        referencedTypeField.set(resolvedRecursiveType, javaType);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(javaType.getErasedSignature(sb).toString(), resolvedRecursiveType.getErasedSignature(sb).toString()); // values are equal
    }

    @Test
    public void testWithContentType() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = new JavaType(Object.class);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType)); // returns itself
    }

    @Test
    public void testWithTypeHandler() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler)); // returns itself
    }

    @Test
    public void testWithContentTypeHandler() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler)); // returns itself
    }

    @Test
    public void testWithValueHandler() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler)); // returns itself
    }

    @Test
    public void testWithContentValueHandler() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler)); // returns itself
    }

    @Test
    public void testWithStaticTyping() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping()); // returns itself
    }

    @Test
    public void test_narrow() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Class<?> subclass = Object.class;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass)); // returns itself
    }

    @Test
    public void testRefine() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Assert.assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces)); // returns null
    }

    @Test
    public void testIsContainerType() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Assert.assertFalse(resolvedRecursiveType.isContainerType()); // returns false
    }

    @Test
    public void testToString() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder(40).append("[recursive type; UNRESOLVED]");
        Assert.assertEquals(sb.toString(), resolvedRecursiveType.toString()); // values are equal
    }

    @Test
    public void testEquals() throws Exception {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType1 = new JavaType(Object.class);
        JavaType javaType2 = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType1);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType1.setReference(javaType1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType2.setReference(javaType2);

        Assert.assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType)); // returns true
        Assert.assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType1)); // returns true
        Assert.assertFalse(resolvedRecursiveType.equals(resolvedRecursiveType2)); // returns false
    }
}