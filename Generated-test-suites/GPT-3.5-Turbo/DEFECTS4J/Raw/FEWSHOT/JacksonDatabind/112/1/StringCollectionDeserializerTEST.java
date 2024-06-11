package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class StringCollectionDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null;
        DeserializationContext context = null;
        Collection<String> result = new ArrayList<String>();
        assertNull(deserializer.deserialize(parser, context, result));

        deserializer = new StringCollectionDeserializer(null, null, null, null, null, false);
        assertEquals(result, deserializer.deserialize(parser, context, result));
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null;
        DeserializationContext context = null;
        Collection<String> result = new ArrayList<String>();
        JsonDeserializer<String> valueDeserializer = null;
        assertNull(deserializer.deserializeUsingCustom(parser, context, result, valueDeserializer));

        valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        result.add("test");
        assertEquals(result, deserializer.deserializeUsingCustom(parser, context, result, valueDeserializer));
    }

    @Test
    public void testHandleNonArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null;
        DeserializationContext context = null;
        Collection<String> result = new ArrayList<String>();
        assertTrue(deserializer.handleNonArray(parser, context, result) instanceof Collection);

        deserializer = new StringCollectionDeserializer(null, null, null, null, null, false);
        assertEquals(result, deserializer.handleNonArray(parser, context, result));
    }
}