package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        // Edge case - key not found
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));

        // Edge case - key not found
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));

        // Error scenario - replace existing key
        attributes.put("key", "updatedValue");
        assertEquals("updatedValue", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));

        // Edge case - remove key
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutBoolean() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));

        // Edge case - remove boolean attribute
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        // Typical use case
        Attribute attribute = new Attribute("key", "value");
        Attributes attributes = new Attributes();
        attributes.put(attribute);
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));
    }

}