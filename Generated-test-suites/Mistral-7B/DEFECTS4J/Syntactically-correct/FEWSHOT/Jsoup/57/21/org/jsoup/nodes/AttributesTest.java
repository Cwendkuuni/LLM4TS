package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals("value", attributes.getIgnoreCase("kEy"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());

        Attributes attributes2 = new Attributes();
        attributes2.put("key", "newValue");
        assertEquals("newValue", attributes2.get("key"));
        assertEquals(1, attributes2.size());

        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.get("key").isDataAttribute());
        assertEquals("", attributes.get("key"));

        Attributes attributes2 = new Attributes();
        attributes2.put("key", false);
        assertTrue(attributes2.hasKey("key"));
        assertFalse(attributes2.hasKey("key") && attributes2.get("key").isDataAttribute());
        assertEquals("", attributes2.get("key"));

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key") && attributes.get("key").isDataAttribute());
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());

        Attributes attributes2 = new Attributes();
        attributes2.put(new Attribute("key", "newValue"));
        assertEquals("newValue", attributes2.get("key"));
        assertEquals(1, attributes2.size());

        attributes.put(new Attribute("key", "newValue"));
        assertEquals("newValue", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertEquals(0, attributes.size());

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertEquals(1, attributes.size());

        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKeyIgnoreCaseWithDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-key", "value"));
        assertTrue(attributes.hasKeyIgnoreCase("data-key"));
        assertFalse(attributes.hasKey("data-key"));
        assertEquals(1, attributes.size());

        attributes.remove("data-key");
        assertFalse(attributes.hasKeyIgnoreCase("data-key"));
        assertFalse(attributes.hasKey("data-key"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes3.put("key3", "value3");

        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));

        attributes1.addAll(attributes3);
        assertEquals(3, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
        assertEquals("value3", attributes1.get("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        for (Attribute attribute : attributes) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            System.out.println(key + "=" + value);
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        List<Attribute> list = attributes.asList();
        for (Attribute attribute : list) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            System.out.println(key + "=" + value);
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-key1", "value1"));
        attributes.put(new Attribute("data-key2", "value2"));
        attributes.put(new Attribute("data-key3", "value3"));

        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "=" + value);
        }
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        StringBuilder sb = new StringBuilder();
        attributes.html(sb, new Document.OutputSettings());
        String html = sb.toString();
        System.out.println(html);
    }
}