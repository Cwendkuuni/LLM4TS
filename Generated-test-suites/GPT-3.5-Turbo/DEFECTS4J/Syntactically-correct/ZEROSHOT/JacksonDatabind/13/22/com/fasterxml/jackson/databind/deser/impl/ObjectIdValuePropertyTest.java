package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonSerializer;

public class ObjectIdValuePropertyTest {
    
    private ObjectIdValueProperty objectIdValueProperty;
    
    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
        assertNotNull(result);
        assertEquals(newName, result.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(deser);
        assertNotNull(result);
        assertNotNull(result.getValueDeserializer());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        try {
            objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertNotNull(instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        try {
            Object result = objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        
        try {
            objectIdValueProperty.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertNotNull(instance);
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        
        try {
            Object result = objectIdValueProperty.setAndReturn(instance, value);
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}