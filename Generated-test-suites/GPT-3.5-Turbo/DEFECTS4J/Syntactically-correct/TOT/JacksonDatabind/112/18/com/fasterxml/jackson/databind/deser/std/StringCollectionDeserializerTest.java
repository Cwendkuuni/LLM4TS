
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize_NormalCase() throws IOException {
        String input = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = new JsonFactory().createParser(input);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,
                null, null);

        Collection<String> result = deserializer.deserialize(parser, ctxt);
        Collection<String> expected = Arrays.asList("value1", "value2", "value3");

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_EmptyCollection() throws IOException {
        String input = "[]";
        JsonParser parser = new JsonFactory().createParser(input);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,
                null, null);

        Collection<String> result = deserializer.deserialize(parser, ctxt);
        Collection<String> expected = new ArrayList<>();

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_NullValue() throws IOException {
        String input = "[null]";
        JsonParser parser = new JsonFactory().createParser(input);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,
                null, null);

        Collection<String> result = deserializer.deserialize(parser, ctxt);
        Collection<String> expected = new ArrayList<>();
        expected.add(null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_InvalidFormat() throws IOException {
        String input = "{ \"key\": \"value\" }";
        JsonParser parser = new JsonFactory().createParser(input);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,
                null, null);

        try {
            deserializer.deserialize(parser, ctxt);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // expected
        }
    }
}

