package org.example;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {
    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetNonExistentAttribute() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertNull(attributes.get(null));
    }

    @Test
    public void testGetFromEmptyMap() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testGetWithSpaceInKey() {
        Attributes attributes = new Attributes();
        attributes.put("key with space", "value");
        assertEquals("value", attributes.get("key with space"));
    }

    // Add test cases for other methods here
}