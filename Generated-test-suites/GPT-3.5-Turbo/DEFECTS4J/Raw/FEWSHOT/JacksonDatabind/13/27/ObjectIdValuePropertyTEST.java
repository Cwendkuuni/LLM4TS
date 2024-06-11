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
        assertEquals(objectIdReader.propertyName, property._objectIdReader.propertyName);
        assertEquals(objectIdReader.getIdType(), property._objectIdReader.getIdType());
        assertEquals(metadata, property.metadata);
        assertEquals(objectIdReader.getDeserializer(), property._objectIdReader.getDeserializer());
    }

    @Test
    public void testWithDeserConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property._objectIdReader.propertyName);
        assertEquals(objectIdReader.getIdType(), property._objectIdReader.getIdType());
        assertEquals(metadata, property.metadata);
        assertEquals(deser, property._valueDeserializer);
    }

    @Deprecated
    @Test
    public void testWithDeprecatedNameConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        String newName = "newName";
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property._objectIdReader.propertyName);
        assertEquals(objectIdReader.getIdType(), property._objectIdReader.getIdType());
        assertEquals(metadata, property.metadata);
        assertEquals(newName, property._propName.getSimpleName());
    }

    @Test
    public void testWithNameConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property._objectIdReader.propertyName);
        assertEquals(objectIdReader.getIdType(), property._objectIdReader.getIdType());
        assertEquals(metadata, property.metadata);
        assertEquals(newName, property._propName);
    }

    @Test
    public void testGetName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName.getSimpleName(), property.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withValueDeserializer(deser);

        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property._objectIdReader.propertyName);
        assertEquals(objectIdReader.getIdType(), property._objectIdReader.getIdType());
        assertEquals(metadata, property.metadata);
        assertEquals(deser, property._valueDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        try {
            property.setAndReturn(instance, value);
        } catch (IOException e) {
            fail();
        }
    }
}