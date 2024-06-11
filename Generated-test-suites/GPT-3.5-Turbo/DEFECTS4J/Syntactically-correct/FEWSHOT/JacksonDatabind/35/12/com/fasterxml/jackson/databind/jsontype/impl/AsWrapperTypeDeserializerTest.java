package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class AsWrapperTypeDeserializerTest {
    
    private AsWrapperTypeDeserializer deserializer;
    private DeserializationContext mockContext;
    private JsonParser mockParser;
    
    @Before
    public void setup() {
        JavaType javaType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        
        deserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl);
        mockContext = mock(DeserializationContext.class);
        mockParser = mock(JsonParser.class);
    }
    
    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer result = deserializer.forProperty(prop);
        assertEquals(deserializer, result);
        verify(prop).getClass();
    }
    
    @Test
    public void testGetTypeInclusion() {
        As result = deserializer.getTypeInclusion();
        assertEquals(As.WRAPPER_OBJECT, result);
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        when(mockParser.canReadTypeId()).thenReturn(true);
        when(mockParser.getTypeId()).thenReturn("test");
        
        Object result = deserializer.deserializeTypedFromObject(mockParser, mockContext);
        
        verify(deserializer).deserialize(mockParser, mockContext);
        verify(mockParser).canReadTypeId();
        verify(mockParser).getTypeId();
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        Object result = deserializer.deserializeTypedFromArray(mockParser, mockContext);
        
        verify(deserializer).deserialize(mockParser, mockContext);
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        Object result = deserializer.deserializeTypedFromScalar(mockParser, mockContext);
        
        verify(deserializer).deserialize(mockParser, mockContext);
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = deserializer.deserializeTypedFromAny(mockParser, mockContext);
        
        verify(deserializer).deserialize(mockParser, mockContext);
        assertEquals(null, result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeThrowExceptionIfTokenNotStartObject() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        
        deserializer.deserializeTypedFromObject(mockParser, mockContext);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeThrowExceptionIfTokenNotFieldName() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        
        deserializer.deserializeTypedFromObject(mockParser, mockContext);
    }
    
    @Test
    public void testDeserializeIfTypeIdVisible() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.END_OBJECT);
        when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("test");
        
        Object result = deserializer.deserializeTypedFromObject(mockParser, mockContext);
        
        verify(mockParser).nextToken();
        verify(mockParser).getTypeId();
        verify(deserializer).deserialize(mockParser, mockContext);
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeIfNativeTypeId() throws IOException {
        when(mockParser.canReadTypeId()).thenReturn(true);
        when(mockParser.getTypeId()).thenReturn(null);
        
        Object result = deserializer.deserializeTypedFromObject(mockParser, mockContext);
        
        verify(deserializer).deserialize(mockParser, mockContext);
        verify(mockParser).canReadTypeId();
        verify(mockParser).getTypeId();
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeIfClosingEndObjectNotFound() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.START_OBJECT);
        when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("test");
        
        try {
            deserializer.deserializeTypedFromObject(mockParser, mockContext);
        } catch (JsonMappingException e) {
            assertEquals("expected closing END_OBJECT after type information and deserialized value", e.getMessage());
        }
    }
}