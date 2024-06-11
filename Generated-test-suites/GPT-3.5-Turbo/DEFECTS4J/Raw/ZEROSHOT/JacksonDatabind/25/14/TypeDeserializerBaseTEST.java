package com.fasterxml.jackson.databind.jsontype.impl;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        // Arrange
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "test";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        // Act
        String result = typeDeserializerBase.baseTypeName();
        
        // Assert
        assertEquals(result, "java.lang.Object");
    }
    
    @Test
    public void testGetPropertyName() {
        // Arrange
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "test";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        // Act
        String result = typeDeserializerBase.getPropertyName();
        
        // Assert
        assertEquals(result, "test");
    }
    
    @Test
    public void testGetTypeIdResolver() {
        // Arrange
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "test";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        // Act
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        
        // Assert
        assertTrue(result instanceof TypeIdResolver);
    }
    
    @Test
    public void testGetDefaultImpl() {
        // Arrange
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "test";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        // Act
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        
        // Assert
        assertEquals(result, Object.class);
    }
    
    @Test
    public void testToString() {
        // Arrange
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "test";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        // Act
        String result = typeDeserializerBase.toString();
        
        // Assert
        assertTrue(result.contains("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
    }
    
    @Test
    public void testFindDeserilizer() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "test";
        
        // Act
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, typeId);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test(expected = IOException.class)
    public void testFindDeserilizerNull() throws IOException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = null;
        
        // Act
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, typeId);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testFindDefaultImplDeserilizer() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        
        // Act
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() {
        // Arrange
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Act
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        // Arrange
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object typeId = null;
        
        // Act
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeIdNotNull() {
        // Arrange
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object typeId = "test";
        
        // Act
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testHandleUnknownTypeId() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "test";
        TypeIdResolver idResolver = new TypeIdResolver();
        JavaType baseType = new JavaType();
        
        // Act
        JsonDeserializer<Object> result =typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        
        // Assert
        assertNotNull(result);
    }
}