package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetEmpty() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.get("key").isSetted());
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
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
        assertEquals(2, attributesList.size());

        for (Attribute attribute : attributesList) {
            if (attribute.getKey().equals("key1")) {
                assertEquals("value1", attribute.getValue());
                break;
            }
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();
        assertEquals(2, attributesList.size());

        for (Attribute attribute : attributesList) {
            if (attribute.getKey().equals("key1")) {
                assertEquals("value1", attribute.getValue());
                break;
            }
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder html = new StringBuilder();
        attributes.html(html, null);

        assertEquals("<key1>value1</key1>", html.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("<key1>value1</key1>", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");

        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
    }
}