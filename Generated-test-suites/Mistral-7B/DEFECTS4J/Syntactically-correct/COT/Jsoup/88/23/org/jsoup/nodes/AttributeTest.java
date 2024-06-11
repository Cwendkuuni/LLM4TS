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
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.parent);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyDuringCreation() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueDuringCreation() {
        new Attribute("key", null);
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyWhenSetting() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        Attribute oldValue = attribute.setValue(newValue);
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("value", oldValue.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueWhenSetting() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test(expected = SerializationException.class)
    public void testIOExceptionDuringHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringWriter writer = new StringWriter();
        attribute.html(writer, new Document("").outputSettings());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());

        Attribute notDataAttribute = new Attribute("key", "value");
        Assert.assertFalse(notDataAttribute.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.isBooleanAttribute());

        Attribute notBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(notBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeEmptyValue() {
        Attribute booleanAttribute = new Attribute("checked", "");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        Attribute notBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(notBooleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeSameValue() {
        Attribute booleanAttribute = new Attribute("checked", "checked");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        Attribute notBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(notBooleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
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
    public void testClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}