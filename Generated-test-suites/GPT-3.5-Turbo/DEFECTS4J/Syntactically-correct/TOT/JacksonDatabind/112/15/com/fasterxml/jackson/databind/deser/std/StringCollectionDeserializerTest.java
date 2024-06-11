package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);

        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");

        List<String> actualValues = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testDeserialize_NullValues_Skip() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        deserializer.setSkipNullValues(true);

        String json = "[\"value1\", null, \"value2\", null]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        List<String> actualValues = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testDeserialize_InvalidJson_ThrowsException() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);

        String json = "{\"key\": \"value\"}";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);

        try {
            deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
            fail("IOException expected");
        } catch (JsonMappingException ex) {
            assertTrue(ex.getCause() instanceof IOException);
        }
    }

    @Test
    public void testDeserialize_EmptyArray_ReturnsEmptyList() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);

        String json = "[]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);

        List<String> expectedValues = Collections.emptyList();

        List<String> actualValues = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testDeserialize_CustomDeserializer_ReturnsExpectedValues() throws IOException {
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, customDeserializer, null, null
        );

        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("custom_value1");
        expectedValues.add("custom_value2");
        expectedValues.add("custom_value3");

        List<String> actualValues = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());

        assertEquals(expectedValues, actualValues);
    }

    private static class CustomStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            return "custom_" + value;
        }
    }
}