package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference_WhenCalled_ShouldSetReference() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        
        // Act
        resolvedType.setReference(ref);
        
        // Assert
        assertEquals(ref, resolvedType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference_WhenCalledMultipleTimes_ShouldThrowIllegalStateException() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        // Act
        resolvedType.setReference(ref1);
        resolvedType.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature_WhenCalled_ShouldReturnGenericSignatureOfStringBuilder() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        
        // Act
        StringBuilder result = resolvedType.getGenericSignature(sb);
        
        // Assert
        assertNotNull(result);
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature_WhenCalled_ShouldReturnErasedSignatureOfStringBuilder() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        
        // Act
        StringBuilder result = resolvedType.getErasedSignature(sb);
        
        // Assert
        assertNotNull(result);
        assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType_WhenCalled_ShouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType contentType = new JavaType();
        
        // Act
        JavaType result = resolvedType.withContentType(contentType);
        
        // Assert
        assertNotNull(result);
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithTypeHandler_WhenCalled_ShouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        // Act
        JavaType result = resolvedType.withTypeHandler(handler);
        
        // Assert
        assertNotNull(result);
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithContentTypeHandler_WhenCalled_ShouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        // Act
        JavaType result = resolvedType.withContentTypeHandler(handler);
        
        // Assert
        assertNotNull(result);
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithValueHandler_WhenCalled_ShouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        // Act
        JavaType result = resolvedType.withValueHandler(handler);
        
        // Assert
        assertNotNull(result);
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithContentValueHandler_WhenCalled_ShouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        // Act
        JavaType result = resolvedType.withContentValueHandler(handler);
        
        // Assert
        assertNotNull(result);
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testWithStaticTyping_WhenCalled_ShouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedType.withStaticTyping();
        
        // Assert
        assertNotNull(result);
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testNarrow_WhenCalled_ShouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> subclass = Object.class;
        
        // Act
        JavaType result = resolvedType._narrow(subclass);
        
        // Assert
        assertNotNull(result);
        assertEquals(resolvedType, result);
    }
    
    @Test
    public void testRefine_WhenCalled_ShouldReturnNull() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        // Act
        JavaType result = resolvedType.refine(rawType, bindings, superClass, superInterfaces);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType_WhenCalled_ShouldReturnFalse() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        // Act
        boolean result = resolvedType.isContainerType();
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testToString_WhenReferencedTypeIsNull_ShouldReturnStringWithUNRESOLVED() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedType.setReference(null);
        
        // Act
        String result = resolvedType.toString();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("UNRESOLVED"));
    }
    
    @Test
    public void testToString_WhenCalled_ShouldReturnStringWithTypeName() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        resolvedType.setReference(ref);
        
        // Act
        String result = resolvedType.toString();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains(ref.getRawClass().getName()));
    }
    
    @Test
    public void testEquals_WhenObjectsAreSame_ShouldReturnTrue() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        // Act
        boolean result = resolvedType.equals(resolvedType);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testEquals_WhenObjectIsNull_ShouldReturnFalse() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        // Act
        boolean result = resolvedType.equals(null);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenClassesAreDifferent_ShouldReturnFalse() {
        // Arrange
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        // Act
        boolean result = resolvedType1.equals(resolvedType2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenReferencedTypesAreDifferent_ShouldReturnFalse() {
        // Arrange
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedType2.setReference(new JavaType());
        
        // Act
        boolean result = resolvedType1.equals(resolvedType2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenCalledWithDifferentType_ShouldReturnFalse() {
        // Arrange
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object obj = new Object();
        
        // Act
        boolean result = resolvedType.equals(obj);
        
        // Assert
        assertFalse(result);
    }

}