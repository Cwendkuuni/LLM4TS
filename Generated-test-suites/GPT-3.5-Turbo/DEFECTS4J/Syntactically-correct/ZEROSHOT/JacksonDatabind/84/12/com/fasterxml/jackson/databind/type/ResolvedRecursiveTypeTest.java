package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType type;
    
    @Before
    public void setUp() {
        type = new ResolvedRecursiveType(String.class, null);
    }
    
    @Test
    public void testSetReference() {
        JavaType ref = new SimpleType(Object.class);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = new SimpleType(Object.class);
        JavaType ref2 = new SimpleType(String.class);
        type.setReference(ref1);
        type.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new SimpleType(Object.class);
        type.setReference(ref);
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(ref.getGenericSignature(sb), result);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new SimpleType(Object.class);
        type.setReference(ref);
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(ref.getErasedSignature(sb), result);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(Object.class);
        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertEquals(type, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        JavaType result = type._narrow(subclass);
        assertEquals(type, result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        boolean result = type.isContainerType();
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        type.setReference(null);
        String expected = "[recursive type; UNRESOLVED]";
        String result = type.toString();
        assertEquals(expected, result);
        
        type.setReference(new SimpleType(Object.class));
        expected = "[recursive type; java.lang.Object]";
        result = type.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testEquals() {
        JavaType ref1 = new SimpleType(Object.class);
        JavaType ref2 = new SimpleType(String.class);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        type1.setReference(ref1);
        type2.setReference(ref2);
        
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        assertFalse(type1.equals(type2));
        
        type2.setReference(ref1);
        
        assertTrue(type1.equals(type2));
    }
}