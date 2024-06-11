package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        Collection<String> result = deserializer.deserialize(null, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeUsingCustom() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        Collection<String> result = deserializer.deserializeUsingCustom(null, null, null, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        Object result = deserializer.deserializeWithType(null, null, null);
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        Collection<String> result = deserializer.handleNonArray(null, null, null);
        assertNull(result);
    }

    @Test
    public void testGetValueInstantiator() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        ValueInstantiator result = deserializer.getValueInstantiator();
        assertNull(result);
    }

    @Test
    public void testGetContentDeserializer() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        JsonDeserializer<Object> result = deserializer.getContentDeserializer();
        assertNull(result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        JsonDeserializer<?> result = deserializer.createContextual(null, null);
        assertNull(result);
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null,null,null,null,null,null);
        boolean result = deserializer.isCachable();
        assertTrue(result);
    }

}