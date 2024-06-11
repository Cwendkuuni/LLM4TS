package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributesWithEmptyConstructor() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Attributes.Empty, attributes.keys);
        Assert.assertArrayEquals(Attributes.Empty, attributes.vals);
    }

    @Test
    public void testAttributesWithInitialCapacity() {
        Attributes attributes = new Attributes(4);
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(new String[4], attributes.keys);
        Assert.assertArrayEquals(new String[4], attributes.vals);
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("key1", attributes.keys[0]);
        Assert.assertEquals("value1", attributes.vals[0]);
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertEquals("key1", attributes.keys[0]);
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.get("booleanKey").equals("false"));
        Assert.assertEquals("booleanKey", attributes.keys[0]);
        Assert.assertNull(attributes.vals[0]);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Attributes.Empty, attributes.keys);
        Assert.assertArrayEquals(Attributes.Empty, attributes.vals);
    }

    @Test
    public void testRemoveNonExistentAttribute() {
        Attributes attributes = new Attributes();
        attributes.remove("nonExistentKey");
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Attributes.Empty, attributes.keys);
        Assert.assertArrayEquals(Attributes.Empty, attributes.vals);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.putAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertEquals("value2", attributes1.get("key2"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(1, clonedAttributes.size());
        Assert.assertEquals("value1", clonedAttributes.get("key1"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        Attributes dataset = attributes.dataset();
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals("value1", dataset.get("key1"));
    }
}