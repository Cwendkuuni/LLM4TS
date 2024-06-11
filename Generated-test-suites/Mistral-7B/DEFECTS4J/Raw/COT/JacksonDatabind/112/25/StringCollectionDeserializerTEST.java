package com.example.tests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringCollectionDeserializerTest {

    @Test
    public void testStringCollectionDeserializer_default() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[\"hello\", \"world\"]");

        List<String> result = deserializer.deserialize(parser, null);

        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testStringCollectionDeserializer_withCustomValueDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null,
                new JsonDeserializer<String>() {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsString();
                    }
                },
                null, null, null);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[\"hello\", \"world\"]");

        List<String> result = deserializer.deserialize(parser, null);

        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testStringCollectionDeserializer_withDelegateDeserializer() throws IOException {
        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, delegateDeserializer, null, null, null);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[1, 2, 3]");

        List<String> result = deserializer.deserialize(parser, null);

        assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializer_whenInputIsNotArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("{\"name\": \"John\"}");

        deserializer.deserialize(parser, null);
    }

    @Test
    public void testStringCollectionDeserializer_whenNullValueProviderIsUsed() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null,
                new StandardNullValueProvider(), null);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[null]");

        List<String> result = deserializer.deserialize(parser, null);

        assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testStringCollectionDeserializer_whenEmptyInputIsUsed() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[]");

        List<String> result = deserializer.deserialize(parser, null);

        assertEquals(Collections.emptyList(), result);
    }
}

class StdDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}