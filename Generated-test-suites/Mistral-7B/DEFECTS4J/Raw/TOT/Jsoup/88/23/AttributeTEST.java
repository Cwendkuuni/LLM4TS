package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());

        Attribute booleanAttr = new Attribute("checked", null);
        Assert.assertTrue(Attribute.isBooleanAttribute(booleanAttr.getKey()));

        Attribute invalidAttr = new Attribute(null, "value");
        Assert.assertNull(invalidAttr);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        Assert.assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attr.setValue(newValue);
        Assert.assertEquals(newValue, attr.getValue());
        Assert.assertEquals("value", oldValue);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));

        Attribute booleanAttr = new Attribute("checked", null);
        attr = booleanAttr.clone();
        attr.setValue("true");
        sb.setLength(0);
        attr.html(sb, new Document("").outputSettings());
        expectedHtml = "checked";
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute booleanAttr = new Attribute("checked", "");
        Assert.assertTrue(booleanAttr.shouldCollapseAttribute(out));

        booleanAttr.setValue("true");
        Assert.assertFalse(booleanAttr.shouldCollapseAttribute(out));

        Attribute invalidAttr = new Attribute("invalid", null);
        Assert.assertFalse(invalidAttr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
        Assert.assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        Assert.assertNotSame(attr1, attr2);
        Assert.assertNotSame(attr1, attr3);
        Assert.assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        int hashCode1 = attr1.hashCode();
        Assert.assertEquals(hashCode1, attr3.hashCode());
        Assert.assertNotEquals(hashCode1, attr2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        Assert.assertEquals(attr, clonedAttr);
    }
}