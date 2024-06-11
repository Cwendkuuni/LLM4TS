package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());
        
        JavaType ref = new JavaType(SomeClass.class);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
        
        try {
            type.setReference(ref);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, e.getMessage());
        }
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        
        assertSame(sb, result);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        
        assertSame(sb, result);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        JavaType contentType = new JavaType(SomeClass.class);
        JavaType result = type.withContentType(contentType);
        
        assertSame(type, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        
        assertSame(type, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        
        assertSame(type, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        
        assertSame(type, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        
        assertSame(type, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        JavaType result = type.withStaticTyping();
        
        assertSame(type, result);
    }
    
    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        JavaType result = type._narrow(SomeClass.class);
        
        assertSame(type, result);
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
        
        boolean result = type.isContainerType();
        
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        
        String result = type.toString();
        
        assertEquals("[recursive type; UNRESOLVED]", result);
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        
        type1.setReference(new JavaType(SomeClass.class));
        assertFalse(type1.equals(type2));
        
        type2.setReference(new JavaType(SomeClass.class));
        assertTrue(type1.equals(type2));
    }
}