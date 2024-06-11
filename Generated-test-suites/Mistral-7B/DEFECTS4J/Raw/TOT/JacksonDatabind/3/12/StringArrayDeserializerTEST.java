package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    /**
     * Test deserializing an empty JSON array to an empty String array.
     */
    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException {
        JsonParser jp = createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    /**
     * Test deserializing a JSON array with one element to a String array with one element.
     */
    @Test
    public void testDeserializeSingleElementArray() throws JsonProcessingException {
        JsonParser jp = createParser("['hello']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertEquals("hello", result[0]);
    }

    /**
     * Test deserializing a JSON array with multiple elements to a String array with multiple elements.
     */
    @Test
    public void testDeserializeMultipleElementsArray() throws JsonProcessingException {
        JsonParser jp = createParser("['hello', 'world']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    /**
     * Test deserializing a JSON array with null value to a String array with a null element.
     */
    @Test
    public void testDeserializeNullElement() throws JsonProcessingException {
        JsonParser jp = createParser("['hello', null, 'world']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello", null, "world"}, result);
    }

    /**
     * Test deserializing a JSON array with a custom value deserializer to a String array with custom elements.
     */
    @Test
    public void testDeserializeCustomElement() throws JsonProcessingException {
        JsonParser jp = createParser("['foo', null, 'bar']");
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return "custom";
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"custom", null, "custom"}, result);
    }

    /**
     * Test deserializing a JSON array with an invalid token to an empty String array.
     */
    @Test(expected = IOException.class)
    public void testDeserializeInvalidToken() throws JsonProcessingException {
        JsonParser jp = createParser("{ \"foo\": 42 }");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, new TestContext());
    }

    /**
     * Test deserializing a JSON array with a non-array root token to an empty String array.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArrayRoot() throws JsonProcessingException {
        JsonParser jp = createParser("'hello'");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, new TestContext());
    }

    /**
     * Utility method to create a JsonParser from a JSON string.
     */
    private JsonParser createParser(String json) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

    /**
     * Test context for deserialization tests.
     */
    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getConfig() {
            return new ObjectMapper();
        }
    }
}