package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributesInstance() {
        Attributes attributes = new Attributes();
        Assert.assertNotNull(attributes);
    }

    @Test
    public void testGetEmptyAttribute() {
        Attributes attributes = new Attributes();
        String value = attributes.get("test");
        Assert.assertEquals("", value);
    }

    @Test
    public void testPutGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        String value = attributes.get("test");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Test", "value");
        String value = attributes.get("test");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        boolean value = attributes.getBoolean("test");
        Assert.assertTrue(value);

        attributes.put("Test", false);
        value = attributes.getBoolean("test");
        Assert.assertFalse(value);
    }

    @Test
    public void testPutAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", null);
        String value = attributes.get("test");
        Assert.assertEquals("", value);
    }

    @Test
    public void testPutAndGetMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        String test1Value = attributes.get("test1");
        String test2Value = attributes.get("test2");
        Assert.assertEquals("value1", test1Value);
        Assert.assertEquals("value2", test2Value);
    }

    @Test
    public void testPutSameAttributeMultipleTimes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.put("test", "newValue");
        String value = attributes.get("test");
        Assert.assertEquals("newValue", value);
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        String value = attributes.getIgnoreCase("test");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testHasKeyAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        boolean hasKey = attributes.hasKey("test");
        Assert.assertTrue(hasKey);
    }

    @Test
    public void testHasKeyIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        boolean hasKeyIgnoreCase = attributes.hasKeyIgnoreCase("test");
        Assert.assertTrue(hasKeyIgnoreCase);
    }

    @Test
    public void testHasNoKeyAttribute() {
        Attributes attributes = new Attributes();
        boolean hasKey = attributes.hasKey("test");
        Assert.assertFalse(hasKey);
    }

    @Test
    public void testHasNoKeyIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        boolean hasKeyIgnoreCase = attributes.hasKeyIgnoreCase("test");
        Assert.assertFalse(hasKeyIgnoreCase);
    }

    @Test
    public void testSizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        int size = attributes.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testAddAllAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        int size = result.size();
        Assert.assertEquals(4, size);

        String test1Value = result.get("test1");
        String test2Value = result.get("test2");
        Assert.assertEquals("value1", test1Value);
        Assert.assertEquals("value2", test2Value);
    }

    @Test
    public void testIteratorAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Attribute> attributesList = attributes.asList();
        for (Attribute attribute : attributesList) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            System.out.println(key + " = " + value);
        }
    }

    @Test
    public void testDatasetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + " = " + value);
        }
    }

    @Test
    public void testHtmlAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        String html = attributes.html();
        System.out.println(html);
    }

    @Test
    public void testToStringAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        String toString = attributes.toString();
        System.out.println(toString);
    }

    @Test
    public void testEqualsAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("test1", "value1");
        attributes1.put("test2", "value2");

        attributes2.put("test2", "value2");
        attributes2.put("test3", "value3");

        attributes3.put("test1", "value1");
        attributes3.put("test2", "value2");
        attributes3.put("test3", "value3");

        boolean equals = attributes1.equals(attributes3);
        Assert.assertTrue(equals);

        equals = attributes1.equals(attributes2);
        Assert.assertFalse(equals);
    }

    @Test
    public void testCloneAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        Attributes clonedAttributes = (Attributes) attributes.clone();

        int size = clonedAttributes.size();
        Assert.assertEquals(2, size);

        String test1Value = clonedAttributes.get("test1");
        String test2Value = clonedAttributes.get("test2");
        Assert.assertEquals("value1", test1Value);
        Assert.assertEquals("value2", test2Value);
    }
}