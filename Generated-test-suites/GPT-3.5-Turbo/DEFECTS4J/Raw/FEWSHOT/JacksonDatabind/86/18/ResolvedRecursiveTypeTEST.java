package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = mock(JavaType.class);
        when(ref.getGenericSignature(sb)).thenReturn(sb);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = mock(JavaType.class);
        when(ref.getErasedSignature(sb)).thenReturn(sb);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        JavaType result = resolvedRecursiveType.withContentType(ref);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = mock(Object.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = mock(Object.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = mock(Object.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = mock(Object.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = mock(Class.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(ref);
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = mock(Class.class);
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType superClass = mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(null, result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        
        boolean result = resolvedRecursiveType.isContainerType();
        
        assertEquals(false, result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        
        String result = resolvedRecursiveType.toString();
        
        assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void testEquals() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType1.setReference(ref);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType2.setReference(ref);
        
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        assertEquals(true, result);
    }

    @Test
    public void testNotEquals_NullReference() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, null);
        
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        assertEquals(false, result);
    }

    @Test
    public void testNotEquals_DifferentClass() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType1.setReference(ref);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Integer.class, null);
        resolvedRecursiveType2.setReference(ref);
        
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        assertEquals(false, result);
    }

    @Test
    public void testNotEquals_DifferentReference() {
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType2.setReference(ref2);
        
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        assertEquals(false, result);
    }
}