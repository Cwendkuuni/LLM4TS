package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserializeArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(null, null);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeCustom() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer._deserializeCustom(null, null);
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(null, null);
        assertNull(result);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(null, null);
        assertNull(result);
    }
}