package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

/**
 * Test cases for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String[] emptyArray = mapper.readValue("[]", String[].class);
        assertArrayEquals(new String[0], emptyArray);
    }

    @Test
    public void testStringArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String[] array = mapper.readValue("[\"foo\", \"bar\", null, \"baz\"]", String[].class);
        assertArrayEquals(new String[]{"foo", "bar", null, "baz"}, array);
    }

    @Test
    public void testStringArrayDeserializationWithCustomElementDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = mapper.getFactory().createValueDeserializer(String.class, new SimpleModule().addDeserializer(String.class, new CustomStringDeserializer()));
        String[] array = mapper.readValue("[\"foo\", \"bar\", null, \"baz\"]", String[].class, new TypeFactory().constructType(String[].class, String.class));
        assertArrayEquals(new String[]{"customFoo", "customBar", null, "customBaz"}, array);
    }

    @Test
    public void testDeserializationWithTypeHandling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String[] array = mapper.readValue("[1, 2, 3]", String[].class, new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsInt();
            }
        });
        assertArrayEquals(new String[]{"1", "2", "3"}, array);
    }

    @Test(expected = IOException.class)
    public void testDeserializationException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue("[]", Integer[].class);
    }

    @Test
    public void testDeserializationContextualization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = mapper.getFactory().createValueDeserializer(String.class, new SimpleModule().addDeserializer(String.class, new CustomStringDeserializer()));
        StringArrayDeserializer deserializer = mapper.readerForArrayOf(String.class).withValueDeserializer(customDeserializer);
        String[] array = deserializer.readValue("[\"foo\", \"bar\", null, \"baz\"]");
        assertArrayEquals(new String[]{"customFoo", "customBar", null, "customBaz"}, array);
    }

    private static class CustomStringDeserializer extends StdDeserializer<String> {
        public CustomStringDeserializer() {
            this(String.class);
        }

        public CustomStringDeserializer(Class<String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "custom" + jp.getValueAsString();
        }
    }
}