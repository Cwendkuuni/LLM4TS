package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setup() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
    }
    
    @Test
    public void testSetReference_ValidReference_ReferenceSetSuccessfully() {
        // Arrange
        JavaType ref = new JavaType();

        // Act
        resolvedRecursiveType.setReference(ref);

        // Assert
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference_MultipleSetReference_ThrowsIllegalStateException() {
        // Arrange
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        // Act
        resolvedRecursiveType.setReference(ref);

        // Assert - Expect IllegalStateException to be thrown
    }
    
    @Test
    public void testGetGenericSignature_NoReferencedType_ReturnsEmptyStringBuilder() {
        // Arrange
        
        // Act
        StringBuilder result = resolvedRecursiveType.getGenericSignature(new StringBuilder());

        // Assert
        Assert.assertTrue(result.toString().isEmpty());
    }
    
    @Test
    public void testGetGenericSignature_WithReferencedType_ReturnsStringBuilderWithGenericSignature() {
        // Arrange
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        // Act
        StringBuilder result = resolvedRecursiveType.getGenericSignature(new StringBuilder());

        // Assert
        Assert.assertFalse(result.toString().isEmpty());
        // Additional Assert based on expected generic signature
    }
    
    @Test
    public void testGetErasedSignature_NoReferencedType_ReturnsEmptyStringBuilder() {
        // Arrange
        
        // Act
        StringBuilder result = resolvedRecursiveType.getErasedSignature(new StringBuilder());

        // Assert
        Assert.assertTrue(result.toString().isEmpty());
    }
    
    @Test
    public void testGetErasedSignature_WithReferencedType_ReturnsStringBuilderWithErasedSignature() {
        // Arrange
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        // Act
        StringBuilder result = resolvedRecursiveType.getErasedSignature(new StringBuilder());

        // Assert
        Assert.assertFalse(result.toString().isEmpty());
        // Additional Assert based on expected erased signature
    }
    
    @Test
    public void testWithContentType_ReturnsSameInstance() {
        // Arrange
        JavaType contentType = new JavaType();

        // Act
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler_ReturnsSameInstance() {
        // Arrange
        Object handler = new Object();

        // Act
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler_ReturnsSameInstance() {
        // Arrange
        Object handler = new Object();

        // Act
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler_ReturnsSameInstance() {
        // Arrange
        Object handler = new Object();

        // Act
        JavaType result = resolvedRecursiveType.withValueHandler(handler);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler_ReturnsSameInstance() {
        // Arrange
        Object handler = new Object();

        // Act
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping_ReturnsSameInstance() {
        // Arrange
        
        // Act
        JavaType result = resolvedRecursiveType.withStaticTyping();

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void test_narrow_ReturnsSameInstance() {
        // Arrange
        Class<?> subclass = String.class;

        // Act
        JavaType result = resolvedRecursiveType._narrow(subclass);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine_AlwaysReturnsNull() {
        // Arrange
        Class<?> rawType = String.class;
        JavaType[] superInterfaces = {};

        // Act
        JavaType result = resolvedRecursiveType.refine(rawType, null, null, superInterfaces);

        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testIsContainerType_ReturnsFalse() {
        // Arrange
        
        // Act
        boolean result = resolvedRecursiveType.isContainerType();

        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToString_NoReferencedType_ReturnsStringWithUNRESOLVED() {
        // Arrange
        
        // Act
        String result = resolvedRecursiveType.toString();

        // Assert
        Assert.assertTrue(result.contains("UNRESOLVED"));
    }
    
    @Test
    public void testToString_WithReferencedType_ReturnsStringWithReferencedTypeName() {
        // Arrange
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        // Act
        String result = resolvedRecursiveType.toString();

        // Assert
        Assert.assertTrue(result.contains(ref.getRawClass().getName()));
    }
    
    @Test
    public void testEquals_WithEqualObject_ReturnsTrue() {
        // Arrange
        ResolvedRecursiveType equalObject = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        equalObject.setReference(ref);

        // Act
        boolean result = resolvedRecursiveType.equals(equalObject);

        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_WithUnequalObject_ReturnsFalse() {
        // Arrange
        ResolvedRecursiveType unequalObject = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        unequalObject.setReference(new JavaType());

        // Act
        boolean result = resolvedRecursiveType.equals(unequalObject);

        // Assert
        Assert.assertFalse(result);
    }

}