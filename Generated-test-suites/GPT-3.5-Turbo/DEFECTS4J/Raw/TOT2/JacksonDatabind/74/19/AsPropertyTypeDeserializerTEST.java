package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(JavaType.unknownType(), mock(TypeIdResolver.class), "type", true, JavaType.unknownType(), As.PROPERTY);
        
        TokenBuffer tokenBuffer = new TokenBuffer(new ObjectMapper().getFactory());
        tokenBuffer.writeString("typeValue");
        
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("typeValue");
        
        Object result = deserializer.deserializeTypedFromObject(jsonParser, mock(DeserializationContext.class));
        
        assertEquals("Type not deserialized correctly", "typeValue", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(JavaType.unknownType(), mock(TypeIdResolver.class), "type", true, JavaType.unknownType(), As.PROPERTY);
        
        TokenBuffer tokenBuffer = new TokenBuffer(new ObjectMapper().getFactory());
        tokenBuffer.writeString("typeValue");
        
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("typeValue");
        
        Object result = deserializer._deserializeTypedUsingDefaultImpl(jsonParser, mock(DeserializationContext.class), tokenBuffer);
        
        assertEquals("Type not deserialized correctly using default implementation", "typeValue", result);
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(JavaType.unknownType(), mock(TypeIdResolver.class), "type", true, JavaType.unknownType(), As.PROPERTY);
        
        TokenBuffer tokenBuffer = new TokenBuffer(new ObjectMapper().getFactory());
        tokenBuffer.writeString("typeValue");
        
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("typeValue");
        
        Object result = deserializer._deserializeTypedForId(jsonParser, mock(DeserializationContext.class), tokenBuffer);
        
        assertEquals("Type not deserialized correctly for Id", "typeValue", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(JavaType.unknownType(), mock(TypeIdResolver.class), "type", true, JavaType.unknownType(), As.PROPERTY);
        
        TokenBuffer tokenBuffer = new TokenBuffer(new ObjectMapper().getFactory());
        tokenBuffer.writeString("typeValue");
        
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY).thenReturn(JsonToken.FIELD_NAME);
        
        Object result = deserializer.deserializeTypedFromAny(jsonParser, mock(DeserializationContext.class));
        
        assertEquals("Type not deserialized correctly from any", "typeValue", result);
    }
}