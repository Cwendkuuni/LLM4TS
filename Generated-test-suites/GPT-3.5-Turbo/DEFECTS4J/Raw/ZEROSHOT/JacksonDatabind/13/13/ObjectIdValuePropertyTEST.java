package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        property = new ObjectIdValueProperty(null, null);
    }

    @Test
    public void testWithNewName() {
        String newName = "newName";
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getName());
        assertNull(newProperty.getAnnotation(Object.class));
    }

    @Test
    public void testWithValueDeserializer() {
        Object valueDeserializer = new Object();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: Implement test case
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: Implement test case
    }

    @Test
    public void testSet() {
        // TODO: Implement test case
    }

    @Test
    public void testSetAndReturn() {
        // TODO: Implement test case
    }
}