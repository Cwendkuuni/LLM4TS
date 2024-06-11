package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_FOR_DATABIND;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_MISSING_CREATOR_FOR_ANY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_INVALID_FOR_DATABIND, false);
        mapper.configure(FAIL_ON_MISSING_CREATOR_FOR_ANY, false);
        mapper.configure(FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(USE_BIG_DECIMAL_FOR_FLOATS, false);
    }

    @Test
    public void testEmptyCollection() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testEmptyCollectionWithNullValues() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ null ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testStringCollection() throws IOException {
        // Test standard deserializer
        JsonParser p = mapper.getFactory().createParser("[ \"foo\", \"bar\", \"baz\" ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);

        // Test with custom deserializer
        JsonDeserializer<String> customDeserializer = new StringDeserializer();
        StringCollectionDeserializer deserializerWithCustom = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, customDeserializer);
        Collection<String> resultWithCustom = deserializerWithCustom.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Arrays.asList("FOO", "BAR", "BAZ"), resultWithCustom);
    }

    @Test
    public void testEmptyCollectionWithCustomDeserializer() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ ]");
        StringCollectionDeserializer deserializerWithCustom = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, new StringDeserializer());
        Collection<String> resultWithCustom = deserializerWithCustom.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Collections.emptyList(), resultWithCustom);
    }

    @Test
    public void testNullCollection() throws IOException {
        // Test standard deserializer
        JsonParser p = mapper.getFactory().createParser("[ null ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertNull(result);

        // Test with custom deserializer
        JsonDeserializer<String> customDeserializer = new StringDeserializer();
        StringCollectionDeserializer deserializerWithCustom = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, customDeserializer);
        Collection<String> resultWithCustom = deserializerWithCustom.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertNull(resultWithCustom);
    }

    @Test
    public void testCollectionWithEmptyString() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ \"\" ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Collections.singletonList(""), result);
    }

    @Test
    public void testCollectionWithEmptyArray() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ [] ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCollectionWithNestedArrays() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ [\"foo\", \"bar\"], [\"baz\"] ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, ArrayList.class),
                null, null, null, null, null);
        Collection<Collection<String>> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<Collection<String>>());
        assertEquals(Arrays.asList(Arrays.asList("foo", "bar"), Arrays.asList("baz")), result);
    }

    @Test
    public void testCollectionWithNullElements() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ null, \"foo\", null ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, new DeserializationContext(mapper),
                new ArrayList<String>());
        assertEquals(Arrays.asList(null, "foo", null), result);
    }

    @Test
    public void testCollectionWithNonStringElements() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ 1, 2, 3 ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        try {
            deserializer.deserialize(p, new DeserializationContext(mapper), new ArrayList<String>());
            assert false : "Expected JsonMappingException";
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCollectionWithInvalidJson() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ { \"foo\": 1 } ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        try {
            deserializer.deserialize(p, new DeserializationContext(mapper), new ArrayList<String>());
            assert false : "Expected JsonMappingException";
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCollectionWithInvalidJsonWithCustomDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new StringDeserializer();
        StringCollectionDeserializer deserializerWithCustom = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, customDeserializer);
        JsonParser p = mapper.getFactory().createParser("[ { \"foo\": 1 } ]");
        try {
            deserializerWithCustom.deserialize(p, new DeserializationContext(mapper), new ArrayList<String>());
            assert false : "Expected JsonMappingException";
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCollectionWithFiniteJson() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[ 100000 ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        try {
            deserializer.deserialize(p, new DeserializationContext(mapper), new ArrayList<String>());
            assert false : "Expected JsonMappingException";
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCollectionWithFiniteJsonWithCustomDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new StringDeserializer();
        StringCollectionDeserializer deserializerWithCustom = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, customDeserializer);
        JsonParser p = mapper.getFactory().createParser("[ 100000 ]");
        try {
            deserializerWithCustom.deserialize(p, new DeserializationContext(mapper), new ArrayList<String>());
            assert false : "Expected JsonMappingException";
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCollectionWithCustomDeserializerThrowsException() throws IOException {
        JsonDeserializer<String> customDeserializer = new StringDeserializer() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException("Custom deserializer throws exception");
            }
        };
        StringCollectionDeserializer deserializerWithCustom = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, customDeserializer);
        JsonParser p = mapper.getFactory().createParser("[ \"foo\", \"bar\" ]");
        try {
            deserializerWithCustom.deserialize(p, new DeserializationContext(mapper), new ArrayList<String>());
            assert false : "Expected JsonMappingException";
        } catch (JsonMappingException e) {
            assertEquals("Custom deserializer throws exception", e.getMessage());
        }
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}