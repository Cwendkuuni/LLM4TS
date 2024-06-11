package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date(1620739200000L); // May 12, 2021
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        serializer.serialize(date, generator, provider);
        
        verify(provider).defaultSerializeDateKey(date, generator);
    }
    
    @Test
    public void testSerializeString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "test";
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        serializer.serialize(str, generator, provider);
        
        verify(generator).writeFieldName(str);
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        JsonNode node = serializer.getSchema(provider, String.class);
        
        assertEquals("string", node.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
    }
}