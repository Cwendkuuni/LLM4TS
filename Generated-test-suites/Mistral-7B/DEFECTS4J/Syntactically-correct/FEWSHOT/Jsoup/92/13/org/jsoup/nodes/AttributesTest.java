package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertNull(attributes.get(""));
        assertFalse(attributes.hasKey(""));
    }

    @Test
    public void testAddAndGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testAddAndGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertNull(attributes.get("key"));
        assertTrue(attributes.hasKey("key"));

        Attributes attributes2 = new Attributes();
        attributes2.put("key", false);
        assertNull(attributes2.get("key"));
        assertFalse(attributes2.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("KEY", "value");
        attributes.putIgnoreCase(attribute.getKey(), attribute.getValue());
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testPutDuplicateAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute1 = new Attribute("key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        attributes.put(attribute1);
        attributes.put(attribute2);
        assertEquals("value2", attributes.get("key"));
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", true);
        attributes.put(attribute);
        assertNull(attributes.get("key"));
        assertTrue(attributes.hasKey("key"));

        Attributes attributes2 = new Attributes();
        Attribute attribute2 = new Attribute("key", false);
        attributes2.put(attribute2);
        assertNull(attributes2.get("key"));
        assertFalse(attributes2.hasKey("key"));
    }

    @Test
    public void testPutAttributeWithParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        parentAttributes.put(attribute);
        Attributes childAttributes = new Attributes();
        childAttributes.put("key", "childValue");
        childAttributes.put("parentKey", parentAttributes);
        assertEquals("childValue", childAttributes.get("key"));
        assertEquals("value", childAttributes.get("parentKey").getValue());
        assertEquals(2, childAttributes.size());
        assertTrue(childAttributes.hasKey("key"));
        assertTrue(childAttributes.hasKey("parentKey"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertNull(attributes.get("key"));
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.removeIgnoreCase("key");
        assertNull(attributes.get("key"));
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveAttributeWithParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        parentAttributes.put(attribute);
        Attributes childAttributes = new Attributes();
        childAttributes.put("key", "childValue");
        childAttributes.put("parentKey", parentAttributes);
        childAttributes.remove("key");
        assertNull(childAttributes.get("key"));
        assertFalse(childAttributes.hasKey("key"));
        assertEquals("value", childAttributes.get("parentKey").getValue());
        assertTrue(childAttributes.hasKey("parentKey"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertTrue(attributes.hasKey(""));
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertTrue(attributes.hasKeyIgnoreCase(""));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        attributes.put("KEY", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testHasKeyWithParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        parentAttributes.put(attribute);
        Attributes childAttributes = new Attributes();
        childAttributes.put("key", "childValue");
        childAttributes.put("parentKey", parentAttributes);
        assertTrue(childAttributes.hasKey("key"));
        assertTrue(childAttributes.hasKey("parentKey"));
        assertTrue(childAttributes.hasKeyIgnoreCase("Key"));
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
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        attributes3.putAll(attributes2);
        assertEquals(3, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
        assertTrue(attributes3.hasKey("key3"));

        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value3", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entries = attributes.asList();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
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

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null);

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        String attributesString = attributes.toString();

        assertEquals(expectedHtml.toString(), attributesString);
    }
}