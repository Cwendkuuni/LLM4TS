package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void setReference_shouldThrowIllegalStateException_whenReferenceAlreadySet() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        resolvedRecursiveType.setReference(null);

        // Assert
        try {
            resolvedRecursiveType.setReference(null);
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void getSelfReferencedType_shouldReturnNull_whenNotSet() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.getSelfReferencedType();

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void getGenericSignature_shouldReturnReferencedTypeGenericSignature() {
        // Arrange
        JavaType referencedType = new JavaType(null, null, false, false);
        referencedType.setGenericSignature(new StringBuilder("genericSignature"));
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(referencedType);

        // Act
        StringBuilder result = resolvedRecursiveType.getGenericSignature(new StringBuilder());

        // Assert
        Assert.assertEquals("genericSignature", result.toString());
    }

    @Test
    public void getErasedSignature_shouldReturnReferencedTypeErasedSignature() {
        // Arrange
        JavaType referencedType = new JavaType(null, null, false, false);
        referencedType.setErasedSignature(new StringBuilder("erasedSignature"));
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(referencedType);

        // Act
        StringBuilder result = resolvedRecursiveType.getErasedSignature(new StringBuilder());

        // Assert
        Assert.assertEquals("erasedSignature", result.toString());
    }

    @Test
    public void withContentType_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.withContentType(null);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withTypeHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.withTypeHandler(null);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withContentTypeHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.withContentTypeHandler(null);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withValueHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.withValueHandler(null);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withContentValueHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.withContentValueHandler(null);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void withStaticTyping_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.withStaticTyping();

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void _narrow_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType._narrow(null);

        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void refine_shouldReturnNull() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        JavaType result = resolvedRecursiveType.refine(null, null, null, null);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void isContainerType_shouldReturnFalse() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        boolean result = resolvedRecursiveType.isContainerType();

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void toString_shouldReturnProperStringRepresentation_whenReferencedTypeIsNull() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        String result = resolvedRecursiveType.toString();

        // Assert
        Assert.assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void equals_shouldReturnTrue_whenSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        boolean result = resolvedRecursiveType.equals(resolvedRecursiveType);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void equals_shouldReturnFalse_whenOtherObjectIsNull() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        boolean result = resolvedRecursiveType.equals(null);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void equals_shouldReturnFalse_whenOtherObjectHasDifferentClass() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        // Act
        boolean result = resolvedRecursiveType.equals("not a ResolvedRecursiveType");

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void equals_shouldReturnFalse_whenReferencedTypeIsNull() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);

        // Act
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void equals_shouldReturnFalse_whenReferencedTypeIsNotEqual() {
        // Arrange
        JavaType referencedType1 = new JavaType(null, null, false, false);
        JavaType referencedType2 = new JavaType(null, null, false, false);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(referencedType1);
        resolvedRecursiveType2.setReference(referencedType2);

        // Act
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void equals_shouldReturnTrue_whenReferencedTypeIsEqual() {
        // Arrange
        JavaType referencedType = new JavaType(null, null, false, false);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(referencedType);
        resolvedRecursiveType2.setReference(referencedType);

        // Act
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);

        // Assert
        Assert.assertTrue(result);
    }

}