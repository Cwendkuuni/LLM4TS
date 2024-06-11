package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals("value", oldValue);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        Assert.assertEquals("key=\"value\"", html);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        throw new IOException();
        attribute.html(sb, new Document("").outputSettings());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Assert.assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", "otherKey", out));
    }

    @Test
    public void testIsDataAttribute() {
        Assert.assertTrue(new Attribute("data-key", null).isDataAttribute());
        Assert.assertFalse(new Attribute("key", null).isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        for (String booleanAttribute : Attribute.booleanAttributes) {
            Attribute attribute = new Attribute(booleanAttribute, null);
            Assert.assertTrue(attribute.isBooleanAttribute());
            attribute = new Attribute("key", null);
            Assert.assertFalse(attribute.isBooleanAttribute());
        }
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertTrue(attribute1.equals(attribute1));
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertNotSame(attribute, clonedAttribute);
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}