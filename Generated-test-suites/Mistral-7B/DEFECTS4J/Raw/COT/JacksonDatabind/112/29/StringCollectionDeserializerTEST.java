package com.example.test; // please replace "com.example.test" with the actual package name for your test class

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testStringCollectionDeserializerWithNullValueProvider() throws IOException {
        JsonDeserializer<Collection<String>> stringCollectionDeserializer =
                new StringCollectionDeserializer(
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        null);

        String jsonString1 = "[]";
        JsonNode jsonNode1 = objectMapper.readTree(jsonString1);

        List<String> result1 = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertEquals(Collections.emptyList(), result1);

        String jsonString2 = "null";
        JsonNode jsonNode2 = objectMapper.readTree(jsonString2);

        List<String> result2 = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertNull(result2);
    }

    @Test
    public void testStringCollectionDeserializerWithValueDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StandardSerializer<>(String.class) {};
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Collection<String>> stringCollectionDeserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                valueInstantiator,
                null,
                stringDeserializer,
                null,
                null);

        String jsonString = "[\"Hello World\",\"Another Test String\"]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<String> result = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertEquals(Arrays.asList("Hello World", "Another Test String"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializerWithInvalidJson() throws IOException {
        JsonDeserializer<Collection<String>> stringCollectionDeserializer =
                new StringCollectionDeserializer(
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        null);

        String jsonString = "{ \"InvalidJson\": \"Invalid Json Format\" }";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<String> result = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
    }

    @Test
    public void testStringCollectionDeserializerWithDelegateDeserializer() throws IOException {
        JsonDeserializer<Object> delegateDeserializer = new StandardSerializer<>(Object.class) {};
        JsonDeserializer<Collection<String>> stringCollectionDeserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null,
                delegateDeserializer,
                null,
                null,
                null);

        String jsonString = "[\"Hello World\",1,3.14]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<String> result = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertEquals(Arrays.asList("Hello World"), result);
    }

    @Test
    public void testStringCollectionDeserializerWithEmptyJsonArray() throws IOException {
        JsonDeserializer<Collection<String>> stringCollectionDeserializer =
                new StringCollectionDeserializer(
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        null);

        String jsonString = "[]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<String> result = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testStringCollectionDeserializerWithSingleElementJsonArray() throws IOException {
        JsonDeserializer<Collection<String>> stringCollectionDeserializer =
                new StringCollectionDeserializer(
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        null);

        String jsonString = "[\"Hello World\"]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<String> result = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertEquals(Collections.singletonList("Hello World"), result);
    }

    @Test
    public void testStringCollectionDeserializerWithValueDeserializerNullValue() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StandardSerializer<>(String.class) {};
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Collection<String>> stringCollectionDeserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                valueInstantiator,
                null,
                stringDeserializer,
                null,
                null);

        String jsonString = "[null]";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<String> result = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertNull(result);
    }

    @Test
    public void testStringCollectionDeserializerWithEmptyJsonObject() throws IOException {
        JsonDeserializer<Collection<String>> stringCollectionDeserializer =
                new StringCollectionDeserializer(
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        null);

        String jsonString = "{}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        List<String> result = stringCollectionDeserializer.createContextual(objectMapper.getDeserializationContext(), null);
        assertEquals(Collections.emptyList(), result);
    }
}