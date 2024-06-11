package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Assert.assertEquals("testKey", attribute.getKey());
        Assert.assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue", parent);
        Assert.assertEquals("testKey", attribute.getKey());
        Assert.assertEquals("testValue", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newTestKey");
        Assert.assertEquals("newTestKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue("newTestValue");
        Assert.assertEquals("newTestValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("testKey", "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expectedHtml = "testkey=\"testValue\"";
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodWithException() throws IOException {
        Attribute attribute = new Attribute("testKey", "testValue");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings()); // IOException should be thrown here
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Assert.assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute("data-testKey", "testValue");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Assert.assertTrue(Attribute.shouldCollapseAttribute("input", null, out));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("input", "", out));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("input", "input", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("input", "other", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("someData-input", null, out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("someData-input", "", out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("someData-input", "other", out));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("testKey1", "testValue1");
        Attribute attribute2 = new Attribute("testKey2", "testValue2");
        Attribute attribute3 = new Attribute("testKey1", "testValue1");

        Assert.assertFalse(attribute1.equals(attribute2));
        Assert.assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
        Assert.assertNull(clonedAttribute.getParent());
    }
}