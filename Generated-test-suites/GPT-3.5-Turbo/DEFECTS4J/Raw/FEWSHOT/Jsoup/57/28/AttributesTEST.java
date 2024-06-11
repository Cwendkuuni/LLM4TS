package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Map;

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
        assertEquals("value", attributes.getIgnoreCase("keY"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertEquals("true", attributes.get("key"));

        attributes.put("key", false);
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        attributes.removeIgnoreCase("Key");
        assertNull(attributes.get("key"));

        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        AbstractMap.SimpleEntry<String, String> entry1 = new AbstractMap.SimpleEntry<String, String>("key1", "value1");
        AbstractMap.SimpleEntry<String, String> entry2 = new AbstractMap.SimpleEntry<String, String>("key2", "value2");

        assertTrue(attributes.asList().contains(entry1));
        assertTrue(attributes.asList().contains(entry2));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "value");

        Map<String, String> dataset = attributes.dataset();
        assertEquals("value", dataset.get("key"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testToString() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");

        assertTrue(attributes.equals(other));
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes other = new Attributes();
        other.put("key1", "value1");
        other.put("key2", "value2");

        assertEquals(attributes.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
    }
}