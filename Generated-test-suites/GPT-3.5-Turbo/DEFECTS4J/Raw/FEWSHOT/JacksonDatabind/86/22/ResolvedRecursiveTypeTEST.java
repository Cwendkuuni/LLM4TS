package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertNull(type.getContentType());
        assertNull(type.getKeyType());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testSetReference() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        JavaType ref = new SimpleType();
        
        type.setReference(ref);
        
        assertEquals(ref, type.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        JavaType contentType = new SimpleType();
        
        JavaType result = type.withContentType(contentType);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        Object handler = new Object();
        
        JavaType result = type.withTypeHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        Object handler = new Object();
        
        JavaType result = type.withContentTypeHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithValueHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        Object handler = new Object();
        
        JavaType result = type.withValueHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        Object handler = new Object();
        
        JavaType result = type.withContentValueHandler(handler);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        
        JavaType result = type.withStaticTyping();
        
        assertEquals(type, result);
    }
    
    @Deprecated
    @Test
    public void testNarrow() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        Class subclass = String.class;
        
        JavaType result = type._narrow(subclass);
        
        assertEquals(type, result);
    }
    
    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        Class rawType = String.class;
        JavaType superClass = new SimpleType();
        JavaType[] superInterfaces = new JavaType[0];
        
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        
        String result = type.toString();
        
        assertEquals("[recursive type; UNRESOLVED]", result);
    }
    
    @Test
    public void testEquals() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);
        
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        assertFalse(type.equals("Test"));
        
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(String.class, bindings);
        assertTrue(type.equals(otherType));
        otherType.setReference(new SimpleType());
        assertFalse(type.equals(otherType));
    }
}