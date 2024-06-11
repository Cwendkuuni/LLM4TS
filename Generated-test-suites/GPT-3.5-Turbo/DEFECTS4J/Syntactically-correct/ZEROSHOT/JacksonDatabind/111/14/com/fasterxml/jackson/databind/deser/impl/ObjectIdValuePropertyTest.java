package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        property = new ObjectIdValueProperty(null, null);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = property.withName(newName);
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty result = property.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty result = property.withNullProvider(nva);
        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        try {
            property.deserializeAndSet(p, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;

        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSet() {
        Object instance = null;
        Object value = null;
        
        try {
            property.set(instance, value);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = null;
        Object value = null;
        
        try {
            property.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}