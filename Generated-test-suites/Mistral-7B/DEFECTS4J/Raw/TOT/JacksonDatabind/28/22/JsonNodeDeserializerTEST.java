package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.testng.Assert;
import org.testng.annotations.*;

@Test
public class JsonNodeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setup() {
        // Set up ObjectMapper for JSON parsing
        mapper.registerModule(new SimpleModule()
                .addDeserializer(ObjectNode.class, ObjectDeserializer.getInstance())
                .addDeserializer(ArrayNode.class, ArrayDeserializer.getInstance())
                .addDeserializer(JsonNode.class, JsonNodeDeserializer.getInstance())
        );
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        String json = "null";
        JsonNode expected = JsonNodeFactory.instance.nullNode();
        JsonNode actual = mapper.readValue(json, JsonNode.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        ObjectNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.textNode("John"))
                .put("age", JsonNodeFactory.instance.numberNode(30))
                .put("city", JsonNodeFactory.instance.textNode("New York"));
        JsonNode actual = mapper.readValue(json, JsonNode.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        String json = "{}";
        JsonNode expected = JsonNodeFactory.instance.objectNode();
        JsonNode actual = mapper.readValue(json, JsonNode.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        String json = "[1,2,\"three\",4.5]";
        ArrayNode expected = JsonNodeFactory.instance.arrayNode()
                .add(JsonNodeFactory.instance.numberNode(1))
                .add(JsonNodeFactory.instance.numberNode(2))
                .add(JsonNodeFactory.instance.textNode("three"))
                .add(JsonNodeFactory.instance.numberNode(4.5));
        JsonNode actual = mapper.readValue(json, JsonNode.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        JsonNode actual = mapper.readValue(json, JsonNode.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithDuplicateKeys() throws IOException {
        String json = "{\"name\":\"John\",\"name\":\"Doe\"}";
        JsonNode actual = mapper.readValue(json, JsonNode.class);
        BaseNodeDeserializer<?> deserializer = JsonNodeDeserializer.getInstance();
        deserializer._handleDuplicateField(JsonParser.nullParser(), mapper.getDeserializationContext(), JsonNodeFactory.instance,
                "name", (ObjectNode) actual, null, actual.get(0));

        ObjectNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.textNode("Doe"));
        Assert.assertEquals(expected, actual);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        String json = "invalidJson";
        mapper.readValue(json, JsonNode.class);
    }
}