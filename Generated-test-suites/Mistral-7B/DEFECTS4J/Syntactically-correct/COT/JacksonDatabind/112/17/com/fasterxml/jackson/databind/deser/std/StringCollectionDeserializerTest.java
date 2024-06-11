package com.example.test; // replace with your test package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for {@link StringCollectionDeserializer}
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test constructor with basic configuration
     */
    @Test
    public void testStringCollectionDeserializerBasicConstruction() {
        JavaType collectionType = ObjectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        JsonDeserializer<String> valueDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiator, delegateDeserializer);
        assertNotNull(deserializer);
    }

    /**
     * Test constructor with custom configuration
     */
    @Test
    public void testStringCollectionDeserializerCustomConstruction() throws Exception {
        JavaType collectionType = ObjectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StringListValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, valueInstantiator, delegateDeserializer, null, valueDeserializer, null);
        assertNotNull(deserializer);
    }

    /**
     * Test deserialization with no custom deserializer or delegate
     */
    @Test
    public void testStringCollectionDeserializerNoCustomDeserializerOrDelegate() throws IOException {
        String json = "[\"hello\", \"world\"]";
        Collection<String> collection = objectMapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(2, collection.size());
        assertEquals("hello", collection.get(0));
        assertEquals("world", collection.get(1));
    }

    /**
     * Test deserialization with custom deserializer
     */
    @Test
    public void testStringCollectionDeserializerCustomDeserializer() throws IOException {
        String json = "[\"1\", \"2\"]";
        JsonDeserializer<String> valueDeserializer = new CustomStringDeserializer();
        Collection<String> collection = objectMapper.readValue(json, new TypeReference<Collection<String>>() {}.getType(), valueDeserializer);
        assertEquals(2, collection.size());
        assertEquals("1", collection.get(0));
        assertEquals("2", collection.get(1));
    }

    /**
     * Test deserialization with delegate deserializer
     */
    @Test
    public void testStringCollectionDeserializerDelegateDeserializer() throws IOException {
        String json = "[{\"name\": \"John\", \"age\": 30}, {\"name\": \"Jane\", \"age\": 25}]";
        JsonDeserializer<Object> delegateDeserializer = new ObjectMapper().findValueDeserializer(Object.class, null);
        Collection<String> collection = objectMapper.readValue(json, StringCollectionDeserializer.class, delegateDeserializer);
        assertEquals(2, collection.size());
        assertTrue(collection.contains("{\"name\": \"John\", \"age\": 30}"));
        assertTrue(collection.contains("{\"name\": \"Jane\", \"age\": 25}"));
    }

    /**
     * Test deserialization with null value provider
     */
    @Test
    public void testStringCollectionDeserializerNullValueProvider() {
        String json = "[null]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                null, null, null, null, null, null);
        deserializer.setNullProvider(new JSONNullValueProvider());
        Collection<String> collection = deserializer.deserialize(null, new JsonFactory(), null);
        assertEquals(1, collection.size());
        assertNull(collection.get(0));
    }

    /**
     * Test deserialization with unwrapping single value
     */
    @Test
    public void testStringCollectionDeserializerUnwrapSingleValue() throws IOException {
        String json = "[\"hello\"]";
        Collection<String> collection = objectMapper.readValue(json, StringCollectionDeserializer.class, null, true);
        assertEquals(1, collection.size());
        assertEquals("hello", collection.get(0));
    }

    /**
     * Test deserialization with invalid JSON
     */
    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializerInvalidJson() throws IOException {
        String json = "[1, 2]";
        Collection<String> collection = objectMapper.readValue(json, StringCollectionDeserializer.class);
        fail("Expected JsonMappingException but got valid collection");
    }

    /**
     * Test deserialization with custom type deserializer
     */
    @Test
    public void testStringCollectionDeserializerCustomTypeDeserializer() throws IOException {
        String json = "[\"2022-01-01\", \"2022-01-02\"]";
        StdDateFormat dateFormat = new StdDateFormat();
        TypeDeserializer typeDeserializer = new DateDeserializer(dateFormat);
        Collection<String> collection = objectMapper.readValue(json, new TypeReference<Collection<String>>() {}.getType(), typeDeserializer);
        assertEquals(2, collection.size());
        assertEquals("2022-01-01", collection.get(0));
        assertEquals("2022-01-02", collection.get(1));
    }

    /**
     * Custom string deserializer
     */
    private static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    /**
     * String list value instantior
     */
    private static class StringListValueInstantiator implements ValueInstantiator {
        @Override
        public Object createValueInstance(SerializationConfig config) throws IOException {
            return new ArrayList<>();
        }
    }

    /**
     * JSON null value provider
     */
    private static class JSONNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    /**
     * Date deserializer
     */
    private static class DateDeserializer extends StdDeserializer<String> {
        private final StdDateFormat dateFormat;

        public DateDeserializer(StdDateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return dateFormat.parse(p.getValueAsString());
        }
    }
}