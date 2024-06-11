package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(new String[0], attributes.keys);
        Assert.assertArrayEquals(new String[0], attributes.vals);
    }

    @Test
    public void testAddGetRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertNull(attributes.get("nonExistentKey"));

        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testCaseInsensitiveGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        Assert.assertNull(attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        Assert.assertEquals(3, attributes1.size());
        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertEquals("value3", attributes1.get("key2"));
        Assert.assertEquals("value3", attributes1.get("key3"));
        Assert.assertNull(attributes1.get("nonExistentKey"));
    }

    @Test
    public void testCaseInsensitiveAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("Key2", "value2");

        attributes1.addAll(attributes2);

        Assert.assertEquals(1, attributes1.size());
        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertNull(attributes1.get("key2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveByKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        attributes.removeIgnoreCase("key1");
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-key1"));
        Assert.assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.put("key2", "value2");

        attributes.normalize();

        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("key1", attributes.keys[0]);
        Assert.assertEquals("value1", attributes.vals[0]);
        Assert.assertEquals("key2", attributes.keys[1]);
        Assert.assertEquals("value2", attributes.vals[1]);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(2, clonedAttributes.size());
        Assert.assertEquals("value1", clonedAttributes.get("key1"));
        Assert.assertEquals("value2", clonedAttributes.get("key2"));
    }
}