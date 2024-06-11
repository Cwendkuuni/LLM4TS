package org.test; // replace with your test package

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @Test
    public void getTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void getIgnoreCaseTest() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void putTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void putIgnoreCaseTest() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void putBooleanTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
        result = attributes.getIgnoreCase("key") == null;
        Assert.assertTrue(result);
    }

    @Test
    public void putTestWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String result = attributes.get("key");
        Assert.assertEquals("value2", result);
    }

    @Test(expected = NullPointerException.class)
    public void putNullKeyTest() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void putNullValueTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void removeTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void removeTestWithMissingKey() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);
        attributes.remove("key");
        result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void hasKeyTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void hasKeyIgnoreCaseTest() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);
    }

    @Test
    public void hasKeyIgnoreCaseTestWithMissingKey() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertFalse(result);
    }

    @Test
    public void sizeTest() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void addAllTest() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int result = attributes1.size();
        Assert.assertEquals(3, result);
        String value1 = attributes1.get("key1");
        Assert.assertEquals("value1", value1);
        String value2 = attributes1.get("key2");
        Assert.assertEquals("value2", value2);
    }

    @Test
    public void asListTest() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        Attribute attribute1 = list.get(0);
        Attribute attribute2 = list.get(1);
        String value1 = attribute1.getValue();
        String value2 = attribute2.getValue();
        Assert.assertEquals("value1", value1);
        Assert.assertEquals("value2", value2);
    }

    @Test
    public void datasetTest() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        String value1 = dataset.get("key1");
        String value2 = dataset.get("key2");
        Assert.assertEquals("value1", value1);
        Assert.assertEquals("value2", value2);
    }

    @Test
    public void htmlTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String html = attributes.html();
        Assert.assertEquals(" key=\"value\"", html);
    }

    @Test
    public void equalsTest() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Assert.assertEquals(attributes1, attributes2);
    }

    @Test
    public void cloneTest() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        String result = clonedAttributes.get("key");
        Assert.assertEquals("value", result);
    }
}