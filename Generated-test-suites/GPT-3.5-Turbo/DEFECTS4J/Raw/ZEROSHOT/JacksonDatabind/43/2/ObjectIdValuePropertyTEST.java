package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty updatedProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, updatedProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = new JsonDeserializer<>();
        ObjectIdValueProperty updatedProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertEquals(newDeserializer, updatedProperty.getValueDeserializer());
    }

    @Test
    public void testAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        // TODO: Assert something
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        // TODO: Assert something
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdValueProperty.set(instance, value);
        // TODO: Assert something
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = objectIdValueProperty.setAndReturn(instance, value);
        // TODO: Assert something
    }
}