package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testCreateAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(parent, attr.getParent());
        parent.put(attr);
        Attribute retrieved = parent.get("key");
        assertSame(attr, retrieved);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
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

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeData() {
        Attribute attr = new Attribute("data-key", "data-value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr1 = new Attribute("key1", "");
        Attribute attr2 = new Attribute("key2", "value");
        Attribute attr3 = new Attribute("key3", "key3");

        assertTrue(attr1.shouldCollapseAttribute(out));
        assertFalse(attr2.shouldCollapseAttribute(out));
        assertFalse(attr3.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeHtml() {
        Document.OutputSettings out = new Document.OutputSettings(Document.OutputSettings.Syntax.html);
        Attribute attr1 = new Attribute("key1", "");
        Attribute attr2 = new Attribute("key2", "");
        Attribute attr3 = new Attribute("key3", "key3");
        Attribute attr4 = new Attribute("checked", "");
        Attribute attr5 = new Attribute("checked", "checked");

        assertTrue(attr1.shouldCollapseAttribute(out));
        assertTrue(attr4.shouldCollapseAttribute(out));
        assertFalse(attr2.shouldCollapseAttribute(out));
        assertFalse(attr3.shouldCollapseAttribute(out));
        assertFalse(attr5.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("checked", null);
        Attribute attr3 = new Attribute("formnovalidate", "value3");

        assertFalse(attr1.isBooleanAttribute());
        assertTrue(attr2.isBooleanAttribute());
        assertTrue(attr3.isBooleanAttribute());
    }
}