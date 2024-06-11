package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        Assert.assertTrue(actual);
        Assert.assertFalse(attributes.hasKey("anotherKey"));
    }

    @Test
    public void testPutAttribute() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean removed = attributes.remove("key");
        Assert.assertTrue(removed);
        String actual = attributes.get("key");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        boolean removed = attributes.removeIgnoreCase("key");
        Assert.assertTrue(removed);
        String actual = attributes.get("Key");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey("key");
        Assert.assertFalse(actual);
        attributes.put("key", "value");
        actual = attributes.hasKey("key");
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKeyIgnoreCase("Key");
        Assert.assertFalse(actual);
        attributes.put("Key", "VALUE");
        actual = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        int actual = attributes.size();
        Assert.assertEquals(0, actual);
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        actual = attributes.size();
        Assert.assertEquals(2, actual);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size = attributes1.size();
        Assert.assertEquals(2, size);
        String key1Value = attributes1.get("key1");
        String value1Expected = "value1";
        Assert.assertEquals(value1Expected, key1Value);
        String key2Value = attributes1.get("key2");
        String value2Expected = "value2";
        Assert.assertEquals(value2Expected, key2Value);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "value");
        Map<String, String> dataset = attributes.dataset();
        String actual = dataset.get("data-test");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Document document = new Document();
        StringBuilder expected = new StringBuilder();
        expected.append(" <div key=\"value\"></div>");
        attributes.html(expected, document.outputSettings());
        String actual = attributes.html();
        Assert.assertEquals(expected.toString(), actual);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() {
        Attributes attributes = new Attributes();
        StringBuilder accum = new StringBuilder();
        // Simulate an IOException
        throw new IOException();
        // This line will never be executed
        attributes.html(accum, new Document("").outputSettings());
    }
}