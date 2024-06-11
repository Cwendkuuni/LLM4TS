package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    private ResolvedRecursiveType resolvedRecursiveType;
    
    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
    }
    
    @Test
    public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        String result = resolvedRecursiveType.toString();
        assertTrue(result.contains("UNRESOLVED"));
        
        resolvedRecursiveType.setReference(new JavaType());
        result = resolvedRecursiveType.toString();
        assertTrue(result.contains("JavaType"));
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, null);
        assertTrue(resolvedRecursiveType.equals(other));
        
        other.setReference(new JavaType());
        assertFalse(resolvedRecursiveType.equals(other));
    }
}