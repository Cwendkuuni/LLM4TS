package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor1() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testConstructor2() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(deser, property._valueDeserializer);
    }
    
    @Test
    public void testConstructor3() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(obj, propertyName);
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(propertyName, property._valueDeserializer);
    }
    
    @Test
    public void testConstructor4() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(obj, propertyName);
        assertNotNull(property);
        assertEquals(propertyName, property._valueDeserializer);
    }
    
    @Test
    public void testWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertNotNull(newProperty);
        assertEquals(newName, newProperty._propName);
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);
        assertNotNull(newProperty);
        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getAnnotation(SomeAnnotation.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        try {
            Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            Object result = property.setAndReturn(instance, value);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}