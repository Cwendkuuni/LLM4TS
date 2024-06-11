package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringCollectionSerializer;

/**
 * Test cases for {@link StringCollectionDeserializer} class.
 */
@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    @Test
    public void testCreateStringCollectionDeserializer() {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer(); // a simple deserializer
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, valueInstantiator, null, valueDeserializer, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testIsCachable() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        assertFalse(serializer.isCachable());

        serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        serializer = new StringCollectionDeserializer(collectionType, null, null, valueDeserializer, null, null);
        assertFalse(serializer.isCachable());

        serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonDeserializer<Object> delegateDeserializer = new ObjectDeserializer();
        serializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, null, null, null);
        assertFalse(serializer.isCachable());
    }

    @Test
    public void testCreateContextualDeserializer() throws JsonMappingException, JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        DeserializationContext ctxt = new TestContext();
        JsonDeserializer<Object> delegateDeserializer = new ObjectDeserializer();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();

        // Test without delegate deserializer
        JsonDeserializer<?> deserializer = serializer.createContextual(ctxt, null);
        assertNull(deserializer);

        // Test with null value deserializer
        valueDeserializer = null;
        deserializer = serializer.createContextual(ctxt, null);
        assertNull(deserializer);

        // Test with value deserializer
        deserializer = serializer.createContextual(ctxt, new BeanProperty());
        assertEquals(StringCollectionDeserializer.class, deserializer.getClass());

        // Test with delegate deserializer
        deserializer = serializer.createContextual(ctxt, new BeanProperty(), delegateDeserializer);
        assertNotNull(deserializer);
        assertEquals(StringCollectionDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testDeserialize() throws IOException, JsonMappingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectReader reader = new ObjectMapper().readerForListOf(String.class);
        JsonNode node = new ObjectMapper().readTree("[\"foo\", \"bar\", \"baz\"]");
        Collection<String> result = serializer.deserialize(node.asToken(), new TestContext(), new ArrayList<>());
        assertEquals(3, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals("baz", result.get(2));
    }

    @Test
    public void testDeserializeUsingCustomDeserializer() throws IOException, JsonMappingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectReader reader = new ObjectMapper().readerForListOf(String.class);
        JsonNode node = new ObjectMapper().readTree("[\"./foo\", \"bar\", \"baz\"]");
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        Collection<String> result = serializer.deserialize(node.asToken(), new TestContext(), new ArrayList<>(), customDeserializer);
        assertEquals(3, result.size());
        assertEquals("./foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals("baz", result.get(2));
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException, JsonMappingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(List.class, new TypeReference<List<String>>(){});
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[{\"foo\":\"bar\"}, {\"foo\":\"baz\"}]");
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(new JsonFormat.Value(StringCollectionSerializer.class));
        Collection<String> result = serializer.deserializeWithType(node.asToken(), new TestContext(), typeDeserializer);
        assertEquals(2, result.size());
        assertEquals("bar", result.get(0));
        assertEquals("baz", result.get(1));
    }

    @Test
    public void testDeserializeWithNullProvider() throws IOException, JsonMappingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[null, \"foo\", null]");
        NullValueProvider nullValueProvider = new NullValueProviderDefaultImpl();
        Collection<String> result = serializer.deserialize(node.asToken(), new TestContext(), new ArrayList<String>(), nullValueProvider);
        assertEquals(3, result.size());
        assertNull(result.get(0));
        assertEquals("foo", result.get(1));
        assertNull(result.get(2));
    }

    @Test
    public void testDeserializeWithCustomNullValueProvider() throws IOException, JsonMappingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        NullValueProvider customNullValueProvider = new CustomNullValueProvider();
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[null, \"foo\", null]");
        Collection<String> result = serializer.deserialize(node.asToken(), new TestContext(), new ArrayList<String>(), customNullValueProvider);
        assertEquals(3, result.size());
        assertEquals(customNullValueProvider.getNullValue(""), result.get(0));
        assertEquals("foo", result.get(1));
        assertEquals(customNullValueProvider.getNullValue(""), result.get(2));
    }

    @Test
    public void testDeserializeWithValueInstantiator() throws IOException, JsonMappingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ValueInstantiator valueInstantiator = new CustomValueInstantiator();
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"foo\", \"bar\", \"baz\"]");
        Collection<String> result = serializer.deserialize(node.asToken(), new TestContext(), new ArrayList<>(), null, valueInstantiator);
        assertEquals(3, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals("baz", result.get(2));

        result = serializer.deserialize(node.asToken(), new TestContext(), new ArrayList<>(), null, valueInstantiator, "custom");
        assertEquals(3, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals("baz", result.get(2));
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException, JsonMappingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> delegateDeserializer = new ObjectDeserializer();
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, 2, 3]"); // a JSON Array of numbers
        Collection<String> result = serializer.deserialize(node.asToken(), new TestContext(), new ArrayList<>());
        assertEquals(3, result.size());
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
        assertEquals("3", result.get(2));
    }

    private static class CustomNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return "custom null value";
        }
    }

    private static class CustomNullValueProviderDefaultImpl implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }

        @Override
        public Object getNullValue(DeserializationContext ctxt, JsonDeserializer<?> deserializer) {
            return null;
        }
    }

    private static class CustomValueInstantiator implements ValueInstantiator {
        @Override
        public Object createValueInstance(DeserializationContext ctxt) throws IOException {
            return new String[]{"foo", "bar", "baz"};
        }

        @Override
        public Object createValueInstance(DeserializationContext ctxt, JavaType type) throws IOException {
            return new String[]{"foo", "bar", "baz"};
        }

        @Override
        public Object createValueInstance(DeserializationContext ctxt, JavaType type, String name) throws IOException {
            return new String[]{"foo", "bar", "baz"};
        }
    }

    private static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}