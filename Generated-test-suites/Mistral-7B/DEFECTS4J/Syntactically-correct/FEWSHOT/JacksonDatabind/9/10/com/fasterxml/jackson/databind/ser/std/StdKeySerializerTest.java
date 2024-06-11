package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(date, gen, mapper.getSerializerProvider());
        gen.flush();

        String output = System.out.getUnicodeStream().asString();
        assertNotNull(output);
        assertEquals("\"$date\"", output);
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String value = "example";
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();

        String output = System.out.getUnicodeStream().asString();
        assertNotNull(output);
        assertEquals("\"example\"", output);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FORMAT_STRINGS, Object.class);

        serializer.acceptJsonFormatVisitor(visitor, null);

        ObjectNode schema = (ObjectNode) visitor.getOutput();
        assertEquals("string", schema.get("type").asText());
    }
}