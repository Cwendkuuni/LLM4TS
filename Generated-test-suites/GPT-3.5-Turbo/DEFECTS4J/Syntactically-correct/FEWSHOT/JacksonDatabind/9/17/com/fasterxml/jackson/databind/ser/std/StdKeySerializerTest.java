package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer = new StdKeySerializer();

    @Test
    public void testSerialize_DateValue() throws IOException {
        JsonGenerator jgen = null;
        SerializerProvider provider = null;
        Date date = new Date();
        
        serializer.serialize(date, jgen, provider);
        
        // assertion
    }
    
    @Test
    public void testSerialize_StringValue() throws IOException {
        JsonGenerator jgen = null;
        SerializerProvider provider = null;
        String string = "Test String";
        
        serializer.serialize(string, jgen, provider);
        
        // assertion
    }
    
    @Test
    public void testSerialize_NullValue() throws IOException {
        JsonGenerator jgen = null;
        SerializerProvider provider = null;
        Object object = null;
        
        serializer.serialize(object, jgen, provider);
        
        // assertion
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // assertion
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // assertion
    }
}