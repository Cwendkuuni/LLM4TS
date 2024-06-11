package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor() {
        // Create an ObjectIdReader object and a PropertyMetadata object
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        
        // Create an ObjectIdValueProperty object using the constructor
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // Assert the values of the ObjectIdValueProperty object
        assertEquals("propertyName", property.propertyName);
        assertEquals(Object.class, property.getIdType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(Object.class, property.getDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithConstructor() {
        // Create a source ObjectIdValueProperty object and a JsonDeserializer object
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        // Create a new ObjectIdValueProperty object using the withConstructor method
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        
        // Assert the values of the ObjectIdValueProperty object
        assertEquals(src.propertyName, property.propertyName);
        assertEquals(src.getIdType(), property.getIdType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testWithPropertyName() {
        // Create a source ObjectIdValueProperty object and a new PropertyName object
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName("newName");
        
        // Create a new ObjectIdValueProperty object using the withPropertyName method
        ObjectIdValueProperty property = src.withName(newName);
        
        // Assert the values of the ObjectIdValueProperty object
        assertEquals(newName, property.propertyName);
        assertEquals(src.getIdType(), property.getIdType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getDeserializer(), property.getDeserializer());
        assertEquals(src._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testDeserializeAndSet() {
        // Create a JsonParser, DeserializationContext, and an instance object
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Create an ObjectIdValueProperty object
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        
        // Call the deserializeAndSet method
        property.deserializeAndSet(p, ctxt, instance);
        
        // Assert that the method returns null
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }
    
    @Test
    public void testSetAndReturn() {
        // Create a JsonParser, DeserializationContext, and an instance object
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Create an ObjectIdValueProperty object
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        
        // Call the setAndReturn method
        property.setAndReturn(p, ctxt, instance);
        
        // Assert that the method returns the instance object
        assertEquals(instance, property.setAndReturn(p, ctxt, instance));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        // Create an instance object
        Object instance = new Object();
        
        // Create an ObjectIdValueProperty object
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        
        // Call the set method
        property.set(instance, null);
    }
}