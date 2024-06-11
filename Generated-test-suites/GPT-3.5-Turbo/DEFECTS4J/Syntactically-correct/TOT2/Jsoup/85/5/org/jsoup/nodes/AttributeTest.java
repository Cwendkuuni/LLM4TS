package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
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
        assertEquals("value", attr.setValue("newValue"));
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attr = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attr.html());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("href", "&gt;example.html");
        assertEquals("href", attr.getKey());
        assertEquals(">example.html", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-name", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("empty", "");
        assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("required", null);
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertTrue(attr1.equals(attr2));
    }

    @Test
    public void testHashCode() {
        Attribute attr = new Attribute("key", "value");
        assertEquals(attr.getKey().hashCode() + attr.getValue().hashCode(), attr.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }

}