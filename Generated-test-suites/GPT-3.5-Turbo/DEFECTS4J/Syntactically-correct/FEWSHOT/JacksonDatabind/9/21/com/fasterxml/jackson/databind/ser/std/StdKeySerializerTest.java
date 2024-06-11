package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
	    
    private StdKeySerializer serializer;
    private JsonGenerator jgen;
    private SerializerProvider provider;
    
    @Before
    public void setUp() {
        serializer = new StdKeySerializer();
        jgen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }
    
    @Test
    public void testSerialize_Date() throws IOException {
        Date date = new Date();
        
        when(provider.defaultSerializeDateKey(date, jgen)).thenReturn(null);
        
        serializer.serialize(date, jgen, provider);
        
        verify(provider).defaultSerializeDateKey(date, jgen);
    }
    
    @Test
    public void testSerialize_NotDate() throws IOException {
        Object value = new Object();
        String expected = value.toString();
        
        serializer.serialize(value, jgen, provider);
        
        verify(jgen).writeFieldName(expected);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expected = createSchemaNode("string");
        
        JsonNode result = serializer.getSchema(provider, null);
        
        assertEquals(expected, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
    }

    private JsonNode createSchemaNode(String string) {
        return null;
    }
}