package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deser = new StringArrayDeserializer();
        
        // Test case 1
        String[] expected1 = { "foo", "bar" };
        String[] result1 = deser.deserialize(jp, ctxt);
        assertArrayEquals(expected1, result1);
        
        // Test case 2
        String[] expected2 = { "abc", "def", "ghi" };
        String[] result2 = deser.deserialize(jp, ctxt);
        assertArrayEquals(expected2, result2);
        
        // Test case 3
        String[] expected3 = { "" };
        String[] result3 = deser.deserialize(jp, ctxt);
        assertArrayEquals(expected3, result3);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deser = new StringArrayDeserializer();
        
        // Test case 1
        Object expected1 = new Object();
        Object result1 = deser.deserializeWithType(jp, ctxt, typeDeserializer);
        assertEquals(expected1, result1);
        
        // Test case 2
        Object expected2 = null;
        Object result2 = deser.deserializeWithType(jp, ctxt, typeDeserializer);
        assertEquals(expected2, result2);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deser = new StringArrayDeserializer();
        
        // Test case 1
        String[] expected1 = { "foo" };
        String[] result1 = deser.handleNonArray(jp, ctxt);
        assertArrayEquals(expected1, result1);
        
        // Test case 2
        String[] expected2 = { null };
        String[] result2 = deser.handleNonArray(jp, ctxt);
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringArrayDeserializer deser = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Test case 1
        JsonDeserializer<?> expected1 = null;
        JsonDeserializer<?> result1 = deser.createContextual(ctxt, property);
        assertEquals(expected1, result1);
        
        // Test case 2
        JsonDeserializer<?> expected2 = new StringArrayDeserializer();
        JsonDeserializer<?> result2 = deser.createContextual(ctxt, property);
        assertEquals(expected2, result2);
    }
}