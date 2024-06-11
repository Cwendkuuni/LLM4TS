package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private StringCollectionDeserializer deserializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
    }

    @Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ ]");
        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getConfig());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testStringCollectionDeserializationWithDefaultValueDeserializer() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ \"Hello\", \"World\" ]");
        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getConfig());
        List<String> expected = new ArrayList<>();
        expected.add("Hello");
        expected.add("World");
        assertEquals(expected, result);
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueDeserializer() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ null, 1 ]");

        JsonDeserializer<String> customValueDeserializer = new StdDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.getText();
            }
        };

        deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                customValueDeserializer, null, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getConfig());
        List<String> expected = new ArrayList<>();
        expected.add("null");
        expected.add("1");
        assertEquals(expected, result);
    }

    @Test
    public void testStringCollectionDeserializationWithNullValues() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ null, null, null ]");
        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getConfig());
        List<String> expected = new ArrayList<>();
        expected.add(null);
        expected.add(null);
        expected.add(null);
        assertEquals(expected, result);
    }

    @Test
    public void testStringCollectionDeserializationWithNonEmptyArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ \"Hello\", 1, null, [ \"World\", \"!\" ] ]");
        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getConfig());
        List<String> expected = new ArrayList<>();
        expected.add("Hello");
        expected.add("World");
        expected.add("!");
        assertEquals(expected, result);
    }

    @Test
    public void testStringCollectionDeserializationWithCustomDeserializerForDelegate() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ null, null ]");
        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.nextValue();
            }
        };

        deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, delegateDeserializer, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, objectMapper.getConfig());
        List<String> expected = new ArrayList<>();
        expected.add(null);
        expected.add(null);
        assertEquals(expected, result);
    }

    @Test
    public void testStringCollectionDeserializationWithUnwrappingSingleValue() throws IOException {
        StringCollectionDeserializer deserializerWithSingleValueUnwrapping = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, Boolean.TRUE);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[ \"Hello\" ]");
        Collection<String> result = deserializerWithSingleValueUnwrapping.deserialize(jsonParser, objectMapper.getConfig());
        assertEquals("Hello", result.iterator().next());
    }
}