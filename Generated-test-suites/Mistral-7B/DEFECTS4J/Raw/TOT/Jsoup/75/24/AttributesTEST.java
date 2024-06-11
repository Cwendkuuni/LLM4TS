package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testBasicUsage() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(2, attributes.size());

        Assert.assertFalse(attributes.hasKey("nonexistentkey"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.putIgnoreCase("KEY1", "newValue");
        Assert.assertEquals("newValue", attributes.getIgnoreCase("KEY1"));
        Assert.assertEquals("newValue", attributes.get("key1"));
    }

    @Test
    public void testCaseInsensitivity() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));

        attributes.putIgnoreCase("Key1", "newValue1");
        Assert.assertEquals("newValue1", attributes.getIgnoreCase("Key1"));
        Assert.assertEquals("newValue1", attributes.get("key1"));

        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertFalse(attributes.get("key").equals(""));
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", false);
        Assert.assertFalse(attributes.get("key").equals(""));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("attr1", "val1");
        attributes1.put("attr2", "val2");

        Attributes attributes2 = new Attributes();
        attributes2.put("attr3", "val3");
        attributes2.put("attr1", "val4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        Assert.assertEquals(4, attributes3.size());
        Assert.assertEquals("val1", attributes3.get("attr1"));
        Assert.assertEquals("val2", attributes3.get("attr2"));
        Assert.assertEquals("val3", attributes3.get("attr3"));
        Assert.assertEquals("val4", attributes3.get("attr1"));

        Assert.assertTrue(attributes3.hasKey("attr1"));
        Assert.assertTrue(attributes3.hasKey("attr2"));
        Assert.assertTrue(attributes3.hasKey("attr3"));
        Assert.assertTrue(attributes3.hasKeyIgnoreCase("ATTR1"));
        Assert.assertTrue(attributes3.hasKeyIgnoreCase("ATTR2"));
        Assert.assertTrue(attributes3.hasKeyIgnoreCase("ATTR3"));
    }

    @Test
    public void testPutAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("attr1", "val1");
        attributes.put("attr2", "val2");

        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("val1", attributes.get("attr1"));
        Assert.assertEquals("val2", attributes.get("attr2"));

        attributes.remove("attr1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("attr1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("ATTR1"));

        attributes.put("attr1", "newVal1");
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("newVal1", attributes.get("attr1"));
        Assert.assertEquals("val2", attributes.get("attr2"));
    }

    @Test
    public void testRemoveAll() {
        Attributes attributes = new Attributes();
        attributes.put("attr1", "val1");
        attributes.put("attr2", "val2");
        attributes.put("attr3", "val3");

        attributes.remove("attr1");
        Assert.assertEquals(2, attributes.size());
        Assert.assertFalse(attributes.hasKey("attr1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("ATTR1"));

        attributes.remove("attr2", "val2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("attr2"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("ATTR2"));

        attributes.remove("attr3");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("attr3"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("ATTR3"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeys() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValues() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateKeys() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArrayKeys() {
        Attributes attributes = new Attributes();
        attributes.keys = new String[0];
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArrayValues() {
        Attributes attributes = new Attributes();
        attributes.vals = new String[0];
    }
}