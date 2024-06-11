package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class JsonNodeDeserializerTest {

    private JsonNodeFactory nodeFactory;

    @Before
    public void setup() {
        nodeFactory = new StandardJsonNodeFactory();
    }

    @Test
    public void testGetDeserializer() {
        JsonNode expectedObjectNode = nodeFactory.objectNode();
        JsonNode expectedArrayNode = nodeFactory.arrayNode();

        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertEquals(ObjectNode.class, deserializer.getType());
        JsonNode deserializedObjectNode = deserializer.deserialize(null, new TestContext(), nodeFactory);
        assertEquals(expectedObjectNode, deserializedObjectNode);

        deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertEquals(ArrayNode.class, deserializer.getType());
        JsonNode deserializedArrayNode = deserializer.deserialize(null, new TestContext(), nodeFactory);
        assertEquals(expectedArrayNode, deserializedArrayNode);

        deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        assertEquals(JsonNode.class, deserializer.getType());
        JsonNode deserializedJsonNode = deserializer.deserialize(null, new TestContext(), nodeFactory);
        assertTrue(deserializedJsonNode instanceof JsonNode);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonNode expectedNullNode = JsonNodeFactory.getInstance().nullNode();
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode deserializedNode = deserializer.deserialize(null, new TestContext(), nodeFactory);
        assertEquals(expectedNullNode, deserializedNode);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectNode expectedObjectNode = nodeFactory.objectNode();
        expectedObjectNode.put("field1", nodeFactory.textNode("value1"));
        expectedObjectNode.put("field2", nodeFactory.numberNode(123));

        JsonParser parser = createParser("{\"field1\":\"value1\",\"field2\":123}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode deserializedNode = deserializer.deserialize(parser, new TestContext(), nodeFactory);

        JsonNode deserializedObject = ((BaseNodeDeserializer) deserializer).deserializeObject(parser, new TestContext(), nodeFactory);
        assertEquals(expectedObjectNode, deserializedObject);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ArrayNode expectedArrayNode = nodeFactory.arrayNode();
        expectedArrayNode.add(nodeFactory.numberNode(1));
        expectedArrayNode.add(nodeFactory.numberNode(2));

        JsonParser parser = createParser("[1,2]");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode deserializedNode = deserializer.deserialize(parser, new TestContext(), nodeFactory);

        JsonNode deserializedArray = ((BaseNodeDeserializer) deserializer).deserializeArray(parser, new TestContext(), nodeFactory);
        assertEquals(expectedArrayNode, deserializedArray);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonParser parser = createParser("{'name':'John Doe'}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode deserializedNode = deserializer.deserialize(parser, new TestContext(), nodeFactory);

        assertTrue(deserializedNode instanceof JsonNode);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser parser = createParser("{invalid:JSON}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode deserializedNode = deserializer.deserialize(parser, new TestContext(), nodeFactory);
    }

    private JsonParser createParser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()));
    }
}

class TestContext extends DeserializationContext {
    @Override
    public JsonNodeFactory getNodeFactory() {
        return nodeFactory;
    }

    @Override
    public ObjectWriter getObjectWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectReader getObjectReader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypeDeserializer getDeserializer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeserializationConfig getConfig() {
        throw new UnsupportedOperationException();
    }
}