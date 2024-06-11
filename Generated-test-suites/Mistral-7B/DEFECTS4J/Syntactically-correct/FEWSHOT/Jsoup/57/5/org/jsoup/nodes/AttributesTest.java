package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("notExist"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        assertEquals("VALUE", attributes.getIgnoreCase("key"));
        assertEquals("VALUE", attributes.getIgnoreCase("Key"));
        assertEquals("", attributes.getIgnoreCase("notExist"));
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

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("notExist"));
        assertEquals("", attributes.get("notExist"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("notExist"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");

        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("notExist"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        assertEquals(4, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
        assertTrue(attributes1.hasKey("key4"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        for (Attribute attribute : attributesList) {
            assertEquals(attribute.getValue(), attributes.get(attribute.getKey()));
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(1, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertEquals("value1", dataset.get("data-key1"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder expected = new StringBuilder();
        expected.append(" key=\"value\"");

        String html = attributes.html();

        assertEquals(expected.toString(), html);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        int hash1 = attributes1.hashCode();
        int hash2 = attributes2.hashCode();
        int hash3 = attributes3.hashCode();

        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes cloned = attributes.clone();

        assertEquals(attributes.size(), cloned.size());
        assertEquals(attributes.get("key1"), cloned.get("key1"));
        assertEquals(attributes.get("key2"), cloned.get("key2"));
    }

    @Test
    public void testDatasetIterator() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("key3", "value3");

        Map.Entry<String, String> entry;

        for (entry = attributes.dataset().entrySet().iterator(); entry != null; entry = attributes.dataset().entrySet().iterator().hasNext()) {
            assertEquals(entry.getKey(), attributes.getIgnoreCase("data-" + entry.getKey().substring(5)));
            assertEquals(entry.getValue(), attributes.getIgnoreCase("data-" + entry.getKey()));
        }
    }
}