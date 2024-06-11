package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for Attribute class
 */
public class AttributeTest {

    @Test
    public void testAttributeConstructorWithKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeConstructorWithKeyValueAndParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();

        Attribute attribute = new Attribute(key, value, parent);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertNotNull(attribute.parent);
    }

    @Test
    public void testSetKeyMethod() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        String newKey = "newTestKey";
        attribute.setKey(newKey);

        Assert.assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        String value = "testValue";
        Attribute attribute = new Attribute(null, value);
        attribute.setKey("newTestKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        String value = "testValue";
        Attribute attribute = new Attribute("", value);
        attribute.setKey("newTestKey");
    }

    @Test
    public void testSetValueMethod() {
        String key = "testKey";
        String oldValue = "oldTestValue";
        String newValue = "newTestValue";
        Attribute attribute = new Attribute(key, oldValue);

        attribute.setValue(newValue);

        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals(oldValue, attribute.getOldValue()); // assuming Attribute has an oldValue field
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithNullValue() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);
        attribute.setValue("newTestValue");
    }

    @Test
    public void testHtmlMethod() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Appendable appendable = StringUtil.borrowBuilder();

        Attribute attribute = new Attribute(key, value);
        attribute.html(appendable, new Document("").outputSettings());

        String expectedHtml = key + "=\"testValue\"";
        String actualHtml = StringUtil.releaseBuilder(appendable).toString();

        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Appendable appendable = StringUtil.borrowBuilder();

        Attribute attribute = new Attribute(key, value);
        attribute.html(appendable, new Document("").outputSettings()); // IOException should be thrown here
    }

    @Test
    public void testIsDataAttributeMethod() {
        String key = "testDataKey";
        Attribute attribute = new Attribute(key, null);

        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithNonDataKey() {
        String key = "testNonDataKey";
        Attribute attribute = new Attribute(key, null);

        Assert.assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        String key1 = "key1";
        String value1 = "";
        Attribute attribute1 = new Attribute(key1, value1);

        Assert.assertTrue(attribute1.shouldCollapseAttribute(outputSettings));

        String key2 = "key2";
        String value2 = "value2";
        Attribute attribute2 = new Attribute(key2, value2);

        Assert.assertFalse(attribute2.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        String key1 = "booleanAttribute";
        Attribute attribute1 = new Attribute(key1, null);

        Assert.assertTrue(attribute1.isBooleanAttribute());

        String key2 = "nonBooleanAttribute";
        Attribute attribute2 = new Attribute(key2, null);

        Assert.assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEqualsMethod() {
        String key1 = "key1";
        String value1 = "value1";
        Attribute attribute1 = new Attribute(key1, value1);

        String key2 = "key2";
        String value2 = "value2";
        Attribute attribute2 = new Attribute(key2, value2);

        Attribute attribute3 = attribute1;

        Attribute attribute4 = new Attribute(key1, value1);
        Attribute attribute5 = new Attribute(key1, value2);
        Attribute attribute6 = new Attribute(key2, value1);

        Assert.assertTrue(attribute1.equals(attribute1));
        Assert.assertTrue(attribute1.equals(attribute3));
        Assert.assertFalse(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute4));
        Assert.assertFalse(attribute1.equals(attribute5));
        Assert.assertFalse(attribute1.equals(attribute6));
    }

    @Test
    public void testCloneMethod() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(key, clonedAttribute.getKey());
        Assert.assertEquals(value, clonedAttribute.getValue());
    }

    @Test
    public void testMapEntryMethods() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Map.Entry<String, String> entry = new HashMap<String, String>(){{
            put(key, value);
        }}.entrySet().iterator().next();

        Assert.assertEquals(entry.getKey(), attribute.getKey());
        Assert.assertEquals(entry.getValue(), attribute.getValue());

        String newKey = "newTestKey";
        String newValue = "newTestValue";

        attribute.setKey(newKey);
        attribute.setValue(newValue);

        entry = new HashMap<String, String>(){{
            put(newKey, newValue);
        }}.entrySet().iterator().next();

        Assert.assertEquals(entry.getKey(), attribute.getKey());
        Assert.assertEquals(entry.getValue(), attribute.getValue());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlEncodedException() throws IOException {
        String key = "testKey";
        Appendable appendable = StringUtil.borrowBuilder();
        Attribute attribute = new Attribute(key, null);
        attribute.html(appendable, new Document("").outputSettings());
    }
}