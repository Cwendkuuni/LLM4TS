package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Dataset;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.serialization.SerializationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = createAttributesWithKeyValue("Key", "value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);

        result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);

        result = attributes.hasKeyIgnoreCase("Key");
        Assert.assertTrue(result);

        Attributes emptyAttributes = new Attributes();
        result = emptyAttributes.hasKey("key");
        Assert.assertFalse(result);

        result = emptyAttributes.hasKeyIgnoreCase("key");
        Assert.assertFalse(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = createAttributesWithKeyValue("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);

        attributes.put("key", "newValue");
        result = attributes.get("key");
        Assert.assertEquals("newValue", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);

        attributes.put("key", false);
        result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        attributes.remove("key");
        String result = attributes.get("key");
        Assert.assertEquals("", result);

        attributes.put("key", "value");
        attributes.removeIgnoreCase("key");
        result = attributes.get("key");
        Assert.assertEquals("value", result);

        attributes.removeIgnoreCase("Key");
        result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        attributes.removeIgnoreCase("key");
        String result = attributes.get("key");
        Assert.assertEquals("", result);

        attributes.removeIgnoreCase("Key");
        result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testHasKeyIgnoreCaseWithEmptyAttributes() {
        Attributes emptyAttributes = new Attributes();
        boolean result = emptyAttributes.hasKeyIgnoreCase("key");
        Assert.assertFalse(result);
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

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        attributes.remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullIgnoreCase() {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        attributes.removeIgnoreCase(null);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = createAttributesWithKeyValue("key1", "value1");
        Attributes attributes2 = createAttributesWithKeyValue("key2", "value2");
        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        String resultValue1 = result.get("key1");
        Assert.assertEquals("value1", resultValue1);

        String resultValue2 = result.get("key2");
        Assert.assertEquals("value2", resultValue2);

        boolean hasKey1 = result.hasKey("key1");
        Assert.assertTrue(hasKey1);

        boolean hasKey2 = result.hasKey("key2");
        Assert.assertTrue(hasKey2);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test
    public void testDataset() {
        Attributes attributes = createAttributesWithDataAttribute("dataKey", "dataValue");
        Dataset dataset = attributes.dataset();
        Map<String, String> result = (Map<String, String>) dataset;
        String resultValue = result.get("dataKey");
        Assert.assertEquals("dataValue", resultValue);
    }

    @Test
    public void testHtml() throws SerializationException {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        StringBuilder accum = new StringBuilder();
        Document doc = new Document();
        attributes.html(accum, doc.outputSettings());
        String result = accum.toString();
        String expected = "<key>value</key>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = createAttributesWithKeyValue("key1", "value1");
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = createAttributesWithKeyValue("key1", "value1");

        boolean result1 = attributes1.equals(attributes1);
        Assert.assertTrue(result1);

        boolean result2 = attributes1.equals(attributes2);
        Assert.assertFalse(result2);

        boolean result3 = attributes1.equals(attributes3);
        Assert.assertTrue(result3);
    }

    @Test
    public void testClone() {
        Attributes attributes = createAttributesWithKeyValue("key", "value");
        Attributes clonedAttributes = attributes.clone();
        String result = clonedAttributes.get("key");
        Assert.assertEquals("value", result);
    }

    private Attributes createAttributesWithKeyValue(String key, String value) {
        Attributes attributes = new Attributes();
        attributes.put(key, value);
        return attributes;
    }

    private Attributes createAttributesWithDataAttribute(String dataKey, String dataValue) {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute(dataKey, dataValue));
        return attributes;
    }
}