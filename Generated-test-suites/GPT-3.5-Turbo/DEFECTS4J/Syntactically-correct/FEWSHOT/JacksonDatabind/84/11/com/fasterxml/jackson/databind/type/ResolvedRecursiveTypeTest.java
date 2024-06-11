package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        
        JavaType ref = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
        
        try {
            resolvedRecursiveType.setReference(new ResolvedRecursiveType(null, null));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + new ResolvedRecursiveType(null, null), e.getMessage());
        }
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new ResolvedRecursiveType(null, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder(40)
            .append("[recursive type; ");
        StringBuilder expected = sb.append("UNRESOLVED").append("]");
        assertEquals(expected.toString(), resolvedRecursiveType.toString());
        
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb2 = new StringBuilder(40)
            .append("[recursive type; ")
            .append(Object.class.getName()).append("]");
        assertEquals(sb2.toString(), resolvedRecursiveType2.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType2));
        
        resolvedRecursiveType2.setReference(new ResolvedRecursiveType(null, null));
        assertFalse(resolvedRecursiveType.equals(resolvedRecursiveType2));
    }
}