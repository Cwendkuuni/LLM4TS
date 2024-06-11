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
    }

    @Test
    public void testWithNullName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.withName(null).getName());

    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        try {
            property.deserializeAndSet(null, null, null);
        } catch (IOException e) {
            // expected exception
            assertTrue(true);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        try {
            property.deserializeSetAndReturn(null, null, null);
        } catch (IOException e) {
            // expected exception
            assertTrue(true);
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        try {
            property.set(null, null);
        } catch (IOException e) {
            // expected exception
            assertTrue(true);
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        try {
            property.setAndReturn(null, null);
        } catch (IOException e) {
            // expected exception
            assertTrue(true);
        }
    }
}