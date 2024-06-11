package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, obj.getName());
        assertEquals(objectIdReader.getIdType(), obj.getType());
        assertEquals(metadata, obj.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), obj.getValueDeserializer());
        assertEquals(objectIdReader, obj._objectIdReader);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(src, deser);
        assertEquals(src.getName(), obj.getName());
        assertEquals(src.getType(), obj.getType());
        assertEquals(src.getMetadata(), obj.getMetadata());
        assertEquals(deser, obj.getValueDeserializer());
        assertEquals(src._objectIdReader, obj._objectIdReader);
    }

    @Test
    public void testDeprecatedConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, newName);
        assertEquals(objectIdReader.propertyName, obj.getName());
        assertEquals(objectIdReader.getIdType(), obj.getType());
        assertEquals(newName, obj.getFullName());
        assertEquals(objectIdReader.getDeserializer(), obj.getValueDeserializer());
        assertEquals(objectIdReader, obj._objectIdReader);
    }

    @Test
    public void testDeprecatedConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        String newName = "newName";
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, newName);
        assertEquals(objectIdReader.propertyName, obj.getName());
        assertEquals(objectIdReader.getIdType(), obj.getType());
        assertEquals(newName, obj.getFullName().getSimpleName());
        assertEquals(objectIdReader.getDeserializer(), obj.getValueDeserializer());
        assertEquals(objectIdReader, obj._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, newName);
        ObjectIdValueProperty obj = src.withName(newName);
        assertEquals(newName, obj.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty obj = src.withValueDeserializer(deser);
        assertEquals(deser, obj.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(obj.getAnnotation(Class.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(obj.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            obj.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            obj.deserializeSetAndReturn(jp, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            obj.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            obj.setAndReturn(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}