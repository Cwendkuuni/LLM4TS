package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any test data or objects here, if needed
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)} with default configuration.
     * @throws IOException
     */
    @org.junit.Test
    public void testDeserializeDefault() throws IOException {
        JsonParser jp = createJsonParser("[\"hello\",\"world\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new TestingDeserializationContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)} with custom element deserializer.
     * @throws IOException
     */
    @org.junit.Test
    public void testDeserializeCustom() throws IOException {
        JsonParser jp = createJsonParser("[\"test\",null]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializerWithAnnotations());
        String[] result = deserializer.deserialize(jp, new TestingDeserializationContext());
        assertArrayEquals(new String[]{"test", null}, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
     * @throws IOException
     */
    @org.junit.Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = createJsonParser("[1, \"one\", 2, \"two\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        Object result = deserializer.deserializeWithType(jp, new TestingDeserializationContext(), new SimpleTypeDeserializer());
        assertArrayEquals(new Object[]{1, "one", 2, "two"}, result.getClass().getComponentType().getComponentValues(result));
    }

    /**
     * Test method for {@link StringArrayDeserializer#handleNonArray(JsonParser, DeserializationContext)} when input is not an array.
     * @throws IOException
     */
    @org.junit.Test(expected = JsonMappingException.class)
    public void testHandleNonArray() throws IOException {
        JsonParser jp = createJsonParser("{ \"key\": \"value\" }");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.handleNonArray(jp, new TestingDeserializationContext());
    }

    /**
     * Test method for {@link StringArrayDeserializer#handleNonArray(JsonParser, DeserializationContext)} when input is a single non-null string.
     * @throws IOException
     */
    @org.junit.Test
    public void testHandleNonArraySingleValue() throws IOException {
        JsonParser jp = createJsonParser("\"hello\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jp, new TestingDeserializationContext());
        assertArrayEquals(new String[]{"hello"}, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#createContextual(DeserializationContext, BeanProperty)}
     * @throws JsonMappingException
     */
    @org.junit.Test
    public void testCreateContextual() throws JsonMappingException {
        JsonDeserializer<String> elementDeserializer = new StringDeserializerWithAnnotations();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        Object contextualDeserializer = deserializer.createContextual(new TestingDeserializationContext(), null);
        assertTrue(contextualDeserializer instanceof StringArrayDeserializer);
    }

    /**
     * Test method for {@link StringArrayDeserializer#createContextual(DeserializationContext, BeanProperty)}
     * when custom deserializer is not found.
     * @throws JsonMappingException
     */
    @org.junit.Test(expected = JsonMappingException.class)
    public void testCreateContextualNoCustomDeserializer() throws JsonMappingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        Object contextualDeserializer = deserializer.createContextual(new TestingDeserializationContext(), null);
        fail("Expected JsonMappingException");
    }

    // Helper methods

    private JsonParser createJsonParser(String json) {
        // Initialize JsonParser with the given JSON string
    }

    private static class TestingDeserializationContext extends BaseDeserializerModule.BaseContextualDeserializerProvider {
        // Implement the DeserializationContext interface as needed
    }

    private static class SimpleTypeDeserializer implements TypeDeserializer {
        // Implement the TypeDeserializer interface as needed
    }
}