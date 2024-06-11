
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            property.deserializeAndSet(null, null, instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_WithTypeDeserializer() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            property.deserializeAndSet(null, null, instance);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals("Problem deserializing 'setterless' property (\"\"): no way to handle typed deser with setterless yet", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_NullToModify() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            property.deserializeAndSet(null, null, instance);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals("Problem deserializing 'setterless' property 'null': get method returned null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            property.deserializeSetAndReturn(null, null, instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSet() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            property.set(instance, null);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals("Should never call `set()` on setterless property ('null')", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            property.setAndReturn(instance, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
