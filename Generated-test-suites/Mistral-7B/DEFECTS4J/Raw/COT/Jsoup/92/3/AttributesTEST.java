package org.jsoup.nodes; // Make sure to replace it with your test package if necessary

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class AttributesTest {

    

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutAndGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutAndGetBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.get("key").isEmpty());
        assertFalse(attributes.get("key").equals(""));
        assertNull(attributes.get("key").trim());
    }

    @Test
    public void testPutAndGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.get("Key"));
    }

    @Test
    public void testPutAndGetMultipleKeys() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutAndGetMultipleKeysIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        attributes.putIgnoreCase("key2", "value2");
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value1", attributes.get("Key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testHasKeyFalse() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCaseFalse() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("OtherKey"));
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

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(2, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            actualList.add(entry);
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        attributes.putIgnoreCase("key2", "value2");

        attributes.removeIgnoreCase("Key1");
        assertEquals(1, attributes.size());
        assertFalse(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveMultipleKeys() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        attributes.remove("key2");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveMultipleKeysIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        attributes.putIgnoreCase("key2", "value2");

        attributes.removeIgnoreCase("Key1");
        attributes.removeIgnoreCase("key2");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = attributes1.clone();
        assertEquals(attributes1.size(), attributes2.size());
        assertEquals(attributes1.get("key1"), attributes2.get("key1"));
        assertEquals(attributes1.get("key2"), attributes2.get("key2"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        assertEquals("value", attributes.get("KEY"));

        attributes.normalize();
        assertEquals("value", attributes.get("key"));
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
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, new Document.OutputSettings());

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    
}