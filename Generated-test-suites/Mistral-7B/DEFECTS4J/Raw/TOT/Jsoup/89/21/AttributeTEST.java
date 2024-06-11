package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

/**
 * Test class for Attribute class.
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());

        Attribute attrParent = new Attributes();
        Attribute attrParentAdded = attrParent.put(attr);
        Assert.assertSame(attr, attrParentAdded);
        Assert.assertEquals(1, attrParent.size());
    }

    @Test
    public void testAttributeKeySetting() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        Assert.assertEquals("newKey", attr.getKey());

        Attribute attrParent = new Attributes();
        Attribute attrParentAdded = attrParent.put(attr);
        Assert.assertSame(attr, attrParentAdded);
        Assert.assertEquals("newKey", attrParent.keyAt(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullValue() {
        new Attribute("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyValue() {
        new Attribute("key", "");
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).trim());
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());
    }

    @Test
    public void testAttributeIsBooleanAttribute() {
        Attribute attr = new Attribute("booleanAttribute", "");
        Assert.assertTrue(attr.isBooleanAttribute());

        Attribute attr2 = new Attribute("notBooleanAttribute", "value");
        Assert.assertFalse(attr2.isBooleanAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Attribute attr = new Attribute("booleanAttribute", "");
        Assert.assertTrue(attr.shouldCollapseAttribute());

        Attribute attr2 = new Attribute("key", "value");
        Assert.assertFalse(attr2.shouldCollapseAttribute());
    }

    @Test
    public void testAttributeEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        Assert.assertFalse(attr1.equals(attr2));
        Assert.assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testAttributeHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        Assert.assertNotSame(attr1.hashCode(), attr2.hashCode());
        Assert.assertEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testAttributeClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        Assert.assertEquals(attr.getKey(), clonedAttr.getKey());
        Assert.assertEquals(attr.getValue(), clonedAttr.getValue());
    }
}