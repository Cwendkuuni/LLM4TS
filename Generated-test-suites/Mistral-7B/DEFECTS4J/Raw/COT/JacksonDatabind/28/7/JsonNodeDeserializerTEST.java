package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.PojoNode;
import com.fasterxml.jackson.databind.node.RawValueNode;

public class JsonNodeDeserializerTest {

    private static JsonFactory jsonFactory;
    private static ObjectMapper mapper;
    private static JsonNodeFactory nodeFactory;

    @BeforeClass
    public static void setup() throws Exception {
        jsonFactory = new JsonFactory();
        mapper = new ObjectMapper();
        nodeFactory = mapper.getNodeFactory();
    }

    @Test
    public void testGetDeserializer() throws IOException {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertNotNull(deserializer);
        assertEquals(ObjectNode.class, deserializer.getValueClass());

        deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertNotNull(deserializer);
        assertEquals(ArrayNode.class, deserializer.getValueClass());

        deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        assertNotNull(deserializer);
        assertEquals(JsonNode.class, deserializer.getValueClass());
    }

    @Test
    public void testGetNullValue() throws IOException {
        JsonNode result = JsonNodeDeserializer.instance.getNullValue(mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals(NodeType.NULL, result.getNodeType());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        String jsonObject = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = jsonFactory.createParser(jsonObject);

        ObjectNode result = (ObjectNode) JsonNodeDeserializer.instance.deserialize(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        String jsonArray = "[1, 2, 3]";
        JsonParser parser = jsonFactory.createParser(jsonArray);

        ArrayNode result = (ArrayNode) JsonNodeDeserializer.instance.deserialize(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        String jsonInvalid = "\"invalidJson\"";
        JsonParser parser = jsonFactory.createParser(jsonInvalid);

        JsonNodeDeserializer.instance.deserialize(parser, mapper.getDeserializationContext(), nodeFactory);
    }

    @Test
    public void testDeserializeAny_String() throws IOException {
        String jsonAnyString = "\"Hello World\"";
        JsonParser parser = jsonFactory.createParser(jsonAnyString);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals("Hello World", result.asText());
    }

    @Test
    public void testDeserializeAny_ObjectNode() throws IOException {
        String jsonAnyObject = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = jsonFactory.createParser(jsonAnyObject);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(NodeType.OBJECT, result.getNodeType());

        ObjectNode objResult = (ObjectNode) result;
        assertEquals("John", objResult.get("name").asText());
        assertEquals(30, objResult.get("age").asInt());
    }

    @Test
    public void testDeserializeAny_ArrayNode() throws IOException {
        String jsonAnyArray = "[1, 2, 3]";
        JsonParser parser = jsonFactory.createParser(jsonAnyArray);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(NodeType.ARRAY, result.getNodeType());

        ArrayNode arrResult = (ArrayNode) result;
        assertEquals(3, arrResult.size());
        assertEquals(1, arrResult.get(0).asInt());
        assertEquals(2, arrResult.get(1).asInt());
        assertEquals(3, arrResult.get(2).asInt());
    }

    @Test
    public void testDeserializeAny_NumberInt() throws IOException {
        String jsonAnyInt = "1";
        JsonParser parser = jsonFactory.createParser(jsonAnyInt);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(1, result.asInt());
    }

    @Test
    public void testDeserializeAny_NumberFloat() throws IOException {
        String jsonAnyFloat = "3.14";
        JsonParser parser = jsonFactory.createParser(jsonAnyFloat);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(3.14d, result.asDouble(), 0.001d);
    }

    @Test
    public void testDeserializeAny_True() throws IOException {
        String jsonAnyTrue = "true";
        JsonParser parser = jsonFactory.createParser(jsonAnyTrue);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(true, result.asBoolean());
    }

    @Test
    public void testDeserializeAny_False() throws IOException {
        String jsonAnyFalse = "false";
        JsonParser parser = jsonFactory.createParser(jsonAnyFalse);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(false, result.asBoolean());
    }

    @Test
    public void testDeserializeAny_Null() throws IOException {
        String jsonAnyNull = "null";
        JsonParser parser = jsonFactory.createParser(jsonAnyNull);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(NodeType.NULL, result.getNodeType());
    }

    @Test
    public void testDeserializeAny_BinaryNode() throws IOException {
        byte[] binaryData = new byte[] { 0x1, 0x2, 0x3 };

        String jsonAnyBinary = "{\"binary\":[" + new String(new String(new sun.misc.BASE64Encoder().encode(binaryData))) + "]}";
        JsonParser parser = jsonFactory.createParser(jsonAnyBinary);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(NodeType.BINARY, result.getNodeType());

        BinaryNode binaryResult = (BinaryNode) result;
        assertNotNull(binaryResult.getData());
        assertEquals(binaryData.length, binaryResult.getData().length);
        for (int i = 0; i < binaryData.length; i++) {
            assertEquals(binaryData[i], binaryResult.getData()[i]);
        }
    }

    @Test
    public void testDeserializeAny_PojoNode() throws IOException {
        String jsonAnyPojo = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = jsonFactory.createParser(jsonAnyPojo);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(NodeType.POJO, result.getNodeType());

        PojoNode pojoResult = (PojoNode) result;
        assertEquals("John", pojoResult.get("name").asText());
        assertEquals(30, pojoResult.get("age").asInt());
    }

    @Test
    public void testDeserializeAny_RawValueNode() throws IOException {
        String jsonAnyRawValue = "{\"rawValue\":\"Hello World\"}";
        JsonParser parser = jsonFactory.createParser(jsonAnyRawValue);

        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(result);
        assertEquals(NodeType.TEXT, result.getNodeType());

        TextNode textResult = (TextNode) result;
        assertEquals("Hello World", textResult.asText());

        RawValueNode rawValueResult = (RawValueNode) JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), nodeFactory);
        assertNotNull(rawValueResult);
        assertEquals("Hello World", rawValueResult.getValueAsString());
    }
}