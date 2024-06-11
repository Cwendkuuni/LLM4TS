package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Tests for {@link StringArrayDeserializer} class.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static ObjectBuffer buffer;

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jp = createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSimpleArray() throws IOException {
        JsonParser jp = createParser("[\"foo\", \"bar\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());

        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithNullValue() throws IOException {
        JsonParser jp = createParser("[\"foo\", null, \"bar\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());

        assertArrayEquals(new String[]{"foo", null, "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithCustomElementDeserializer() throws IOException {
        JsonDeserializer<String> elementDeserializer = new TestStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializer.deserialize(createParser("[\";foo\", \"bar\"]"), new TestDeserializationContext());

        assertArrayEquals(new String[]{"", "foo", "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithTypeDeserializer() throws IOException {
        TypeDeserializer typeDeserializer = new TestTypeDeserializer<>();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(createParser("[\", \"foo\", \"bar\"]"), new TestDeserializationContext(), typeDeserializer);

        assertArrayEquals(new String[]{"", "foo", "bar"}, result);
    }

    @Test
    public void testDeserializeNonArray() throws IOException {
        JsonParser jp = createParser("\"foo\"");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jp, new TestDeserializationContext());

        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testCreateContextualDeserializerWithNullElementDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(ctxt, null);

        assertNotNull(result);
    }

    @Test
    public void testCreateContextualDeserializerWithNonStringElementDeserializer() throws JsonMappingException {
        JsonDeserializer<Number> numberDeserializer = new TestNumberDeserializer();
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(ctxt, new StdFieldProperty("test", String[].class));

        assertInstanceOf(StringArrayDeserializer.class, result);
        assertEquals(numberDeserializer, result._elementDeserializer);
    }

    @Test
    public void testCreateContextualDeserializerWithStringElementDeserializer() throws JsonMappingException {
        JsonDeserializer<String> stringDeserializer = new TestStringDeserializer();
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(ctxt, new StdFieldProperty("test", String[].class));

        assertInstanceOf(StringArrayDeserializer.class, result);
        assertNull(result._elementDeserializer);
    }

    @Test
    public void testCreateContextualDeserializerWithDefaultStringDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(ctxt, new StdFieldProperty("test", String[].class));

        assertInstanceOf(StringArrayDeserializer.class, result);
        assertNull(result._elementDeserializer);
    }

    @Test
    public void testCreateContextualDeserializerWithContentConverter() throws JsonMappingException {
        JsonDeserializer<String> stringDeserializer = new TestStringDeserializer();
        DeserializationContext ctxt = new TestDeserializationContext();
        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(ctxt, new StdFieldProperty("test", String[].class));

        JsonDeserializer<String[]> deserializer = new TestContentConverterStringArrayDeserializer(stringDeserializer);
        result = result.createContextual(ctxt, new StdFieldProperty("test", String[].class));

        assertInstanceOf(StringArrayDeserializer.class, result);
        assertEquals(deserializer, result);
    }

    private JsonParser createParser(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(content.getBytes()));
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public ObjectBuffer leaseObjectBuffer(int sizeHint) {
            if (buffer == null) {
                buffer = new ObjectBuffer(16);
            }
            return buffer;
        }
    }

    private static class TestStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "foo";
        }
    }

    private static class TestNumberDeserializer extends StdDeserializer<Number> {
        @Override
        public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return 42;
        }
    }

    private static class TestTypeDeserializer<T> implements TypeDeserializer {
        @Override
        public T deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, TypeDeserializer elementDeserializer) throws IOException {
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();

            int ix = 0;
            JsonToken t;

            while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
                T value;
                if (t == JsonToken.VALUE_NUMBER_INT) {
                    value = (T) Integer.valueOf(p.getIntValue());
                } else if (t == JsonToken.VALUE_STRING) {
                    value = (T) p.getText();
                } else {
                    value = elementDeserializer.deserializeTypedFromArray(p, ctxt, null);
                }

                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }

            T[] result = (T[]) buffer.completeAndClearBuffer(chunk, ix, ctxt.constructType(T[].class));
            ctxt.returnObjectBuffer(buffer);

            return result[0];
        }

        @Override
        public void reset() {
            // no-op
        }
    }

    private static class TestContentConverterStringArrayDeserializer extends StringArrayDeserializer {
        private final JsonDeserializer<String> _elementDeserializer;

        public TestContentConverterStringArrayDeserializer(JsonDeserializer<String> deserializer) {
            super(deserializer);
            _elementDeserializer = deserializer;
        }

        @Override
        protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();

            int ix = 0;
            JsonToken t;

            while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
                String value;
                if (t == JsonToken.VALUE_STRING) {
                    value = jp.getText();
                } else {
                    value = _elementDeserializer.deserialize(jp, ctxt);
                }

                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }

            String[] result = buffer.completeAndClearBuffer(chunk, ix, String[].class);
            ctxt.returnObjectBuffer(buffer);

            return result;
        }
    }
}