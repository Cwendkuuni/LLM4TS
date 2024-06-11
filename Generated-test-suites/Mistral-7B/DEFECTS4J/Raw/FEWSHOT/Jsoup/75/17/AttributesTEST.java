package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonexistentkey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value2");
        attributes.put("key2", "value3");

        assertEquals("value3", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key1", "value2");

        assertEquals("value2", attributes.get("key1"));

        attributes.put("Key2", "value2");
        attributes.put("Key2", "value3");

        assertEquals("value3", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertTrue(attributes.get("booleanKey") == null);

        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);

        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));

        attributes.remove("nonexistentkey");
        assertEquals("", attributes.get("nonexistentkey"));
        assertFalse(attributes.hasKey("nonexistentkey"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));

        attributes.removeIgnoreCase("NONEEXISTENTKEY");
        assertEquals("", attributes.get("NONEEXISTENTKEY"));
        assertFalse(attributes.hasKey("NONEEXISTENTKEY"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        assertFalse(attributes.hasKey("nonexistentkey"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
        assertFalse(attributes.hasKeyIgnoreCase("NONEEXISTENTKEY"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(4, attributes3.size());

        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
        assertTrue(attributes3.hasKey("key3"));
        assertTrue(attributes3.hasKey("key4"));

        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
        assertEquals("value4", attributes3.get("key4"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = attributes.asList();

        Iterator<Map.Entry<String, String>> iterator = list.iterator();

        Map.Entry<String, String> entry = iterator.next();
        assertEquals("key1", entry.getKey());
        assertEquals("value1", entry.getValue());

        entry = iterator.next();
        assertEquals("key2", entry.getKey());
        assertEquals("value2", entry.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());

        assertTrue(dataset.containsKey("data-key1"));
        assertTrue(dataset.containsKey("data-key2"));

        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }
}