package org.jsoup.nodes; // update the package name if necessary

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
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
    public void testGetMethodWhenAttributeIsNotPresent() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        int size = attributes.size();
        Assert.assertEquals(1, size);
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethodWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethodWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutMethodWhenAttributeIsAlreadyPresent() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethodWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.getBoolean("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethodWithBooleanValueAndRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        attributes.put("key", false);
        boolean actual = attributes.getBoolean("key");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        int size = attributes.size();
        Assert.assertEquals(1, size);
        String actual = attributes.get("key2");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethodWhenAttributeIsNotPresent() {
        Attributes attributes = new Attributes();
        int size = attributes.size();
        attributes.remove("key");
        Assert.assertEquals(0, size);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyMethodWhenAttributeIsNotPresent() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey("key");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size = attributes1.size();
        Assert.assertEquals(2, size);
        String actualKey1 = attributes1.get("key1");
        String expectedKey1 = "value1";
        Assert.assertEquals(expectedKey1, actualKey1);
        String actualKey2 = attributes1.get("key2");
        String expectedKey2 = "value2";
        Assert.assertEquals(expectedKey2, actualKey2);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> actualList = attributes.asList();
        int size = actualList.size();
        Assert.assertEquals(2, size);
        Attribute attribute1 = actualList.get(0);
        String actualKey1 = attribute1.getKey();
        String expectedKey1 = "key1";
        Assert.assertEquals(expectedKey1, actualKey1);
        String actualValue1 = attribute1.getValue();
        String expectedValue1 = "value1";
        Assert.assertEquals(expectedValue1, actualValue1);
        Attribute attribute2 = actualList.get(1);
        String actualKey2 = attribute2.getKey();
        String expectedKey2 = "key2";
        Assert.assertEquals(expectedKey2, actualKey2);
        String actualValue2 = attribute2.getValue();
        String expectedValue2 = "value2";
        Assert.assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> actualMap = attributes.dataset();
        int size = actualMap.size();
        Assert.assertEquals(2, size);
        String actualKey1 = actualMap.get("data-key1");
        String expectedKey1 = "data-key1";
        Assert.assertEquals(expectedKey1, actualKey1);
        String actualValue1 = actualMap.get("data-key1");
        String expectedValue1 = "value1";
        Assert.assertEquals(expectedValue1, actualValue1);
        String actualKey2 = actualMap.get("data-key2");
        String expectedKey2 = "data-key2";
        Assert.assertEquals(expectedKey2, actualKey2);
        String actualValue2 = actualMap.get("data-key2");
        String expectedValue2 = "value2";
        Assert.assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actualHtml = attributes.html();
        String expectedHtml = " key=\"value\"";
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        boolean actual1 = attributes1.equals(attributes2);
        boolean expected1 = true;
        Assert.assertEquals(expected1, actual1);
        boolean actual2 = attributes1.equals(attributes3);
        boolean expected2 = false;
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        boolean actual1 = attributes.equals(clonedAttributes);
        boolean expected1 = true;
        Assert.assertEquals(expected1, actual1);
    }
}