package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);

        attributes.put("key", "newValue");
        actual = attributes.get("key");
        expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.getBoolean("key");
        boolean expected = true;
        assertEquals(expected, actual);

        attributes.put("key", false);
        actual = attributes.getBoolean("key");
        expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        Attribute actual = attributes.get("key");
        Attribute expected = new Attribute("key", "value");
        assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        assertEquals("", actual);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        String actual = attributes.get("Key");
        assertEquals("", actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        attributes1.addAll(attributes2);

        int size1 = attributes1.size();
        int size2 = attributes2.size();
        int expected = size1 + size2;
        assertEquals(expected, size1);

        for (String key : attributes2.asList()) {
            String value = attributes1.get(key);
            String expectedValue = attributes2.get(key);
            assertEquals(expectedValue, value);
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        String actualKey1 = attributes.get("key1");
        String expectedKey1 = "key1";
        assertEquals(expectedKey1, actualKey1);

        String actualValue1 = attributes.get("key1");
        String expectedValue1 = "value1";
        assertEquals(expectedValue1, actualValue1);

        String actualKey2 = dataset.keySet().iterator().next();
        String expectedKey2 = "data-key2";
        assertEquals(expectedKey2, actualKey2);

        String actualValue2 = dataset.get(expectedKey2);
        String expectedValue2 = "value2";
        assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder actual = new StringBuilder();
        Document document = new Document();
        attributes.html(actual, document.outputSettings());

        String expected = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expected, actual.toString());
    }
}