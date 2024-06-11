package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testBasicUsage() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertNull(attributes.get("nonExistentKey"));

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertEquals("value2", dataset.get("key2"));

        // Check if the attribute values are case sensitive
        Assert.assertNotEquals("Value1", attributes.get("key1"));

        // Test empty attributes
        Attributes emptyAttributes = new Attributes();
        Assert.assertEquals(0, emptyAttributes.size());
        Assert.assertNull(emptyAttributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("key1", "value2");

        Assert.assertEquals("value2", attributes.getIgnoreCase("key1"));
        Assert.assertNull(attributes.get("key1")); // Case sensitive key does not match

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals("value2", dataset.get("key1"));
    }

    @Test
    public void testBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", "true");
        attributes.put("booleanKey", "false");

        Assert.assertFalse(attributes.getBoolean("booleanKey"));
        attributes.put("booleanKey", "");
        Assert.assertTrue(attributes.getBoolean("booleanKey"));
        attributes.put("booleanKey", null);
        Assert.assertFalse(attributes.getBoolean("booleanKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1_ignored", "value1");
        attributes.put("key2_ignored", "value2");

        attributes.removeIgnoreCase("key1_ignored");
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2_ignored"));
        Assert.assertNull(attributes.getIgnoreCase("key1_ignored"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes mergedAttributes = new Attributes();
        mergedAttributes.addAll(attributes1);
        mergedAttributes.addAll(attributes2);

        Map<String, String> mergedDataset = mergedAttributes.dataset();
        Assert.assertEquals(3, mergedAttributes.size());
        Assert.assertEquals("value1", mergedDataset.get("key1"));
        Assert.assertEquals("value2", mergedDataset.get("key2"));
        Assert.assertEquals("value3", mergedDataset.get("key3"));
    }

    @Test
    public void testClone() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("key1", "value1");
        originalAttributes.put("key2", "value2");

        Attributes clonedAttributes = originalAttributes.clone();
        Assert.assertNotSame(originalAttributes, clonedAttributes);
        Assert.assertEquals(originalAttributes.size(), clonedAttributes.size());

        for (int i = 0; i < originalAttributes.size(); i++) {
            String key = originalAttributes.keys[i];
            String value = originalAttributes.vals[i];
            Assert.assertEquals(key, clonedAttributes.keys[i]);
            Assert.assertEquals(value, clonedAttributes.vals[i]);
        }
    }
}