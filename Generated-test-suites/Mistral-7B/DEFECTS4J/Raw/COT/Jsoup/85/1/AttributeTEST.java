package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreationWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullKeyThrowsException() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSettingEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
        Assert.assertEquals("", attribute.getKey());
    }

    @Test
    public void testSettingSameKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("key");
        Assert.assertEquals("key", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullValueThrowsException() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testSettingEmptyValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("");
        Assert.assertEquals("", attribute.getValue());
    }

    @Test
    public void testSettingSameValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("value");
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(expected);
        Assert.assertEquals("key=\"value\"", result);
    }

    @Test
    public void testShouldCollapseAttributeMethod() {
        Document.OutputSettings out = new Document.OutputSettings();
        Assert.assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("notABooleanAttribute", null, out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("notABooleanAttribute", "", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("notABooleanAttribute", "value", out));
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Assert.assertTrue(Attribute.isBooleanAttribute("allowfullscreen"));
        Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
        Assert.assertTrue(Attribute.isBooleanAttribute("async"));
        Assert.assertFalse(Attribute.isBooleanAttribute("class"));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Assert.assertFalse(attribute1.equals(attribute2));
        Assert.assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Assert.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}