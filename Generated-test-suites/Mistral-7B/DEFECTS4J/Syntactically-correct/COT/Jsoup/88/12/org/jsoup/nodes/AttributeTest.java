package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

/**
 * Test class for Attribute class.
 */
public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        new Attribute("key", null);
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
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
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
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
    }

    @Test(expected = IOException.class)
    public void testHtmlWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", null);
        Assert.assertTrue(attribute.isDataAttribute());
        Attribute nonDataAttribute = new Attribute("key", null);
        Assert.assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        attribute.setValue("value");
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithHtmlSyntax() {
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        Attribute attribute = new Attribute("key", "value");
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        attribute.setValue("newValue");
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
        booleanAttribute.setValue("");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
        booleanAttribute.setValue("checked");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithNonBooleanAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        Assert.assertTrue(attribute.isBooleanAttribute());
        Attribute nonBooleanAttribute = new Attribute("key", null);
        Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithBooleanValue() {
        Attribute attribute = new Attribute("checked", "checked");
        Assert.assertTrue(attribute.isBooleanAttribute());
        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
        Assert.assertFalse(attribute1.equals(null));
        Assert.assertFalse(attribute1.equals(new Object()));
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
        Assert.assertEquals(attribute, clonedAttribute);
        Assert.assertNotSame(attribute, clonedAttribute);
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}