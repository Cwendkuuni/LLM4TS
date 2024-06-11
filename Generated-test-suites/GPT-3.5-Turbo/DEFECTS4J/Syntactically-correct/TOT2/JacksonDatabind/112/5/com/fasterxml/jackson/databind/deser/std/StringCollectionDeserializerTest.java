package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"value1\", \"value2\"]");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Collection<String> result = deserializer.deserialize(jsonParser, ctxt);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Collection<String> result = deserializer.deserialize(jsonParser, ctxt);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[null]");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Collection<String> result = deserializer.deserialize(jsonParser, ctxt);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeException() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"value1\", \"value2\"}");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        deserializer.deserialize(jsonParser, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"value1\", \"value2\"]");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        List<String> result = (List<String>) deserializer.deserializeWithType(jsonParser, ctxt, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testDeserializeWithContextual() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        StringCollectionDeserializer contextualDeserializer = (StringCollectionDeserializer) deserializer.createContextual(ctxt, null);

        assertNotNull(contextualDeserializer);
        assertSame(deserializer, contextualDeserializer);
    }

}