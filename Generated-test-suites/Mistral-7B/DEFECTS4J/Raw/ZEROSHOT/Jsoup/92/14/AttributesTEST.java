package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertEquals("value", attributes.get("test"));
        Assert.assertEquals("value", attributes.getIgnoreCase("test"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("test"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("test"));
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertFalse(attributes.get("test").isEmpty());
        Assert.assertEquals(EmptyString, attributes.get("test").trim());
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("test");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testRemoveBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        attributes.remove("test");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Test", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("Test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Test"));
        Assert.assertEquals("value", attributes.get("Test"));
        Assert.assertEquals("value", attributes.getIgnoreCase("test"));
    }

    @Test
    public void testPutIgnoreCaseWithDuplicate() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Test", "value1");
        attributes.putIgnoreCase("Test", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("Test"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("test"));
    }

    @Test
    public void testPutAndGetDatasets() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-test1"));
        Assert.assertEquals("value2", dataset.get("data-test2"));

        String value = attributes.get("data-test1");
        Assert.assertEquals("value1", value);
        Assert.assertEquals("value1", attributes.getIgnoreCase("data-test1"));
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Attribute> attributesList = attributes.asList();
        Assert.assertEquals(2, attributesList.size());
        Attribute attribute1 = attributesList.get(0);
        Attribute attribute2 = attributesList.get(1);

        Assert.assertEquals("test1", attribute1.getKey());
        Assert.assertEquals("value1", attribute1.getValue());
        Assert.assertEquals("test1", attribute1.getKey().toLowerCase());
        Assert.assertEquals("test1", attribute1.getKeyIgnoreCase());

        Assert.assertEquals("test2", attribute2.getKey());
        Assert.assertEquals("value2", attribute2.getValue());
        Assert.assertEquals("test2", attribute2.getKey().toLowerCase());
        Assert.assertEquals("test2", attribute2.getKeyIgnoreCase());
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        Assert.assertEquals(2, attributes.size());
        attributes.remove("test1");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertNotSame(attributes, clonedAttributes);
        Assert.assertEquals(2, clonedAttributes.size());
        Assert.assertEquals("test1", clonedAttributes.get("test1"));
        Assert.assertEquals("test2", clonedAttributes.get("test2"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        String html = attributes.html();
        String expectedHtml = " test1=\"value1\" test2=\"value2\"";

        Assert.assertEquals(expectedHtml, html);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("test1", "value1");
        attributes2.put("test1", "value1");
        attributes3.put("test2", "value2");

        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testCloneWithDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Attributes clonedAttributes = attributes.clone();

        Map<String, String> dataset = clonedAttributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-test1"));
        Assert.assertEquals("value2", dataset.get("data-test2"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "Value");

        attributes.normalize();

        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("test", attributes.get("Test"));
        Assert.assertEquals("value", attributes.getIgnoreCase("Test"));
    }

    @Test
    public void testRemoveDuplicateAttributesWithCaseSensitivity() {
        Attributes attributes = new Attributes();
        attributes.put("Test1", "Value1");
        attributes.put("Test1", "Value2");

        attributes.removeDuplicateAttributes(true);

        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("Test1", attributes.get("Test1"));
        Assert.assertEquals("Value2", attributes.getIgnoreCase("Test1"));
    }

    @Test
    public void testRemoveDuplicateAttributesWithIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test1", "Value1");
        attributes.put("Test1", "Value2");

        attributes.removeDuplicateAttributes(false);

        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("Test1", attributes.get("Test1"));
        Assert.assertEquals("Test1", attributes.getIgnoreCase("Test1"));
    }

    @Test
    public void testRemoveDuplicateAttributesWithIgnoreCaseAndRemoveValue() {
        Attributes attributes = new Attributes();
        attributes.put("Test1", "Value1");
        attributes.put("Test1", "Value2");

        attributes.removeDuplicateAttributesWithValue(false);

        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("Test1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Test1"));
    }

    @Test
    public void testRemoveDuplicateAttributesWithCaseSensitivityAndRemoveValue() {
        Attributes attributes = new Attributes();
        attributes.put("Test1", "Value1");
        attributes.put("Test1", "Value2");

        attributes.removeDuplicateAttributesWithValue(true);

        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("Test1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Test1"));
    }

    @Test
    public void testRemoveBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Test", true);
        attributes.put("Test", false);

        attributes.removeBooleanAttributes();

        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("Test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Test"));
    }
}