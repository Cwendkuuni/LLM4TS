package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testNewInstance() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testNewInstanceWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(parent, attr.getParent());
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
        String newValue = "newValue";
        assertEquals("value", attr.getValue());
        attr.setValue(newValue);
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithEmptyValue() throws IOException {
        Attribute attr = new Attribute("key", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithEmptyKey() throws IOException {
        Attribute attr = new Attribute("", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("\" value=\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "%6B%65%78%65%72%6F%6F%65%6C%65%6D%65%6E%63%6F%64%65%64");
        assertEquals("key", attr.getKey());
        assertEquals("boolean", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-foo"));
        assertFalse(Attribute.isDataAttribute("foo"));
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);

        Attribute attr = new Attribute("foo", "");
        assertFalse(attr.shouldCollapseAttribute(settings));

        attr.setKey("foo");
        assertFalse(attr.shouldCollapseAttribute(settings));

        attr.setKey("data-foo");
        assertTrue(attr.shouldCollapseAttribute(settings));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        int hashcode1 = attr1.hashCode();
        int hashcode2 = attr2.hashCode();
        int hashcode3 = attr3.hashCode();
        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }

    @Test
    public void testIsBooleanAttributeDeprecated() {
        assertFalse(Attribute.class.isInstance(new Attribute("boolean", "")).isBooleanAttribute());
        assertTrue(new Attribute("checked", "").isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testBooleanAttributes() {
        String[] booleanAttributes = Attribute.booleanAttributes;
        String[] expectedBooleanAttributes = {"allowfullscreen", "async", "autofocus", "checked", "compact", "declare",
                "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple",
                "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required",
                "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch"};
        assertArrayEquals(expectedBooleanAttributes, booleanAttributes);
    }
}