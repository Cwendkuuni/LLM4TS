package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@SuppressWarnings("serial")
public class StdKeySerializerTest {
    @Test
    public void testSerializeWithDateValue() throws IOException {
        // Arrange
        Date date = new Date();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.serialize(date, jgen, provider);
        
        // Assert
        verify(provider).defaultSerializeDateKey(date, jgen);
    }
    
    @Test
    public void testSerializeWithStringValue() throws IOException {
        // Arrange
        String value = "test";
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.serialize(value, jgen, provider);
        
        // Assert
        verify(jgen).writeFieldName(value);
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("string", result.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }
}