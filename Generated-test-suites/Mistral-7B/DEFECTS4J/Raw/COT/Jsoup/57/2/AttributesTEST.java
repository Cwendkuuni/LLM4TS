package org.jsoup.nodes; // update the package name if necessary

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    private static final String TEST_CASE_SUFFIX = "Test";

    @Test
    public void testGet() {
        Attributes attributes = createAttributesWithKeyValuePair("key", "value");
        String value = attributes.get("key");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = createAttributesWithKeyValuePair("Key", "value");
        String value = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String value = attributes.get("key");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "existingValue");
        attributes.put("key", "newValue");
        String value = attributes.get("key");
        Assert.assertEquals("newValue", value);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("key") && attributes.get("key").equals("false"));
    }

    @Test
    public void testPutNullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(null); // should be a compile-time error but just to illustrate the scenario
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        String value = attributes.get("key");
        Assert.assertNull(value);
    }

    @Test
    public void testRemove() {
        Attributes attributes = createAttributesWithKeyValuePair("key", "value");
        attributes.remove("key");
        String value = attributes.get("key");
        Assert.assertEquals("", value);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = createAttributesWithKeyValuePair("Key", "value");
        attributes.removeIgnoreCase("key");
        String value = attributes.get("key");
        Assert.assertEquals("", value);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = createAttributesWithKeyValuePair("key", "value");
        boolean hasKey = attributes.hasKey("key");
        Assert.assertTrue(hasKey);
        boolean hasIgnoreCaseKey = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(hasIgnoreCaseKey);
        boolean hasMissingKey = attributes.hasKey("missingKey");
        Assert.assertFalse(hasMissingKey);
    }

    @Test
    public void testHasKeyIgnoreCaseWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        boolean hasIgnoreCaseKey = attributes.hasKeyIgnoreCase("key");
        Assert.assertFalse(hasIgnoreCaseKey);
    }

    @Test
    public void testSize() {
        Attributes attributes = createAttributesWithKeyValuePair("key1", "value1");
        int size = attributes.size();
        Assert.assertEquals(1, size);

        attributes.put("key2", "value2");
        size = attributes.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testAddAll() {
        Attributes incoming = createAttributesWithKeyValuePair("key1", "value1");
        Attributes attributes = new Attributes();
        attributes.addAll(incoming);

        boolean hasKey1 = attributes.hasKey("key1");
        Assert.assertTrue(hasKey1);
        String value1 = attributes.get("key1");
        Assert.assertEquals("value1", value1);

        boolean hasKey2 = attributes.hasKey("key2");
        Assert.assertFalse(hasKey2);
    }

    @Test
    public void testDataset() {
        Attributes attributes = createAttributesWithDataAttributes("dataKey1", "dataValue1");
        Map<String, String> dataset = attributes.dataset();

        boolean hasDataKey1 = dataset.containsKey("dataKey1");
        Assert.assertTrue(hasDataKey1);
        String dataValue1 = dataset.get("dataKey1");
        Assert.assertEquals("dataValue1", dataValue1);
    }

    @Test
    public void testHtml() {
        Attributes attributes = createAttributesWithKeyValuePair("key", "value");
        String html = attributes.html();
        String expectedHtml = " key=\"value\"";
        Assert.assertEquals(expectedHtml, html);
    }

    @Test
    public void testToString() {
        Attributes attributes = createAttributesWithKeyValuePair("key", "value");
        String toString = attributes.toString();
        String expectedToString = "Attributes{attributes=" + attributes.attributes + "}";
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes3.put("key", "value");

        boolean equalsAttributes1ToAttributes1 = attributes1.equals(attributes1);
        Assert.assertTrue(equalsAttributes1ToAttributes1);

        boolean equalsAttributes1ToAttributes2 = attributes1.equals(attributes2);
        Assert.assertFalse(equalsAttributes1ToAttributes2);

        boolean equalsAttributes1ToAttributes3 = attributes1.equals(attributes3);
        Assert.assertFalse(equalsAttributes1ToAttributes3);

        boolean equalsAttributes3ToAttributes3 = attributes3.equals(attributes3);
        Assert.assertTrue(equalsAttributes3ToAttributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes3.put("key", "value");

        int hashCodeAttributes1 = attributes1.hashCode();
        int hashCodeAttributes2 = attributes2.hashCode();
        int hashCodeAttributes3 = attributes3.hashCode();
        int expectedHashCode = 1; // just an arbitrary non-zero value

        Assert.assertEquals(expectedHashCode, hashCodeAttributes1);
        Assert.assertEquals(expectedHashCode, hashCodeAttributes2);
        Assert.assertNotEquals(expectedHashCode, hashCodeAttributes3);
    }

    @Test
    public void testClone() {
        Attributes attributes = createAttributesWithKeyValuePair("key", "value");
        Attributes clonedAttributes = attributes.clone();

        boolean hasKey = attributes.hasKey("key");
        Assert.assertTrue(hasKey);
        boolean clonedHasKey = clonedAttributes.hasKey("key");
        Assert.assertTrue(clonedHasKey);
        String value = attributes.get("key");
        String clonedValue = clonedAttributes.get("key");
        Assert.assertEquals(value, clonedValue);
    }

    // Add test cases for edge cases and exception handling scenarios here

    private Attributes createAttributesWithKeyValuePair(String key, String value) {
        Attributes attributes = new Attributes();
        attributes.put(key, value);
        return attributes;
    }

    private Attributes createAttributesWithDataAttributes(String dataKey, String dataValue) {
        Attributes attributes = new Attributes();
        attributes.put(dataKey, dataValue);
        return attributes;
    }
}