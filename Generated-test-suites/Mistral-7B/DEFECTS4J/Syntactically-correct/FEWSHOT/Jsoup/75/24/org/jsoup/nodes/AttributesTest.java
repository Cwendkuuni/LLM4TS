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
    public void testPut() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value3");
        assertEquals("value3", attributes.get("key2"));

        attributes.put("key1", null);
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));

        attributes.put("Key1", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key1"));

        attributes.put("Key2", "value3");
        assertEquals("value3", attributes.getIgnoreCase("key2"));

        attributes.put("Key1", null);
        assertNull(attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));

        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("Key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));

        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertNull(attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        assertEquals(3, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value3", attributes1.get("key2"));
        assertEquals("value3", attributes1.get("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entries = attributes.asList();

        Iterator<Map.Entry<String, String>> iterator = entries.iterator();

        Map.Entry<String, String> entry = iterator.next();
        assertEquals("key1", entry.getKey());
        assertEquals("value1", entry.getValue());

        entry = iterator.next();
        assertEquals("key2", entry.getKey());
        assertEquals("value2", entry.getValue());

        assertFalse(iterator.hasNext());
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

        String html = attributes.html();

        assertEquals(expectedHtml.toString(), html);
    }
}