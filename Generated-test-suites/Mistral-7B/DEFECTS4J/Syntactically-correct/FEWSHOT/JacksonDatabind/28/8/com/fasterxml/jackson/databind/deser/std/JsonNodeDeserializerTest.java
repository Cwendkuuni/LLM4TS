package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ObjectDeserializer);
        
        deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ArrayDeserializer);
        
        deserializer = JsonNodeDeserializer.getDeserializer(String.class);
        assertNotNull(deserializer);
        assertFalse(deserializer instanceof BaseNodeDeserializer);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser jsonParser = createParser("{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}");
        ObjectNode objectNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, new ObjectMapper().getDeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertEquals("John", objectNode.get("name").asText());
        assertEquals(30, objectNode.get("age").asInt());
        ArrayNode hobbiesNode = (ArrayNode) objectNode.get("hobbies");
        assertEquals(2, hobbiesNode.size());
        assertEquals("reading", hobbiesNode.get(0).asText());
        assertEquals("swimming", hobbiesNode.get(1).asText());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser jsonParser = createParser("[1, 2, 3, 4]");
        ArrayNode arrayNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, new ObjectMapper().getDeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertEquals(4, arrayNode.size());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
        assertEquals(4, arrayNode.get(3).asInt());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonParser jsonParser = createParser("\"Hello World\"");
        JsonNode jsonNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, new ObjectMapper().getDeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertEquals("Hello World", jsonNode.asText());
        
        jsonParser = createParser("42");
        jsonNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, new ObjectMapper().getDeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertEquals(42, jsonNode.asInt());
        
        jsonParser = createParser("true");
        jsonNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, new ObjectMapper().getDeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertTrue(jsonNode.asBoolean());
        
        jsonParser = createParser("null");
        jsonNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, new ObjectMapper().getDeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
        assertNull(jsonNode);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonParser jsonParser = createParser("null");
        JsonNode jsonNode = JsonNodeDeserializer.getInstance().getNullValue(new ObjectMapper().getDeserializationContext());
        assertNull(jsonNode);
        
        jsonParser = createParser("");
        jsonNode = JsonNodeDeserializer.getInstance().getNullValue(new ObjectMapper().getDeserializationContext());
        assertNull(jsonNode);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser jsonParser = createParser("{INVALID_JSON}");
        JsonNodeDeserializer.getInstance().deserialize(jsonParser, new ObjectMapper().getDeserializationContext(), ObjectMapper.getDefaultInstance().getNodeFactory());
    }

    private JsonParser createParser(String json) throws IOException {
        return new ObjectMapper().parser().readValue(new StringReader(json));
    }

}