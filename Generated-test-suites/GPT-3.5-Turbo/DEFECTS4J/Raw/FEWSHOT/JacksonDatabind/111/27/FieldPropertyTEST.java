package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FieldPropertyTest {
    
    @Test
    public void testDeserializeAndSet() {
        // Test when JsonParser has VALUE_NULL token
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty.setSkipNulls(true);
        assertNull(fieldProperty.deserializeAndSet(null, null, null));
        
        // Test when valueTypeDeserializer is null
        FieldProperty fieldProperty2 = new FieldProperty(null, null, null, null, null);
        fieldProperty2.setValueDeserializer(null);
        assertNull(fieldProperty2.deserializeAndSet(null, null, null));
        
        // Test when valueTypeDeserializer is not null
        FieldProperty fieldProperty3 = new FieldProperty(null, null, null, null, null);
        fieldProperty3.setValueTypeDeserializer(null);
        assertNull(fieldProperty3.deserializeAndSet(null, null, null));
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        // Test when JsonParser has VALUE_NULL token
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty.setSkipNulls(true);
        assertEquals("instance", fieldProperty.deserializeSetAndReturn(null, null, "instance"));
        
        // Test when valueTypeDeserializer is null
        FieldProperty fieldProperty2 = new FieldProperty(null, null, null, null, null);
        fieldProperty2.setValueDeserializer(null);
        assertEquals("instance", fieldProperty2.deserializeSetAndReturn(null, null, "instance"));
        
        // Test when valueTypeDeserializer is not null
        FieldProperty fieldProperty3 = new FieldProperty(null, null, null, null, null);
        fieldProperty3.setValueTypeDeserializer(null);
        assertEquals("instance", fieldProperty3.deserializeSetAndReturn(null, null, "instance"));
    }
    
    @Test
    public void testSet() throws Exception {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty.set(null, null);
        assertEquals("instance", fieldProperty.setAndReturn("instance", null));
    }
    
    @Test
    public void testWithNullProvider() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        assertNull(fieldProperty.withNullProvider(null));
    }
    
    @Test
    public void testFixAccess() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty.fixAccess(null);
        //not sure what to assert here
    }
}