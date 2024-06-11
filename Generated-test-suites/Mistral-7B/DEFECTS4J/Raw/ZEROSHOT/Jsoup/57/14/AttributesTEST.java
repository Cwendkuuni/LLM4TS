package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
        result = attributes.hasKey("key") && attributes.getBoolean("key");
        Assert.assertTrue(result);
        attributes.remove("key");
        result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String result = attributes.get("key");
        Assert.assertEquals("", result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("", result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size = attributes1.size();
        Assert.assertEquals(2, size);

        String result1 = attributes1.get("key1");
        Assert.assertEquals("value1", result1);
        String result2 = attributes1.get("key2");
        Assert.assertEquals("value2", result2);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<String> keys = new ArrayList<>();
        for (Attribute attribute : attributes) {
            keys.add(attribute.getKey());
        }

        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("key1");
        expectedKeys.add("key2");

        Assert.assertEquals(expectedKeys, keys);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        List<String> keys = new ArrayList<>();
        for (Attribute attribute : attributesList) {
            keys.add(attribute.getKey());
        }

        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("key1");
        expectedKeys.add("key2");

        Assert.assertEquals(expectedKeys, keys);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-key1", "value1"));
        attributes.put(new Attribute("data-key2", "value2"));

        Map<String, String> dataset = attributes.dataset();

        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            keys.add(entry.getKey());
        }

        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("data-key1");
        expectedKeys.add("data-key2");

        Assert.assertEquals(expectedKeys, keys);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String expectedHtml = " key=\"value\"";
        String resultHtml = attributes.html();
        Assert.assertEquals(expectedHtml, resultHtml);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        boolean result1 = attributes1.equals(attributes2);
        Assert.assertTrue(result1);

        boolean result2 = attributes1.equals(attributes3);
        Assert.assertFalse(result2);

        Attributes attributes4 = new Attributes();
        boolean result3 = attributes1.equals(attributes4);
        Assert.assertFalse(result3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");

        int result1 = attributes1.hashCode();
        int result2 = attributes2.hashCode();

        Assert.assertNotEquals(result1, result2);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        String result1 = attributes.get("key1");
        String result2 = clonedAttributes.get("key1");
        Assert.assertEquals(result1, result2);

        result1 = attributes.get("key2");
        result2 = clonedAttributes.get("key2");
        Assert.assertEquals(result1, result2);
    }
}