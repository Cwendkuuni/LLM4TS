package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        // Typical use case: map with entries
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null; // Not needed for this test
        SerializerProvider provider = null; // Not needed for this test
        
        writer.getAndSerialize(map, gen, provider);
        // No exception should be thrown, test passed
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_ValueNotMap() throws Exception {
        // Error scenario: value is not a map
        Object value = "value";
        
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null; // Not needed for this test
        SerializerProvider provider = null; // Not needed for this test
        
        writer.getAndSerialize(value, gen, provider);
        // JsonMappingException should be thrown, test passed
    }

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        // Edge case: value is null
        Object value = null;
        
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null; // Not needed for this test
        SerializerProvider provider = null; // Not needed for this test
        
        writer.getAndSerialize(value, gen, provider);
        // No exception should be thrown, test passed
    }

    @Test
    public void testGetAndFilter() throws Exception {
        // Typical use case: map with entries
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null; // Not needed for this test
        SerializerProvider provider = null; // Not needed for this test
        PropertyFilter filter = null; // Not needed for this test
        
        writer.getAndFilter(map, gen, provider, filter);
        // No exception should be thrown, test passed
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_ValueNotMap() throws Exception {
        // Error scenario: value is not a map
        Object value = "value";
        
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null; // Not needed for this test
        SerializerProvider provider = null; // Not needed for this test
        PropertyFilter filter = null; // Not needed for this test
        
        writer.getAndFilter(value, gen, provider, filter);
        // JsonMappingException should be thrown, test passed
    }

    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        // Edge case: value is null
        Object value = null;
        
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null; // Not needed for this test
        SerializerProvider provider = null; // Not needed for this test
        PropertyFilter filter = null; // Not needed for this test
        
        writer.getAndFilter(value, gen, provider, filter);
        // No exception should be thrown, test passed
    }
}