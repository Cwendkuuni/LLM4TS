package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithNullValueProvider() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        NullValueProvider nva = new NullValueProvider();
        
        SettableBeanProperty result = property.withNullProvider(nva);
        
        assertEquals(property.getName(), result.getName());
        assertEquals(property.getType(), result.getType());
        assertEquals(property.getMetadata(), result.getMetadata());
        assertEquals(property.getValueDeserializer(), result.getValueDeserializer());
        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testWithNewName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        PropertyName newName = new PropertyName("newName");
        
        SettableBeanProperty result = property.withName(newName);
        
        assertEquals(newName, result.getName());
        assertEquals(property.getType(), result.getType());
        assertEquals(property.getMetadata(), result.getMetadata());
        assertEquals(property.getValueDeserializer(), result.getValueDeserializer());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        SettableBeanProperty result = property.withValueDeserializer(deser);
        
        assertEquals(property.getName(), result.getName());
        assertEquals(property.getType(), result.getType());
        assertEquals(property.getMetadata(), result.getMetadata());
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            // handle exception
        }
        
        // add assert statements
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            // handle exception
        }
        
        // add assert statements
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        try {
            property.set(instance, value);
        } catch (IOException e) {
            // handle exception
        }
        
        // add assert statements
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        try {
            property.setAndReturn(instance, value);
        } catch (IOException e) {
            // handle exception
        }
        
        // add assert statements
    }
}