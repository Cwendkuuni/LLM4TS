package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testGetName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName, property.getName());
    }

    @Test
    public void testGetType() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.getIdType(), property.getType());
    }

    @Test
    public void testGetMetadata() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(metadata, property.getMetadata());
    }

    @Test
    public void testGetValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty.getName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNewValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonDeserializer<?> newDeserializer = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);

        assertEquals(objectIdReader.propertyName, newProperty.getName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // TODO: Write test for this method
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // TODO: Write test for this method
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // TODO: Write test for this method
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // TODO: Write test for this method
    }

}