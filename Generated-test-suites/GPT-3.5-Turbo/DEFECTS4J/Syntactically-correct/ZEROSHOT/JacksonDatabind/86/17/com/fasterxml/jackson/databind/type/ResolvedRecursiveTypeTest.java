package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType() {};
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = resolvedRecursiveType.getGenericSignature(new StringBuilder());
        Assert.assertNotNull(sb);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = resolvedRecursiveType.getErasedSignature(new StringBuilder());
        Assert.assertNotNull(sb);
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = new JavaType() {};
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        boolean result = resolvedRecursiveType.isContainerType();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        String result = resolvedRecursiveType.toString();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        Assert.assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
        Assert.assertFalse(resolvedRecursiveType1.equals(null));
        Assert.assertFalse(resolvedRecursiveType1.equals(new Object()));
        Assert.assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}