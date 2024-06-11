package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToStandardJavaType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        ObjectNode rootNode = writer.writeValueAsObject(Map.of("key", date));
        JsonNode keyNode = rootNode.get("key");

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        Map<String, Object> map = new ObjectMapper().readValue(parser, new TypeReference<Map<String, Object>>() {});
        Object key = map.get("key");

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(key, gen, new SerializerProvider());
        gen.flush();

        assertEquals(keyNode.asText(), key.toString());
    }

    @Test
    public void testSerializeString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String key = "test";

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        ObjectNode rootNode = writer.writeValueAsObject(Map.of("key", key));
        JsonNode keyNode = rootNode.get("key");

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        Map<String, Object> map = new ObjectMapper().readValue(parser, new TypeReference<Map<String, Object>>() {});
        Object keyObj = map.get("key");

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(keyObj, gen, new SerializerProvider());
        gen.flush();

        assertEquals(keyNode.asText(), keyObj.toString());
    }

    @Test
    public void testGetSchema() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(new SerializerProvider(), Object.class.getType());

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode expectedSchema = JsonNodeFactory.instance.textNode("string");

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializationConfig(), new TypeReference<JsonFormat<Object, ObjectNode>>() {});
        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());

        assertEquals(expectedSchema, visitor.getCurrentOutput());
    }
}