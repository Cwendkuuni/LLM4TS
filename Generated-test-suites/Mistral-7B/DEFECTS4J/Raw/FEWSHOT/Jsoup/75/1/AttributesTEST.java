package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
    public void testPutGetKeyCaseSensitive() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("key1", attributes.get("key1"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testPutGetKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("key1", attributes.get("key1"));
        assertEquals("key1", attributes.getIgnoreCase("KEY1"));
        assertNull(attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPutGetBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.get("key1").equals("true"));
        assertFalse(attributes.get("key1").equals("false"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testPutGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key1", "value1"));
        assertEquals("value1", attributes.get("key1"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testPutMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3");
        assertEquals("value3", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testRemoveKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("key1");
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        attributes3.putAll(attributes2);

        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"), new AbstractMap.SimpleEntry<>("key2", "value2"));
        assertEquals(list, attributes.asList());
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
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, new Document.OutputSettings());

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
}