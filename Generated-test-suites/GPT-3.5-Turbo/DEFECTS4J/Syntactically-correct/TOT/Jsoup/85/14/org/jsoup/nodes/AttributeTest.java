package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String key = attribute.getKey();
        Assert.assertEquals("key", key);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        String newKey = attribute.getKey();
        Assert.assertEquals("newKey", newKey);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String value = attribute.getValue();
        Assert.assertEquals("value", value);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        String newValue = attribute.getValue();
        Assert.assertEquals("newValue", newValue);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        String html = attribute.html();
        Assert.assertEquals("href=\"index.html\"", html);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        String value = attribute.getValue();
        Assert.assertEquals("encodedValue", value);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        Assert.assertTrue(isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(out);
        Assert.assertFalse(shouldCollapse);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "true");
        boolean isBoolean = attribute.isBooleanAttribute();
        Assert.assertTrue(isBoolean);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        boolean equals = attribute1.equals(attribute2);
        Assert.assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        Assert.assertNotNull(hashCode);
    }
}