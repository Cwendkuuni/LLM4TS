package org.jsoup.nodes;
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
    public void testPutAndGet() {
        assertEquals("", attributes.get("key"));
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        assertEquals("", attributes.getIgnoreCase("key"));
        attributes.putIgnoreCase("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBooleanAttribute() {
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", true);
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKey("key"));
        assertEquals("", attributes.get("key"));
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
        attributes.putIgnoreCase("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKeyIgnoreCase("key"));
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
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }
}