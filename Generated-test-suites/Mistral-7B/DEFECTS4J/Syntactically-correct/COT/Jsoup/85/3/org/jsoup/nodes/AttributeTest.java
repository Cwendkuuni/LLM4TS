package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attributeWithParent.getKey());
        Assert.assertEquals("value", attributeWithParent.getValue());
        Assert.assertNull(attributeWithParent.getParent());
    }

    @Test
    public void testKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeySetterWithNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeySetterWithEmpty() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueSetterWithNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(expected);
        Assert.assertEquals("<key=\"value\">", result);
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        throw new IOException();
        attribute.html(expected, new Document("").outputSettings());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", null);
        Assert.assertTrue(attribute.isDataAttribute());
        Attribute attributeWithoutPrefix = new Attribute("key", null);
        Assert.assertFalse(attributeWithoutPrefix.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings settings = new Document.OutputSettings();
        Attribute booleanAttribute = new Attribute("checked", null);
        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(settings));
        Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(settings));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
        Assert.assertFalse(attribute1.equals(null));
        Assert.assertFalse(attribute1.equals(""));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
        Assert.assertNull(clonedAttribute.getParent());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        Attribute nonBooleanAttribute = new Attribute("key", null);
        Assert.assertTrue(booleanAttribute.isBooleanAttribute());
        Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }
}