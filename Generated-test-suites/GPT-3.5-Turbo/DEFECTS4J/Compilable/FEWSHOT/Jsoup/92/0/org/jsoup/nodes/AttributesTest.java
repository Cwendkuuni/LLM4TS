package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals("John", attributes.get("name"));
        assertEquals("", attributes.get("age"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals("John", attributes.getIgnoreCase("name"));
        assertEquals("", attributes.getIgnoreCase("age"));
        attributes.put("AGE", "28");
        assertEquals("28", attributes.getIgnoreCase("age"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertEquals("John", attributes.get("name"));
        attributes.put("name", "Mary");
        assertEquals("Mary", attributes.get("name"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("required", true);
        assertTrue(attributes.hasKey("required"));
        assertNull(attributes.get("required"));
        attributes.put("disabled", false);
        assertFalse(attributes.hasKey("disabled"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
        attributes.removeIgnoreCase("name");
        assertFalse(attributes.hasKey("name"));
        attributes.put("NAME", "John");
        assertTrue(attributes.hasKeyIgnoreCase("name"));
        attributes.removeIgnoreCase("name");
        assertFalse(attributes.hasKeyIgnoreCase("name"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
        assertFalse(attributes.hasKey("age"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        assertTrue(attributes.hasKeyIgnoreCase("name"));
        assertTrue(attributes.hasKeyIgnoreCase("NAME"));
        attributes.put("AGE", "28");
        assertTrue(attributes.hasKeyIgnoreCase("age"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("name", "John");
        assertEquals(1, attributes.size());
        attributes.put("age", "28");
        assertEquals(2, attributes.size());
        attributes.remove("name");
        assertEquals(1, attributes.size());
    }
}