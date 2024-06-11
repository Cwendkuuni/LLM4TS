package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testConstructor() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertEquals(String.class, recursiveType.getRawClass());
        assertFalse(recursiveType.isContainerType());
    }
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType reference = new JavaType();
        
        recursiveType.setReference(reference);
        
        assertEquals(reference, recursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGenericSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType reference = new JavaType();
        StringBuilder sb = new StringBuilder();
        reference.getGenericSignature(sb);
        
        recursiveType.setReference(reference);
        
        StringBuilder genericSignature = new StringBuilder();
        recursiveType.getGenericSignature(genericSignature);
        
        assertEquals(sb.toString(), genericSignature.toString());
    }
    
    @Test
    public void testErasedSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType reference = new JavaType();
        StringBuilder sb = new StringBuilder();
        reference.getErasedSignature(sb);
        
        recursiveType.setReference(reference);
        
        StringBuilder erasedSignature = new StringBuilder();
        recursiveType.getErasedSignature(erasedSignature);
        
        assertEquals(sb.toString(), erasedSignature.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new JavaType();
        
        JavaType withContentType = recursiveType.withContentType(contentType);
        
        assertEquals(recursiveType, withContentType);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType withTypeHandler = recursiveType.withTypeHandler(handler);
        
        assertEquals(recursiveType, withTypeHandler);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType withContentTypeHandler = recursiveType.withContentTypeHandler(handler);
        
        assertEquals(recursiveType, withContentTypeHandler);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType withValueHandler = recursiveType.withValueHandler(handler);
        
        assertEquals(recursiveType, withValueHandler);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType withContentValueHandler = recursiveType.withContentValueHandler(handler);
        
        assertEquals(recursiveType, withContentValueHandler);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType withStaticTyping = recursiveType.withStaticTyping();
        
        assertEquals(recursiveType, withStaticTyping);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> subclass = String.class;
        
        JavaType narrowed = recursiveType._narrow(subclass);
        
        assertEquals(recursiveType, narrowed);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[] {};
        
        JavaType refined = recursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(refined);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertFalse(recursiveType.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType unresolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedType.setReference(new JavaType());
        
        String unresolvedString = unresolvedType.toString();
        String resolvedString = resolvedType.toString();
        
        assertTrue(unresolvedString.contains("UNRESOLVED"));
        assertFalse(resolvedString.contains("UNRESOLVED"));
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        recursiveType.setReference(new JavaType());
        
        assertFalse(recursiveType.equals(null));
        assertFalse(recursiveType.equals(new Object()));
        
        ResolvedRecursiveType sameType = new ResolvedRecursiveType(String.class, new TypeBindings());
        sameType.setReference(new JavaType());
        
        assertTrue(recursiveType.equals(sameType));
        
        ResolvedRecursiveType differentType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        differentType.setReference(new JavaType());
        
        assertFalse(recursiveType.equals(differentType));
    }
}