package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        // Arrange
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TokenBuffer tb = mock(TokenBuffer.class);

        // Act
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        
        // Assert
        // Add assertions here
    }
    
    @Test
    public void testDeserializeTypedForId() throws Exception {
        // Arrange
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TokenBuffer tb = mock(TokenBuffer.class);

        // Act
        Object result = deserializer._deserializeTypedForId(p, ctxt, tb);
        
        // Assert
        // Add assertions here
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        // Arrange
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TokenBuffer tb = mock(TokenBuffer.class);

        // Act
        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        
        // Assert
        // Add assertions here
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        // Arrange
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Act
        Object result = deserializer.deserializeTypedFromAny(p, ctxt);
        
        // Assert
        // Add assertions here
    }
}