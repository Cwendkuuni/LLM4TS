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
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonEmpty = "[]";
        Collection<String> expected = new LinkedList<>();

        Collection<String> result = mapper.readValue(jsonEmpty, StringCollectionDeserializer.class);

        assertEquals(expected.size(), result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testStringCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonStringArray = "[\"apple\", \"banana\", \"cherry\"]";
        Collection<String> expected = Arrays.asList("apple", "banana", "cherry");

        Collection<String> result = mapper.readValue(jsonStringArray, StringCollectionDeserializer.class);

        assertEquals(expected.size(), result.size());
        assertContentEquals(expected, result);
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("foo", null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.createContextual(mapper.getDeserializationContext(), mapper.findBeanProperty(node, "foo"));

        assertTrue(result.isEmpty());
        assertNull(result.iterator().next());
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonInvalid = "{\"foo\":[1, 2]}";
        mapper.readValue(jsonInvalid, StringCollectionDeserializer.class);
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleStringDeserializer customDeserializer = new SimpleStringDeserializer();

        String jsonStringArray = "[null, \"apple\", \"banana\", \"cherry\"]";
        Collection<String> expected = Arrays.asList("apple", "banana", "cherry");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, customDeserializer, null, null);

        Collection<String> result = mapper.readValue(jsonStringArray, StringCollectionDeserializer.class);

        assertEquals(expected.size(), result.size());
        assertContentEquals(expected, result);

        for (String s : result) {
            assertEquals("Custom deserializer should not change string value", s, customDeserializer.deserialize(null, null, null));
        }
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonOtherClassArray = "[{\"foo\": \"apple\"}, {\"foo\": \"banana\"}]";
        Collection<String> expected = Arrays.asList("apple", "banana");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, new BasicMapperDeserializer(), null, null, null);

        Collection<String> result = mapper.readValue(jsonOtherClassArray, StringCollectionDeserializer.class);

        assertEquals(expected.size(), result.size());
        assertContentEquals(expected, result);
    }

    private void assertContentEquals(Collection<String> expected, Collection<String> result) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals("String at position " + i + " is not equal", expected.get(i), result.get(i));
        }
    }

    static class BasicMapperDeserializer extends StdDeserializer<Object> {
        public BasicMapperDeserializer() {
            this(null);
        }

        protected BasicMapperDeserializer(Class<Object> vc) {
            super(vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsTree().asTree();
        }
    }

    static class SimpleStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "CustomValue";
        }
    }
}