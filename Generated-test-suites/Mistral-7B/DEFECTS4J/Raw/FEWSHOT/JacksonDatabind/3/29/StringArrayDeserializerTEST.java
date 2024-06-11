package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

public class StringArrayDeserializerTest {

    private ObjectMapper mapper;
    private ObjectReader reader;

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        String[] input = StringArrayDeserializer.instance.deserialize(createParser("[]"), createContext());
        assertArrayEquals(new String[0], input);
    }

    @Test
    public void testDeserializeSingleValue() throws Exception {
        String[] input = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\"]"), createContext());
        assertEquals("foo", input[0]);
    }

    @Test
    public void testDeserializeSingleValueAsNull() throws Exception {
        String[] input = StringArrayDeserializer.instance.deserialize(createParser("[null]"), createContext());
        assertNull(input);
    }

    @Test
    public void testDeserializeMultipleValues() throws Exception {
        String[] input = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\", \"bar\", null]"), createContext());
        assertEquals("foo", input[0]);
        assertEquals("bar", input[1]);
        assertNull(input[2]);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = createCustomDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] input = deserializer.deserialize(createParser("[\"foo\", \"bar\", null]"), createContext());
        assertEquals("foo", input[0]);
        assertEquals("bar", input[1]);
        assertNull(input[2]);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws Exception {
        DeserializationContext ctxt = createContext();
        ctxt.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String[] input = StringArrayDeserializer.instance.deserialize(createParser("[\"\"]"), ctxt);
        assertNull(input);
    }

    @Test
    public void testDeserializeNonArray() throws Exception {
        String[] input = StringArrayDeserializer.instance.deserialize(createParser("\"foo\""), createContext());
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            assertEquals("foo", input[0]);
        } else {
            throw new AssertionError("Expected error, but none thrown");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws Exception {
        String[] input = StringArrayDeserializer.instance.deserialize(createParser("{"), createContext());
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonDeserializer<String> customDeserializer = createCustomDeserializer();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance.createContextual(createContext(), null);
        StringArrayDeserializer deserializerWithCustom = new StringArrayDeserializer(customDeserializer);
        assertEquals(deserializerWithCustom, deserializer);
    }

    private JsonParser createParser(String json) throws IOException {
        JsonFactory factory = new JsonFactoryBuilder().build();
        return factory.createParser(json);
    }

    private DeserializationContext createContext() {
        return new TestingDeserializationContext();
    }

    private JsonDeserializer<String> createCustomDeserializer() {
        return new StringDeserializer();
    }

    private static class TestingDeserializationContext extends DeserializationContextBase {
        @Override
        public ObjectMapper _getConfig() {
            return mapper;
        }

        @Override
        public ObjectBuffer _getBuffer() {
            return new ObjectBuffer(16);
        }

        @Override
        public ObjectReader _reader() {
            return reader;
        }

        @Override
        public JavaType _constructType(Class<?> raw) throws MappingException {
            return null;
        }

        @Override
        public JavaType findConvertingContentType(Class<?> type, JsonValueType valueType) throws MappingException {
            return null;
        }

        @Override
        public JsonDeserializer<String> findContextualValueDeserializer(Class<?> type, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public Object handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty property) throws JsonMappingException {
            return deser;
        }

        @Override
        public void reportBindingAndDiagnostics(JsonMappingException e) throws JsonMappingException {
        }

        @Override
        public void reportError(JsonParseException e) throws IOException {
        }

        @Override
        public void reportError(MappingException e) throws IOException {
        }

        @Override
        public void reportError(JsonMappingException e, Throwable t) throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "custom-deserialized";
        }
    }

    static {
        mapper = new ObjectMapper();
        reader = mapper.readerForValueType(String[].class);
    }
}