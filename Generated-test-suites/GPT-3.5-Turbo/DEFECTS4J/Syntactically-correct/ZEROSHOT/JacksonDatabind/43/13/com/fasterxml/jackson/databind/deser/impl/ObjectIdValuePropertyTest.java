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
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty result = property.withName(newName);
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty result = property.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
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
        // Check if the bindings were successful
        assertNotNull(ctxt.findObjectId());
        // Check if the setAndReturn method is called if idProperty is not null
        assertNotNull(ctxt.findObjectId().idProperty);
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
        // Check if the bindings were successful
        assertNotNull(ctxt.findObjectId());
        // Check if the setAndReturn method is called if idProperty is not null
        assertNotNull(ctxt.findObjectId().idProperty);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        Object instance = new Object();
        property.setAndReturn(instance, null);
    }
}