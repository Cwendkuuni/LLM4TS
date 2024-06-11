package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        
        assertNotNull(property);
        assertEquals(reader, property._objectIdReader);
    }
    
    @Test
    public void testWithDeser() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonDeserializer<?> deser = null;
        ObjectIdValueProperty src = new ObjectIdValueProperty(reader, metadata);
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        PropertyName newName = null;
        ObjectIdValueProperty src = new ObjectIdValueProperty(reader, metadata);
        ObjectIdValueProperty property = src.withName(newName);
        
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        Object instance = null;
        Object value = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        try {
            property.set(instance, value);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // success
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        Object instance = null;
        Object value = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        try {
            property.setAndReturn(instance, value);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // success
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}