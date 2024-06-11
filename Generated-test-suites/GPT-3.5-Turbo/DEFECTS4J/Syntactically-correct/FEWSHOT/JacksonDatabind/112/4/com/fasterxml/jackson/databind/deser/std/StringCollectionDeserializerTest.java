package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class StringCollectionDeserializerTest {
    private StringCollectionDeserializer stringCollectionDeserializer;

    @Before
    public void setUp() {
        stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Collection<String> result = new ArrayList<>();
        assertEquals(result, stringCollectionDeserializer.deserialize(p, ctxt, result));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        assertNull(stringCollectionDeserializer.deserializeWithType(p, ctxt, typeDeserializer));
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Collection<String> result = new ArrayList<>();
        assertEquals(result, stringCollectionDeserializer.handleNonArray(p, ctxt, result));
    }
}