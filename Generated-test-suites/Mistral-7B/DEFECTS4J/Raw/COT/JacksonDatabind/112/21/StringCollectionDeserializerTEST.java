package com.example.test; // replace with your test package

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for StringCollectionDeserializer
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeEmptyCollection() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, null, null, null);
        Assert.assertEquals(Collections.emptyList(), deserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeSingleElementCollection() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"Hello World\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, null, null, null);
        List<String> actualResult = deserializer.deserialize(jsonParser, null);
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Hello World");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeMultipleElementsCollection() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"Hello World\", \"Java Test\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, null, null, null);
        List<String> actualResult = deserializer.deserialize(jsonParser, null);
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Hello World");
        expectedResult.add("Java Test");
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullValueProvider() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[null]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, null, null, null);
        deserializer.deserialize(jsonParser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnsupportedJsonToken() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[true]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, null, null, null);
        deserializer.deserialize(jsonParser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnresolvedJsonDeserializer() throws IOException {
        JsonFormat valueFormat = new JsonFormat(StandardDeserializationContext.builder()
                .withDeserializer(JsonDeserializer.none(String.class)).build(), String[].class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"Hello World\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectType.StdClass(String[].class), null, null, null, null, null);
        deserializer.deserialize(jsonParser, new StandardDeserializationContext(null, null, null, valueFormat));
    }
}