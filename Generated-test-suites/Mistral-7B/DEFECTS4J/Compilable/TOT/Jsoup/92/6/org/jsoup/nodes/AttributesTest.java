package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAddAndGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(2, attributes.size());

        Assert.assertNull(attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");

        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.putIgnoreCase("key1", "newValue");

        Assert.assertEquals("newValue", attributes.get("Key1"));
        Assert.assertEquals("newValue", attributes.getIgnoreCase("key1"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertNull(attributes.get("key1"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testAddDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "anotherValue");

        Assert.assertEquals("anotherValue", attributes.get("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddEmptyStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        Assert.assertEquals("", attributes.get("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.get("key").equals(""));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddBooleanAttributeAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAttributes() {
        Attributes baseAttributes = new Attributes();
        baseAttributes.put("baseKey1", "baseValue1");
        baseAttributes.put("baseKey2", "baseValue2");

        Attributes incomingAttributes = new Attributes();
        incomingAttributes.put("incomingKey1", "incomingValue1");
        incomingAttributes.put("incomingKey2", "incomingValue2");

        Attributes mergedAttributes = new Attributes();
        mergedAttributes.addAll(baseAttributes);
        mergedAttributes.addAll(incomingAttributes);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("baseKey1", "baseValue1");
        expectedMap.put("baseKey2", "baseValue2");
        expectedMap.put("incomingKey1", "incomingValue1");
        expectedMap.put("incomingKey2", "incomingValue2");

        for (Map.Entry<String, String> entry : expectedMap.entrySet()) {
            Assert.assertEquals(entry.getValue(), mergedAttributes.get(entry.getKey()));
        }

        Assert.assertEquals(4, mergedAttributes.size());
    }
}