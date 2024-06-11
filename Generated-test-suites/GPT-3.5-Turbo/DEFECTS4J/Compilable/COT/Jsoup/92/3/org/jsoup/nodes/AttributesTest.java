package org.jsoup.nodes;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("checked", true);
        assertTrue(attributes.hasKey("checked"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("notPresent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
        attributes.remove("key");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key", "value");
        attributes.addAll(incoming);
        assertEquals("value", attributes.get("key"));
    }
}