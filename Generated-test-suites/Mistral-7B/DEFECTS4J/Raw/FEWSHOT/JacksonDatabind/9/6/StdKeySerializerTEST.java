package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Before;
import org.junit.Test;

public class StdKeySerializerTest {
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        Date date = new Date();

        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider().getDefaultSerializerProvider(), 0);
        serializer.serialize(date, gen, provider);
        gen.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputFunction().getOutput();
        String json = new String(outputStream.toByteArray());

        assertEquals("\"@JsonKey(\"value\")\"", json);
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        String str = "test";

        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider().getDefaultSerializerProvider(), 0);
        serializer.serialize(str, gen, provider);
        gen.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputFunction().getOutput();
        String json = new String(outputStream.toByteArray());

        assertEquals("\"test\"", json);
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = objectMapper.getSerializerProvider();

        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider().getDefaultSerializerProvider(), 0);
        serializer.serialize(null, gen, provider);
        gen.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputFunction().getOutput();
        String json = new String(outputStream.toByteArray());

        assertNull(json);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().getDefaultVisitors().findJsonFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), Object.class.getType());

        assertEquals("null", schema.toString());
    }
}