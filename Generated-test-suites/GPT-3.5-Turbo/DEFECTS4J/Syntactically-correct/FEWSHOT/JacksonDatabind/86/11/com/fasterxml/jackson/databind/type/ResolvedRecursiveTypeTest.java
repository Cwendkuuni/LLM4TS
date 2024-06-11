package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.setReference(null);
        assertNull(type.getSelfReferencedType());

        ResolvedRecursiveType referenceType = new ResolvedRecursiveType(Integer.class, null);
        type.setReference(referenceType);
        assertEquals(referenceType, type.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new ResolvedRecursiveType(Integer.class, null);
        assertEquals(type, type.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        assertEquals(type, type.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        assertEquals(type, type.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        assertEquals(type, type.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        assertEquals(type, type.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> subclass = String.class;
        assertEquals(type, type._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());

        ResolvedRecursiveType referenceType = new ResolvedRecursiveType(Integer.class, null);
        type.setReference(referenceType);
        assertEquals("[recursive type; java.lang.Integer]", type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertTrue(type.equals(type));

        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));

        ResolvedRecursiveType referenceType1 = new ResolvedRecursiveType(Integer.class, null);
        ResolvedRecursiveType referenceType2 = new ResolvedRecursiveType(Double.class, null);
        type.setReference(referenceType1);

        ResolvedRecursiveType otherType1 = new ResolvedRecursiveType(String.class, null);
        otherType1.setReference(referenceType1);
        assertTrue(type.equals(otherType1));

        ResolvedRecursiveType otherType2 = new ResolvedRecursiveType(String.class, null);
        otherType2.setReference(referenceType2);
        assertFalse(type.equals(otherType2));

        ResolvedRecursiveType otherType3 = new ResolvedRecursiveType(Integer.class, null);
        assertFalse(type.equals(otherType3));
    }
}