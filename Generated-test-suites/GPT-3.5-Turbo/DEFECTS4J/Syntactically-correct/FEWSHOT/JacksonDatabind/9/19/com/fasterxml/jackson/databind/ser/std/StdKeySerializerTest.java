package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

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

public class StdKeySerializerTest {

    @Test
    public void testSerialize_withDate_shouldSerializeDateKey() throws IOException {
        JsonGenerator jgen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        Date date = new Date();

        StdKeySerializer keySerializer = new StdKeySerializer();
        
        keySerializer.serialize(date, jgen, provider);
        
        // Assert serialization result
        assertEquals(date, jgen.getCurrentValue());
    }

    @Test
    public void testSerialize_withNonDate_shouldSerializeToString() throws IOException {
        JsonGenerator jgen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        Object obj = new Object();

        StdKeySerializer keySerializer = new StdKeySerializer();
        
        keySerializer.serialize(obj, jgen, provider);
        
        // Assert serialization result
        assertEquals(obj.toString(), jgen.getCurrentValue());
    }

    @Test
    public void testGetSchema_shouldReturnStringSchemaNode() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = String.class;

        StdKeySerializer keySerializer = new StdKeySerializer();
        
        JsonNode schema = keySerializer.getSchema(provider, typeHint);
        
        // Assert schema type
        assertEquals("string", schema.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldExpectStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = JavaType.STRING;

        StdKeySerializer keySerializer = new StdKeySerializer();
        
        keySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert expected format visitor
        assertEquals(JavaType.STRING, visitor.getExpectedFormat());
    }
}