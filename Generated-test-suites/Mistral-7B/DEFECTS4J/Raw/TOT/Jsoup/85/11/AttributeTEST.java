package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeKeySetterNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeValueSetterNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testAttributeToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
    }

    @Test
    public void testAttributeHtmlWithEmptyValue() throws IOException {
        Attribute attr = new Attribute("key", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key", StringUtil.releaseBuilder(sb).toString());
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data-key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));

        Attribute attrBoolean = new Attribute("checked", "");
        assertTrue(attrBoolean.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testAttributeIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());
    }

    @Test
    public void testAttributeEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testAttributeClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals("key", clonedAttr.getKey());
        assertEquals("value", clonedAttr.getValue());
        assertSame(attr.getParent(), clonedAttr.getParent());
    }
}