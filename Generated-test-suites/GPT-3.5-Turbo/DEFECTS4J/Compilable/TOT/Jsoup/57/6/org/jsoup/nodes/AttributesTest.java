package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));
        assertEquals(3, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertEquals("", attributes.get("booleanKey"));
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get(attribute.getKey()));
        assertEquals(3, attributes.size());
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incomingAttributes = new Attributes();
        incomingAttributes.put("newKey1", "newValue1");
        incomingAttributes.put("newKey2", "newValue2");

        attributes.addAll(incomingAttributes);
        assertEquals("newValue1", attributes.get("newKey1"));
        assertEquals("newValue2", attributes.get("newKey2"));
        assertEquals(4, attributes.size());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        assertEquals("data-value1", attributes.dataset().get("key1"));
        assertEquals(2, attributes.dataset().size());
        assertNull(attributes.dataset().put("key3", "value3"));
        assertEquals("value3", attributes.get("data-key3"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "<html>");
        attributes.put("key2", "\"value\"");
        assertEquals(" key1=\"<html>\" key2=\"&quot;value&quot;\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertTrue(attributes.equals(attributes2));

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value3");
        assertFalse(attributes.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertNotSame(attributes, clone);
        assertEquals(attributes, clone);
    }
}