package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        // Typical test case: set reference for the first time
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(null, null, false, false, null, null, null);
        
        type.setReference(ref);
        
        Assert.assertEquals(ref, type.getSelfReferencedType());
        
        // Error scenario: try to set reference again, should throw an IllegalStateException
        try {
            type.setReference(ref);
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }
    
    @Test
    public void testGetGenericSignature() {
        // Typical test case: referenced type has generic signature
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(null, null, false, false, null, null, null);
        ref.setGenericSignature(new StringBuilder("Lcom/example/Type;"));
        
        type.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        
        Assert.assertEquals("Lcom/example/Type;", sb.toString());
        
        // Edge case: referenced type does not have generic signature
        type = new ResolvedRecursiveType(Object.class, null);
        ref = new JavaType(null, null, false, false, null, null, null);
        
        type.setReference(ref);
        
        sb = new StringBuilder();
        type.getGenericSignature(sb);
        
        Assert.assertEquals("", sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        // Typical test case: referenced type has erased signature
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(null, null, false, false, null, null, null);
        ref.setErasedSignature(new StringBuilder("Ljava/lang/Object;"));
        
        type.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        
        Assert.assertEquals("Ljava/lang/Object;", sb.toString());
        
        // Edge case: referenced type does not have erased signature
        type = new ResolvedRecursiveType(Object.class, null);
        ref = new JavaType(null, null, false, false, null, null, null);
        
        type.setReference(ref);
        
        sb = new StringBuilder();
        type.getErasedSignature(sb);
        
        Assert.assertEquals("", sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        // Typical test case: return the current object without any modification
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = new JavaType(null, null, false, false, null, null, null);
        
        JavaType newType = type.withContentType(contentType);
        
        Assert.assertEquals(type, newType);
    }
    
    @Test
    public void testWithTypeHandler() {
        // Typical test case: return the current object without any modification
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        
        JavaType newType = type.withTypeHandler(handler);
        
        Assert.assertEquals(type, newType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        // Typical test case: return the current object without any modification
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        
        JavaType newType = type.withContentTypeHandler(handler);
        
        Assert.assertEquals(type, newType);
    }
    
    @Test
    public void testWithValueHandler() {
        // Typical test case: return the current object without any modification
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        
        JavaType newType = type.withValueHandler(handler);
        
        Assert.assertEquals(type, newType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        // Typical test case: return the current object without any modification
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        
        JavaType newType = type.withContentValueHandler(handler);
        
        Assert.assertEquals(type, newType);
    }
    
    @Test
    public void testWithStaticTyping() {
        // Typical test case: return the current object without any modification
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        
        JavaType newType = type.withStaticTyping();
        
        Assert.assertEquals(type, newType);
    }
    
    @Test
    public void testNarrow() {
        // Typical test case: return the current object without any modification
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        
        JavaType newType = type._narrow(String.class);
        
        Assert.assertEquals(type, newType);
    }
    
    @Test
    public void testRefine() {
        // Typical test case: return null as per implementation
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType superClass = new JavaType(null, null, false, false, null, null, null);
        JavaType[] superInterfaces = new JavaType[0];
        
        JavaType refinedType = type.refine(Object.class, null, superClass, superInterfaces);
        
        Assert.assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        // Typical test case: return false
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        
        Assert.assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        // Typical test case: referenced type is not null
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(null, null, false, false, null, null, null);
        ref.setRawClass(String.class);
        
        type.setReference(ref);
        
        String expected = "[recursive type; java.lang.String]";
        
        Assert.assertEquals(expected, type.toString());
        
        // Edge case: referenced type is null
        type = new ResolvedRecursiveType(Object.class, null);
        
        expected = "[recursive type; UNRESOLVED]";
        
        Assert.assertEquals(expected, type.toString());
    }
    
    @Test
    public void testEquals() {
        // Typical test case: same class and same referenced type
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = new JavaType(null, null, false, false, null, null, null);
        
        type1.setReference(ref1);
        
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref2 = new JavaType(null, null, false, false, null, null, null);
        
        type2.setReference(ref2);
        
        Assert.assertTrue(type1.equals(type2));
        
        // Typical test case: different class
        type1 = new ResolvedRecursiveType(Object.class, null);
        ref1 = new JavaType(null, null, false, false, null, null, null);
        
        type1.setReference(ref1);
        
        type2 = new ResolvedRecursiveType(String.class, null);
        ref2 = new JavaType(null, null, false, false, null, null, null);
        
        type2.setReference(ref2);
        
        Assert.assertFalse(type1.equals(type2));
        
        // Typical test case: different referenced types
        type1 = new ResolvedRecursiveType(Object.class, null);
        ref1 = new JavaType(null, null, false, false, null, null, null);
        ref1.setRawClass(String.class);
        
        type1.setReference(ref1);
        
        type2 = new ResolvedRecursiveType(Object.class, null);
        ref2 = new JavaType(null, null, false, false, null, null, null);
        ref2.setRawClass(Integer.class);
        
        type2.setReference(ref2);
        
        Assert.assertFalse(type1.equals(type2));
        
        // Typical test case: null referenced type
        type1 = new ResolvedRecursiveType(Object.class, null);
        
        type2 = new ResolvedRecursiveType(Object.class, null);
        
        Assert.assertFalse(type1.equals(type2));
    }
}