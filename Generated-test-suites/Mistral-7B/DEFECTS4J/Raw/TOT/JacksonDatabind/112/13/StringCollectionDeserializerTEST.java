package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.SerUtil;
import com.fasterxml.jackson.test.BaseTest;

public class StringCollectionDeserializerTest extends BaseTest {

    @Test
    public void testEmptyCollectionDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = new JsonFactory().createParser("[]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleStringDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = new JsonFactory().createParser("[\"" + "test" + "\"]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(1, result.size());
        assertEquals("test", result.iterator().next());
    }

    @Test
    public void testMultipleStringsDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = new JsonFactory().createParser("[\"" + "test1" + "\", \"" + "test2" + "\"]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(2, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
    }

    @Test
    public void testNullValueDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = new JsonFactory().createParser("[]");
        ObjectNullProvider nullProvider = new ObjectNullProvider();
        parser.setNullValueProvider(nullProvider);

        Collection<String> result = deserializer.deserialize(parser, new TestContext() {
            @Override
            public ObjectNullProvider getNullProvider() {
                return nullProvider;
            }
        });
        assertTrue(result.isEmpty());
        assertTrue(nullProvider.wasCalled());
    }

    @Test
    public void testEmptyStringDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = new JsonFactory().createParser("[\"\"]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(1, result.size());
        assertEquals("", result.iterator().next());
    }

    @Test
    public void testNullDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<String> valueDeserializer = null;
        JsonParser parser = new JsonFactory().createParser("[\"" + "test" + "\"]");
        try {
            deserializer.deserialize(parser, new TestContext(), null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Verify that no deserialization occurred
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        String input = "{\"test\":[]}";
        Object readValue = mapper.readValue(input, String[].class);
        assertEquals(0, readValue.length);
    }

    @Test
    public void testCustomValueDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        deserializer = deserializer.withValueDeserializer(valueDeserializer);

        JsonParser parser = new JsonFactory().createParser("[\"" + "test1" + "\", \"" + "test2" + "\"]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(2, result.size());
        assertEquals("test1", result.iterator().next());
        assertEquals("test2", result.iterator().next());
    }

    @Test
    public void testCustomValueDeserializerWithEmptyArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        deserializer = deserializer.withValueDeserializer(valueDeserializer);

        JsonParser parser = new JsonFactory().createParser("[]");
        Collection<String> result = deserializer.deserialize(parser, new TestContext());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCustomValueDeserializerWithNullValue() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        deserializer = deserializer.withValueDeserializer(valueDeserializer);

        ObjectNullProvider nullProvider = new ObjectNullProvider();
        JsonParser parser = new JsonFactory().createParser("[]");
        parser.setNullValueProvider(nullProvider);

        Collection<String> result = deserializer.deserialize(parser, new TestContext() {
            @Override
            public ObjectNullProvider getNullProvider() {
                return nullProvider;
            }
        });
        assertTrue(result.isEmpty());
        assertTrue(nullProvider.wasCalled());
    }

    @Test
    public void testCustomValueDeserializerWithValueDeserializerThrowsException() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<String> valueDeserializer = new ThrowingStringDeserializer();
        deserializer = deserializer.withValueDeserializer(valueDeserializer);

        JsonParser parser = new JsonFactory().createParser("[\"" + "test" + "\"]");
        try {
            deserializer.deserialize(parser, new TestContext(), null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Verify that no deserialization occurred
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        String input = "{\"test\":[]}";
        Object readValue = mapper.readValue(input, String[].class);
        assertEquals(0, readValue.length);
    }

    @Test
    public void testDelegateDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<Object> delegateDeserializer = new DefaultDeserializer();
        deserializer = deserializer.withDelegateDeserializer(delegateDeserializer);

        JsonParser parser = new JsonFactory().createParser("{\"test\":[]}");
        Object result = deserializer.deserializeWithType(parser, new TestContext(), null);
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(((List) result).isEmpty());
    }

    @Test
    public void testDelegateDeserializerWithValueDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<Object> delegateDeserializer = new DefaultDeserializerWithValueDeserializer();
        deserializer = deserializer.withDelegateDeserializer(delegateDeserializer);

        JsonParser parser = new JsonFactory().createParser("{\"test\":[\"test1\", \"test2\"]}");
        Object result = deserializer.deserializeWithType(parser, new TestContext(), null);
        assertNotNull(result);
        assertTrue(result instanceof List);
        List<String> listResult = (List<String>) result;
        assertEquals(2, listResult.size());
        assertEquals("test1", listResult.get(0));
        assertEquals("test2", listResult.get(1));
    }

    @Test
    public void testDelegateDeserializerWithEmptyArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonDeserializer<Object> delegateDeserializer = new DefaultDeserializer();
        deserializer = deserializer.withDelegateDeserializer(delegateDeserializer);

        JsonParser parser = new JsonFactory().createParser("{\"test\":[]}");
        Object result = deserializer.deserializeWithType(parser, new TestContext(), null);
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(((List) result).isEmpty());
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class ThrowingStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new IOException("This deserializer should not be used");
        }
    }

    private static class DefaultDeserializer extends StdDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ArrayList<>();
        }
    }

    private static class DefaultDeserializerWithValueDeserializer
            extends StdDeserializer<Object> {
        private final JsonDeserializer<Object> _delegateDeserializer;

        public DefaultDeserializerWithValueDeserializer() {
            this(null);
        }

        public DefaultDeserializerWithValueDeserializer(JsonDeserializer<Object> delegateDeserializer) {
            super(Object.class);
            _delegateDeserializer = delegateDeserializer;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object deserializedValue = _delegateDeserializer.deserialize(p, ctxt);
            return new ArrayList<>((Collection<?>) deserializedValue);
        }

        @Override
        public BeanDescription getBeanDescription() {
            return new BeanDescription();
        }
    }

    public static class TestContext extends DeserializationContext {
        private ObjectNullProvider _nullProvider = new ObjectNullProvider();

        public ObjectNullProvider getNullProvider() {
            return _nullProvider;
        }

        public void setNullValueProvider(ObjectNullProvider nullProvider) {
            _nullProvider = nullProvider;
        }
    }

    public static class ObjectNullProvider implements NullValueProvider {
        boolean wasCalled = false;

        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            wasCalled = true;
            return null;
        }
    }
}