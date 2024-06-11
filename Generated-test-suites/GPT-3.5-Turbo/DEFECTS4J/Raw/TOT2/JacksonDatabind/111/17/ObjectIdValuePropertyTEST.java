package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public boolean isNull() {
                return true;
            }
        };
        SettableBeanProperty newProperty = property.withNullProvider(nva);
        assertNotNull(newProperty);
        assertEquals(nva, newProperty.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() {
        // Test case for deserializeAndSet method
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Test case for deserializeSetAndReturn method
    }

    @Test
    public void testSetAndReturn() {
        // Test case for setAndReturn method
    }

    @Test
    public void testSetWithNoIdProperty() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertThrows(UnsupportedOperationException.class, () -> {
            property.set(null, null);
        });
    }

}