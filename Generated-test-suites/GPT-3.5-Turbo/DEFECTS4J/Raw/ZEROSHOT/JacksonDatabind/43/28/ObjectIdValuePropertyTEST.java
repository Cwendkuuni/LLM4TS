package com.fasterxml.jackson.databind.deser.impl;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {
    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertNotNull(newProperty);
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);
        assertNotNull(newProperty);
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        try {
            property.setAndReturn(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}