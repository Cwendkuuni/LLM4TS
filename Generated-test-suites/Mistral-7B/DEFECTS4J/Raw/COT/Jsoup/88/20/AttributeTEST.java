package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Put;
import org.jsoup.nodes.Attributes.OutputSettings;
import org.jsoup.nodes.Elements.AttributeSupplier;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());

        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNotNull(attributeWithParent.getParent());
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
    public void testSetKeyEmpty() {
        Attribute attribute = new Attribute("key", "value");

        attribute.setKey("");

        assertEquals("", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");

        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");

        String oldValue = attribute.setValue("newValue");

        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
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
        attribute.html(sb, new OutputSettings());

        assertEquals("key=\"value\"", sb.toString());
        
        StringUtil.releaseBuilder(sb);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new OutputSettings());
        throw new IOException();
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
        OutputSettings out = new OutputSettings();

        assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "value", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "value", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "value", out));
        assertTrue(Attribute.shouldCollapseAttribute("boolean", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("boolean", "", out));
        assertTrue(Attribute.shouldCollapseAttribute("boolean", "boolean", out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", "false", out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", "true", out));

        out.syntax(OutputSettings.Syntax.xml);

        assertFalse(Attribute.shouldCollapseAttribute("key", null, out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "key", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", null, out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", "boolean", out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", "false", out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", "true", out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");

        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("boolean", null);

        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("boolean", "true");

        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("boolean", "false");

        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");

        Attribute clone = attribute.clone();

        assertEquals(attribute, clone);
    }
}