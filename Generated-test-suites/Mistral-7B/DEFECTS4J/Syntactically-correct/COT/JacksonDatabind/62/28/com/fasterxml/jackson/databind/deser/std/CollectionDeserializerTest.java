package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test cases for CollectionDeserializer class.
 */
public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
    }

    @After
    public void teardown() throws Exception {
        objectMapper = null;
        jsonFactory = null;
    }

    @Test
    public void testCollectionDeserializer() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[\"test\", \"test2\"]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("test", result.get(0));
        Assert.assertEquals("test2", result.get(1));
    }

    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws IOException {
        // Given
        JavaType javaTypeDelegate = objectMapper.getTypeFactory().constructParametricType(Collection.class, Integer.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaTypeDelegate,
                new IntDeserializer(), null, null, new IntDeserializer(), null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(1, result.get(0).intValue());
        Assert.assertEquals(2, result.get(1).intValue());
        Assert.assertEquals(3, result.get(2).intValue());
    }

    @Test
    public void testCollectionDeserializerWithEmptyString() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCollectionDeserializerWithNullValue() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());
        jsonParser.nextToken(); // consume END_ARRAY
        jsonParser.nextValue(); // consume null value

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertNull(result.get(0));
    }

    @Test
    public void testCollectionDeserializerWithTypeDeserializer() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, CustomType.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null,
                new TypeDeserializer() {
                    @Override
                    public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
                            throws IOException {
                        return new CustomType();
                    }
                }, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[{}]");
        Collection<CustomType> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertNotNull(result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithInvalidJson() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When & Then
        JsonParser jsonParser = jsonFactory.createParser("invalidJson");
        collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());
    }

    @Test
    public void testCollectionDeserializerWithEmptyArray() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCollectionDeserializerWithSingleValue() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("\"test\"");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("test", result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithUnwrappingSingleValueAsArray() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, Boolean.TRUE);

        // When & Then
        JsonParser jsonParser = jsonFactory.createParser("\"test\"");
        collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());
    }

    @Test
    public void testCollectionDeserializerWithEmptyJsonArray() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCollectionDeserializerWithNullTypeDeserializer() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[\"test\", null]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("test", result.get(0));
        Assert.assertNull(result.get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNonArrayJson() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When & Then
        JsonParser jsonParser = jsonFactory.createParser("{\"test\": \"value\"}");
        collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());
    }

    @Test
    public void testCollectionDeserializerWithEmptyCollection() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(0, result.size());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithInvalidCollectionType() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        // When & Then
        JsonParser jsonParser = jsonFactory.createParser("[\"test\", \"test2\"]");
        collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());
    }

    @Test
    public void testCollectionDeserializerWithEmptyJsonArrayAndUnwrappingSingleValueAsArray() throws IOException {
        // Given
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, Boolean.TRUE);

        // When
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCollectionDeserializerWithCustomJsonFactory() throws IOException {
        // Given
        JsonFactory customJsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.setFactory(customJsonFactory);

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Collection.class, String.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, null, null);
        JsonParser jsonParser = customJsonFactory.createParser("[\"test\", \"test2\"]");

        // When
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, new TestDeserializationContext(), new ArrayList<>());

        // Then
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("test", result.get(0));
        Assert.assertEquals("test2", result.get(1));
    }

    /**
     * Custom deserializer for testing.
     */
    public static class IntDeserializer extends StdDeserializer<Integer> {
        public IntDeserializer() {
            this(null);
        }

        public IntDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getIntValue();
        }
    }

    /**
     * Custom type.
     */
    public static class CustomType {

    }

    /**
     * Custom deserialization context.
     */
    public static class TestDeserializationContext extends DeserializationContext {
        public TestDeserializationContext() {
            super(new ObjectMapper(), new TestSerializerProvider());
        }
    }

    /**
     * Custom serializer provider.
     */
    public static class TestSerializerProvider extends SerializerProvider {
        @Override
        public JsonSerializer<Object> findValueSerializer(Class<?> type, JsonSerializer<?> valueSerializer) {
            return valueSerializer;
        }

        @Override
        public JsonSerializer<Object> findValueSerializer(JavaType type, JsonSerializer<?> valueSerializer) {
            return valueSerializer;
        }

        @Override
        public JsonSerializer<Object> findValueSerializer(Class<?> type, JsonSerializer<?> valueSerializer,
                JsonSerializer<?> containerSerializer) {
            return valueSerializer;
        }

        @Override
        public JsonSerializer<Object> findValueSerializer(JavaType type, JsonSerializer<?> valueSerializer,
                JsonSerializer<?> containerSerializer) {
            return valueSerializer;
        }
    }
}