package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

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
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        Assert.assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String toString = attribute.toString();
        Assert.assertEquals("key=\"value\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        String key = attribute.getKey();
        String value = attribute.getValue();
        Assert.assertEquals("key", key);
        Assert.assertEquals("value", value);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        Assert.assertTrue(isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(outputSettings);
        Assert.assertFalse(shouldCollapseAttribute);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        Assert.assertFalse(isBooleanAttribute);
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

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        Assert.assertEquals(attribute, clone);
    }

}