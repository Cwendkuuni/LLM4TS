package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

/**
 * Test class for StringCollectionDeserializer
 */
@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(java.util.List.class, null);
        assertEquals(new ArrayList<>(), deserializer.deserialize(mapper.readTree("[]"), null, null));
    }

    @Test
    public void testCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(java.util.List.class, null);
        ArrayNode jsonNode = mapper.createArrayNode();
        jsonNode.add("foo");
        jsonNode.add("bar");
        List<String> result = deserializer.deserialize(mapper.readTree(jsonNode.toString()), null, null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testCollectionDeserializationWithCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(java.util.List.class, null);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class, null);
        deserializer = mapper.handleSecondaryContextualization(deserializer, null, java.util.List.class);
        deserializer = deserializer.withResolved(null, stringDeserializer, null, null);
        ArrayNode jsonNode = mapper.createArrayNode();
        jsonNode.add("foo");
        jsonNode.add("bar");
        List<String> result = deserializer.deserialize(mapper.readTree(jsonNode.toString()), null, null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testCollectionDeserializationWithValueInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(java.util.List.class, null);
        ValueInstantiator valueInstantiator = mapper.getValueInstantiator();
        deserializer = mapper.handleSecondaryContextualization(deserializer, null, java.util.List.class);
        deserializer = deserializer.withResolved(null, null, valueInstantiator, null);
        ArrayNode jsonNode = mapper.createArrayNode();
        jsonNode.add("foo");
        jsonNode.add("bar");
        List<String> result = deserializer.deserialize(mapper.readTree(jsonNode.toString()), null, null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testCollectionDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(java.util.List.class, null);
        JsonDeserializer<Object> delegateDeserializer = mapper.findValueDeserializer(String[].class, null);
        deserializer = deserializer.withResolved(delegateDeserializer, null, null, null);
        ArrayNode jsonNode = mapper.createArrayNode();
        jsonNode.add("foo");
        jsonNode.add("bar");
        List<String> result = deserializer.deserialize(mapper.readTree(jsonNode.toString()), null, null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testCollectionDeserializationWithNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(java.util.List.class, null);
        NullValueProvider nullValueProvider = mapper.getConfiguration().getNullValueProvider();
        deserializer = deserializer.withResolved(null, null, null, null);
        deserializer = deserializer.withResolved(null, null, nullValueProvider, null);
        ArrayNode jsonNode = mapper.createArrayNode();
        jsonNode.addNull();
        jsonNode.add("bar");
        List<String> result = deserializer.deserialize(mapper.readTree(jsonNode.toString()), null, null);
        assertEquals(2, result.size());
        assertNull(result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testCollectionDeserializationWithFormatFeature() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Feature acceptSingleValueAsArray = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
        ObjectMapper mapperWithFeature = mapper.copy();
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_FOR_CONCRETE_TYPES, false);
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_INVALID_FOR_DATABIND, false);
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_EMPTY_AND_ABSENT, false);
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_EXCEPTIONS, false);
        mapperWithFeature.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithFeature.configure(SerializationFeature.FAIL_ON_EMPTY_OR_NULL_FOR_PROPERTIES, false);
        mapperWithFeature.configure(SerializationFeature.FAIL_ON_EMPTY_ARRAYS, false);
        mapperWithFeature.configure(SerializationFeature.FAIL_ON_NULL_FOR_SERIALIZATION, false);
        mapperWithFeature.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, acceptSingleValueAsArray);
        StringCollectionDeserializer deserializer = mapperWithFeature.findValueDeserializer(java.util.List.class, null);
        ArrayNode jsonNode = mapperWithFeature.createArrayNode();
        jsonNode.add("foo");
        List<String> result = deserializer.deserialize(mapperWithFeature.readTree(jsonNode.toString()), null, null);
        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }
}