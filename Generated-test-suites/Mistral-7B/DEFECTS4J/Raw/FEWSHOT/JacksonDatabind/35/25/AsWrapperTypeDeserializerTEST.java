package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.databind.node.JsonNodeType.START_OBJECT;
import static com.fasterxml.jackson.databind.node.JsonNodeType.FIELD_NAME;
import static com.fasterxml.jackson.databind.node.JsonNodeType.STRING;
import static com.fasterxml.jackson.databind.node.JsonNodeType.END_OBJECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer(String.class.getTypeName(), null, null, false, String.class);
        BeanProperty property = new BeanProperty("testProperty", String.class);
        JsonDeserializer<Object> deserializer = wrapperDeserializer.forProperty(property);

        String jsonString = "{\"type\":\"java.lang.String\",\"value\":\"test\"}";
        ObjectNode jsonObject = objectMapper.readValue(jsonString, ObjectNode.class);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = deserializer.deserialize(jsonParser, ctxt);

        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer(String.class.getTypeName(), null, null, false, String.class);

        String jsonString = "[{\"type\":\"java.lang.String\",\"value\":\"test1\"},{\"type\":\"java.lang.String\",\"value\":\"test2\"}]";
        ObjectNode jsonObject = objectMapper.readValue(jsonString, ObjectNode.class);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = wrapperDeserializer.deserializeTypedFromArray(jsonParser, ctxt);

        assertEquals(String[].class, result.getClass());
        assertArrayEquals(new String[]{"test1", "test2"}, (String[]) result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer(String.class.getTypeName(), null, null, false, String.class);

        String jsonString = "\"test\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = wrapperDeserializer.deserializeTypedFromScalar(jsonParser, ctxt);

        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer(String.class.getTypeName(), null, null, false, String.class);

        String jsonString = "{\"type\":\"java.lang.Integer\",\"value\":123}";
        ObjectNode jsonObject = objectMapper.readValue(jsonString, ObjectNode.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = wrapperDeserializer.deserializeTypedFromAny(jsonParser, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer("java.lang.String", null, null, false, String.class);

        String jsonString = "{\"type\":\"java.lang.String\",\"value\":\"test\"}";
        ObjectNode jsonObject = objectMapper.readValue(jsonString, ObjectNode.class);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = wrapperDeserializer.deserializeTypedFromObject(jsonParser, ctxt);

        assertEquals("test", result);
    }

    @Test
    public void testMissingTypeId() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer("java.lang.String", null, null, false, String.class);

        String jsonString = "{\"value\":\"test\"}";
        ObjectNode jsonObject = objectMapper.readValue(jsonString, ObjectNode.class);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        try {
            wrapperDeserializer.deserializeTypedFromObject(jsonParser, ctxt);
            fail("Expected WrongTokenException");
        } catch (IOException e) {
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testMissingFieldName() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer("java.lang.String", null, null, false, String.class);

        String jsonString = "{\"type\":\"java.lang.String\"}";
        ObjectNode jsonObject = objectMapper.readValue(jsonString, ObjectNode.class);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        try {
            wrapperDeserializer.deserializeTypedFromObject(jsonParser, ctxt);
            fail("Expected WrongTokenException");
        } catch (IOException e) {
            assertEquals("need JSON String that contains type id (for subtype of java.lang.String)", e.getMessage());
        }
    }

    @Test
    public void testInvalidJsonFormat() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = objectMapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer wrapperDeserializer = new AsWrapperTypeDeserializer("java.lang.String", null, null, false, String.class);

        String jsonString = "{\"type\":\"java.lang.String\", \"value\":\"test\"}";
        ObjectNode jsonObject = objectMapper.readValue(jsonString, ObjectNode.class);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonObject);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        try {
            wrapperDeserializer.deserializeTypedFromObject(jsonParser, ctxt);
            fail("Expected WrongTokenException");
        } catch (IOException e) {
            JsonToken expectedToken = JsonToken.START_OBJECT;
            JsonToken actualToken = jsonParser.getCurrentToken();
            assertEquals("expected closing END_OBJECT after type information and deserialized value", expectedToken.id(), actualToken.id());
        }
    }
}