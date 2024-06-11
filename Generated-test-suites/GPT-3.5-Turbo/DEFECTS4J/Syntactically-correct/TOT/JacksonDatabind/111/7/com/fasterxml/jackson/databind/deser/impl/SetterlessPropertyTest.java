package com.fasterxml.jackson.databind.deser.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;

    @Before
    public void setUp() {
        setterlessProperty = new SetterlessProperty(null, null, null, null, null);
    }

    @After
    public void tearDown() {
        setterlessProperty = null;
    }

    @Test
    public void testDeserializeAndSetWithValueNull() throws Exception {
        // Given
        // Create a dummy object
        Object instance = new Object();

        // When
        setterlessProperty.deserializeAndSet(null, null, instance);

        // Then
        // Check if nothing is set
        assertNull(instance.getProperty());
    }

    @Test
    public void testDeserializeAndSetWithTypedDeserializer() throws IOException {
        // Given
        // Create a dummy object
        Object instance = new Object();

        // When
        // Attempt deserializing with value type deserializer
        try {
            setterlessProperty.deserializeAndSet(null, null, instance);
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Problem deserializing 'setterless' property (null): " +
                    "no way to handle typed deser with setterless yet", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSetWithNullToModify() throws IOException {
        // Given
        // Create a dummy object with null property
        Object instance = new Object();
        instance.setProperty(null);

        // When
        setterlessProperty.deserializeAndSet(null, null, instance);

        // Then
        // Check if property is still null
        assertNull(instance.getProperty());
    }

    @Test
    public void testDeserializeAndSetWithNonNullToModify() throws IOException {
        // Given
        // Create a dummy object with non-null property
        Object instance = new Object();
        instance.setProperty(new Object());

        // When
        setterlessProperty.deserializeAndSet(null, null, instance);

        // Then
        // Check if property is modified
        assertNotNull(instance.getProperty());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Given
        // Create a dummy object
        Object instance = new Object();

        // When
        Object result = setterlessProperty.deserializeSetAndReturn(null, null, instance);

        // Then
        // Check if object is returned
        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        // Given
        // Create a dummy object
        Object instance = new Object();

        // When
        try {
            setterlessProperty.set(instance, new Object());
            fail("Expected an UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should never call `set()` on setterless property ('null')", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // Given
        // Create a dummy object
        Object instance = new Object();

        // When
        try {
            setterlessProperty.setAndReturn(instance, new Object());
            fail("Expected an UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should never call `set()` on setterless property ('null')", e.getMessage());
        }
    }
}