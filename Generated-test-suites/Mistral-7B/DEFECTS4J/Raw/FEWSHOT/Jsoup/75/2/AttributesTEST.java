package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key"));
        assertFalse(attributes.hasKey("key"));

        attributes.put("key", false);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutMultiple() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutSameKeyTwice() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        assertEquals(1, attributes.size());
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
        attributes.remove("key");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "value");
        assertEquals(1, attributes.size());
        attributes.removeIgnoreCase("key");
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("KEY"));
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
        attributes.putIgnoreCase("KEY", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
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
    public void testClear() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        attributes.clear();
        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", clonedAttributes.get("key1"));
        assertEquals(1, attributes.size());
        assertEquals(1, clonedAttributes.size());
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        assertEquals(2, attributes3.size());
        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));

        attributes3.putAll(attributes2);
        assertEquals(3, attributes3.size());
        assertEquals("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
    }

    @Test
    public void testPutAllOverwrite() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes2);
        assertEquals(0, attributes3.size());

        attributes3.putAll(attributes1);
        assertEquals(2, attributes3.size());
        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributeList = attributes.asList();
        assertEquals(2, attributeList.size());
        Attribute attribute1 = attributeList.get(0);
        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());

        Attribute attribute2 = attributeList.get(1);
        assertEquals("key2", attribute2.getKey());
        assertEquals("value2", attribute2.getValue());
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

    @Test
    public void testDataKey() {
        assertEquals("data-key", Attributes.dataKey("key"));
    }
}