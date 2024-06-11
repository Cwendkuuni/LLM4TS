package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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
        String expected = String.format("key=\"value\"");
        assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testShouldCollapseAttributeWhenHtmlAndValueIsEmptyOrSameAsKey() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(out));
        attr = new Attribute("key", "key");
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWhenHtmlButValueIsNotEmptyOrDifferentFromKey() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWhenNotHtml() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(new Attribute("data-key", null).isDataAttribute());
        assertFalse(new Attribute("key", null).isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        // Deprecated
        assertTrue(new Attribute("checked", null).isBooleanAttribute());
        assertFalse(new Attribute("key", null).isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeInHtmlSyntax() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttributeInXmlSyntax() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        Attribute attr = new Attribute("checked", null);
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();
        assertEquals(attr, clone);
    }
}