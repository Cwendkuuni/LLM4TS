package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    
    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }
    
    @Test
    public void testSetReferencce() {
        JavaType ref = new JavaType();
        
        resolvedRecursiveType.setReference(ref);
        
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType oldRef = new JavaType();
        JavaType newRef = new JavaType();
        
        resolvedRecursiveType.setReference(oldRef);
        resolvedRecursiveType.setReference(newRef);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ref.setGenericSignature(sb);
        
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(new StringBuilder());
        
        Assert.assertEquals(sb.toString(), result.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ref.setErasedSignature(sb);
        
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(new StringBuilder());
        
        Assert.assertEquals(sb.toString(), result.toString());
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
    public void testNarrow() {
        Class<?> subclass = new Class<Object>();
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = new Class<Object>();
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
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ref.setRawClass(String.class);
        
        resolvedRecursiveType.setReference(ref);
        
        String result = resolvedRecursiveType.toString();
        
        Assert.assertEquals("[recursive type; java.lang.String", result);
    }
    
    @Test
    public void testEquals() {
        Object obj = new ResolvedRecursiveType(null, null);
        
        boolean result = resolvedRecursiveType.equals(obj);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentReferenceType() {
        Object obj = new Object();
        
        boolean result = resolvedRecursiveType.equals(obj);
        
        Assert.assertFalse(result);
    }
}