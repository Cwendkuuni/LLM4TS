package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonNodeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonNodeDeserializer() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30}");
        JsonNode expectedObjectNode = mapper.readValue(parser, JsonNode.class);
        JsonNode actualObjectNode = deserializer.deserialize(parser, createContext());

        Assertions.assertEquals(expectedObjectNode, actualObjectNode);

        parser = mapper.getFactory().createParser("[1, 2, 3]");
        JsonNode expectedArrayNode = mapper.readValue(parser, JsonNode.class);
        JsonNode actualArrayNode = deserializer.deserialize(parser, createContext());

        Assertions.assertEquals(expectedArrayNode, actualArrayNode);

        parser = mapper.getFactory().createParser("[]");
        JsonNode emptyNode = deserializer.deserialize(parser, createContext());

        Assertions.assertNotNull(emptyNode);
        Assertions.assertTrue(emptyNode instanceof JsonNode);

        parser = mapper.getFactory().createParser("null");
        JsonNode nullNode = deserializer.deserialize(parser, createContext());

        Assertions.assertNotNull(nullNode);
        Assertions.assertTrue(nullNode instanceof JsonNull);

        parser = mapper.getFactory().createParser("true");
        JsonNode booleanNode = deserializer.deserialize(parser, createContext());

        Assertions.assertTrue(booleanNode.isBoolean());
        Assertions.assertTrue((boolean) booleanNode);

        parser = mapper.getFactory().createParser("false");
        JsonNode falseNode = deserializer.deserialize(parser, createContext());

        Assertions.assertTrue(falseNode.isBoolean());
        Assertions.assertFalse((boolean) falseNode);

        parser = mapper.getFactory().createParser("null");
        JsonNode nullValue = deserializer.getNullValue(createContext());

        Assertions.assertNotNull(nullValue);
        Assertions.assertTrue(nullValue instanceof JsonNull);

        JsonParser wrongParser = mapper.getFactory().createParser("[]");
        wrongParser.nextToken(); // Move past [

        try {
            deserializer.deserialize(wrongParser, createContext());
            Assertions.fail();
        } catch (JsonProcessingException e) {
            // Expected
        }

        JsonParser objectParser = mapper.getFactory().createParser("{\"name\":\"John\"}");
        objectParser.nextToken(); // Move past {

        try {
            deserializer.deserialize(objectParser, createContext());
            Assertions.fail();
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    private DeserializationContext createContext() {
        return mapper.getDeserializationContext();
    }
}