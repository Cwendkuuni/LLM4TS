package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class StdKeySerializerTest {
    @Test
    public void testSerializeDate() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        serializer.serialize(date, jgen, provider);
        jgen.flush();

        // Verify that default serialization of Date was used
        assertEquals("\"2022-12-31T15:30:45.123Z\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "testString";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        serializer.serialize(str, jgen, provider);
        jgen.flush();

        // Verify that the string was serialized correctly
        assertEquals("\"testString\"", jgen.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JavaType typeHint = JavaType.constructCollectionType(JsonNode.class, String.class);

        JsonNode schema = serializer.getSchema(new SerializerProvider(), typeHint);

        // Verify that the schema correctly identifies the expected format
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(javaType, null);

        serializer.acceptJsonFormatVisitor(visitor, javaType);

        // Verify that the visitor was informed of the expected format
        assertEquals("STRING", visitor.getFormatName());
        assertEquals("std.StdKeySerializer", visitor.getSerializerClass().getName());
    }
}