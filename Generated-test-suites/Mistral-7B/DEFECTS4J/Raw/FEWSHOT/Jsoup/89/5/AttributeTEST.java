package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testConstructorWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb);
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithOutputSettings() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document.OutputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "Attribute{key='key', val='value'}";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        attribute.setKey("key");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        attribute.setValue("value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        attribute.setKey("checked");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeHtml() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }
}