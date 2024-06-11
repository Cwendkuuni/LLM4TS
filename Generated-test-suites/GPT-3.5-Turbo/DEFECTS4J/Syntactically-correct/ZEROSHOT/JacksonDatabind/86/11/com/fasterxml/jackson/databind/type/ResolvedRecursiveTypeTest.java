package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    
    @Before
    public void setUp() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
    }
    
    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        // add assertion here
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        // add assertion here
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void test_narrow() {
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
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
        String result = resolvedRecursiveType.toString();
        // add assertion here
    }
    
    @Test
    public void testEquals() {
        Object o = new ResolvedRecursiveType(Object.class, new TypeBindings());
        boolean result = resolvedRecursiveType.equals(o);
        Assert.assertTrue(result);
    }
}