package com.yourpackage.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StringCollectionDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());

    @org.junit.Test
    public void testEmptyCollectionDeserialization() throws IOException {
        String json = "[]";
        Collection<String> expected = new ArrayList<>();
        Collection<String> actual = MAPPER.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        String json = "[\"test\"]";
        String expected = "test";
        Collection<String> actual = MAPPER.readValue(json, StringCollectionDeserializer.class);
        assertEquals(Arrays.asList(expected), actual);
    }

    @org.junit.Test
    public void testNullValueCollectionDeserialization() throws IOException {
        String json = "[null]";
        Collection<String> expected = Arrays.asList((String) null);
        Collection<String> actual = MAPPER.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        String json = "[\"\"]";
        Collection<String> expected = new ArrayList<>();
        Collection<String> actual = MAPPER.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testStringCollectionDeserializationWithCustomDeserializer() throws IOException {
        String json = "[\"test1\",\"test2\"]";
        JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(List.class, String.class),
                customDeserializer, null, null, null, Boolean.FALSE);

        Collection<String> expected = Arrays.asList("test1", "test2");
        Collection<String> actual = deserializer.deserialize(MAPPER.getFactory().createParser(json), null);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testStringCollectionDeserializationWithValueDeserializer() throws IOException, JsonParseException {
        String json = "[{\"name\": \"test1\"},{\"name\": \"test2\"}]";
        JsonDeserializer<String> valueDeserializer = new DefaultStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, valueDeserializer, null, Boolean.FALSE);

        Collection<String> expected = Arrays.asList("test1", "test2");
        Collection<String> actual = deserializer.deserialize(MAPPER.getFactory().createParser(json), null);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEmptyArrayDeserialization() throws IOException {
        String json = "[]";
        assertThrows(JsonMappingException.class, () -> MAPPER.readValue(json, StringCollectionDeserializer.class));
    }

    @org.junit.Test
    public void testNullJsonDeserialization() throws IOException {
        String json = null;
        assertThrows(JsonParseException.class, () -> MAPPER.readValue(json, StringCollectionDeserializer.class));
    }

    @org.junit.Test
    public void testInvalidJsonDeserialization() throws IOException {
        String json = "{ \"name\": \"test\" }";
        assertThrows(JsonMappingException.class, () -> MAPPER.readValue(json, StringCollectionDeserializer.class));
    }
}