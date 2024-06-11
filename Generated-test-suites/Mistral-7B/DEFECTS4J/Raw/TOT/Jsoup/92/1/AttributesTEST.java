package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    @Test
    public void testBasicUsage() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertNull(attributes.get("nonExistentKey"));

        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testCaseInsensitivity() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("KEY2"));
        Assert.assertNull(attributes.get("nonExistentKey"));

        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("KEY2"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("KEY2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("KEY2"));
        Assert.assertNull(attributes.get("nonExistentKey"));

        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("KEY2"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        Assert.assertNull(attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));

        attributes.put("key1", false);
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));

        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("KEY2"));

        attributes.removeIgnoreCase("key1");
        Assert.assertEquals("value2", attributes.get("KEY2"));
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertFalse(attributes.hasKey("value1"));
    }

    @Test
    public void testPutWithExistingKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY1", "value2");

        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertFalse(attributes.hasKey("value1"));
    }

    @Test
    public void testPutAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));

        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        Assert.assertEquals(3, attributes3.size());
        Assert.assertTrue(attributes3.hasKey("key1"));
        Assert.assertTrue(attributes3.hasKey("key2"));
        Assert.assertTrue(attributes3.hasKey("key3"));
        Assert.assertEquals("value1", attributes3.get("key1"));
        Assert.assertEquals("value2", attributes3.get("key2"));
        Assert.assertEquals("value3", attributes3.get("key3"));
    }

    @Test
    public void testSizeAndCapacity() {
        Attributes attributes = new Attributes();
        for (int i = 0; i < 10; i++) {
            attributes.put("key" + i, "value" + i);
        }

        Assert.assertEquals(10, attributes.size());
        // This should trigger a capacity increase and not throw an exception
        attributes.put("key11", "value11");
        Assert.assertEquals(11, attributes.size());
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
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder sb = new StringBuilder();
        try {
            attributes.html(sb, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String expected = " key1=\"value1\" key2=\"value2\"";
        String actual = sb.toString();
        Assert.assertEquals(expected, actual);
    }
}