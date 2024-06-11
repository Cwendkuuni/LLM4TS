package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

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
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);

        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeConstructorWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeConstructorWithNullValue() {
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
        Attribute newAttribute = attribute.setValue("newValue");

        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("value", attribute.getOldValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testGetHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        Assert.assertEquals("key=\"value\"", sb.toString());
    }

    @Test(expected = IOException.class)
    public void testGetHtmlWithException() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        attribute.html(null, new Document("").outputSettings());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");

        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
        Assert.assertFalse(Attribute.isBooleanAttribute("class"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Assert.assertTrue(Attribute.shouldCollapseAttribute("checked", null, out));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("class", "class", out));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");

        Attribute attribute3 = new Attribute("key2", "value2");
        Attribute attribute4 = new Attribute("key1", "value1");

        Attribute attribute5 = attribute1.clone();

        Assert.assertTrue(attribute1.equals(attribute1));
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
        Assert.assertTrue(attribute1.equals(attribute5));
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