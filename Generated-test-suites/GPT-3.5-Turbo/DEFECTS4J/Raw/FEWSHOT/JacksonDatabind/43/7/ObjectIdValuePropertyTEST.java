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
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithDeser() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        assertEquals(src, property._objectIdReader);
        assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
        PropertyName newName = new PropertyName("new");
        ObjectIdValueProperty property = src.withName(newName);
        assertEquals(src, property._objectIdReader);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
        assertNull(property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        try {
            JsonParser p = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();
            ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
            Object instance = new Object();
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        try {
            JsonParser p = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();
            ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
            Object instance = new Object();
            property.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        try {
            ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
            Object instance = new Object();
            property.set(instance, new Object());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetAndReturn() {
        try {
            ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyName());
            Object instance = new Object();
            property.setAndReturn(instance, new Object());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}