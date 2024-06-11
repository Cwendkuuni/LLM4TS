package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    private StdKeySerializer serializer = new StdKeySerializer();

    @Test
    public void testSerialize() throws IOException {
        // Test with Date value
        JsonGenerator jgen = new JsonFactory().createJsonGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        Date date = new Date();
        serializer.serialize(date, jgen, provider);
        // Verify output
        assertEquals("Expected string representation of Date", date.toString(), jgen.getCurrentValue().toString());
        jgen.close();

        // Test with non-Date value
        jgen = new JsonFactory().createJsonGenerator(System.out);
        provider = new SerializerProvider();
        Object obj = "test";
        serializer.serialize(obj, jgen, provider);
        // Verify output
        assertEquals("Expected string representation of non-Date object", obj.toString(), jgen.getCurrentValue().toString());
        jgen.close();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = null;
        JsonNode schema = serializer.getSchema(provider, typeHint);
        // Verify schema
        assertNotNull("Schema should not be null", schema);
        assertEquals("Schema value should be 'string'", "string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // No assertions as this is just a void method
    }
}