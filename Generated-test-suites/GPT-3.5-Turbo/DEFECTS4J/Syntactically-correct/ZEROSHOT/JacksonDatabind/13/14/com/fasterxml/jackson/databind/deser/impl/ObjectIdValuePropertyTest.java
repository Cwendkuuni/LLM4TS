package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    
    private ObjectIdValueProperty property;
    
    @Before
    public void setUp() {
        property = new ObjectIdValueProperty(null, null);
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
    }
    
    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new ObjectMapper().getFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();
        
        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new ObjectMapper().getFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();
        
        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        
        try {
            property.set(instance, value);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        
        try {
            property.setAndReturn(instance, value);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}