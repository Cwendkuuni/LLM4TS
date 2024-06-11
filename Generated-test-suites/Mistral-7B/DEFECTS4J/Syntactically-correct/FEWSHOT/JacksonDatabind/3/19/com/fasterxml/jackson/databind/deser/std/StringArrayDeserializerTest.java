package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;

/**
 * Test Cases for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser jp = createTestParser("[\n" +
                "  \"foo\",\n" +
                "  \"bar\",\n" +
                "  null,\n" +
                "  \"baz\"\n" +
                "]\n");

        String[] result = deserializer.deserialize(jp, new DeserializationContext());
        assertArrayEquals(new String[] {"foo", "bar", null, "baz"}, result);

        jp = createTestParser("[]");
        result = deserializer.deserialize(jp, new DeserializationContext());
        assertArrayEquals(EMPTY_STRING_ARRAY, result);

        jp = createTestParser("null");
        result = deserializer.deserialize(jp, new DeserializationContext());
        assertNull(result);

        jp = createTestParser("\"foo\"");
        result = deserializer.deserialize(jp, new DeserializationContext());
        assertArrayEquals(new String[] {"foo"}, result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        TypeDeserializer typeDeserializer = new None();

        String[] result = deserializer.deserializeWithType(createTestParser("[\n" +
                "  \"foo\",\n" +
                "  \"bar\",\n" +
                "  null,\n" +
                "  \"baz\"\n" +
                "]\n"), new DeserializationContext(), typeDeserializer);
        assertArrayEquals(new String[] {"foo", "bar", null, "baz"}, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "custom-value";
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);

        String[] result = deserializer.deserialize(createTestParser("[\n" +
                "  null,\n" +
                "  \"foo\",\n" +
                "  \"bar\"\n" +
                "]\n"), new DeserializationContext());
        assertArrayEquals(new String[] {"custom-value", "custom-value", "foo"}, result);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser jp = createTestParser("{\"value\": \"foo\"}");
        deserializer.handleNonArray(jp, new DeserializationContext());
    }

    private JsonParser createTestParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

}