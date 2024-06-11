package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("key1", "value2");

        assertEquals("value2", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.get("key") != null);
    }

    @Test
    public void testPutReplace() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals("value2", attributes.get("key2"));
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("key2", "value2");

        attributes.removeIgnoreCase("key1");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("key2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = new ArrayList<>(attributes.asList());
        int i = 0;
        for (Map.Entry<String, String> entry : attributes) {
            assertEquals(list.get(i).getKey(), entry.getKey());
            assertEquals(list.get(i).getValue(), entry.getValue());
            i++;
        }
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        List<Map.Entry<String, String>> list = new ArrayList<>(attributes3.asList());
        assertEquals(4, attributes3.size());
        for (Map.Entry<String, String> entry : attributes3) {
            assertTrue(list.contains(entry));
            list.remove(entry);
        }
        assertEquals(0, list.size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes().put("data-key1", "value1").put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");

        assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes2 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes3 = new Attributes().put("key1", "value1").put("key3", "value3");

        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes2 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attributes3 = new Attributes().put("key1", "value1").put("key3", "value3");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }
}