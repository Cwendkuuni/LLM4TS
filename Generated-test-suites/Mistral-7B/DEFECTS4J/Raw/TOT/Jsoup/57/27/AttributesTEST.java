package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        String value = attributes.get("test");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "testValue");

        String value = attributes.getIgnoreCase("test");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        String newValue = "newTestValue";;
        attributes.put("test", newValue);

        String value = attributes.get("test");
        Assert.assertEquals(newValue, value);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("test", false);

        boolean value = attributes.getBoolean("test");
        Assert.assertFalse(value);

        attributes.put("test", true);
        value = attributes.getBoolean("test");
        Assert.assertTrue(value);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("test", "testValue"));

        String value = attributes.get("test");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        attributes.remove("test");

        String value = attributes.get("test");
        Assert.assertEquals("", value);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "testValue");

        attributes.removeIgnoreCase("test");

        String value = attributes.get("Test");
        Assert.assertEquals("", value);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        boolean hasKey = attributes.hasKey("test");
        Assert.assertTrue(hasKey);

        hasKey = attributes.hasKey("Test");
        Assert.assertFalse(hasKey);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "testValue");

        boolean hasKey = attributes.hasKeyIgnoreCase("test");
        Assert.assertTrue(hasKey);

        hasKey = attributes.hasKeyIgnoreCase("Test");
        Assert.assertTrue(hasKey);

        hasKey = attributes.hasKeyIgnoreCase("TEST");
        Assert.assertTrue(hasKey);

        hasKey = attributes.hasKeyIgnoreCase("other");
        Assert.assertFalse(hasKey);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        int size = attributes.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "testValue1");
        attributes1.put("test2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test3", "testValue3");
        attributes2.put("test4", "testValue4");

        attributes1.addAll(attributes2);

        int size = attributes1.size();
        Assert.assertEquals(4, size);

        String value1 = attributes1.get("test1");
        Assert.assertEquals("testValue1", value1);

        String value2 = attributes1.get("test2");
        Assert.assertEquals("testValue2", value2);

        String value3 = attributes1.get("test3");
        Assert.assertEquals("testValue3", value3);

        String value4 = attributes1.get("test4");
        Assert.assertEquals("testValue4", value4);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        List<Attribute> asList = attributes.asList();
        for (Attribute attribute : asList) {
            String value = attribute.getValue();
            System.out.println(attribute.getKey() + "=" + value);
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-test1", "dataTestValue1"));
        attributes.put(new Attribute("data-test2", "dataTestValue2"));

        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "=" + value);
        }
    }

    @Test
    public void testHtml() throws IOException {
        Document document = new Document();
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        Element element = new Element("div");
        element.attributes(attributes);
        document.appendElement(element);

        String html = attributes.html();
        System.out.println(html);

        Assert.assertTrue(html.contains(" test=\"testValue\""));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        Attributes clonedAttributes = attributes.clone();

        String value1 = attributes.get("test");
        Assert.assertEquals("testValue", value1);

        String value2 = clonedAttributes.get("test");
        Assert.assertEquals("testValue", value2);

        attributes.remove("test");

        value1 = attributes.get("test");
        Assert.assertEquals("", value1);

        value2 = clonedAttributes.get("test");
        Assert.assertEquals("testValue", value2);
    }
}