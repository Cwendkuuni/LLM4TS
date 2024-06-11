package org.example; // replace with your test package name

import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {
    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String retrievedValue = attributes.get("key");
        Assert.assertEquals("value", retrievedValue);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String retrievedValue = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", retrievedValue);
    }

    @Test
    public void testGetEmpty() {
        Attributes attributes = new Attributes();
        String retrievedValue = attributes.get("nonExistentKey");
        Assert.assertEquals(Attributes.EmptyString, retrievedValue);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String retrievedValue = attributes.get("key");
        Assert.assertEquals("value2", retrievedValue);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value1");
        attributes.put("KEY", "value2");
        String retrievedValue = attributes.getIgnoreCase("key");
        Assert.assertEquals("value2", retrievedValue);
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
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.getBoolean("key"));
        attributes.remove("key");
        Assert.assertFalse(attributes.getBoolean("key"));
    }

    @Test
    public void testPutIgnoreCaseBoolean() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", true);
        Assert.assertTrue(attributes.getBoolean("key"));
        attributes.remove("key");
        Assert.assertFalse(attributes.getBoolean("key"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("Key1", "Value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("Key2", "Value2");
        Attributes combinedAttributes = new Attributes();

        combinedAttributes.addAll(attributes1);
        combinedAttributes.addAll(attributes2);

        Assert.assertEquals(2, combinedAttributes.size());
        Assert.assertEquals("Value1", combinedAttributes.get("Key1"));
        Assert.assertEquals("Value2", combinedAttributes.get("Key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");
        attributes.remove("KEY1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("KEY1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());
    }

    
}