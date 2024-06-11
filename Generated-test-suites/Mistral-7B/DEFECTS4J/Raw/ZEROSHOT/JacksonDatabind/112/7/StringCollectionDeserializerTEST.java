package com.example.test.jackson.deserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeEmptyStringCollection() throws IOException {
        String jsonEmpty = "[]";
        JsonNode jsonNodeEmpty = mapper.readTree(jsonEmpty);
        JsonParser jsonParserEmpty = jsonNodeEmpty.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParserEmpty, null);
        assertNotNull(result);
        assertEquals(Arrays.asList(), result);
    }

    @Test
    public void testDeserializeStringCollectionWithStrings() throws IOException {
        String jsonStringCollection = "[\"hello\", \"world\", \"test\"]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList("hello", "world", "test"), result);
    }

    @Test
    public void testDeserializeStringCollectionWithCustomDeserializer() throws IOException {
        String jsonStringCollection = "[\"hello\", \"world\", \"test\"]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, new MyCustomStringDeserializer(), null, null);

        Collection<String> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList("hello", "world", "test"), result);
    }

    @Test
    public void testDeserializeStringCollectionWithWrapperType() throws IOException {
        String jsonStringCollection = "[{\"value\": \"hello\"}, {\"value\": \"world\"}, {\"value\": \"test\"}]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, MyWrapper.class),
                null, null, null, null, null);

        Collection<MyWrapper> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList(new MyWrapper("hello"), new MyWrapper("world"), new MyWrapper("test")), result);
    }

    @Test
    public void testDeserializeStringCollectionWithDelegateDeserializer() throws IOException {
        String jsonStringCollection = "[{\"name\": \"hello\"}, {\"name\": \"world\"}, {\"name\": \"test\"}]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, new MyNameDeserializer(), null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList("hello", "world", "test"), result);
    }

    @Test
    public void testDeserializeStringCollectionWithNullValue() throws IOException {
        String jsonStringCollection = "[null, \"hello\", \"world\"]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, MyNullValueProvider.instance);

        Collection<String> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList(null, "hello", "world"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeStringCollectionWithInvalidJson() throws IOException {
        String jsonStringCollection = "[1, \"hello\", \"world\"]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        deserializer.deserialize(jsonParserStringCollection, null);
    }

    @Test
    public void testDeserializeStringCollectionWithEmptyValue() throws IOException {
        String jsonStringCollection = "[\"\", \"hello\", \"world\"]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList("", "hello", "world"), result);
    }

    @Test
    public void testDeserializeStringCollectionWithUnwrappedSingleValue() throws IOException {
        String jsonStringCollection = "[\"hello world\"]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, Boolean.TRUE, null);

        Collection<String> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList("hello world"), result);
    }

    @Test
    public void testDeserializeStringCollectionWithValueDeserializer() throws IOException {
        String jsonStringCollection = "[{\"value\": 1}, {\"value\": 2}]";
        JsonNode jsonNodeStringCollection = mapper.readTree(jsonStringCollection);
        JsonParser jsonParserStringCollection = jsonNodeStringCollection.asParser();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, new MyNumberDeserializer(), null, null);

        Collection<String> result = deserializer.deserialize(jsonParserStringCollection, null);
        assertNotNull(result);
        assertEquals(Arrays.asList("1", "2"), result);
    }

    private static class MyCustomStringDeserializer extends StdDeserializer<String> {
        public MyCustomStringDeserializer() {
            this(null);
        }

        public MyCustomStringDeserializer(Class<?> c) {
            super(c);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString().toUpperCase();
        }
    }

    private static class MyWrapper {
        private final String value;

        public MyWrapper(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class MyNameDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.readValueAs(String.class);
        }
    }

    private static class MyNumberDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return String.valueOf(jp.readValueAs(Integer.class));
        }
    }

    private static class MyNullValueProvider implements NullValueProvider {
        private static final MyNullValueProvider instance = new MyNullValueProvider();

        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }
    }
}