package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyArrayDeserialization() throws JsonProcessingException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = MAPPER.getFactory().createParser("[]");
        String[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArrayDeserialization() throws JsonProcessingException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = MAPPER.getFactory().createParser("[\"foo\"]");
        String[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testCustomElementDeserialization() throws JsonProcessingException {
        JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        JsonParser parser = MAPPER.getFactory().createParser("[\"foo\", \"bar\"]");
        String[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testNullElementDeserialization() throws JsonProcessingException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = MAPPER.getFactory().createParser("[null]");
        String[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertNull(result[0]);
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonProcessingException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = MAPPER.getFactory().createParser("[\"\"]");
        String[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testCustomDeserializationWithType() throws JsonProcessingException {
        JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class);
        TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        JsonParser parser = MAPPER.getFactory().createParser("[\"foo\", \"bar\"]");
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        String[] expectedResult = new String[]{"foo", "bar"};
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testNonArrayDeserialization() throws JsonProcessingException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = MAPPER.getFactory().createParser("{\"foo\": \"bar\"}");
        String[] result = deserializer.handleNonArray(parser, new DeserializationContext());
        assertArrayEquals(new String[]{"bar"}, result);
    }
}