package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
  
    private ResolvedRecursiveType resolvedRecursiveType;
    
    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }
    
    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(ref.getGenericSignature(sb), result);
        assertSame(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(ref.getErasedSignature(sb), result);
        assertSame(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = ResolvedRecursiveType.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
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
    public void testToStringUnresolved() {
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testToStringResolved() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        String expected = "[recursive type; java.lang.String]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testEqualsSameInstance() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    public void testEqualsNull() {
        assertFalse(resolvedRecursiveType.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }
    
    @Test
    public void testEqualsUnresolved() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        assertFalse(resolvedRecursiveType.equals(other));
    }
    
    @Test
    public void testEqualsResolvedDifferentReference() {
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(ref1);
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        other.setReference(ref2);
        assertFalse(resolvedRecursiveType.equals(other));
    }
    
    @Test
    public void testEqualsResolvedSameReference() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        other.setReference(ref);
        assertTrue(resolvedRecursiveType.equals(other));
    }
}