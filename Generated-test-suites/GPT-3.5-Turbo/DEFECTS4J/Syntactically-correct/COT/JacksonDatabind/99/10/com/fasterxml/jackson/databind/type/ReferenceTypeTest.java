
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testWithContentType_sameType_returnThis() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        JavaType contentType = new SimpleType(String.class);
        
        // Act
        JavaType result = referenceType.withContentType(contentType);
        
        // Assert
        assertEquals(referenceType, result);
    }
    
    @Test
    public void testWithContentType_differentType_returnNewReferenceType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        JavaType contentType = new SimpleType(Integer.class);
        
        // Act
        JavaType result = referenceType.withContentType(contentType);
        
        // Assert
        assertNotEquals(referenceType, result);
    }
    
    @Test
    public void testWithTypeHandler_sameHandler_returnThis() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        Object typeHandler = new Object();
        
        // Act
        JavaType result = referenceType.withTypeHandler(typeHandler);
        
        // Assert
        assertEquals(referenceType, result);
    }
    
    @Test
    public void testWithTypeHandler_differentHandler_returnNewReferenceType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        Object typeHandler = new Object();
        
        // Act
        JavaType result = referenceType.withTypeHandler(typeHandler);
        
        // Assert
        assertNotEquals(referenceType, result);
    }
    
    @Test
    public void testWithContentTypeHandler_sameHandler_returnThis() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        Object handler = new Object();
        
        // Act
        JavaType result = referenceType.withContentTypeHandler(handler);
        
        // Assert
        assertEquals(referenceType, result);
    }
    
    @Test
    public void testWithContentTypeHandler_differentHandler_returnNewReferenceType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        Object handler = new Object();
        
        // Act
        JavaType result = referenceType.withContentTypeHandler(handler);
        
        // Assert
        assertNotEquals(referenceType, result);
    }
    
    @Test
    public void testWithAnchorType_sameType_returnThis() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        JavaType anchorType = new SimpleType(String.class);
        
        // Act
        JavaType result = referenceType.withAnchorType(anchorType);
        
        // Assert
        assertEquals(referenceType, result);
    }
    
    @Test
    public void testWithAnchorType_differentType_returnNewReferenceType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        JavaType anchorType = new SimpleType(Integer.class);
        
        // Act
        JavaType result = referenceType.withAnchorType(anchorType);
        
        // Assert
        assertNotEquals(referenceType, result);
    }
    
    @Test
    public void testWithStaticTyping_alreadyStatic_returnThis() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, true);
        
        // Act
        JavaType result = referenceType.withStaticTyping();
        
        // Assert
        assertEquals(referenceType, result);
    }
    
    @Test
    public void testWithStaticTyping_notStatic_returnNewReferenceType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        
        // Act
        JavaType result = referenceType.withStaticTyping();
        
        // Assert
        assertNotEquals(referenceType, result);
    }
    
    @Test
    public void testRefine_correctParameters_returnNewReferenceType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        // Act
        JavaType result = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        
        // Assert
        assertEquals(referenceType, result);
    }
    
    @Test
    public void testGetContentType_returnReferencedType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        
        // Act
        JavaType result = referenceType.getContentType();
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGetReferencedType_returnReferencedType() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        
        // Act
        JavaType result = referenceType.getReferencedType();
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testHasContentType_returnTrue() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        
        // Act
        boolean result = referenceType.hasContentType();
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsReferenceType_returnTrue() {
        // Arrange
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, null, null, null, null, false);
        
        // Act
        boolean result = referenceType.isReferenceType();
        
        // Assert
        assertTrue(result);
    }
}
