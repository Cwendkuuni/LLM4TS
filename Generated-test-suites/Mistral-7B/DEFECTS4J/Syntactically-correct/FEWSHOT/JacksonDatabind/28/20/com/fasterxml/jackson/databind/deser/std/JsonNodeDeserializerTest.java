package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class JsonNodeDeserializerTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testJsonNodeDeserializer_getDeserializer() {
        JsonNode expectedObjectNode = MAPPER.valueToTree("{\"foo\": 1, \"bar\": [2, 3]}");
        JsonNode expectedArrayNode = MAPPER.valueToTree("[4, 5, 6]");

        JsonDeserializer<JsonNode> deserializerObjectNode = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonDeserializer<JsonNode> deserializerArrayNode = JsonNodeDeserializer.getDeserializer(ArrayNode.class);

        assertEquals(expectedObjectNode.getClass(), deserializerObjectNode.deserialize(MAPPER, new JsonFactory(), null).getClass());
        assertEquals(expectedArrayNode.getClass(), deserializerArrayNode.deserialize(MAPPER, new JsonFactory(), null).getClass());
    }

    @Test
    public void testJsonNodeDeserializer_deserializeNullValue() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        JsonNode result = deserializer.deserialize(MAPPER.getFactory(), MAPPER, null);
        assertNull(result);
    }

    @Test
    public void testJsonNodeDeserializer_deserializeObject() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        String jsonString = "{\"foo\": 1, \"bar\": [2, 3], \"baz\": null}";
        JsonNode expectedResult = MAPPER.readTree(jsonString);

        JsonNode result = deserializer.deserialize(MAPPER.getFactory(), MAPPER, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJsonNodeDeserializer_deserializeArray() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        String jsonString = "[\"apple\", \"banana\", \"orange\"]";
        JsonNode expectedResult = MAPPER.readTree(jsonString);

        JsonNode result = deserializer.deserialize(MAPPER.getFactory(), MAPPER, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJsonNodeDeserializer_deserializeAny() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        String jsonStringObject = "{\"foo\": 1, \"bar\": 2.5}";
        String jsonStringArray = "[\"apple\", 1, null]";
        String jsonStringObjectWithArray = "{\"foo\": [1, 2], \"bar\": 3}";

        JsonNode expectedObjectResult = MAPPER.readTree(jsonStringObject);
        JsonNode expectedArrayResult = MAPPER.readTree(jsonStringArray);
        JsonNode expectedObjectWithArrayResult = MAPPER.readTree(jsonStringObjectWithArray);

        JsonNode objectResult = deserializer.deserialize(MAPPER.getFactory(), MAPPER, null, ObjectNode.class);
        JsonNode arrayResult = deserializer.deserialize(MAPPER.getFactory(), MAPPER, null, ArrayNode.class);
        JsonNode objectWithArrayResult = deserializer.deserialize(MAPPER.getFactory(), MAPPER, null);

        assertEquals(expectedObjectResult, objectResult);
        assertEquals(expectedArrayResult, arrayResult);
        assertEquals(expectedObjectWithArrayResult, objectWithArrayResult);
    }
}