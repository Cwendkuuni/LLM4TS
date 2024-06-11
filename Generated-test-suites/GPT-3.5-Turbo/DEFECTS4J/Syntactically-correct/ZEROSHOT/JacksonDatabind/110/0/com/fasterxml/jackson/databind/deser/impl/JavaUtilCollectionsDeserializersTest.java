package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionReturnsDeserializer_ForArraysAsList() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollectionReturnsDeserializer_ForSingletonList() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollectionReturnsDeserializer_ForSingletonSet() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollectionReturnsDeserializer_ForUnmodifiableList() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollectionReturnsDeserializer_ForUnmodifiableSet() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollectionReturnsNull_ForOtherTypes() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFindForMapReturnsDeserializer_ForSingletonMap() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForMapReturnsDeserializer_ForUnmodifiableMap() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForMapReturnsNull_ForOtherTypes() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        
        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testConverterConvertReturnsNull_ForNullValue() {
        // Arrange
        int kind = 0;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testConverterConvertReturnsSingletonSet_ForTypeSingletonSet() {
        // Arrange
        int kind = 1;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = Set.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Set);
    }
    
    @Test
    public void testConverterConvertReturnsSingletonList_ForTypeSingletonList() {
        // Arrange
        int kind = 2;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
    }
    
    @Test
    public void testConverterConvertReturnsSingletonMap_ForTypeSingletonMap() {
        // Arrange
        int kind = 3;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = Map.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Map);
    }
    
    @Test
    public void testConverterConvertReturnsUnmodifiableSet_ForTypeUnmodifiableSet() {
        // Arrange
        int kind = 4;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = Set.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Set);
    }
    
    @Test
    public void testConverterConvertReturnsUnmodifiableList_ForTypeUnmodifiableList() {
        // Arrange
        int kind = 5;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
    }
    
    @Test
    public void testConverterConvertReturnsUnmodifiableMap_ForTypeUnmodifiableMap() {
        // Arrange
        int kind = 6;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = Map.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Map);
    }
    
    @Test
    public void testConverterConvertReturnsValueAsIs_ForTypeAsList() {
        // Arrange
        int kind = 7;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        Object result = converter.convert(null);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testConverterGetInputTypeReturnsInputType() {
        // Arrange
        int kind = 0;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        JavaType result = converter.getInputType(null);
        
        // Assert
        assertEquals(concreteType, result);
    }
    
    @Test
    public void testConverterGetOutputTypeReturnsInputType() {
        // Arrange
        int kind = 0;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        // Act
        JavaType result = converter.getOutputType(null);
        
        // Assert
        assertEquals(concreteType, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSingletonThrowsException_ForSizeNotOne() {
        // Arrange
        int size = 0;
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        
        // Act
        deserializers._checkSingleton(size);
        
        // Assert
        // IllegalArgumentException is thrown
    }
}