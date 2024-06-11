package com.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        collectionDeserializer = objectMapper.findValueDeserializer(ArrayList.class, null);
    }

    // Basic Test Case for deserialize method with empty collection
    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        String json = "[]";
        List<Integer> collection = collectionDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), null);
        assertNotNull(collection);
        assertTrue(collection.isEmpty());
    }

    // Basic Test Case for deserialize method with single element collection
    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        String json = "[1]";
        List<Integer> collection = collectionDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), null);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        assertEquals(1, collection.get(0).intValue());
    }

    // Basic Test Case for deserialize method with multiple elements collection
    @Test
    public void testMultipleElementsCollectionDeserialization() throws IOException {
        String json = "[1, 2, 3]";
        List<Integer> collection = collectionDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), null);
        assertNotNull(collection);
        assertEquals(3, collection.size());
        assertEquals(1, collection.get(0).intValue());
        assertEquals(2, collection.get(1).intValue());
        assertEquals(3, collection.get(2).intValue());
    }

    // Test Case for deserialization with custom value deserializer
    @Test
    public void testCustomValueDeserializer() throws IOException {
        String json = "[1, \"John\", true]";
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        CollectionDeserializer collectionDeserializerWithCustomDeserializer = new CollectionDeserializer(Arrays.asList(Object.class, Integer.class, String.class), null, null, null, null, null)
                .withResolved(null, null, null, null).withValueDeserializer(stringDeserializer);
        List<Object> collection = collectionDeserializerWithCustomDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), null);
        assertNotNull(collection);
        assertEquals(3, collection.size());
        assertEquals(1, collection.get(0).intValue());
        assertEquals("John", collection.get(1));
        assertTrue(collection.get(2) instanceof Boolean);
    }

    // Test Case for deserialization with polymorphic types
    @Test
    public void testPolymorphicDeserialization() throws IOException {
        String json = "[1, \"apple\", 3.14]";
        TypeDeserializer typeDeserializer = new BaseTypeDeserializer();
        CollectionDeserializer collectionDeserializerWithTypeDeserializer = new CollectionDeserializer(Arrays.asList(Object.class), null, typeDeserializer, null, null, null)
                .withResolved(null, null, null, null);
        List<Object> collection = collectionDeserializerWithTypeDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), null);
        assertNotNull(collection);
        assertEquals(3, collection.size());
        assertEquals(1, collection.get(0).intValue());
        assertEquals("apple", collection.get(1));
        assertTrue(collection.get(2) instanceof Number);
    }

    // Test Case for deserialization with null value
    @Test
    public void testNullValueDeserialization() throws IOException {
        String json = "[null]";
        List<String> collection = new ArrayList<>();
        CollectionDeserializer collectionDeserializerWithCustomDeserializer = new CollectionDeserializer(Arrays.asList(Object.class, String.class), null, null, null, null, null)
                .withResolved(null, null, null, null)
                .withValueDeserializer(new StringDeserializer());
        collectionDeserializerWithCustomDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), collection);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        assertEquals(null, collection.get(0));
    }

    // Test Case for deserialization with empty String as a single value
    @Test
    public void testEmptyStringAsSingleValueDeserialization() throws IOException {
        String json = "[\"\"]";
        List<String> collection = new ArrayList<>();
        CollectionDeserializer collectionDeserializerWithCustomDeserializer = new CollectionDeserializer(Arrays.asList(Object.class, String.class), null, null, null, null, null)
                .withResolved(null, null, null, null)
                .withValueDeserializer(new StringDeserializer());
        collectionDeserializerWithCustomDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), collection);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        assertEquals("", collection.get(0));
    }

    // Test Case for deserialization with empty String as a single element in a collection
    @Test
    public void testEmptyStringAsSingleElementInCollectionDeserialization() throws IOException {
        String json = "[\"\"]";
        List<List<String>> collection = new ArrayList<>();
        CollectionDeserializer collectionDeserializerWithCustomDeserializer = new CollectionDeserializer(Arrays.asList(Object.class, List.class, String.class), null, null, null, null, null)
                .withResolved(null, null, null, null)
                .withValueDeserializer(new CollectionDeserializer(Arrays.asList(Object.class, String.class), null, null, null, null, null)
                        .withResolved(null, null, null, null)
                        .withValueDeserializer(new StringDeserializer()));
        collectionDeserializerWithCustomDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), collection);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        assertNotNull(collection.get(0));
        assertEquals(1, collection.get(0).size());
        assertEquals("", collection.get(0).get(0));
    }

    // Test Case for deserialization with an empty JSON array
    @Test(expected = JsonMappingException.class)
    public void testEmptyJsonArrayDeserialization() throws IOException {
        String json = "[]";
        List<Object> collection = new ArrayList<>();
        CollectionDeserializer collectionDeserializerWithCustomDeserializer = new CollectionDeserializer(Arrays.asList(Object.class), null, null, null, null, null)
                .withResolved(null, null, null, null)
                .withValueDeserializer(new StringDeserializer());
        collectionDeserializerWithCustomDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), collection);
    }

    // Test Case for deserialization with an invalid JSON structure
    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonStructureDeserialization() throws IOException {
        String json = "[1, { \"name\": \"John\" }]";
        List<Object> collection = new ArrayList<>();
        CollectionDeserializer collectionDeserializerWithCustomDeserializer = new CollectionDeserializer(Arrays.asList(Object.class), null, null, null, null, null)
                .withResolved(null, null, null, null)
                .withValueDeserializer(new StringDeserializer());
        collectionDeserializerWithCustomDeserializer.deserialize(objectMapper.getFactory(), new JsonParser(new StringReader(json)), collection);
    }

    // Nested classes

    /**
     * Custom value deserializer for strings.
     */
    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    /**
     * Custom value deserializer for base types.
     */
    static class BaseTypeDeserializer extends StdDeserializer<Object> {
        public BaseTypeDeserializer() {
            this(null);
        }

        public BaseTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken token = p.getCurrentToken();
            Object result;
            switch (token) {
                case START_ARRAY:
                    result = createFromJsonValue(p, ctxt, new ArrayList<>());
                    break;
                case VALUE_NULL:
                    result = null;
                    break;
                case VALUE_STRING:
                    result = p.getValueAsString();
                    break;
                case NUMBER_INT:
                    result = p.getValueAsInt();
                    break;
                case NUMBER_FLOAT:
                    result = p.getValueAsDouble();
                    break;
                case BOOLEAN:
                    result = p.getValueAsBoolean();
                    break;
                default:
                    throw new IOException("Cannot deserialize value of type [" + getClass().getName() + "] from JSON: " + p.getValueAsString());
            }
            return result;
        }

        @Override
        public void setupContext(DeserializationContext ctxt, BeanProperty property, JsonDeserializer<?> deserializer) throws JsonMappingException {
            super.setupContext(ctxt, property, deserializer);
            ctxt.findValueDeserializer(property.getType(), property);
        }
    }
}