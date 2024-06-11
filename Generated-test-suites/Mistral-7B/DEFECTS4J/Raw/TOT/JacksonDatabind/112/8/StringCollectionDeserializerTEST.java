package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyStringCollectionDeserialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleStringDeserialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[\"" + "test" + "\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(1, result.size());
        assertEquals("test", result.iterator().next());
    }

    @Test
    public void testNullValueDeserialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[]");
        parser.nextValue(); // consume START_ARRAY token
        parser.nextToken(); // consume null value
        parser.nextToken(); // consume END_ARRAY token
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
        assertNull(result.iterator().next());
    }

    @Test
    public void testCustomValueDeserializer() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        TestCustomStringDeserializer customDeserializer = new TestCustomStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, customDeserializer);
        String testString = "test";
        JsonParser parser = jsonFactory.getParser("[\"" + testString + "\"]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(1, result.size());
        assertEquals(testString.toUpperCase(), result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonStructure() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("{\"test\": \"value\"}");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonValue() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingStartArrayToken() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("test");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testCustomNullValueProvider() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        TestCustomNullValueProvider nullValueProvider = new TestCustomNullValueProvider();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        deserializer.setNullProvider(nullValueProvider);
        String testString = "test";
        JsonParser parser = jsonFactory.getParser("[\"" + testString + "\", null]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(2, result.size());
        assertEquals(testString, result.iterator().next());
        assertEquals("nullValue", result.iterator().next());
    }

    private static class TestCustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return p.getValueAsString().toUpperCase();
        }
    }

    private static class TestCustomNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return "nullValue";
        }
    }
}