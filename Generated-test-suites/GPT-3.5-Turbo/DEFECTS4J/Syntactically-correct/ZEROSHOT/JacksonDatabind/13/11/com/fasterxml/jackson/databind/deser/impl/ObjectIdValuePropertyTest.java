package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullInstanceThrowsException() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        try {
            property.deserializeAndSet(jp, ctxt, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        Object instance = new Object();
        Object value = new Object();

        try {
            property.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

}