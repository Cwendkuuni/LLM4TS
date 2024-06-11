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
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeKeySetterWithNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeValueSetterWithNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(expected));

        StringBuilder expectedCollapsed = StringUtil.borrowBuilder();
        if (Attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings())) {
            attribute.html(expectedCollapsed, new Document("").outputSettings());
            Assert.assertEquals("key", StringUtil.releaseBuilder(expectedCollapsed));
        }
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
        Assert.assertFalse(Attribute.isBooleanAttribute("key"));
        Assert.assertFalse(Attribute.isBooleanAttribute(null));
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings settings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", null);
        Assert.assertTrue(Attribute.shouldCollapseAttribute(settings));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", "value", settings));
    }

    @Test
    public void testIsDataAttribute() {
        Assert.assertTrue(Attribute.isDataAttribute("data-key"));
        Assert.assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertTrue(attribute1.equals(attribute1));
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
        Assert.assertFalse(attribute1.equals(null));
        Assert.assertFalse(attribute1.equals("value"));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertEquals(attribute1.hashCode(), attribute1.hashCode());
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), 1L);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}