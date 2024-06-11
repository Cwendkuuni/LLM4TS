import com.fasterxml.jackson.databind.JavaType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResolvedRecursiveTypeTest {

    @Test
    void setReference_shouldSetCorrectReference() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType reference = new JavaType();
        
        // Act
        resolvedRecursiveType.setReference(reference);
        JavaType actualReference = resolvedRecursiveType.getSelfReferencedType();
        
        // Assert
        assertEquals(reference, actualReference);
    }
    
    @Test
    void setReference_shouldThrowExceptionOnMultipleCalls() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType reference1 = new JavaType();
        JavaType reference2 = new JavaType();
        resolvedRecursiveType.setReference(reference1);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            resolvedRecursiveType.setReference(reference2);
        });
        assertEquals("Trying to re-set self reference; old value = " + reference1 + ", new = " + reference2,
                exception.getMessage());
    }
    
    @Test
    void getGenericSignature_shouldReturnSameSignatureAsReferencedType() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        
        // Act
        StringBuilder actualSignature = resolvedRecursiveType.getGenericSignature(sb);
        
        // Assert
        assertEquals(referencedType.getGenericSignature(sb), actualSignature);
    }
    
    @Test
    void getErasedSignature_shouldReturnSameSignatureAsReferencedType() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        
        // Act
        StringBuilder actualSignature = resolvedRecursiveType.getErasedSignature(sb);
        
        // Assert
        assertEquals(referencedType.getErasedSignature(sb), actualSignature);
    }
    
    @Test
    void withContentType_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        
        // Act
        JavaType actualType = resolvedRecursiveType.withContentType(contentType);
        
        // Assert
        assertSame(resolvedRecursiveType, actualType);
    }
    
    @Test
    void withTypeHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        // Act
        JavaType actualType = resolvedRecursiveType.withTypeHandler(handler);
        
        // Assert
        assertSame(resolvedRecursiveType, actualType);
    }
    
    @Test
    void withContentTypeHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        // Act
        JavaType actualType = resolvedRecursiveType.withContentTypeHandler(handler);
        
        // Assert
        assertSame(resolvedRecursiveType, actualType);
    }
    
    @Test
    void withValueHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        // Act
        JavaType actualType = resolvedRecursiveType.withValueHandler(handler);
        
        // Assert
        assertSame(resolvedRecursiveType, actualType);
    }
    
    @Test
    void withContentValueHandler_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        // Act
        JavaType actualType = resolvedRecursiveType.withContentValueHandler(handler);
        
        // Assert
        assertSame(resolvedRecursiveType, actualType);
    }
    
    @Test
    void withStaticTyping_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType actualType = resolvedRecursiveType.withStaticTyping();
        
        // Assert
        assertSame(resolvedRecursiveType, actualType);
    }
    
    @Test
    void narrow_shouldReturnSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = String.class;
        
        // Act
        JavaType actualType = resolvedRecursiveType._narrow(subclass);
        
        // Assert
        assertSame(resolvedRecursiveType, actualType);
    }

    @Test
    void refine_shouldReturnNull() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType actualType = resolvedRecursiveType.refine(null, null, null, null);
        
        // Assert
        assertNull(actualType);
    }
    
    @Test
    void isContainerType_shouldReturnFalse() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        // Act
        boolean isContainer = resolvedRecursiveType.isContainerType();
        
        // Assert
        assertFalse(isContainer);
    }
    
    @Test
    void toString_shouldReturnCorrectStringRepresentation() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        // Act
        String actualString = resolvedRecursiveType.toString();
        
        // Assert
        assertEquals("[recursive type; " + referencedType.getRawClass().getName() + "]", actualString);
    }
    
    @Test
    void equals_shouldReturnTrueForSameInstance() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        // Act & Assert
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        OtherClass otherClass = new OtherClass();
        
        // Act & Assert
        assertFalse(resolvedRecursiveType.equals(otherClass));
    }
    
    @Test
    void equals_shouldReturnFalseForNull() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        // Act & Assert
        assertFalse(resolvedRecursiveType.equals(null));
    }
    
    @Test
    void equals_shouldReturnFalseForUnresolvedReferences() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        
        // Act & Assert
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
    
    @Test
    void equals_shouldReturnFalseForDifferentReferencedTypes() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType referencedType1 = new JavaType();
        JavaType referencedType2 = new JavaType();
        resolvedRecursiveType1.setReference(referencedType1);
        resolvedRecursiveType2.setReference(referencedType2);
        
        // Act & Assert
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
    
    @Test
    void equals_shouldReturnTrueForSameReferencedTypes() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedRecursiveType1.setReference(referencedType);
        resolvedRecursiveType2.setReference(referencedType);
        
        // Act & Assert
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
    
    private class OtherClass {}

}