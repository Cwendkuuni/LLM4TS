package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> deserializer;
    private ObjectIdValueProperty property;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        propertyMetadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);
        assertNotNull(newProperty);
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        property.deserializeAndSet(p, ctxt, instance);
        
        // Make assertions about the behavior of the method
        
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        
        // Make assertions about the behavior of the method
        
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        property.set(instance, value);
        
        // Make assertions about the behavior of the method
        
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        Object result = property.setAndReturn(instance, value);
        
        // Make assertions about the behavior of the method
        
    }

}