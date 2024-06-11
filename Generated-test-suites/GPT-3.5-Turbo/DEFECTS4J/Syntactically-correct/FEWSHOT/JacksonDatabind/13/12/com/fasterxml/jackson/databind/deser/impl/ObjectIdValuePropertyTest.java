package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property.propertyName);
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertNull(property.getMember());
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        String newName = "newName";
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, newName);
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty.propertyName.getSimpleName());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        property.deserializeAndSet(jp, ctxt, instance);
        // assert something
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
        // assert something
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);
        // assert something
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();

        Object result = property.setAndReturn(instance, value);
        // assert something
    }
}