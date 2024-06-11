package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AttributesTest {
    // Basic test cases for get method

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        Assert.assertEquals("red", attributes.get("color"));
    }

    @Test
    public void testGetMethodWithEmptyString() {
        Attributes attributes = new Attributes();
        Assert.assertEquals("", attributes.get("empty"));
    }

    @Test
    public void testGetMethodWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("class", null);
        Assert.assertEquals("", attributes.get("class"));
    }

    @Test
    public void testGetMethodWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        Assert.assertEquals("", attributes.get("checked"));
        Assert.assertTrue(attributes.hasKey("checked"));
    }

    // Basic test cases for getIgnoreCase method

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        Assert.assertEquals("red", attributes.getIgnoreCase("color"));
        Assert.assertEquals("red", attributes.getIgnoreCase("Color"));
    }

    @Test
    public void testGetIgnoreCaseMethodWithEmptyString() {
        Attributes attributes = new Attributes();
        Assert.assertEquals("", attributes.getIgnoreCase("empty"));
        Assert.assertEquals("", attributes.getIgnoreCase("eMPTY"));
    }

    @Test
    public void testGetIgnoreCaseMethodWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("CLASS", null);
        Assert.assertEquals("", attributes.getIgnoreCase("class"));
        Assert.assertEquals("", attributes.getIgnoreCase("CLaSs"));
    }

    @Test
    public void testGetIgnoreCaseMethodWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("CHECKED", true);
        Assert.assertEquals("", attributes.getIgnoreCase("checked"));
        Assert.assertEquals("", attributes.getIgnoreCase("cHeCkEd"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("checked"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("checked"));
    }

    // Test cases for put method

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "medium");
        Assert.assertEquals("red", attributes.get("color"));
        Assert.assertEquals("medium", attributes.get("size"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("color", "blue");
    }

    @Test
    public void testPutMethodWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        attributes.put("checked", false);
        Assert.assertFalse(attributes.hasKey("checked"));
        Assert.assertEquals("", attributes.get("checked"));
    }

    // Test cases for putIgnoreCase method

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.put("size", "medium");
        Assert.assertEquals("red", attributes.get("Color"));
        Assert.assertEquals("medium", attributes.get("size"));
    }

    @Test
    public void testPutIgnoreCaseMethodWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.put("cOLor", "blue");
        Assert.assertEquals("blue", attributes.get("Color"));
        Assert.assertEquals("red", attributes.get("cOLor"));
    }

    @Test
    public void testPutIgnoreCaseMethodWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("cHeCkEd", true);
        attributes.put("CHECKED", false);
        Assert.assertFalse(attributes.hasKey("checked"));
        Assert.assertEquals("", attributes.get("checked"));
        Assert.assertEquals("", attributes.get("CHECKED"));
    }

    // Test cases for put method with Attribute object

    @Test
    public void testPutMethodWithAttributeObject() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("color", "red");
        attributes.put(attribute);
        Assert.assertEquals("red", attributes.get("color"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithAttributeObjectWithSameKey() {
        Attributes attributes = new Attributes();
        Attribute attribute1 = new Attribute("color", "red");
        Attribute attribute2 = new Attribute("color", "blue");
        attributes.put(attribute1);
        attributes.put(attribute2);
    }

    // Test cases for putIgnoreCaseMethod with Attribute object

    @Test
    public void testPutIgnoreCaseMethodWithAttributeObject() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("Color", "red");
        attributes.put(attribute);
        Assert.assertEquals("red", attributes.get("Color"));
    }

    @Test
    public void testPutIgnoreCaseMethodWithAttributeObjectWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        Attribute attribute1 = new Attribute("Color", "red");
        Attribute attribute2 = new Attribute("cOLor", "blue");
        attributes.put(attribute1);
        attributes.put(attribute2);
        Assert.assertEquals("red", attributes.get("Color"));
        Assert.assertEquals("blue", attributes.get("cOLor"));
    }

    // Test cases for remove method

    @Test
    public void testRemoveMethodWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "medium");
        attributes.remove("color");
        Assert.assertEquals("medium", attributes.get("size"));
        Assert.assertFalse(attributes.hasKey("color"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethodWithNonExistingKey() {
        Attributes attributes = new Attributes();
        attributes.remove("color");
    }

    // Test cases for remove method with ignore case

    @Test
    public void testRemoveMethodWithIgnoreCaseExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.put("size", "medium");
        attributes.remove("Color");
        Assert.assertEquals("medium", attributes.get("size"));
        Assert.assertFalse(attributes.hasKey("Color"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethodWithIgnoreCaseNonExistingKey() {
        Attributes attributes = new Attributes();
        attributes.remove("Color");
    }

    // Test cases for hasKey method

    @Test
    public void testHasKeyMethodWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        Assert.assertTrue(attributes.hasKey("color"));
    }

    @Test
    public void testHasKeyMethodWithNonExistingKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("color"));
    }

    // Test cases for hasKeyIgnoreCase method

    @Test
    public void testHasKeyIgnoreCaseMethodWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Color"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("cOlOr"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethodWithNonExistingKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Color"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("cOlOr"));
    }

    // Test cases for size method

    @Test
    public void testSizeMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testSizeMethodWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "medium");
        Assert.assertEquals(2, attributes.size());
    }

    // Test cases for addAll method

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("color1", "red1");
        attributes1.put("color2", "red2");

        Attributes attributes2 = new Attributes();
        attributes2.put("color3", "red3");
        attributes2.put("color4", "red4");

        Attributes combinedAttributes = new Attributes();
        combinedAttributes.addAll(attributes1);
        combinedAttributes.addAll(attributes2);

        List<Map.Entry<String, String>> result = Arrays.asList(
                new AbstractMap.SimpleEntry<>("color1", "red1"),
                new AbstractMap.SimpleEntry<>("color2", "red2"),
                new AbstractMap.SimpleEntry<>("color3", "red3"),
                new AbstractMap.SimpleEntry<>("color4", "red4")
        );

        Assert.assertEquals(result, combinedAttributes.asList());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllMethodWithNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    // Test cases for dataset method

    @Test
    public void testDatasetMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(0, dataset.size());
    }

    @Test
    public void testDatasetMethodWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-color", "red");
        attributes.put("data-size", "medium");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("red", dataset.get("color"));
        Assert.assertEquals("medium", dataset.get("size"));
    }

    // Test cases for html method

    @Test
    public void testHtmlMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        String html = attributes.html();
        Assert.assertEquals("", html);
    }

    @Test
    public void testHtmlMethodWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "medium");
        String html = attributes.html();
        Assert.assertEquals(" color=\"red\" size=\"medium\" ", html);
    }

    @Test
    public void testHtmlMethodWithBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        String html = attributes.html();
        Assert.assertEquals(" checked ", html);
    }
}