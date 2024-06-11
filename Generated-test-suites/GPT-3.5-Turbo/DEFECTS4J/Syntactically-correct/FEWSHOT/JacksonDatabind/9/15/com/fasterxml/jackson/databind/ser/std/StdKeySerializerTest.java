package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

public class StdKeySerializerTest {
    
    @Test
    public void testSerializeDate() throws IOException {
        Object value = new Date();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        serializer.serialize(value, jgen, provider);
        
        verify(provider).defaultSerializeDateKey((Date) value, jgen);
        verifyNoMoreInteractions(jgen, provider);
    }
    
    @Test
    public void testSerializeOtherObject() throws IOException {
        Object value = "test";
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        serializer.serialize(value, jgen, provider);
        
        verify(jgen).writeFieldName((String) value);
        verifyNoMoreInteractions(jgen, provider);
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        assertEquals("string", result.asText());
        verifyNoMoreInteractions(provider, typeHint);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        StdKeySerializer serializer = new StdKeySerializer();
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
        verifyNoMoreInteractions(visitor, typeHint);
    }
}