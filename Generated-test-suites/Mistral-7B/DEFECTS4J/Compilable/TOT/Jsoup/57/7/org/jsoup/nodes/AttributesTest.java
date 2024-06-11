package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");

        String id = attributes.get("id");
        String classAttribute = attributes.get("class");

        Assert.assertEquals("testId", id);
        Assert.assertEquals("testClass", classAttribute);

        String nullValue = attributes.get("nonExistentAttribute");
        Assert.assertEquals("", nullValue);
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "testId");
        attributes.put("Class", "testClass");

        String id = attributes.getIgnoreCase("id");
        String classAttribute = attributes.getIgnoreCase("class");

        Assert.assertEquals("testId", id);
        Assert.assertEquals("testClass", classAttribute);

        String nullValue = attributes.getIgnoreCase("nonExistentAttribute");
        Assert.assertEquals("", nullValue);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");

        String newValue = "newValue";
        attributes.put("id", newValue);

        Assert.assertEquals(newValue, attributes.get("id"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-toggle", "true");
        String value = attributes.get("data-toggle");
        Assert.assertEquals("true", value);

        attributes.put("data-toggle", false);
        value = attributes.get("data-toggle");
        Assert.assertFalse(value.equals("true"));
    }

    @Test
    public void testPutAttributeWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("id", "newTestId");

        String id = attributes.get("id");
        Assert.assertEquals("newTestId", id);
    }

    @Test
    public void testPutNullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(null, "testValue");
        String nullValue = attributes.get(null);
        Assert.assertEquals("testValue", nullValue);
    }

    @Test
    public void testPutNullAttributeValue() {
        Attributes attributes = new Attributes();
        attributes.put("id", null);
        String nullValue = attributes.get("id");
        Assert.assertEquals("", nullValue);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.remove("id");
        String nullValue = attributes.get("id");
        Assert.assertEquals("", nullValue);
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "testId");
        attributes.removeIgnoreCase("id");
        String nullValue = attributes.get("Id");
        Assert.assertEquals("", nullValue);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        boolean hasKey = attributes.hasKey("id");
        Assert.assertTrue(hasKey);

        hasKey = attributes.hasKeyIgnoreCase("Id");
        Assert.assertTrue(hasKey);

        hasKey = attributes.hasKey("nonExistentAttribute");
        Assert.assertFalse(hasKey);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");

        int size = attributes.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("id1", "testId1");
        Attributes attributes2 = new Attributes();
        attributes2.put("id2", "testId2");

        attributes1.addAll(attributes2);
        int size = attributes1.size();
        Assert.assertEquals(

           3, size);

        String id1 = attributes1.get("id1");
        Assert.assertEquals("testId1", id1);

        String id2 = attributes1.get("id2");
        Assert.assertEquals("testId2", id2);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");

        List<Attribute> attributeList = attributes.asList();

        Attribute idAttribute = attributeList.get(0);
        Assert.assertEquals("id", idAttribute.getKey());
        Assert.assertEquals("testId", idAttribute.getValue());

        Attribute classAttribute = attributeList.get(1);
        Assert.assertEquals("class", classAttribute.getKey());
        Assert.assertEquals("testClass", classAttribute.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-testId", "testId");
        attributes.put("data-testClass", "testClass");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("testId", dataset.get("testId"));
        Assert.assertEquals("testClass", dataset.get("testClass"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");

        String html = attributes.html();
        Assert.assertEquals("<html><head><meta charset=\"utf-8\"></head><body " +
                "id=\"testId\" class=\"testClass\"></body></html>", html);
    }
}