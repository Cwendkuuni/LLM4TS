package org.junit.tests;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("key") && attributes.size() > 1);
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        Assert.assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        Assert.assertEquals(2, attributes3.size());
        Assert.assertTrue(attributes3.hasKey("key1"));
        Assert.assertTrue(attributes3.hasKey("key2"));
        Assert.assertEquals("value1", attributes3.get("key1"));
        Assert.assertEquals("value2", attributes3.get("key2"));
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("value1", list.get(0).getValue());
        Assert.assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("value1", list.get(0).getValue());
        Assert.assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key=\"value\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null);

        Assert.assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithException() throws IOException {
        Attributes attributes = new Attributes();

        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes, clonedAttributes);
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
    }

    @Test
    public void testEmptyConstructor() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testDataPrefixVariable() {
        Attributes attributes = new Attributes();
        String dataKey = Attributes.dataPrefix + "test";
        attributes.put(new Attribute(dataKey, "value"));

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals("value", dataset.get(dataKey));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        Attributes attributes = new Attributes();
        attributes.get(null);
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

    @Test(expected = IllegalArgumentException.class)
    public void testPutBooleanNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAttributeNullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(null, new Attribute("key", "value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAttributeNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute(null, "value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseNullKey() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("non-existent-key");
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        Assert.assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutDuplicateKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value1");
        attributes.put("key", "value2");

        Assert.assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testGetEmptyList() {
        Attributes attributes = new Attributes();
        List<Attribute> attributesList = attributes.asList();

        Assert.assertNotNull(attributesList);
        Assert.assertEquals(0, attributesList.size());
    }

    @Test
    public void testGetNullList() {
        Attributes attributes = new Attributes();
        List<Attribute> attributesList = null;

        List<Attribute> result = attributes.asList();

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
        attributes.asList() == attributesList; // check that the returned list is not the same as the null input
    }

    @Test
    public void testGetEmptyMap() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();

        Assert.assertNotNull(dataset);
        Assert.assertEquals(0, dataset.size());
    }

    @Test
    public void testGetNullMap() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = null;

        Map<String, String> result = attributes.dataset();

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
        attributes.dataset() == dataset; // check that the returned map is not the same as the null input
    }
}