package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.EntityUtil;
import org.jsoup.nodes.PutAttribute;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.jsoup.select.ElementsSelector;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeTest {

    @org.junit.Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        
        Attribute attrParent = new Attributes();
        attr = new Attribute("key", "value", attrParent);
        assertEquals(attrParent, attr.getParent());
        assertEquals("key", attrParent.getKeyByValue(attr.getKey()));
    }

    @org.junit.Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @org.junit.Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals("value", oldValue);
    }

    @org.junit.Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);

        attr.html(sb, new Document("").outputSettings().syntax(Document.OutputSettings.Syntax.xml));
        html = StringUtil.releaseBuilder(sb);
        assertEquals("<key>value</key>", html);
    }

    @org.junit.Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "<script>alert('Hello')</script>");
        assertEquals("key", attr.getKey());
        assertEquals("script\u003Ealert('Hello')\u003C/script\u003E", attr.getValue());
    }

    @org.junit.Test(expected = SerializationException.class)
    public void testCreateFromEncodedWithIOException() throws IOException {
        Attribute.createFromEncoded("key", "");
    }

    @org.junit.Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("not:data:key", "value");
        assertFalse(attr.isDataAttribute());
    }

    @org.junit.Test
    public void testShouldCollapseAttribute() throws IOException {
        Document doc = new Document("");
        Attribute attr1 = new Attribute("key1", "");
        Attribute attr2 = new Attribute("key2", "value");
        Attribute attr3 = new Attribute("key3", "");
        Attribute attr4 = new Attribute("boolean:key", "");

        attr1.html(doc.appendBody().appendElement("div").html(), doc.outputSettings());
        attr2.html(doc.appendBody().appendElement("div").html(), doc.outputSettings());
        attr3.html(doc.appendBody().appendElement("div").html(), doc.outputSettings());
        attr4.html(doc.appendBody().appendElement("div").html(), doc.outputSettings());

        assertTrue(attr1.shouldCollapseAttribute(doc.outputSettings()));
        assertFalse(attr2.shouldCollapseAttribute(doc.outputSettings()));
        assertFalse(attr3.shouldCollapseAttribute(doc.outputSettings()));
        assertTrue(attr4.shouldCollapseAttribute(doc.outputSettings()));
    }

    @org.junit.Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("unsupported:key", null);
        assertFalse(attr.isBooleanAttribute());
    }

    @org.junit.Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertTrue(attr1.equals(attr3));
        assertFalse(attr1.equals(attr2));
    }

    @org.junit.Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNotSame(attr.getParent(), clonedAttr.getParent());
    }

    @org.junit.Test
    public void testMapEntryMethods() {
        Attribute attr = new Attribute("key", "value");
        Map.Entry<String, String> entry = attr;

        assertEquals("key", entry.getKey());
        assertEquals("value", entry.getValue());

        assertEquals(attr, entry.getKey());
        assertEquals(attr, entry.getValue());

        String key = "newKey";
        attr.setKey(key);
        assertEquals(key, entry.getKey());

        String value = "newValue";
        attr.setValue(value);
        assertEquals(value, entry.getValue());

        PutAttribute putAttribute = new PutAttribute("newKey", "newValue", (Attributes) null);
        entry = putAttribute;

        assertEquals("newKey", entry.getKey());
        assertEquals("newValue", entry.getValue());

        assertEquals(putAttribute, entry.getKey());
        assertEquals(putAttribute, entry.getValue());
    }

    @org.junit.Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @org.junit.Test
    public void testIsDataAttributeWithPrefix() {
        Attribute attr = new Attribute("data:key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @org.junit.Test
    public void testShouldCollapseAttributeWithEmptyValueAndBooleanAttribute() {
        Document doc = new Document("");
        Attribute attr = new Attribute("boolean:key", "");

        attr.html(doc.appendBody().appendElement("div").html(), doc.outputSettings());

        assertTrue(attr.shouldCollapseAttribute(doc.outputSettings()));
    }
}