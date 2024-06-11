package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Attributes;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructorWithKeyValueAndParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertSame(parentAttributes, attribute.parent);
    }

    @Test
    public void testSetKeyMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValueMethod() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertEquals("value", attribute.parent.get(attribute.getKey()));
        attribute.setValue(newValue);
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("newValue", attribute.parent.get(attribute.getKey()));
    }

    @Test(expected = AssertionError.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        Attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "key", "value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeStaticMethod() {
        Assert.assertFalse(Attribute.isDataAttribute("key"));
        Assert.assertTrue(Attribute.isDataAttribute(Attributes.dataPrefix + "key"));
    }

    @Test
    public void testShouldCollapseAttributeMethod() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        Attribute booleanAttribute = new Attribute("booleanAttribute");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));

        Attribute emptyBooleanAttribute = new Attribute("booleanAttribute");
        Assert.assertTrue(emptyBooleanAttribute.shouldCollapseAttribute(outputSettings));

        Document.OutputSettings htmlSyntaxOutputSettings = new Document.OutputSettings(Document.OutputSettings.Syntax.html);
        Attribute caseInsensitiveBooleanAttribute = new Attribute("BooleanAttribute");
        Assert.assertTrue(Attribute.shouldCollapseAttribute(caseInsensitiveBooleanAttribute.getKey(), null, htmlSyntaxOutputSettings));
    }

    @Test(expected = AssertionError.class)
    public void testIsBooleanAttributeDeprecatedMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.isBooleanAttribute();
    }
}