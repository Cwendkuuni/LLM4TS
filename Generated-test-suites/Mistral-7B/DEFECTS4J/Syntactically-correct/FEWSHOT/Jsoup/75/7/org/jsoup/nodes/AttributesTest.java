package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.put("key3", "value3");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));

        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.putIgnoreCase("Key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.putIgnoreCase("KEY1", "value3");
        assertEquals("value3", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key1", true);
        assertFalse(attributes.get("key1").isEmpty());
        assertNull(attributes.get("key1"));

        attributes.put("key2", false);
        assertFalse(attributes.hasKey("key2"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, attributes);

        attributes.put(attribute);
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));

        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key2"));

        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
        assertNull(attributes.get("key2"));

        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key1", "value2");

        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("Key1"));
        assertNull(attributes.get("key1"));
        assertNull(attributes.get("Key1"));

        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entries = attributes.asList();

        int count = 0;
        for (Map.Entry<String, String> entry : entries) {
            assertEquals(entry.getKey(), attributes.get(entry.getKey()));
            assertEquals(entry.getValue(), attributes.get(entry.getKey()));
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }
}