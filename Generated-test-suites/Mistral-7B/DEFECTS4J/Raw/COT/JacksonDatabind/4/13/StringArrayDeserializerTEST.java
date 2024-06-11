package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test for StringArrayDeserializer class
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeStandard() throws Exception {
        String json = "[\"Alice\", \"Bob\", \"Charlie\"]";
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[]{"Alice", "Bob", "Charlie"}, result);
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws Exception {
        String json = "[{\"name\": \"Alice\"}, {\"name\": \"Bob\"}, {\"name\": \"Charlie\"}]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[]{"Alice", "Bob", "Charlie"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidJson() throws Exception {
        String json = "[{";
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        mapper.readValue(json, deserializer.getType());
    }

    @Test
    public void testDeserializeWithEmptyJsonArray() throws Exception {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testDeserializeWithNullValue() throws Exception {
        String json = "[null]";
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeWithEmptyStringAsNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readValue("[\"\", \"Alice\", \"Bob\"]", deserializer.getType());
        assertArrayEquals(new String[]{null, "Alice", "Bob"}, result);
    }

    @Test
    public void testCreateContextualDeserializerWithNullElementDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        assertTrue(result instanceof StringArrayDeserializer);
    }

    @Test
    public void testCreateContextualDeserializerWithCustomElementDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, new StdName("testName"));
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(stringDeserializer, result.getNullValue());
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        String json = "[1, 2, 3]";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(StringSerializer.instance, String[].class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        Object result = deserializer.deserializeWithType(new JsonParser(new StringReader(json)), mapper.getDeserializationContext(), typeDeserializer);
        assertArrayEquals(new String[]{"1", "2", "3"}, (String[])result);
    }

    static class StringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    static class ArrayTypeDeserializer extends TypeDeserializer {
        private final JsonDeserializer<Object> _valueDeserializer;
        private final Class<?> _valueClass;

        public ArrayTypeDeserializer(JsonDeserializer<Object> valueDeserializer, Class<?> valueClass) {
            super(_valueClass, null);
            _valueDeserializer = valueDeserializer;
            _valueClass = valueClass;
        }

        @Override
        public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();
            int ix = 0;
            JsonToken t;

            while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
                Object value = _valueDeserializer.deserialize(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }

            Object result = buffer.completeAndClearBuffer(chunk, ix, _valueClass);
            ctxt.returnObjectBuffer(buffer);
            return result;
        }
    }
}