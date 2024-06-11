package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, bindings);
        
        assertEquals(String.class, resolvedRecursiveType.getRawClass());
        assertEquals(bindings, resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getValueHandler());
        assertNull(resolvedRecursiveType.getContentValueHandler());
        assertFalse(resolvedRecursiveType.isContainerType());
        assertFalse(resolvedRecursiveType.isCollectionLikeType());
        assertFalse(resolvedRecursiveType.isArrayType());
        assertFalse(resolvedRecursiveType.isEnumType());
        assertFalse(resolvedRecursiveType.isFinal());
        assertFalse(resolvedRecursiveType.isInterface());
        assertFalse(resolvedRecursiveType.isMapLikeType());
        assertFalse(resolvedRecursiveType.isPrimitive());
        assertFalse(resolvedRecursiveType.isReferenceType());
        assertNull(resolvedRecursiveType.getContentType());
        assertNull(resolvedRecursiveType.getContentTypeHandler());
        assertNull(resolvedRecursiveType.getContentValueHandler());
        assertEquals(0, resolvedRecursiveType.getBindings().getTypeParameters().size());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
        
        JavaType ref2 = new SimpleType(Integer.class);
        resolvedRecursiveType.setReference(ref2);
        
        assertEquals(ref2, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(ref.getGenericSignature().length(), sb.length());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        
        assertEquals(ref.getErasedSignature().length(), sb.length());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        
        JavaType result = resolvedRecursiveType.withContentType(ref);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withTypeHandler(null);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(null);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withValueHandler(null);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(null);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testEquals() {
        TypeBindings bindings1 = new TypeBindings();
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, bindings1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, bindings1);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Integer.class, bindings1);
        ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(String.class, bindings2);
        
        resolvedRecursiveType1.setReference(new SimpleType(String.class));
        resolvedRecursiveType2.setReference(new SimpleType(String.class));
        resolvedRecursiveType4.setReference(new SimpleType(String.class));
        resolvedRecursiveType4.setReference(new SimpleType(String.class));
        
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType4));
    }
}