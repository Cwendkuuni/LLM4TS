package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        assertEquals(null, deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        assertEquals(null, deserializer.deserializeWithType(null, null, null));
    }

    @Test
    public void testHandleNonArray() {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        assertEquals(null, deserializer.handleNonArray(null, null, null));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        assertEquals(null, deserializer.createContextual(null, null));
    }

    @Test
    public void testGetContentDeserializer() {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        assertNull(deserializer.getContentDeserializer());
    }

    @Test
    public void testGetContentType() {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        assertNull(deserializer.getContentType());
    }

    @Test
    public void testDeserializeWithReferring() throws IOException {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        CollectionDeserializer.CollectionReferringAccumulator accumulator = deserializer.new CollectionReferringAccumulator(null, null);
        CollectionDeserializer.CollectionReferring referring = deserializer.new CollectionReferring(accumulator, null, null);
        referring.handleResolvedForwardReference(null, null);
        assertNull(accumulator.next.get(0));
    }

    @Test
    public void testResolveForwardReference() throws IOException {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        CollectionDeserializer.CollectionReferringAccumulator accumulator = deserializer.new CollectionReferringAccumulator(null, null);
        accumulator.resolveForwardReference(null, null);
        assertNull(accumulator.next.get(0));
    }
}