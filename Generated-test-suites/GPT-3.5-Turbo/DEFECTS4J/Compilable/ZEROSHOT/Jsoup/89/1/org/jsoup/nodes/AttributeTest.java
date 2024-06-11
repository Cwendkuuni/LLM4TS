package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        String expectedHtml = "href=\"index.html\"";
        Assert.assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("href", "index.html");
        String expectedToString = "href=\"index.html\"";
        Assert.assertEquals(expectedToString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "value";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        Assert.assertNotNull(attribute);
        Assert.assertEquals(unencodedKey, attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "");
        Assert.assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertEquals(attribute1, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int expectedHashCode = 961 + "value".hashCode();
        Assert.assertEquals(expectedHashCode, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute cloneAttribute = attribute.clone();
        Assert.assertEquals(attribute, cloneAttribute);
    }
}