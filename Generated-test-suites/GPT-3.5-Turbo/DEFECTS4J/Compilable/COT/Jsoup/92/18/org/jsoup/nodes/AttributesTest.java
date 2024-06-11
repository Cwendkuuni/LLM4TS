package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        attributes.put("key", false);
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }
}
