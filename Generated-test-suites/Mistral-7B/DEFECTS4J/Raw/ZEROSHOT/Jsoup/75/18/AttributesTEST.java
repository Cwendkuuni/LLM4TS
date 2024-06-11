package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.internal.Normalizer.lowerCase;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals(Attributes.EMPTY_STRING, attributes.get("empty"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertEquals(Attributes.EMPTY_STRING, attributes.getIgnoreCase("EMPTY"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        Assert.assertEquals(expected, attributes.asMap());
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        Map<String, String> expected = new HashMap<>();
        expected.put("Key1", "Value1");
        expected.put("Key2", "Value2");
        Assert.assertEquals(expected, attributes.asMap());
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertFalse(attributes.get("key").isEmpty());
        Assert.assertNull(attributes.get("key"));
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testPutBooleanIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Key"));
        Assert.assertNull(attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        Map<String, String> expected = new HashMap<>();
        expected.put("key", "value");
        Assert.assertEquals(expected, attributes.asMap());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Map<String, String> expected = new HashMap<>();
        expected.put("key2", "value2");
        Assert.assertEquals(expected, attributes.asMap());
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        attributes.removeIgnoreCase("KEY1");
        Map<String, String> expected = new HashMap<>();
        expected.put("Key2", "value2");
        Assert.assertEquals(expected, attributes.asMap());
        Assert.assertFalse(attributes.hasKey("Key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("empty"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"));
        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes.asMap().entrySet()) {
            actualList.add(entry);
        }
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2"));
        Assert.assertEquals(expectedList, attributes.asList());
    }

    @Test
    public void testDataSet() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> expected = new HashMap<>();
        expected.put("data-key1", "value1");
        expected.put("data-key2", "value2");
        Assert.assertEquals(expected, attributes.dataset());
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(" key1=\"value1\" key2=\"value2\"");
        Assert.assertEquals(expectedOutput.toString(), attributes.html());
    }

    @Test
    public void testClone() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("key1", "value1");
        originalAttributes.put("key2", "value2");
        Attributes clonedAttributes = originalAttributes.clone();
        Assert.assertNotSame(originalAttributes, clonedAttributes);
        Assert.assertEquals(originalAttributes.asMap(), clonedAttributes.asMap());
    }
}