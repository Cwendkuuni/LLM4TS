package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        
        assertNotNull(resolvedType);
        assertEquals(Integer.class, resolvedType.getRawClass());
        assertNull(resolvedType.getTypeHandler());
        assertNotNull(resolvedType.getTypeBindings());
        assertNull(resolvedType.getErasedSignature(new StringBuilder()));
    }
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(Object.class);
        resolvedType.setReference(ref);
        
        assertEquals(ref, resolvedType.getSelfReferencedType());
        
        // Should throw IllegalStateException when trying to set reference multiple times
        try {
            resolvedType.setReference(new JavaType(Double.class));
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = "+ ref +", new = " + new JavaType(Double.class), e.getMessage());
        }
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Double.class, new TypeBindings());
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Float.class, new TypeBindings());
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Long.class, new TypeBindings());
        
        JavaType contentType = new JavaType(Boolean.class);
        JavaType result = resolvedType.withContentType(contentType);
        
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Byte.class, new TypeBindings());
        
        Object handler = new Object();
        JavaType result = resolvedType.withTypeHandler(handler);
        
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Short.class, new TypeBindings());
        
        Object handler = new Object();
        JavaType result = resolvedType.withContentTypeHandler(handler);
        
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Double.class, new TypeBindings());
        
        Object handler = new Object();
        JavaType result = resolvedType.withValueHandler(handler);
        
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Float.class, new TypeBindings());
        
        Object handler = new Object();
        JavaType result = resolvedType.withContentValueHandler(handler);
        
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Character.class, new TypeBindings());
        
        JavaType result = resolvedType.withStaticTyping();
        
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        
        Class<?> subclass = resolvedType.getRawClass();
        JavaType result = resolvedType._narrow(subclass);
        
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Number.class, new TypeBindings());
        
        Class<?> rawType = resolvedType.getRawClass();
        TypeBindings bindings = resolvedType.getTypeBindings();
        JavaType superClass = new JavaType(String.class);
        JavaType[] superInterfaces = new JavaType[0];
        
        JavaType result = resolvedType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        assertFalse(resolvedType.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        String result = resolvedType.toString();
        
        assertEquals("[recursive type; java.lang.String]", result);
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(Double.class, new TypeBindings());
        selfRefType.setReference(selfRefType);

        ResolvedRecursiveType sameRefType = new ResolvedRecursiveType(Float.class, new TypeBindings());
        sameRefType.setReference(selfRefType);
        
        ResolvedRecursiveType differentRefType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        differentRefType.setReference(new JavaType(Boolean.class));
        
        assertTrue(selfRefType.equals(selfRefType));
        assertFalse(selfRefType.equals(null));
        assertFalse(selfRefType.equals("test"));
        assertFalse(selfRefType.equals(sameRefType));
        assertFalse(selfRefType.equals(differentRefType));
    }
}