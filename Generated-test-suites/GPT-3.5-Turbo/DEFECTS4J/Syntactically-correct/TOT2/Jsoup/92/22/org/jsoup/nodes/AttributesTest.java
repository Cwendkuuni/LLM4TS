package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key2"));
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
        attributes.put("key2", false);
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
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
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        int expectedHash = 31 * 1 + "key1".hashCode() + "value1".hashCode();
        assertEquals(expectedHash, attributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = attributes1.clone();
        assertEquals(attributes1, attributes2);
    }
}