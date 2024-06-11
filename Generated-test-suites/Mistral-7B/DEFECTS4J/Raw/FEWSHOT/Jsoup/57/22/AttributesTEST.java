package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

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
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key2"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
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
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertEquals("value1", attributes.get("key1"));
        assertEquals(attribute, attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertFalse(attributes.remove("key2"));
        assertEquals("value1", attributes.get("key1"));
        assertTrue(attributes.remove("key1"));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertFalse(attributes.removeIgnoreCase("key2"));
        assertTrue(attributes.removeIgnoreCase("key1"));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        for (Attribute attribute : attributesList) {
            assertEquals(attribute.getKey(), "key1"
                    || "key2");
            assertEquals(attribute.getValue(), "value1"
                    || "value2");
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        assertEquals(2, attributesList.size());
        Attribute attribute = attributesList.get(0);
        assertEquals(attribute.getKey(), "key1");
        assertEquals(attribute.getValue(), "value1");
        attribute = attributesList.get(1);
        assertEquals(attribute.getKey(), "key2");
        assertEquals(attribute.getValue(), "value2");
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("key1"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder sb = new StringBuilder();
        attributes.html(sb, null);
        assertEquals(" key1=\"value1\"", sb.toString());
    }

    @Test
    public void testToString() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals(" key1=\"value1\"", attributes.toString());
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
        assertNotEquals(attributes1.hashCode(), 123);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clone = attributes.clone();
        assertEquals(attributes.size(), clone.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(clone.hasKey("key1"));
        assertEquals(attributes.get("key1"), clone.get("key1"));
    }
}