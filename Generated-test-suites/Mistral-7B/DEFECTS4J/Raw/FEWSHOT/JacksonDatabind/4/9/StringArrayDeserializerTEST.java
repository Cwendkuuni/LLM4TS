package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for {@link StringArrayDeserializer}.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final StringArrayDeserializer standardDeserializer = StringArrayDeserializer.instance;

    /**
     * Verify deserialization of simple JSON array of strings.
     */
    @Test
    public void testDeserializeSimpleArray() throws IOException {
        final String json = "[\"foo\", \"bar\", \"baz\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final String[] result = standardDeserializer.deserialize(parser, new DeserializationContext());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    /**
     * Verify handling of null JSON array.
     */
    @Test
    public void testDeserializeNullArray() throws IOException {
        final JsonParser parser = new JsonFactory().createParser(new StringReader("null"));
        final String[] result = standardDeserializer.deserialize(parser, new DeserializationContext());
        assertArrayEquals(new String[0], result);
    }

    /**
     * Verify handling of single JSON value that is a string.
     */
    @Test
    public void testDeserializeSingleValue() throws IOException {
        final String json = "\"foo\"";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final String[] result = standardDeserializer.deserialize(parser, new DeserializationContext());
        assertEquals("foo", result[0]);
    }

    /**
     * Verify handling of single JSON value that is null.
     */
    @Test
    public void testDeserializeNullValue() throws IOException {
        final JsonParser parser = new JsonFactory().createParser(new StringReader("null"));
        final String[] result = standardDeserializer.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    /**
     * Verify handling of JSON array with empty string as the only element.
     */
    @Test
    public void testDeserializeEmptyArray() throws IOException {
        final String json = "[\"\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final String[] result = standardDeserializer.deserialize(parser, new DeserializationContext());
        assertEquals(new String[0], result);
    }

    /**
     * Verify handling of JSON array with a mix of strings and null values.
     */
    @Test
    public void testDeserializeMixedArray() throws IOException {
        final String json = "[\"foo\", null, \"bar\", null, \"baz\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final String[] result = standardDeserializer.deserialize(parser, new DeserializationContext());
        assertArrayEquals(new String[]{"foo", null, "bar", null, "baz"}, result);
    }

    /**
     * Verify handling of JSON array with a custom value deserializer.
     */
    @Test
    public void testDeserializeCustomValueDeserializer() throws IOException {
        final JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        };
        final StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        final String json = "[\"foo\", \"bar\", \"baz\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final String[] result = deserializer.deserialize(parser, new DeserializationContext());
        assertArrayEquals(new String[]{"FOO", "BAR", "BAZ"}, result);
    }

    /**
     * Verify handling of JSON array with custom deserializer and a content converter.
     */
    @Test
    public void testDeserializeCustomDeserializerAndContentConverter() throws IOException {
        final JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        };
        final MyContentConverter contentConverter = new MyContentConverter();
        final StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer, contentConverter);
        final String json = "[1, 2, 3]"; // Note: not a JSON array of strings
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final String[] result = deserializer.deserialize(parser, new DeserializationContext());
        assertArrayEquals(new String[]{"1", "2", "3"}, result);
    }

    /**
     * Verify handling of JSON array with contextual deserializer.
     */
    @Test
    public void testDeserializeContextualDeserializer() throws IOException {
        final JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText() + "_contextual";
            }
        };
        final StringArrayDeserializer deserializer = new StringArrayDeserializer();
        final Object context = new Object();
        final JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new DeserializationContext(), null);
        final String json = "[\"foo\", \"bar\", \"baz\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final JsonDeserializer<String[]> contextualDeserializerWithContext = (JsonDeserializer<String[]>) contextualDeserializer.contextualize(context);
        final String[] result = contextualDeserializerWithContext.deserialize(parser, new DeserializationContext());
        assertArrayEquals(new String[]{"foo_contextual", "bar_contextual", "baz_contextual"}, result);
    }

    /**
     * Verify handling of JSON array with custom deserializer and type deserializer.
     */
    @Test
    public void testDeserializeCustomDeserializerAndTypeDeserializer()
            throws IOException {
        final JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        };
        final TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                ObjectBuffer buffer = ctxt.leaseObjectBuffer();
                final JsonDeserializer<String> deserializer = new StringArrayDeserializer(stringDeserializer);
                final Object[] result = deserializer.deserialize(p, ctxt);
                buffer.completeAndClearBuffer(result, result.length, String[].class);
                return result;
            }
        };
        final String json = "[\"foo\", \"bar\", \"baz\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final Object result = typeDeserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        assertEquals(new String[]{"FOO", "BAR", "BAZ"}, result);
    }

    /**
     * Verify handling of JSON array with custom deserializer but no type deserializer.
     */
    @Test
    public void testDeserializeCustomDeserializerNoTypeDeserializer() throws IOException {
        final JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        };
        final StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        final String json = "[\"foo\", \"bar\", \"baz\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        final Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), null);
        assertEquals(new String[]{"FOO", "BAR", "BAZ"}, result);
    }

    /**
     * Verify handling of JSON array that is not an array (throws {@link IOException}).
     */
    @Test(expected = IOException.class)
    public void testDeserializeNonArray() throws IOException {
        final String json = "{\"foo\": \"bar\"}";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        standardDeserializer.deserialize(parser, new DeserializationContext());
    }

    /**
     * Verify handling of JSON array with an invalid element type.
     */
    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidElementType() throws IOException {
        final JsonParser parser = new JsonFactory().createParser(new StringReader("[1, 2, 3]"));
        standardDeserializer.deserialize(parser, new DeserializationContext());
    }

    /**
     * Verify handling of JSON array with an invalid element type when a type deserializer is used.
     */
    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidElementTypeWithTypeDeserializer() throws IOException {
        final TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new JsonProcessingException("Invalid element type in JSON array");
            }
        };
        final String json = "[\"foo\", \"bar\", \"baz\"]";
        final JsonParser parser = new JsonFactory().createParser(new StringReader(json));
        standardDeserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
    }
}