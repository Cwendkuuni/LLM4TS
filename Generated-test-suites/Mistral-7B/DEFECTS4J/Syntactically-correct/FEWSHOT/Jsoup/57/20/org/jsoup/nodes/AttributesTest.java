package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("other"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals("", attributes.getIgnoreCase("other"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        Attributes newAttributes = new Attributes();
        newAttributes.put("key", "newValue");
        attributes.putAll(newAttributes);
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.get("key").isBooleanAttribute());
        attributes.remove("key");
        attributes.put("key", false);
        assertFalse(attributes.get("key").isBooleanAttribute());
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key").getValue());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertNull(attributes.get("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");
        attributes.removeIgnoreCase("key1");
        assertNull(attributes.get("key1"));
        assertTrue(attributes.hasKey("KEY2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
        attributes.put("anotherKey", "anotherValue");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals("value2", attributes1.get("key2").getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value", dataset.get("key"));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("key=\"value\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(""));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes cloned = attributes.clone();
        assertEquals("value1", cloned.get("key1").getValue());
        assertEquals("value2", cloned.get("key2").getValue());
        assertEquals(attributes.size(), cloned.size());
    }

    @Test
    public void testDatasetIterator() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("data-key3", "value3");
        Map<String, String> dataset = attributes.dataset();
        List<Map.Entry<String, String>> entries = new ArrayList<>(dataset.entrySet());
        Collections.sort(entries);
        Iterator<Map.Entry<String, String>> iterator = dataset.entrySet().iterator();
        for (Map.Entry<String, String> entry : entries) {
            Map.Entry<String, String> next = iterator.next();
            assertEquals(entry.getKey(), next.getKey());
            assertEquals(entry.getValue(), next.getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringAsValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyMapAsValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", new Attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIllegalAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Object());
    }
}