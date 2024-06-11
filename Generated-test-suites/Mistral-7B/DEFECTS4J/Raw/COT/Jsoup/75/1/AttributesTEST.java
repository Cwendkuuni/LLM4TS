package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jsoup.internal.Normalizer.lowerCase;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetNullable() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("nonExistentKey");
        Assert.assertEquals(Attributes.EmptyString, actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testPutEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put(Attributes.EmptyString, "value");
        String actual = attributes.get(Attributes.EmptyString);
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutEmptyList() {
        Attributes attributes = new Attributes();
        List<String> keys = Arrays.asList("key1", "key2");
        List<String> values = Arrays.asList("value1", "value2");
        attributes.putAll(new Attributes(keys, values));
        int size = attributes.size();
        Assert.assertEquals(keys.size(), size);
        for (int i = 0; i < keys.size(); i++) {
            String actualKey = attributes.get(i);
            String actualValue = attributes.getIgnoreCase(lowerCase(actualKey));
            String expectedKey = keys.get(i);
            String expectedValue = values.get(i);
            Assert.assertEquals(expectedKey, actualKey);
            Assert.assertEquals(expectedValue, actualValue);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.putAll(null);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        String actual = attributes.get("key");
        String expected = "Value";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseNullKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase(null, "Value");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveByIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove(1);
    }

    @Test
    public void testRemoveByIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String actual1 = attributes.get("key1");
        String actual2 = attributes.get("key2");
        attributes.remove(0);
        String removedValue = attributes.get("key1");
        Assert.assertEquals("value1", actual1);
        Assert.assertEquals("value2", actual2);
        Assert.assertEquals(Attributes.EmptyString, removedValue);
    }

    @Test
    public void testRemoveByKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String actual1 = attributes.get("key1");
        String actual2 = attributes.get("key2");
        attributes.remove("key1");
        String removedValue = attributes.get("key1");
        Assert.assertEquals("value1", actual1);
        Assert.assertEquals("value2", actual2);
        Assert.assertEquals(Attributes.EmptyString, removedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveByKeyNull() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testHasKeyByIndexOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        boolean actual = attributes.hasKey(1);
        Assert.assertFalse(actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        boolean actual = attributes.hasKey("key1");
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        boolean actual = attributes.hasKeyIgnoreCase("key1");
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCaseFalse() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKeyIgnoreCase("nonExistentKey");
        Assert.assertFalse(actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        int size = attributes.size();
        Assert.assertEquals(0, size);
        attributes.put("key1", "value1");
        size = attributes.size();
        Assert.assertEquals(1, size);
        attributes.put("key2", "value2");
        size = attributes.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testIsEmpty() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.isEmpty();
        Assert.assertTrue(actual);
        attributes.put("key1", "value1");
        actual = attributes.isEmpty();
        Assert.assertFalse(actual);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes clonedAttributes = attributes.clone();
        int size = attributes.size();
        Assert.assertEquals(size, clonedAttributes.size());
        for (int i = 0; i < size; i++) {
            String actualKey = attributes.get(i);
            String actualValue = attributes.getIgnoreCase(lowerCase(actualKey));
            String clonedKey = clonedAttributes.get(i);
            String clonedValue = clonedAttributes.getIgnoreCase(lowerCase(clonedKey));
            Assert.assertEquals(actualKey, clonedKey);
            Assert.assertEquals(actualValue, clonedValue);
        }
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.normalize();
        String actual = attributes.get("key1");
        Assert.assertEquals("value1", actual);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertNull(dataset.get("nonExistentKey"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, null);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
        String actual = accum.toString();
        String expected = " key1=\"value1\"";
        Assert.assertEquals(expected, actual);
    }
}