package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    CollectionDeserializer collectionDeserializer;

    @Before
    public void setUp() {
        collectionDeserializer = new CollectionDeserializer(null,
                null, null, null);
    }

    @Test
    public void testIsValid() {
        assertTrue(collectionDeserializer.isValid(null));
    }

    @Test
    public void testGetValueClass() {
        assertEquals(Collection.class, collectionDeserializer.getValueClass());
    }

    @Test
    public void testGetContentType() {
        assertNull(collectionDeserializer.getContentType());
    }

    @Test
    public void testGetContentDeserializer() {
        assertNull(collectionDeserializer.getContentDeserializer());
    }

    @Test
    public void testDeserialize() throws Exception {
        // Test for deserialize(JsonParser p, DeserializationContext ctxt, Collection<Object> result)
        fail("Not implemented");
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        // Test for deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
        fail("Not implemented");
    }
}