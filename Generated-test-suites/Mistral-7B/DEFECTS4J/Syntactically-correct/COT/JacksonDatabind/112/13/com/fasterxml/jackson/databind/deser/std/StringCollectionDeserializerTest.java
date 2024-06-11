package com.example; // Replace this package name with the actual one

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link StringCollectionDeserializer}
 */
public class StringCollectionDeserializerTest {

    

    @org.junit.Test
    public void testStringCollectionDeserializer() throws Exception {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        // Test null input
        JsonParser jsonParser = createJsonParser("[]");
        List<String> result = stringCollectionDeserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Collections.emptyList(), result);

        // Test empty input
        jsonParser = createJsonParser("[]");
        result = stringCollectionDeserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Collections.emptyList(), result);

        // Test single String input
        jsonParser = createJsonParser("[\"test\"]");
        result = stringCollectionDeserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Collections.singletonList("test"), result);

        // Test multiple String inputs
        jsonParser = createJsonParser("[\"test1\", \"test2\", \"test3\"]");
        result = stringCollectionDeserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Arrays.asList("test1", "test2", "test3"), result);

        // Test String with special characters input
        jsonParser = createJsonParser("[\"test\\\"1\", \"test\\\"2\"]");
        result = stringCollectionDeserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Arrays.asList("test\"1", "test\"2"), result);

        // Test null value in the collection
        jsonParser = createJsonParser("[null, \"test\"]");
        result = stringCollectionDeserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Arrays.asList(null, "test"), result);

        // Test empty delegate deserializer
        JsonDeserializer<Object> emptyDelegateDeserializer = null;
        jsonParser = createJsonParser("[\"test\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, emptyDelegateDeserializer, null, null, null);
        result = deserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Collections.singletonList("test"), result);

        // Test custom deserializer
        @JsonDeserialize(using = CustomStringDeserializer.class)
        class CustomString {
            private String value;

            public String getValue() {
                return value;
            }
        }

        JsonDeserializer<CustomString> customStringDeserializer = new CustomStringDeserializer();
        jsonParser = createJsonParser("[{\"value\":\"test\"}]");
        deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, customStringDeserializer);
        result = deserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Arrays.asList("test"), result);

        // Test JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY
        JsonFormat.Feature feature = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
        TestContext context = new TestContext(true);
        jsonParser = createJsonParser("[\"test\"]");
        result = stringCollectionDeserializer.deserialize(jsonParser, context, new ArrayList<>());
        assertEquals(Collections.singletonList("test"), result);

        jsonParser = createJsonParser("[\"test\"]");
        deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        result = deserializer.deserialize(jsonParser, context, new ArrayList<>());
        assertFalse(result.isEmpty());

        // Test JsonDeserializer.ValueDeserializer
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        jsonParser = createJsonParser("[\"test\"]");
        deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, valueDeserializer, null, null);
        result = deserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Collections.singletonList("test"), result);

        // Test JsonDeserializer.NullValueProvider
        JsonDeserializer<Object> nullValueProvider = new NullObjectDeserializer();
        jsonParser = createJsonParser("[null]");
        deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, nullValueProvider, null);
        result = deserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(Collections.singletonList(null), result);

        // Test JsonDeserializer.ValueInstantiator
        ValueInstantiator valueInstantiator = new CollectionValueInstantiator();
        jsonParser = createJsonParser("[\"test\"]");
        deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                valueInstantiator, null, null, null, null);
        result = deserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof List);

        // Test JsonDeserializer.TypeDeserializer
        TypeDeserializer typeDeserializer = new BaseTypeDeserializer();
        jsonParser = createJsonParser("[1, 2, 3]");
        deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, null, null, null, null);
        result = deserializer.deserializeWithType(jsonParser, new TestContext(), typeDeserializer);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @org.junit.Test
    public void testStringCollectionDeserializerWithException() throws Exception {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        // Test IOException when current token is not START_ARRAY
        JsonParser jsonParser = createJsonParser("{\"test\":\"test\"}");
        try {
            stringCollectionDeserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test JsonMappingException when deserialization fails with custom deserializer
        jsonParser = createJsonParser("[{\"value\":\"test\"}]");
        @JsonDeserialize(using = CustomStringDeserializer.class)
        class CustomString {
            private String value;

            public String getValue() {
                throw new RuntimeException();
            }
        }

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, new CustomStringDeserializer());
        try {
            deserializer.deserialize(jsonParser, new TestContext(), new ArrayList<>());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private JsonParser createJsonParser(String json) throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        return objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

    static class TestContext extends DeserializationContext {
        private boolean acceptSingleValueAsArray;

        public TestContext() {
            this(false);
        }

        public TestContext(boolean acceptSingleValueAsArray) {
            super(null, null, null, null, null, new ObjectMapper());
            this.acceptSingleValueAsArray = acceptSingleValueAsArray;
        }

        @Override
        public DeserializationFeature findFeature(DeserializationFeature feature) {
            return acceptSingleValueAsArray ? DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY : null;
        }
    }

    
}