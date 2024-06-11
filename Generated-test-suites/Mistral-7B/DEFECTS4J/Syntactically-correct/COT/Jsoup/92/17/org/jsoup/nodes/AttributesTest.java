package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        attributes.put("KEY", "value");

        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetEmptyString() {
        Attributes attributes = new Attributes();
        String result = attributes.get("key");
        Assert.assertEquals("", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");

        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);

        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);

        attributes.put("key", true);
        result = attributes.hasKey("key");
        Assert.assertTrue(result);
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

        List<Map.Entry<String, String>> expected = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"));

        List<Map.Entry<String, String>> result = attributes3.asList();
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllNull() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);

        result = attributes.hasKey("anotherKey");
        Assert.assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");

        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");

        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNull() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testRemoveKeyDoesNotExist() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);

        attributes.remove("key");
        result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");

        attributes.removeIgnoreCase("key");
        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);
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
    public void testEmpty() {
        Attributes attributes = new Attributes();
        int result = attributes.size();
        Assert.assertEquals(0, result);
    }
}