package com.example.junit.tests.jackson;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when the input stream is a valid JSON array of strings
     */
    @Test
    public void testDeserializeValidJsonArray() throws IOException {
        String jsonString = "[\"Hello\",\"World\",\"!\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, new TestContext());

        String[] expected = new String[] {"Hello", "World", "!"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when the input stream is an empty JSON array
     */
    @Test
    public void testDeserializeEmptyJsonArray() throws IOException {
        String jsonString = "[]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, new TestContext());

        assertArrayEquals(new String[0], result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)}
     * when the input stream is a JSON array containing a null value
     */
    @Test
    public void testDeserializeJsonArrayWithNullValue() throws IOException {
        String jsonString = "[null]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, new TestContext());

        assertNull(result[0]);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)}
     * when the input stream is a valid JSON array of strings with a custom value deserializer
     */
    @Test
    public void testDeserializeCustomValidJsonArray() throws IOException {
        String jsonString = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";
        CustomStringDeserializer customStringDeserializer = new CustomStringDeserializer();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customStringDeserializer);
        String[] result = deserializer.deserializeCustom(jsonParser, new TestContext());

        String[] expected = new String[] {"{\"name\":\"John\",\"age\":30}", "{\"name\":\"Jane\",\"age\":25}"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)}
     * when the input stream is an empty JSON array with a custom value deserializer
     */
    @Test
    public void testDeserializeCustomEmptyJsonArray() throws IOException {
        String jsonString = "[]";
        CustomStringDeserializer customStringDeserializer = new CustomStringDeserializer();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customStringDeserializer);
        String[] result = deserializer.deserializeCustom(jsonParser, new TestContext());

        assertArrayEquals(new String[0], result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)}
     * when the input stream is a JSON array containing a null value with a custom value deserializer
     */
    @Test
    public void testDeserializeCustomJsonArrayWithNullValue() throws IOException {
        String jsonString = "[null]";
        CustomStringDeserializer customStringDeserializer = new CustomStringDeserializer();
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customStringDeserializer);
        String[] result = deserializer.deserializeCustom(jsonParser, new TestContext());

        assertNull(result[0]);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        String jsonString = "[1,2,3]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(int[].class);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        int[] result = (int[]) deserializer.deserializeWithType(jsonParser, new TestContext(), typeDeserializer);

        int[] expected = new int[] {1, 2, 3};
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#handleNonArray(JsonParser, DeserializationContext)}
     * when the input stream is a single valid JSON string
     */
    @Test
    public void testHandleNonArrayValidJsonString() throws IOException {
        String jsonString = "\"Hello\"";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jsonParser, new TestContext());

        assertArrayEquals(new String[] {"Hello"}, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#handleNonArray(JsonParser, DeserializationContext)}
     * when the input stream is a null JSON value
     */
    @Test
    public void testHandleNonArrayJsonNullValue() throws IOException {
        String jsonString = "null";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jsonParser, new TestContext());

        assertNull(result[0]);
    }

    /**
     * Test method for {@link StringArrayDeserializer#createContextual(DeserializationContext, BeanProperty)}
     * when the property is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualNullProperty() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.createContextual(new TestContext(), null);
    }

    /**
     * Test method for {@link StringArrayDeserializer#createContextual(DeserializationContext, BeanProperty)}
     * when the property has a custom value deserializer
     */
    @Test
    public void testCreateContextualCustomValueDeserializer() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        PropertyName propertyName = PropertyName.construct("testProperty");
        CustomStringDeserializer customStringDeserializer = new CustomStringDeserializer();
        ObjectContext objectContext = new TestContext();
        objectContext.pushProperty("testProperty");

        JsonDeserializer<?> result = deserializer.createContextual(objectContext, propertyName);
        assertTrue(result instanceof StringArrayDeserializer);
        CustomStringDeserializer customResult = (CustomStringDeserializer) result;
        assertNull(customResult._elementDeserializer);
    }

    /**
     * Test method for {@link StringArrayDeserializer#createContextual(DeserializationContext, BeanProperty)}
     * when the property has no custom value deserializer
     */
    @Test
    public void testCreateContextualNoCustomValueDeserializer() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        PropertyName propertyName = PropertyName.construct("testProperty");
        ObjectContext objectContext = new TestContext();
        objectContext.pushProperty("testProperty");

        JsonDeserializer<?> result = deserializer.createContextual(objectContext, propertyName);
        assertTrue(result instanceof StringArrayDeserializer);
        assertNull(result._elementDeserializer);
    }

    /**
     * Test helper class for JUnit tests
     */
    public static class TestContext extends ObjectMapper.DefaultContextDecorator {
        @Override
        public ObjectMapper getMapper() {
            return mapper;
        }
    }

    /**
     * Custom value deserializer for testing
     */
    public static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText();
        }
    }
}