
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        String json = "[1, 2, 3, 4, 5]";
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        Collection<Object> result = new ArrayList<>();
        deserializer.deserialize(new com.fasterxml.jackson.core.json.JsonFactory().createParser(json), null, result);
        assertEquals(1, result.toArray()[0]);
        assertEquals(2, result.toArray()[1]);
        assertEquals(3, result.toArray()[2]);
        assertEquals(4, result.toArray()[3]);
        assertEquals(5, result.toArray()[4]);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String json = "[1, 2, 3, 4, 5]";
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        deserializer.deserializeWithType(new com.fasterxml.jackson.core.json.JsonFactory().createParser(json), null, null);
        // Assert if there are no exceptions thrown during deserialization
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String json = "5";
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        Collection<Object> result = new ArrayList<>();
        deserializer.handleNonArray(new com.fasterxml.jackson.core.json.JsonFactory().createParser(json), null, result);
        assertEquals(5, result.toArray()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleNonArrayWithInvalidInput() throws IOException {
        String json = "{\"a\": 5}";
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        Collection<Object> result = new ArrayList<>();
        deserializer.handleNonArray(new com.fasterxml.jackson.core.json.JsonFactory().createParser(json), null, result);
    }
}
