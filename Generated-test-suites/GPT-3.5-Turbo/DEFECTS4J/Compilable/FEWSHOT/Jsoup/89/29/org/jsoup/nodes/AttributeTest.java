package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String expectedKey = "key";
        String actualKey = attribute.getKey();
        Assert.assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        String newKey = "newKey";
        attribute.setKey(newKey);
        String expectedKey = "newKey";
        String actualKey = attribute.getKey();
        Assert.assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String expectedValue = "value";
        String actualValue = attribute.getValue();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        String expectedValue = "newValue";
        String actualValue = attribute.getValue();
        Assert.assertEquals(expectedValue, actualValue);
        Assert.assertEquals("value", oldValue);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String expectedHtml = "key=\"value\"";
        String actualHtml = attribute.html();
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expectedString = "key=\"value\"";
        String actualString = attribute.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "value";
        Attribute expectedAttribute = new Attribute("key", "value", null);
        Attribute actualAttribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        Assert.assertEquals(expectedAttribute, actualAttribute);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean expectedIsDataAttribute = true;
        boolean actualIsDataAttribute = attribute.isDataAttribute();
        Assert.assertEquals(expectedIsDataAttribute, actualIsDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expectedShouldCollapseAttribute = false;
        boolean actualShouldCollapseAttribute = attribute.shouldCollapseAttribute(outputSettings);
        Assert.assertEquals(expectedShouldCollapseAttribute, actualShouldCollapseAttribute);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        boolean expectedEquals = true;
        boolean actualEquals = attribute1.equals(attribute2);
        Assert.assertEquals(expectedEquals, actualEquals);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int expectedHashCode = 3127835;
        int actualHashCode = attribute.hashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute, clonedAttribute);
    }
}