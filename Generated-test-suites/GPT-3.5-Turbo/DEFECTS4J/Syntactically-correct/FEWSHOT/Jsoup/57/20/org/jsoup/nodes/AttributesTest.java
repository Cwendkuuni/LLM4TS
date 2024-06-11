package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("non_existing_key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("non_existing_key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertEquals("true", attributes.get("key"));
        attributes.put("key", false);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("non_existing_key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("non_existing_key"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
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
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int count = 0;
        for (Attribute attribute : attributes.asList()) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals("value1", attributes.dataset().get("key1"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        assertEquals(attributes, attributes);
        assertEquals(attributes, attributes2);
        attributes.put("key", "value");
        assertFalse(attributes.equals(attributes2));
        attributes2.put("key", "value");
        assertEquals(attributes, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        assertEquals(attributes.hashCode(), attributes2.hashCode());
        attributes.put("key", "value");
        assertFalse(attributes.hashCode() == attributes2.hashCode());
        attributes2.put("key", "value");
        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key", "value");
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
        assertFalse(attributes == clone);
    }

}