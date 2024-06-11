package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonParser jp = mapper.getFactory().createParser(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleValueArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\"]";
        JsonParser jp = mapper.getFactory().createParser(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertEquals("hello", result[0]);
        assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testDeserializeCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"foo\": \"bar\"}]";
        JsonParser jp = mapper.getFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.findValueDeserializer(String.class, "foo"));
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertEquals("bar", result[0]);
        assertArrayEquals(new String[]{"bar"}, result);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        JsonParser jp = mapper.getFactory().createParser(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertArrayEquals(new String[]{"null"}, result);
        assertEquals(null, result[0]);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"\"]";
        JsonParser jp = mapper.getFactory().createParser(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
        assertArrayEquals(new String[]{"", ""}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "invalidJson";
        JsonParser jp = mapper.getFactory().createParser(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArrayJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\": \"bar\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext()));
    }
}