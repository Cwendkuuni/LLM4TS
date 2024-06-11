package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() {
        // Test case 1
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromObject(p, ctxt);
        
        // Verify the result
        assertNull(result);
        
        // Test case 2
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromObject(p, ctxt);
        
        // Verify the result
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedForId() {
        // Test case 1
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedForId(p, ctxt, tb);
        
        // Verify the result
        assertNull(result);
        
        // Test case 2
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedForId(p, ctxt, tb);
        
        // Verify the result
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        // Test case 1
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        
        // Verify the result
        assertNull(result);
        
        // Test case 2
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        
        // Verify the result
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // Test case 1
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromAny(p, ctxt);
        
        // Verify the result
        assertNull(result);
        
        // Test case 2
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromAny(p, ctxt);
        
        // Verify the result
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() {
        // Test case 1
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromArray(p, ctxt);
        
        // Verify the result
        assertNull(result);
        
        // Test case 2
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromArray(p, ctxt);
        
        // Verify the result
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() {
        // Test case 1
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromScalar(p, ctxt);
        
        // Verify the result
        assertNull(result);
        
        // Test case 2
        // Initialize test input
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        // Call method under test
        Object result = AsPropertyTypeDeserializer.deserializeTypedFromScalar(p, ctxt);
        
        // Verify the result
        assertNull(result);
    }
}