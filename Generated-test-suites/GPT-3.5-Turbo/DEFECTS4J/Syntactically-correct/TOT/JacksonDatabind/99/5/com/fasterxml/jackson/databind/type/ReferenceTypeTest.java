
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        // create a new ReferenceType instance for each test case
        referenceType = ReferenceType.construct(String.class, JavaType.STRING);
    }

    // Test cases for withContentType method

    @Test
    public void withContentType_shouldReturnNewReferenceTypeWithDifferentContentType() {
        // Arrange
        JavaType newContentType = JavaType.INT;
        
        // Act
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        
        // Assert
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    // Test cases for withTypeHandler method

    @Test
    public void withTypeHandler_shouldReturnNewReferenceTypeWithDifferentTypeHandler() {
        // Arrange
        Object newTypeHandler = new Object();
        
        // Act
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        
        // Assert
        assertNotSame(referenceType, newReferenceType);
        assertSame(newTypeHandler, newReferenceType.getTypeHandler());
    }

    // Test cases for withContentTypeHandler method

    @Test
    public void withContentTypeHandler_shouldReturnNewReferenceTypeWithDifferentContentTypeHandler() {
        // Arrange
        Object newContentTypeHandler = new Object();
        
        // Act
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        
        // Assert
        assertNotSame(referenceType, newReferenceType);
        assertSame(newContentTypeHandler, newReferenceType.getContentType().getTypeHandler());
    }

    // Test cases for withValueHandler method

    @Test
    public void withValueHandler_shouldReturnNewReferenceTypeWithDifferentValueHandler() {
        // Arrange
        Object newValueHandler = new Object();
        
        // Act
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        
        // Assert
        assertNotSame(referenceType, newReferenceType);
        assertSame(newValueHandler, newReferenceType.getValueHandler());
    }

    // Test cases for withContentValueHandler method

    @Test
    public void withContentValueHandler_shouldReturnNewReferenceTypeWithDifferentContentValueHandler() {
        // Arrange
        Object newContentValueHandler = new Object();
        
        // Act
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        
        // Assert
        assertNotSame(referenceType, newReferenceType);
        assertSame(newContentValueHandler, newReferenceType.getContentType().getValueHandler());
    }

    // Test cases for withStaticTyping method

    @Test
    public void withStaticTyping_shouldReturnNewReferenceTypeWithStaticTyping() {
        // Arrange
        
        // Act
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        
        // Assert
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStaticTyping());
    }

    // Test cases for refine method

    @Test
    public void refine_shouldReturnNewReferenceTypeWithRefinedParameters() {
        // Arrange
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.OBJECT;
        JavaType[] superInterfaces = {};
        
        // Act
        ReferenceType newReferenceType = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        
        // Assert
        assertNotSame(referenceType, newReferenceType);
        assertSame(rawType, newReferenceType.getRawClass());
        assertSame(bindings, newReferenceType.getBindings());
        assertSame(superClass, newReferenceType.getSuperClass());
        assertArrayEquals(superInterfaces, newReferenceType.getInterfaces());
    }

    // Test cases for getAnchorType method

    @Test
    public void getAnchorType_shouldReturnAnchorType() {
        // Arrange
        
        // Act
        
        // Assert
        assertSame(referenceType, referenceType.getAnchorType());
    }

    // Test cases for isAnchorType method

    @Test
    public void isAnchorType_shouldReturnTrueWhenReferenceTypeIsAnchorType() {
        // Arrange
        
        // Act
        
        // Assert
        assertTrue(referenceType.isAnchorType());
    }

    // Test cases for toString method

    @Test
    public void toString_shouldReturnStringRepresentation() {
        // Arrange
        String expectedString = "[reference type, class java.lang.String<java.lang.String>]";
        
        // Act
        String actualString = referenceType.toString();
        
        // Assert
        assertEquals(expectedString, actualString);
    }

    // Test cases for equals method

    @Test
    public void equals_shouldReturnTrueWhenSameReferenceTypeInstance() {
        // Arrange
        
        // Act
        
        // Assert
        assertEquals(referenceType, referenceType);
    }

    @Test
    public void equals_shouldReturnTrueWhenReferenceTypeInstancesAreEqual() {
        // Arrange
        ReferenceType other = ReferenceType.construct(String.class, JavaType.STRING);
        
        // Act
        
        // Assert
        assertEquals(referenceType, other);
    }

    @Test
    public void equals_shouldReturnFalseWhenDifferentClasses() {
        // Arrange
        ReferenceType other = ReferenceType.construct(Integer.class, JavaType.STRING);
        
        // Act
        
        // Assert
        assertNotEquals(referenceType, other);
    }

    @Test
    public void equals_shouldReturnFalseWhenDifferentReferencedTypes() {
        // Arrange
        ReferenceType other = ReferenceType.construct(String.class, JavaType.INT);
        
        // Act
        
        // Assert
        assertNotEquals(referenceType, other);
    }

    @Test
    public void equals_shouldReturnFalseWhenNull() {
        // Arrange
        
        // Act
        
        // Assert
        assertNotEquals(referenceType, null);
    }

    @Test
    public void equals_shouldReturnFalseWhenDifferentClass() {
        // Arrange
        
        // Act
        
        // Assert
        assertNotEquals(referenceType, "Not a ReferenceType");
    }

}

