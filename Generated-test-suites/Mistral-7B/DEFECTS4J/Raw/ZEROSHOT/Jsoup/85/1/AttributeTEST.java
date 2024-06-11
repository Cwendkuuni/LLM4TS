package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

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
        Assert.assertNull(attributeWithParent.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        attributeWithParent.setKey("newKey");
        Assert.assertEquals("newKey", attributeWithParent.getKey());
        Assert.assertNotNull(attributeWithParent.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullParent() {
        new Attribute("key", "value", null);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("value", attributeWithParent.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("value", attribute.setValue("newValue"));
        Assert.assertEquals("newValue", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("value", attributeWithParent.setValue("newValue"));
        Assert.assertEquals("newValue", attributeWithParent.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        StringBuilder sb2 = StringUtil.borrowBuilder();
        attributeWithParent.html(sb2, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb2));
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key=\"value\"", attributeWithParent.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Assert.assertTrue(new Attribute("data-test", null).isDataAttribute());
        Assert.assertFalse(new Attribute("test", null).isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));

        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertFalse(new Attribute("key", null).isBooleanAttribute());
        Assert.assertTrue(new Attribute("checked", null).isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value1");
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals("key", clonedAttribute.getKey());
        Assert.assertEquals("value", clonedAttribute.getValue());
    }
}