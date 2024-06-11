package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        
        resolvedRecursiveType.setReference(javaType);
        JavaType referencedType = resolvedRecursiveType.getSelfReferencedType();
        
        Assert.assertEquals(javaType, referencedType);
    }
    
    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        StringBuilder sb = resolvedRecursiveType.getGenericSignature(new StringBuilder());
        
        Assert.assertEquals(javaType.getGenericSignature(new StringBuilder()).toString(), sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        StringBuilder sb = resolvedRecursiveType.getErasedSignature(new StringBuilder());
        
        Assert.assertEquals(javaType.getErasedSignature(new StringBuilder()).toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType.withContentType(javaType);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType.withTypeHandler("handler");
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler("handler");
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType.withValueHandler("handler");
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType.withContentValueHandler("handler");
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType._narrow(ResolvedRecursiveTypeTest.class);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        JavaType result = resolvedRecursiveType.refine(ResolvedRecursiveTypeTest.class, null, null, null);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        
        boolean isContainerType = resolvedRecursiveType.isContainerType();
        
        Assert.assertFalse(isContainerType);
    }
    
    @Test
    public void testToString() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType.setReference(javaType);
        
        String str = resolvedRecursiveType.toString();
        
        Assert.assertTrue(str.startsWith("[recursive type; "));
        Assert.assertTrue(str.endsWith(javaType.getRawClass().getName() + "]"));
        Assert.assertTrue(str.contains(javaType.getRawClass().getName()));
    }
    
    @Test
    public void testEquals() {
        JavaType javaType = new JavaTypeImpl();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType1.setReference(javaType);
        
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
        resolvedRecursiveType2.setReference(javaType);
        
        boolean equals = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        Assert.assertTrue(equals);
    }
}