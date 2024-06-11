package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() {
        // Test case 1
        JsonParser jp1 = null;
        DeserializationContext ctxt1 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromObject(jp1, ctxt1));
        
        // Test case 2
        JsonParser jp2 = null;
        DeserializationContext ctxt2 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromObject(jp2, ctxt2));
        
        // Test case 3
        JsonParser jp3 = null;
        DeserializationContext ctxt3 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromObject(jp3, ctxt3));
        
        // Test case 4
        JsonParser jp4 = null;
        DeserializationContext ctxt4 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromObject(jp4, ctxt4));
        
        // Test case 5
        JsonParser jp5 = null;
        DeserializationContext ctxt5 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromObject(jp5, ctxt5));
    }
    
    @Test
    public void testDeserializeTypedFromArray() {
        // Test case 1
        JsonParser jp1 = null;
        DeserializationContext ctxt1 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromArray(jp1, ctxt1));
        
        // Test case 2
        JsonParser jp2 = null;
        DeserializationContext ctxt2 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromArray(jp2, ctxt2));
        
        // Test case 3
        JsonParser jp3 = null;
        DeserializationContext ctxt3 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromArray(jp3, ctxt3));
        
        // Test case 4
        JsonParser jp4 = null;
        DeserializationContext ctxt4 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromArray(jp4, ctxt4));
        
        // Test case 5
        JsonParser jp5 = null;
        DeserializationContext ctxt5 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromArray(jp5, ctxt5));
    }
    
    @Test
    public void testDeserializeTypedFromScalar() {
        // Test case 1
        JsonParser jp1 = null;
        DeserializationContext ctxt1 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromScalar(jp1, ctxt1));
        
        // Test case 2
        JsonParser jp2 = null;
        DeserializationContext ctxt2 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromScalar(jp2, ctxt2));
        
        // Test case 3
        JsonParser jp3 = null;
        DeserializationContext ctxt3 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromScalar(jp3, ctxt3));
        
        // Test case 4
        JsonParser jp4 = null;
        DeserializationContext ctxt4 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromScalar(jp4, ctxt4));
        
        // Test case 5
        JsonParser jp5 = null;
        DeserializationContext ctxt5 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromScalar(jp5, ctxt5));
    }
    
    @Test
    public void testDeserializeTypedFromAny() {
        // Test case 1
        JsonParser jp1 = null;
        DeserializationContext ctxt1 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromAny(jp1, ctxt1));
        
        // Test case 2
        JsonParser jp2 = null;
        DeserializationContext ctxt2 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromAny(jp2, ctxt2));
        
        // Test case 3
        JsonParser jp3 = null;
        DeserializationContext ctxt3 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromAny(jp3, ctxt3));
        
        // Test case 4
        JsonParser jp4 = null;
        DeserializationContext ctxt4 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromAny(jp4, ctxt4));
        
        // Test case 5
        JsonParser jp5 = null;
        DeserializationContext ctxt5 = null;
        assertNull(AsWrapperTypeDeserializer.deserializeTypedFromAny(jp5, ctxt5));
    }
}