
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertSame(sb, result);
        assertNotNull(result.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertSame(sb, result);
        assertNotNull(result.toString());
    }
    
    @Test
    public void testWithContentType() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.withContentType(ref);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.withTypeHandler(ref);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.withContentTypeHandler(ref);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.withValueHandler(ref);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.withContentValueHandler(ref);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void test_narrow() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType._narrow(String.class);
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        JavaType result = resolvedRecursiveType.refine(String.class, new TypeBindings(), null, null);
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        boolean result = resolvedRecursiveType.isContainerType();
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(null);
        String result = resolvedRecursiveType.toString();
        assertNotNull(result);
    }
    
    @Test
    public void testEquals() {
        JavaType ref1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType2.setReference(ref2);
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        assertFalse(result);
    }
}
