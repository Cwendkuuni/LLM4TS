package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

/**
 * Test cases for {@link StringCollectionDeserializer} class.
 */
@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyCollectionDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> emptyCollection = deserializer.deserialize(MAPPER.getFactory().createParser("[]"), MAPPER.getFactory());
        assertTrue(emptyCollection.isEmpty());
    }

    @Test
    public void testSingleValueCollectionDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        String value = "testValue";
        Collection<String> collection = Arrays.asList(value);
        String jsonString = MAPPER.writeValueAsString(collection);
        Collection<String> deserializedCollection = deserializer.deserialize(MAPPER.getFactory().createParser(jsonString), MAPPER.getFactory());
        assertTrue(deserializedCollection.contains(value));
    }

    @Test
    public void testCustomValueDeserializerCollectionDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class),
                new MyStringDeserializer(), null, null, null, null);
        String jsonString = "{\"values\":[\"test1\",\"test2\"]}";
        Collection<String> deserializedCollection = deserializer.deserialize(MAPPER.getFactory().createParser(jsonString), MAPPER.getFactory());
        Collection<String> expectedCollection = Arrays.asList("test1", "test2");
        assertTrue(deserializedCollection.containsAll(expectedCollection));
    }

    @Test
    public void testNullValueDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(MAPPER.getFactory().createParser("[]"), MAPPER.getFactory());
        Object nullValue = MAPPER.getFactory().getNullValueProvider().getDefaultValue("java.lang.String");
        collection.add(nullValue);
        Collection<String> deserializedCollection = deserializer.deserialize(MAPPER.getFactory().createParser(MAPPER.writeValueAsString(collection)), MAPPER.getFactory());
        assertTrue(deserializedCollection.contains(nullValue));
    }

    @Test
    public void testDelegatedDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, new MyJsonDeserializer(), null, null, null);
        String jsonString = "{\"values\":[\"test1\",\"test2\"]}";
        Collection<String> deserializedCollection = deserializer.deserialize(MAPPER.getFactory().createParser(jsonString), MAPPER.getFactory());
        Collection<String> expectedCollection = Arrays.asList("test1", "test2");
        assertTrue(deserializedCollection.containsAll(expectedCollection));
    }

    @Test
    public void testDefaultDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"test1\",\"test2\"]";
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getFactory());
        Collection<String> expectedCollection = Arrays.asList("test1", "test2");
        assertTrue(deserializedCollection.containsAll(expectedCollection));
    }

    private static class MyJsonDeserializer extends StdDeserializer<Object> {
        public MyJsonDeserializer() {
            this(null);
        }

        public MyJsonDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = p.getCurrentToken();
            if (currentToken != JsonToken.START_ARRAY) {
                throw ctxt.mappingException("Expected START_ARRAY but found " + currentToken);
            }
            p.nextToken(); // Consume START_ARRAY token
            Collection<String> result = new LinkedList<>();
            while (p.nextValue() != null) {
                result.add(p.getText());
            }
            return result;
        }
    }

    private static class MyStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                String result = p.getText();
                p.nextToken(); // Consume VALUE_STRING token
                return result;
            }
            throw ctxt.mappingException("Expected VALUE_STRING but found " + p.getCurrentName());
        }
    }
}