package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withTypeHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withContentTypeHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withValueHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withContentValueHandler(h);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = String.class;
        JavaType result = type._narrow(subclass);
        assertEquals(type, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        StringBuilder sb1 = new StringBuilder();
        sb1.append("[recursive type; UNRESOLVED]");
        assertEquals(sb1.toString(), type1.toString());
        
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[recursive type; java.lang.String]");
        assertEquals(sb2.toString(), type2.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        assertFalse(type1.equals(null));
        
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        assertTrue(type1.equals(type2));
        
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(null, null);
        type3.setReference(new JavaType());
        assertFalse(type1.equals(type3));
    }
}