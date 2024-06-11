package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes.NotFound;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAndGetAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testCreateAndGetAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(1, parent.size());
        assertEquals("key", parent.keyAt(0));
        assertEquals("value", parent.valueAt(0));
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
        String newValue = "newValue";
        String oldValue = attr.setValue(newValue);
        assertEquals(newValue, attr.getValue());
        assertEquals("value", oldValue);
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
    public void testShouldCollapseAttribute() {
        boolean isCollapsible = Attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings());
        assertTrue(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("key", "", new Document.OutputSettings());
        assertTrue(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings());
        assertTrue(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings());
        assertFalse(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("key", "value1", new Document.OutputSettings());
        assertFalse(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("key", "value2", new Document.OutputSettings());
        assertFalse(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("booleanAttribute", null, new Document.OutputSettings());
        assertTrue(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("booleanAttribute", "", new Document.OutputSettings());
        assertTrue(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("booleanAttribute", "booleanAttribute", new Document.OutputSettings());
        assertTrue(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("booleanAttribute", "true", new Document.OutputSettings());
        assertFalse(isCollapsible);

        isCollapsible = Attribute.shouldCollapseAttribute("booleanAttribute", "false", new Document.OutputSettings());
        assertFalse(isCollapsible);
    }

    @Test
    public void testIsBooleanAttribute() {
        boolean isBoolean = Attribute.isBooleanAttribute("booleanAttribute");
        assertTrue(isBoolean);

        isBoolean = Attribute.isBooleanAttribute("nonBooleanAttribute");
        assertFalse(isBoolean);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("unencodedKey", "encodedValue=\"key=value&anotherKey=anotherValue\"");
        assertEquals("unencodedKey", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertNotEquals(attr1, attr3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();
        assertEquals(attr.getKey(), clone.getKey());
        assertEquals(attr.getValue(), clone.getValue());
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        String html = attr.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testCreateFromEncodedWithParent() {
        Attributes parent = new Attributes();
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue=\"key=value&anotherKey=anotherValue\"";
        Attribute attr = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(1, parent.size());
        assertEquals(unencodedKey, parent.keyAt(0));
        assertEquals("value", parent.valueAt(0));
    }

    @Test
    public void testCreateAndAddAttributeToElement() throws IOException {
        Document doc = new Document();
        Element elem = doc.createElement("div");
        String key = "class";
        String value = "css";
        Attribute attr = new Attribute(key, value);
        elem.attributes().put(attr);
        assertEquals(1, elem.attributes().size());
        assertEquals(key, elem.attributeNodes().item(0).getKey());
        assertEquals(value, elem.attributeNodes().item(0).getValue());
        assertEquals("class=\"css\"", elem.outerHtml());
    }

    @Test
    public void testCreateAndAddAttributeToTextNode() throws IOException {
        Document doc = new Document();
        Element elem = doc.createElement("div");
        TextNode textNode = doc.createTextNode("Test Text");
        elem.appendChild(textNode);
        String key = "data-test";
        String value = "pass";
        Attribute attr = new Attribute(key, value);
        textNode.attributes().put(attr);
        assertEquals(1, textNode.attributes().size());
        assertEquals(key, textNode.attributeNodes().item(0).getKey());
        assertEquals(value, textNode.attributeNodes().item(0).getValue());
        assertEquals("Test Text", textNode.data());
        assertEquals("Test Text", textNode.outerHtml());
    }
}