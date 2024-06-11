package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
    }

    @Test
    public void testAttributeKeySet() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        assertEquals("newKey", attrWithParent.getKey());
    }

    @Test
    public void testAttributeValueSet() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setValue("newValue");
        assertEquals("newValue", attrWithParent.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute());

        Document.OutputSettings outputSettings = Document.OutputSettings.syntaxFromString("html5");
        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.shouldCollapseAttribute(outputSettings));

        Attribute emptyStringAttr = new Attribute("key", "");
        Attribute emptyStringValueAttr = new Attribute("key", "");
        assertTrue(emptyStringAttr.shouldCollapseAttribute(outputSettings, emptyStringValueAttr.getValue()));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "value=%20foo");
        assertEquals("key", attr.getKey());
        assertEquals("foo", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());

        Attribute nonDataAttr = new Attribute("key", "value");
        assertFalse(nonDataAttr.isDataAttribute());
    }
}