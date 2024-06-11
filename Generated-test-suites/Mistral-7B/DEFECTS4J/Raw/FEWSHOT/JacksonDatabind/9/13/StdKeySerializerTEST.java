package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtil.convertValueToType;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();

        JsonGenerator gen = mapper.getFactory().createGenerator(new StringBuilder(), null);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, gen, null);
        gen.flush();

        ObjectNode tree = mapper.readTree(gen.getOutputStream());
        assertEquals("\"@date\"", tree.get("@type").asText());
        assertEquals("\"2023-03-15 12:34:56.789\"", tree.get("@value").asText());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String key = "foo";

        JsonGenerator gen = mapper.getFactory().createGenerator(new StringBuilder(), null);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(key, gen, null);
        gen.flush();

        ObjectNode tree = mapper.readTree(gen.getOutputStream());
        assertEquals("foo", tree.get("foo").asText());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(null, mapper.getFactory().createGenerator(root, null), null);
        root.remove("@type");

        assertEquals("null", root.get("null").asText());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectNode tree = factory.createObjectNode();
        JsonFormatVisitorWrapper visitor = factory.createJsonFormatVisitor(tree, null);

        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        assertEquals("STRING", visitor.getCurrentName());
    }
}