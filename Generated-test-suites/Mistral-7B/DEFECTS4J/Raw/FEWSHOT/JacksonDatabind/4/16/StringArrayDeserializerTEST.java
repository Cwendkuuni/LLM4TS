package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    // Test utilities
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String JSON_STRING = "[\"foo\", \"bar\", null]";

    @Test
    public void testDefaultDeserialization() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = deserializer.deserialize(mapper.parse(JSON_STRING), new DeserializationContext());
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"foo", "bar", null}, result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        // Assuming we have a custom JsonDeserializer<String> named MyStringDeserializer
        JsonDeserializer<String> customDeserializer = new MyStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String[] result = deserializer.deserialize(mapper.parse(JSON_STRING), new DeserializationContext());
        assertEquals(3, result.length);
        // Custom deserialization logic goes here
    }

    @Test
    public void testNullElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);

        String[] result = deserializer.deserialize(mapper.parse(JSON_STRING), new DeserializationContext());
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"foo", "bar", null}, result);
    }

    @Test
    public void testEmptyInput() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = deserializer.deserialize(mapper.parse("[]"), new DeserializationContext());
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testNonArrayInput() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String jsonString = "{\"foo\": \"bar\"}";
        try {
            deserializer.deserialize(mapper.parse(jsonString), new DeserializationContext());
            fail("Expected JacksonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        // Assuming we have a custom JsonDeserializer<String> named MyStringDeserializer
        JsonDeserializer<String> customDeserializer = new MyStringDeserializer();

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        Object context = new Object();
        BeanProperty property = new BeanProperty("testProperty", String[].class);

        JsonDeserializer<?> result = deserializer.createContextual(new DeserializationContext(mapper.getFactory(), context), property);

        assertTrue(result instanceof StringArrayDeserializer);
        StringArrayDeserializer stringArrayDeserializer = (StringArrayDeserializer) result;
        assertEquals(customDeserializer, stringArrayDeserializer._elementDeserializer);
    }
}