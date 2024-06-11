package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        
        assertEquals(ref, type.getSelfReferencedType());
    }
   
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getGenericSignature(sb);
        
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getErasedSignature(sb);
        
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new JavaType();
        JavaType result = type.withContentType(contentType);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType result = type.withStaticTyping();
        
        assertEquals(type, result);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> subclass = String.class;
        JavaType result = type._narrow(subclass);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        String expected = "[recursive type; UNRESOLVED]";
        
        assertEquals(expected, type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type1.setReference(ref);
        type2.setReference(ref);
        
        assertEquals(type1, type2);
    }
}