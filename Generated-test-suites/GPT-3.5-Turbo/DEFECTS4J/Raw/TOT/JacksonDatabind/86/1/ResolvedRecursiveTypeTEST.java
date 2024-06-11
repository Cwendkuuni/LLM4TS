import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference_validInput_setsReference() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        
        // Act
        type.setReference(ref);
        
        // Assert
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_calledMultipleTimes_throwsIllegalStateException() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        
        // Act
        type.setReference(ref);
        type.setReference(ref);
        
        // Assert
        // IllegalStateException should be thrown
    }

    @Test
    public void testGetGenericSignature_returnsStringBuilderWithGenericSignature() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        type.setReference(ref);
        
        // Act
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        
        // Assert
        assertNotNull(result);
        assertEquals(result.toString(), ref.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature_returnsStringBuilderWithErasedSignature() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        type.setReference(ref);
        
        // Act
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        
        // Assert
        assertNotNull(result);
        assertEquals(result.toString(), ref.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType_returnsThis() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType result = type.withContentType(null);
        
        // Assert
        assertEquals(result, type);
    }

    @Test
    public void testWithTypeHandler_returnsThis() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType result = type.withTypeHandler(null);
        
        // Assert
        assertEquals(result, type);
    }

    @Test
    public void testWithContentTypeHandler_returnsThis() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType result = type.withContentTypeHandler(null);
        
        // Assert
        assertEquals(result, type);
    }

    @Test
    public void testWithValueHandler_returnsThis() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType result = type.withValueHandler(null);
        
        // Assert
        assertEquals(result, type);
    }

    @Test
    public void testWithContentValueHandler_returnsThis() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType result = type.withContentValueHandler(null);
        
        // Assert
        assertEquals(result, type);
    }

    @Test
    public void testWithStaticTyping_returnsThis() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        
        // Act
        JavaType result = type.withStaticTyping();
        
        // Assert
        assertEquals(result, type);
    }

    @Test
    public void test_narrow_returnsThis() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = null; 
        
        // Act
        JavaType result = type._narrow(subclass);
        
        // Assert
        assertEquals(result, type);
    }

    @Test
    public void testRefine_returnsNull() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        // Act
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testIsContainerType_returnsFalse() {
        // Arrange
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        
        // Act
        boolean result = type.isContainerType();
        
        // Assert
        assertFalse(result);
    }

}