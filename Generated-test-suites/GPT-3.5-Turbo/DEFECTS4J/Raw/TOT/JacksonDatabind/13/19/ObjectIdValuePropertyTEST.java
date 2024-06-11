package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.ReadableObjectId;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdValueProperty property;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("{\"id\": 1}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();

        property.deserializeAndSet(jp, ctxt, instance);

        // Test that the object is bound to the ReadableObjectId
        ReadableObjectId roid = ctxt.findObjectId(1, null, null);
        assertEquals(instance, roid.item);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("{\"id\": 1}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        // Test that the object is bound to the ReadableObjectId
        ReadableObjectId roid = ctxt.findObjectId(1, null, null);
        assertEquals(instance, roid.item);

        // Test that the returned value is the instance object
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        try {
            property.set(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Test that UnsupportedOperationException is thrown when idProperty is null
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty",
                    e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        try {
            property.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Test that UnsupportedOperationException is thrown when idProperty is null
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty",
                    e.getMessage());
        }
    }
}