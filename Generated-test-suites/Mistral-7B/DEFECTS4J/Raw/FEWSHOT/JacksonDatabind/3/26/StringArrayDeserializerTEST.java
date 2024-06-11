package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Tests for {@link StringArrayDeserializer}
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyArray() throws IOException {
        JsonParser jp = MAPPER.getFactory().createParser("[]");
        JsonDeserializer<String[]> deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArray() throws IOException {
        JsonParser jp = MAPPER.getFactory().createParser("['one']");
        JsonDeserializer<String[]> deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
        assertArrayEquals(new String[] {"one"}, result);
    }

    @Test
    public void testMultipleElementArray() throws IOException {
        JsonParser jp = MAPPER.getFactory().createParser("[\"one\", \"two\", \"three\"]");
        JsonDeserializer<String[]> deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
        assertArrayEquals(new String[] {"one", "two", "three"}, result);
    }

    @Test
    public void testNullArray() throws IOException {
        JsonParser jp = MAPPER.getFactory().createParser("null");
        JsonDeserializer<String[]> deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
        assertNull(result);
    }

    @Test
    public void testEmptyStringAsNull() throws IOException {
        JsonParser jp = MAPPER.getFactory().createParser("[\"\"]");
        JsonDeserializer<String[]> deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
        assertNull(result);
    }

    @Test
    public void testSingleElementArrayWithCustomDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + " custom";
            }
        };
        JsonParser jp = MAPPER.getFactory().createParser("['one']");
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
        assertArrayEquals(new String[] {"one custom"}, result);
    }

    @Test(expected = IOException.class)
    public void testNonArray() throws IOException {
        JsonParser jp = MAPPER.getFactory().createParser("{'one': 1}");
        JsonDeserializer<String[]> deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + " custom";
            }
        };
        JsonParser jp = MAPPER.getFactory().createParser("[\"one\", null, \"three\"]");
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = deserializer.deserialize(jp, new DeserializationContext(MAPPER.getConfig()));
        assertArrayEquals(new String[] {"one custom", null, "three custom"}, result);
    }

    @Test
    public void testTypeDeserialization() throws IOException {
        JsonParser jp = MAPPER.getFactory().createParser("[1, 2, 3]");
        JsonDeserializer<String[]> deserializer = StringArrayDeserializer.instance;
        TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(String[].class);
        String[] result = deserializer.deserializeWithType(jp, new DeserializationContext(MAPPER.getConfig()), typeDeserializer);
        assertArrayEquals(new String[0], result);
    }
}