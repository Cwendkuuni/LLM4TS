package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

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

        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
        assertEquals(null, attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");

        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("", attributes.getIgnoreCase("key2"));
        assertEquals(null, attributes.getIgnoreCase("Key3"));
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
        assertEquals("value2", attributes.get("Key1"));

        attributes.put("Key2", "value2");
        attributes.put("Key2", "value3");
        assertEquals("value3", attributes.get("Key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertFalse(attributes.get("key1").equals(""));
        assertNull(attributes.get("key1").equals("false"));

        attributes.put("key1", false);
        assertNull(attributes.get("key1"));
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
        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));

        attributes.remove("key2");
        assertEquals("", attributes.get("key2"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("Key1");
        assertEquals("", attributes.get("Key1"));
        assertFalse(attributes.hasKey("Key1"));

        attributes.removeIgnoreCase("Key2");
        assertEquals("", attributes.get("Key2"));
        assertFalse(attributes.hasKey("Key2"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(4, result.size());
        assertTrue(result.hasKey("key1"));
        assertTrue(result.hasKey("key2"));
        assertTrue(result.hasKey("key3"));
        assertTrue(result.hasKey("key4"));

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
        assertEquals("value4", result.get("key4"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();

        for (Attribute attribute : list) {
            assertEquals(attribute.getKey(), "key1"
                    || "key2");
            assertEquals(attribute.getValue(), "value1"
                    || "value2");
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

        StringBuilder result = new StringBuilder();
        attributes.html(result, new Document.OutputSettings());

        assertEquals("<key1>value1</key1>", result.toString());
    }
}