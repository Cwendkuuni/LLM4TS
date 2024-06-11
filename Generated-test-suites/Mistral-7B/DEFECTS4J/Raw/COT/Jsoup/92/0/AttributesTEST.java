package org.test; // Replace with your test package

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
        attributes.put("key", "value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetEmptyString() {
        Attributes attributes = new Attributes();
        String result = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, result);
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
        attributes.putIgnoreCase("Key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
        result = attributes.hasKeyIgnoreCase("Key");
        Assert.assertTrue(result);
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        String result = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, result);
    }

    @Test
    public void testPutWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String result1 = attributes.get("key");
        Assert.assertEquals("value2", result1);
        String result2 = attributes.getIgnoreCase("key");
        Assert.assertEquals("value2", result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put(Attributes.EmptyString, "value");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String result = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        String result = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, result);
    }

    @Test
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        String result = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, result);
        attributes.remove("key");
        result = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
        result = attributes.hasKeyIgnoreCase("Key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
        result = attributes.hasKeyIgnoreCase("KEY");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyEmptyString() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey(Attributes.EmptyString);
        Assert.assertFalse(result);
        result = attributes.hasKeyIgnoreCase(Attributes.EmptyString);
        Assert.assertFalse(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);
        int size = result.size();
        Map<String, String> map = result.dataset();
        String[] keys = new String[size];
        String[] vals = new String[size];
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            keys[i] = entry.getKey();
            vals[i] = entry.getValue();
            i++;
        }
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key1", "value1");
        expectedAttributes.put("key2", "value2");
        String[] expectedKeys = new String[]{"key1", "key2"};
        String[] expectedVals = new String[]{"value1", "value2"};
        Assert.assertArrayEquals(expectedKeys, keys);
        Assert.assertArrayEquals(expectedVals, vals);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.toString();
        String expected = "Attributes{size=1, keys=[key], vals=[value]}";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        Attributes attributes4 = new Attributes();
        Attributes expected = new Attributes();
        expected.put("key1", "value1");
        Assert.assertTrue(attributes1.equals(attributes1));
        Assert.assertTrue(attributes1.equals(attributes2));
        Assert.assertFalse(attributes1.equals(attributes3));
        Assert.assertFalse(attributes1.equals(attributes4));
        Assert.assertFalse(attributes1.equals(null));
        Assert.assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        Attributes expected = new Attributes();
        expected.put("key1", "value1");
        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertEquals(attributes1.hashCode(), expected.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        String result1 = attributes.get("key1");
        String result2 = clonedAttributes.get("key1");
        Assert.assertEquals("value1", result1);
        Assert.assertEquals("value1", result2);
        Assert.assertNotSame(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.normalize();
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testRemoveDuplicates() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        int sizeBeforeRemoveDuplicates = attributes.size();
        attributes.removeDuplicates();
        int sizeAfterRemoveDuplicates = attributes.size();
        Assert.assertEquals(1, sizeBeforeRemoveDuplicates - sizeAfterRemoveDuplicates);
        String key = "key1";
        String result = attributes.get(key);
        Assert.assertNotNull(result);
    }

    
}