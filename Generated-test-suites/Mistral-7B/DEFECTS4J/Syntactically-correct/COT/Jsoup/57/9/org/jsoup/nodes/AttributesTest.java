package org.jsoup.nodes; // Make sure to import necessary classes from the Attributes class

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        Assert.assertTrue(actual);
        actual = attributes.get("key").isBoolean();
        Assert.assertTrue(actual);
        attributes.remove("key");
        actual = attributes.hasKey("key");
        Assert.assertFalse(actual);
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        String key = "key";
        Attribute attribute = new Attribute(key, "value");
        attributes.put(attribute);
        String actual = attributes.get(key);
        String expected = "value";
        Assert.assertEquals(expected, actual);

        // Test for null attribute
        Assert.assertThrows(NullPointerException.class, () -> attributes.put(null));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String actual1 = attributes.get("key1");
        String expected1 = "value1";
        Assert.assertEquals(expected1, actual1);
        attributes.remove("key1");
        String actual2 = attributes.get("key1");
        Assert.assertEquals("", actual2);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        String actual1 = attributes.getIgnoreCase("Key1");
        String expected1 = "Value1";
        Assert.assertEquals(expected1, actual1);
        attributes.removeIgnoreCase("Key1");
        String actual2 = attributes.getIgnoreCase("Key1");
        Assert.assertEquals("", actual2);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key1", "value1");
        expectedAttributes.put("key2", "value2");

        attributes1.addAll(attributes2);
        Attributes actualAttributes = attributes1;

        Assert.assertEquals(expectedAttributes.size(), actualAttributes.size());
        for (Map.Entry<String, String> entry : expectedAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String actualValue = actualAttributes.get(key);
            Assert.assertEquals(value, actualValue);
        }
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> actualDataset = attributes.dataset();
        Map<String, String> expectedDataset = new LinkedHashMap<>();
        expectedDataset.put("key1", "value1");
        expectedDataset.put("key2", "value2");

        Assert.assertEquals(expectedDataset.size(), actualDataset.size());
        for (Map.Entry<String, String> entry : expectedDataset.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String actualValue = actualDataset.get(key);
            Assert.assertEquals(value, actualValue);
        }
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actualHtml = attributes.html();
        String expectedHtml = " key=\"value\"";
        Assert.assertEquals(expectedHtml, actualHtml);

        Attributes attributesWithData = new Attributes();
        attributesWithData.put("data-key", "value");
        actualHtml = attributesWithData.html();
        expectedHtml = " data-key=\"value\"";
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testEqualsAndHashCodeMethods() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");

        Assert.assertTrue(attributes1.equals(attributes2));
        Assert.assertFalse(attributes1.equals(attributes3));
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clonedValue = clonedAttributes.get(key);
            Assert.assertEquals(value, clonedValue);
        }
    }

    @Test
    public void testEmptyIteratorAndAsListMethods() {
        Attributes attributes = new Attributes();
        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> list = attributes.asList();

        Assert.assertFalse(iterator.hasNext());
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testIteratorAndAsListMethodsWithDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        List<Map.Entry<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        expectedList.add(new AbstractMap.SimpleEntry<>("key2", "value2"));

        List<Map.Entry<String, String>> actualList = attributes.asList();

        Assert.assertEquals(expectedList.size(), actualList.size());
        for (Map.Entry<String, String> entry : expectedList) {
            String key = entry.getKey();
            String value = entry.getValue();
            Map.Entry<String, String> actualEntry = actualList.get(0);
            Assert.assertEquals(key, actualEntry.getKey());
            Assert.assertEquals(value, actualEntry.getValue());
            actualList.remove(0);
        }
    }
}