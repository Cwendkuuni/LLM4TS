package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for StringArrayDeserializer class
 */
public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue("[]", StringArrayDeserializer.class);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeArrayWithSingleElement() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue("[\"foo\"]", StringArrayDeserializer.class);
        assertArrayEquals(new String[] {"foo"}, result);
    }

    @Test
    public void testDeserializeArrayWithMultipleElements() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue("[\"foo\", \"bar\"]", StringArrayDeserializer.class);
        assertArrayEquals(new String[] {"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithNullElement() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue("[null]", StringArrayDeserializer.class);
        assertArrayEquals(new String[] {null}, result);
    }

    @Test
    public void testDeserializeArrayWithCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StubStringDeserializer();
        mapper.setDefaultTyping(DefaultTyping.NON_FINAL);
        String[] result = mapper.readValue("[\"foo\", \"bar\"]", String[].class, new StringArrayDeserializer(customDeserializer));
        assertArrayEquals(new String[] {"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithEmptyStringAsNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String[] result = mapper.readValue("[\"]", StringArrayDeserializer.class);
        assertArrayEquals(new String[] {null}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue("\"foo\"", StringArrayDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue("null", StringArrayDeserializer.class);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(String[].class);
        String[] result = mapper.readValue("[\"foo\", \"bar\"]", String[].class, typeDeserializer);
        assertArrayEquals(new String[] {"foo", "bar"}, result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue("[\"foo\", \"bar\"]", String[].class);
        JsonDeserializer<String> customDeserializer = new StubStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<String[]> contextualDeserializer = deserializer.createContextual(mapper.getDeserializationContext(), null);
        assertEquals(StringArrayDeserializer.class, contextualDeserializer.getClass());
        JsonDeserializer<String> deser = contextualDeserializer.getSerializers().get(0);
        assertEquals(customDeserializer.getClass(), deser.getClass());
    }

    @Test
    public void testDeserializeWithNullCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue("[\"foo\", \"bar\"]", StringArrayDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());
        JsonDeserializer<String> deser = deserializer.getSerializers().get(0);
        assertNull(deser);
    }
}

class StubStringDeserializer extends StdDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return "stub";
    }
}