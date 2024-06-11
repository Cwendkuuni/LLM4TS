package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertNotNull(attribute.parent);
    }

    @Test
    public void testGetKey() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null, null);

        Assert.assertEquals(key, attribute.getKey());
    }

    @Test
    public void testSetKey() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null, null);

        String newKey = "newTestKey";
        attribute.setKey(newKey);

        Assert.assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute(null, null, null);
        attribute.setKey("newKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyString() {
        Attribute attribute = new Attribute("", null, null);
        attribute.setKey("newKey");
    }

    @Test
    public void testGetValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String key = "testKey";
        String oldValue = "oldValue";
        String newValue = "newValue";
        Attribute attribute = new Attribute(key, oldValue, null);

        String returnedOldValue = attribute.setValue(newValue);

        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals(oldValue, returnedOldValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("testKey", null, null);
        attribute.setValue("newValue");
    }

    @Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expectedHtml = String.format("%s=\"%s\"", key, value);
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb).toString());
    }

    @Test
    public void testToString() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        Assert.assertEquals(attribute.html(), attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue%20%3D%22test%22";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        Assert.assertEquals(unencodedKey, attribute.getKey());
        Assert.assertEquals("test Value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null, null);

        Assert.assertFalse(attribute.isDataAttribute());

        key = "data-test";
        attribute = new Attribute(key, null, null);

        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        String key = "test";
        String value = "";
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute(key, value, null);

        Assert.assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithNonEmptyValue() throws IOException {
        String key = "test";
        String value = "test";
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute(key, value, null);

        Assert.assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttribute() throws IOException {
        String key = "checked";
        String value = "";
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute(key, value, null);

        Assert.assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeAndNonEmptyValue() throws IOException {
        String key = "checked";
        String value = "checked";
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute(key, value, null);

        Assert.assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        Attribute attribute1 = new Attribute(key1, value1, null);

        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute2 = new Attribute(key2, value2, null);

        Attribute attribute3 = new Attribute(key1, value1, null);

        Assert.assertTrue(attribute1.equals(attribute3));
        Assert.assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        Attribute attribute1 = new Attribute(key1, value1, null);

        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute2 = new Attribute(key2, value2, null);

        Attribute attribute3 = new Attribute(key1, value1, null);

        int hashCode1 = attribute1.hashCode();
        Assert.assertEquals(hashCode1, attribute3.hashCode());
        Assert.assertNotEquals(hashCode1, attribute2.hashCode());
    }

    @Test
    public void testClone() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(key, clonedAttribute.getKey());
        Assert.assertEquals(value, clonedAttribute.getValue());
    }
}