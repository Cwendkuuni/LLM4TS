package org.jsoup.nodes;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "value2");

        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("key1", false);

        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));

        attributes.remove("key1");

        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.removeIgnoreCase("KEY1");

        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    // Add more test cases for other methods as needed

}