package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertSame(attrWithParent.getParent(), attrWithParent.getParent());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "value=\"foo\"");
        assertEquals("key", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());

        Attribute nonBooleanAttr = new Attribute("key", "value");
        assertFalse(nonBooleanAttr.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute dataAttr = new Attribute("data-key", "value");
        assertTrue(dataAttr.isDataAttribute());

        Attribute nonDataAttr = new Attribute("key", "value");
        assertFalse(nonDataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.shouldCollapseAttribute(outputSettings));

        Attribute nonBooleanAttr = new Attribute("key", "value");
        assertFalse(nonBooleanAttr.shouldCollapseAttribute(outputSettings));

        Attribute emptyBooleanAttr = new Attribute("checked", "");
        assertTrue(booleanAttr.shouldCollapseAttribute(outputSettings));

        Attribute sameBooleanAttr = new Attribute("checked", "checked");
        assertTrue(booleanAttr.shouldCollapseAttribute(outputSettings));

        Attribute nonEmptyBooleanAttr = new Attribute("checked", "foo");
        assertFalse(nonEmptyBooleanAttr.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }
}