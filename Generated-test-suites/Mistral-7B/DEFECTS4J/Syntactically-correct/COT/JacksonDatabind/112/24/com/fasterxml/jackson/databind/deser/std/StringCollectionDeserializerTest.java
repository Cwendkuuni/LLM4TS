package com.example.test;

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
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for StringCollectionDeserializer
 */
@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultDeserialization() throws IOException {
        String json = "[\"test1\", \"test2\", \"test3\"]";
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), StringCollectionDeserializer.class);
        assertEquals(3, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
        assertEquals("test3", result.iterator().next());
    }

    @Test
    public void testDeserializationWithCustomDeserializer() throws IOException {
        String json = "[{\"value\":\"test1\"},{\"value\":\"test2\"},{\"value\":\"test3\"}]";
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, null, null, null, null);
        serializer = serializer.withValueDeserializer(stringDeserializer);
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), serializer.getClass());
        assertEquals(3, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
        assertEquals("test3", result.iterator().next());
    }

    @Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        String json = "[{\"value\":\"test1\"},{\"value\":\"test2\"},{\"value\":\"test3\"}]";
        JsonDeserializer<Object> delegateDeserializer = new BaseJsonDeserializer<Object>() {};
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, delegateDeserializer, null, null, null);
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), serializer.getClass());
        assertEquals(3, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
        assertEquals("test3", result.iterator().next());
    }

    @Test
    public void testDeserializationWithNullValueProvider() throws IOException {
        String json = "[null, \"test1\", \"test2\"]";
        NullValueProvider nullValueProvider = new JsonNullValueProvider();
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, null, null, nullValueProvider, null);
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), serializer.getClass());
        assertEquals(3, result.size());
        assertNull(result.iterator().next());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
    }

    @Test
    public void testDeserializationWithUnwrapSingle() throws IOException {
        String json = "[\"test1\", \"test2\"]";
        Boolean unwrapSingle = true;
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, null, null, null, unwrapSingle);
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), serializer.getClass());
        assertEquals(2, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
    }

    @Test
    public void testDeserializationWithEmptyArray() throws IOException {
        String json = "[]";
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), StringCollectionDeserializer.class);
        assertEquals(0, result.size());
    }

    @Test(expected = IOException.class)
    public void testDeserializationWithInvalidJson() throws IOException {
        String json = "[\"test1\", \"test2\"{";
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), StringCollectionDeserializer.class);
    }

    @Test
    public void testDeserializationWithUnexpectedToken() throws IOException {
        String json = "{\"value\":[\"test1\", \"test2\"]}";
        Collection<String> result = objectMapper.readValue(JsonFactory.getParser(json), StringCollectionDeserializer.class);
        assertEquals(2, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
    }

    /**
     * Custom deserializer for testing
     */
    static class StdDeserializer<T> extends JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (T) p.getValueAsString();
        }
    }

    /**
     * JsonNullValueProvider for testing
     */
    static class JsonNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}