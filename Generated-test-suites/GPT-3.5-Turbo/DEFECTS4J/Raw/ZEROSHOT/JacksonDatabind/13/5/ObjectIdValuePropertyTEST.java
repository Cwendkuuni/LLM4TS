package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructorAndGetters() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructorAndGetters() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructorWithNewNameAndGetters() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(jp, ctxt, instance);
        // assert something here
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
        assertNotNull(result);
    }

    @Test
    public void testSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
            fail("UnsupportedOperationException Expected");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        try {
            property.setAndReturn(instance, value);
            fail("UnsupportedOperationException Expected");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }
}