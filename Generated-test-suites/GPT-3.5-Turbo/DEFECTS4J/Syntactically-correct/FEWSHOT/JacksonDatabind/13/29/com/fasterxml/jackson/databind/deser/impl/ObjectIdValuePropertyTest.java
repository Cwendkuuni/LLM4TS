package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);
        assertSame(reader.propertyName, prop.getName());
        assertSame(reader.getIdType(), prop.getType());
        assertSame(metadata, prop.getMetadata());
        assertSame(reader.getDeserializer(), prop.getValueDeserializer());

        ObjectIdValueProperty copy = new ObjectIdValueProperty(prop, new JsonDeserializer());
        assertSame(reader, copy._objectIdReader);
        assertSame(copy.getValueDeserializer(), prop.getValueDeserializer());
        assertSame(copy.getName(), prop.getName());
        assertSame(copy.getType(), prop.getType());
        assertSame(copy.getMetadata(), prop.getMetadata());
        assertNotNull(copy.getMetadata());
    }

    @Test
    public void testWithName() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);
        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty newNameProp = prop.withName(newName);
        assertSame(newName, newNameProp.getName());
        assertSame(prop.getType(), newNameProp.getType());
        assertSame(prop.getMetadata(), newNameProp.getMetadata());
        assertSame(prop.getValueDeserializer(), newNameProp.getValueDeserializer());
        assertNotSame(prop, newNameProp);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);
        JsonDeserializer<?> newDeserializer = new JsonDeserializer<>();

        ObjectIdValueProperty newDeserProp = prop.withValueDeserializer(newDeserializer);
        assertSame(newDeserializer, newDeserProp.getValueDeserializer());
        assertSame(prop.getName(), newDeserProp.getName());
        assertSame(prop.getType(), newDeserProp.getType());
        assertSame(prop.getMetadata(), newDeserProp.getMetadata());
        assertNotSame(prop, newDeserProp);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);
        assertNull(prop.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);
        assertNull(prop.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);

        // TODO: Create test cases for deserializeAndSet method
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);

        // TODO: Create test cases for deserializeSetAndReturn method
    }

    @Test
    public void testSet() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);
        Object instance = new Object();
        Object value = new Object();

        // TODO: Create test cases for set method
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(reader, metadata);
        Object instance = new Object();
        Object value = new Object();

        // TODO: Create test cases for setAndReturn method
    }

}