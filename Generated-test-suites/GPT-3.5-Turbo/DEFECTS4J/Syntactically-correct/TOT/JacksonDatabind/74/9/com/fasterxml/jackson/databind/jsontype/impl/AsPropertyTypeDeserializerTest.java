package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject_WhenCanReadTypeId_ReturnDeserializedObjectWithNativeTypeId() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        when(p.canReadTypeId()).thenReturn(true);
        Object typeId = 1;
        when(p.getTypeId()).thenReturn(typeId);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);

        // Assert
        assertEquals(result, deserializer._deserializeWithNativeTypeId(p, ctxt, typeId));
    }

    @Test
    public void testDeserializeTypedFromObject_WhenCurrentTokenIsStartObject_ReturnDeserializedTypedForId() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.FIELD_NAME);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.getCurrentName()).thenReturn("type");

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);

        // Assert
        assertEquals(result, deserializer._deserializeTypedForId(p, ctxt, null));
    }
    
    @Test
    public void testDeserializeTypedFromObject_WhenCurrentTokenIsFieldnameButTypePropertyNameIsDifferent_ReturnDeserializedTypedUsingDefaultImpl() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.getCurrentName()).thenReturn("name");

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);

        // Assert
        assertEquals(result, deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, null));
    }

    @Test
    public void testDeserializeTypedFromObject_WhenCurrentTokenIsNotStartObject_ReturnDeserializedTypedUsingDefaultImpl() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);

        // Assert
        assertEquals(result, deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, null));
    }

    @Test
    public void testDeserializeTypedForId_ReturnDeserializedObjectWithMatchingType() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        String typeId = "1";
        when(p.getText()).thenReturn(typeId);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer._deserializeTypedForId(p, ctxt, null);

        // Assert
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, typeId);
        assertEquals(result, deser.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenDefaultImplDeserializerExists_ReturnDeserializedObject() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, null);

        // Assert
        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(ctxt);
        assertEquals(result, deser.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenCurrentTokenIsNaturalType_ReturnDeserializedObject() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(p.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, null);

        // Assert
        assertEquals(result, TypeDeserializer.deserializeIfNatural(p, ctxt, deserializer._baseType));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenCurrentTokenIsStartArray_ReturnDeserializedTypedFromAny() throws IOException {
        // Arrange
        JavaType bt = mock(JavaType.class);
        when(bt.getRawClass()).thenReturn(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        // Act
        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, null);

        // Assert
        assertEquals(result, deserializer.deserializeTypedFromAny(p, ctxt));
    }

}