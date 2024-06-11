package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(obj);
        assertEquals(objectIdReader, obj._objectIdReader);
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty obj = new ObjectIdValueProperty(src, deser);
        assertNotNull(obj);
        assertEquals(objectIdReader, obj._objectIdReader);
        assertEquals(deser, obj._valueDeserializer);
    }

    @Test
    public void testConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty obj = new ObjectIdValueProperty(src, newName);
        assertNotNull(obj);
        assertEquals(objectIdReader, obj._objectIdReader);
        assertEquals(newName, obj._propertyName);
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            obj.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            obj.deserializeSetAndReturn(jp, ctxt, instance);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        Object instance = new Object();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            obj.set(instance, instance);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        Object instance = new Object();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            obj.setAndReturn(instance, instance);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

}