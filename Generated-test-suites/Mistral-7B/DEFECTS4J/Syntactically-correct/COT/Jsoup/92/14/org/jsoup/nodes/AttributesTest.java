package org.junit.tests; // replace with your test package name

import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.internal.StringUtil.copyOf;

public class AttributesTest {
    

    // Test basic functionality for get() method
    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String actual = attributes.get("key1");
        String expected = "value1";
        Assert.assertEquals(expected, actual);
    }

    // Test basic functionality for getIgnoreCase() method
    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key2", "value2");
        String actual = attributes.getIgnoreCase("Key2");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    // Test basic functionality for put() method
    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key3", "value3");
        int size = attributes.size();
        Assert.assertEquals(1, size);
        String actual = attributes.get("key3");
        String expected = "value3";
        Assert.assertEquals(expected, actual);
    }

    // Test basic functionality for putIgnoreCase() method
    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY4", "value4");
        int size = attributes.size();
        Assert.assertEquals(1, size);
        String actual = attributes.get("KEY4");
        String expected = "value4";
        Assert.assertEquals(expected, actual);
    }

    // Test basic functionality for put() method with boolean value
    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key5", true);
        int size = attributes.size();
        Assert.assertEquals(1, size);
        boolean actual = attributes.getBoolean("key5");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    // Test basic functionality for put() method with Attribute object
    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        String key6 = "key6";
        String value6 = "value6";
        Attribute attribute6 = new Attribute(key6, value6, null);
        attributes.put(attribute6);
        int size = attributes.size();
        Assert.assertEquals(1, size);
        Attribute actual = attributes.get(key6);
        Assert.assertEquals(attribute6, actual);
    }

    // Test basic functionality for remove() method
    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key7", "value7");
        int sizeBeforeRemoval = attributes.size();
        attributes.remove("key7");
        int sizeAfterRemoval = attributes.size();
        Assert.assertEquals(sizeBeforeRemoval - 1, sizeAfterRemoval);
    }

    // Test basic functionality for hasKey() method
    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key8", "value8");
        boolean actual = attributes.hasKey("key8");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    // Test basic functionality for hasKeyIgnoreCase() method
    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key9", "value9");
        boolean actual = attributes.hasKeyIgnoreCase("Key9");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    // Test size() method
    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key10", "value10");
        int actual = attributes.size();
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }

    // Test addAll() method
    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key11", "value11");

        Attributes attributes2 = new Attributes();
        attributes2.put("key12", "value12");

        attributes1.addAll(attributes2);
        int size = attributes1.size();
        Assert.assertEquals(2, size);
    }

    // Test iterator() method
    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key13", "value13");
        Iterator<Attribute> iterator = attributes.iterator();
        Attribute attribute = iterator.next();
        String actual = attribute.getKey();
        String expected = "key13";
        Assert.assertEquals(expected, actual);
    }

    // Test asList() method
    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key14", "value14");
        List<Attribute> actual = attributes.asList();
        Attribute attribute = actual.get(0);
        String actualKey = attribute.getKey();
        String expectedKey = "key14";
        Assert.assertEquals(expectedKey, actualKey);
    }

    // Test dataset() method
    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey15", "dataValue15");
        Map<String, String> dataset = attributes.dataset();
        String actual = dataset.get("dataKey15");
        String expected = "dataValue15";
        Assert.assertEquals(expected, actual);
    }

    // Test html() method
    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("htmlKey16", "htmlValue16");
        StringBuilder expected = new StringBuilder();
        expected.append("<html key16=\"htmlValue16\" />");
        String actual = attributes.html();
        Assert.assertEquals(expected.toString(), actual);
    }

    // Test equals() method
    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key17", "value17");

        Attributes attributes2 = new Attributes();
        attributes2.put("key17", "value17");

        boolean actual = attributes1.equals(attributes2);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    // Test clone() method
    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key18", "value18");

        Attributes clonedAttributes = attributes.clone();
        int sizeBeforeCloning = attributes.size();
        int sizeAfterCloning = clonedAttributes.size();
        Assert.assertEquals(sizeBeforeCloning, sizeAfterCloning);
    }

    // Test normalize() method
    @Test
    public void testNormalizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("KEY19", "value19");

        attributes.normalize();
        String actual = attributes.get("key19");
        String expected = "key19";
        Assert.assertEquals(expected, actual);
    }

    // Test removeDuplicateAttributes() method (assuming it exists)
    @Test
    public void testRemoveDuplicateAttributesMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key20", "value20");
        attributes.put("key20", "value20");

        attributes.removeDuplicateAttributes(); // assuming this method exists and is case-sensitive
        int sizeBeforeRemoval = attributes.size();
        int sizeAfterRemoval = attributes.size();
        Assert.assertEquals(sizeBeforeRemoval - 1, sizeAfterRemoval);
    }

    // Test removeDuplicateAttributesIgnoreCase() method (assuming it exists)
    @Test
    public void testRemoveDuplicateAttributesIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("KEY21", "value21");
        attributes.put("Key21", "value21");

        attributes.removeDuplicateAttributesIgnoreCase(); // assuming this method exists
        int sizeBeforeRemoval = attributes.size();
        int sizeAfterRemoval = attributes.size();
        Assert.assertEquals(sizeBeforeRemoval - 1, sizeAfterRemoval);
    }

    
}