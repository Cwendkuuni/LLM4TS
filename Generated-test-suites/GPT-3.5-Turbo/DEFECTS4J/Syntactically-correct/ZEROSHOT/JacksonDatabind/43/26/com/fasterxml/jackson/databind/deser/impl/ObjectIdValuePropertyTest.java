package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNewName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertNotSame(property, newProperty);
        assertEquals("newName", newProperty.getName().getSimpleName());
        assertNull(newProperty.getAnnotation(null));
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(null);
        assertNotSame(property, newProperty);
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            property.deserializeAndSet(null, null, null);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            Object result = property.deserializeSetAndReturn(null, null, null);
            assertNull(result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            property.set(null, null);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            Object result = property.setAndReturn(null, null);
            assertNull(result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}