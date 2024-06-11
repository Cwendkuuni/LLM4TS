package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("key=\"value\"", attr.html());
        assertEquals("key=\"value\"", attr.toString());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertEquals("key=\"value\"", attrWithParent.html());
        assertEquals("key=\"value\"", attrWithParent.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("newKey=\"value\"", attr.html());
        assertEquals("newKey=\"value\"", attr.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyInSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("key", attr.getKey());
        assertEquals("newValue", attr.getValue());
        assertEquals("key=\"newValue\"", attr.html());
        assertEquals("key=\"newValue\"", attr.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueInSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("key=\"value\"", attr.html());
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-test", "test");
        assertTrue(attr.isDataAttribute());
        Attribute attrWithoutPrefix = new Attribute("test", "test");
        assertFalse(attrWithoutPrefix.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Attribute attr = new Attribute("checked", "");
        assertTrue(attr.shouldCollapseAttribute(null));
        Document doc = new Document();
        assertTrue(attr.shouldCollapseAttribute(doc.outputSettings()));

        attr = new Attribute("hidden", "");
        assertTrue(attr.shouldCollapseAttribute(null));
        assertTrue(attr.shouldCollapseAttribute(doc.outputSettings()));

        attr = new Attribute("disabled", "");
        assertTrue(attr.shouldCollapseAttribute(null));
        assertTrue(attr.shouldCollapseAttribute(doc.outputSettings()));

        attr = new Attribute("required", "");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(doc.outputSettings()));

        attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(doc.outputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("checked", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("disabled", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("required", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("multiple", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("readonly", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("selected", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("hidden", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("async", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("autofocus", "");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("checked", "checked");
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("disabled", "");
        assertFalse(attr.isBooleanAttribute());
        attr = new Attribute("multiple", "multiple");
        assertFalse(attr.isBooleanAttribute());
        attr = new Attribute("selected", "selected");
        assertFalse(attr.isBooleanAttribute());
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
        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }

    @Test
    public void testCreateAttributeFromElement() throws IOException {
        Element el = new Element("div");
        el.attr("key", "value");
        Attribute attr = Element.asAttribute(el.tagName(), el.attr(0).getKey(), el.attr(0).getValue(), null);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("key=\"value\"", attr.html());
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testCreateAttributesFromElement() throws IOException {
        Element el = new Element("div");
        el.setAttribute("key1", "value1");
        el.setAttribute("key2", "value2");
        Attributes attrs = Element.asAttributes(el);
        assertEquals(2, attrs.size());
        Iterator<Map.Entry<String, Object>> it = attrs.iterator();
        Map.Entry<String, Object> entry1 = it.next();
        Map.Entry<String, Object> entry2 = it.next();
        assertEquals("key1", entry1.getKey());
        assertEquals("value1", entry1.getValue());
        assertEquals("key2", entry2.getKey());
        assertEquals("value2", entry2.getValue());

        for (Map.Entry<String, Object> entry : attrs) {
            Attribute attr = (Attribute) entry;
            assertEquals(attr.getKey(), entry.getKey());
            assertEquals(attr.getValue(), entry.getValue());
        }
    }

    @Test
    public void testCreateAttributesFromElementWithEmptyValues() throws IOException {
        Element el = new Element("div");
        el.setAttribute("key1", "");
        el.setAttribute("key2", "");
        Attributes attrs = Element.asAttributes(el);
        assertEquals(2, attrs.size());
        Iterator<Map.Entry<String, Object>> it = attrs.iterator();
        Map.Entry<String, Object> entry1 = it.next();
        Map.Entry<String, Object> entry2 = it.next();
        assertEquals("key1", entry1.getKey());
        assertEquals("", entry1.getValue());
        assertEquals("key2", entry2.getKey());
        assertEquals("", entry2.getValue());

        for (Map.Entry<String, Object> entry : attrs) {
            Attribute attr = (Attribute) entry;
            assertEquals(attr.getKey(), entry.getKey());
            assertEquals(attr.getValue(), entry.getValue());
        }
    }

    @Test
    public void testCreateAttributesFromElementWithBooleanValues() throws IOException {
        Element el = new Element("input");
        el.setAttribute("checked", "");
        el.setAttribute("disabled", "");
        Attributes attrs = Element.asAttributes(el);
        assertEquals(2, attrs.size());
        Iterator<Map.Entry<String, Object>> it = attrs.iterator();
        Map.Entry<String, Object> entry1 = it.next();
        Map.Entry<String, Object> entry2 = it.next();
        assertEquals("checked", entry1.getKey());
        assertEquals(true, entry1.getValue());
        assertEquals("disabled", entry2.getKey());
        assertEquals(false, entry2.getValue());

        for (Map.Entry<String, Object> entry : attrs) {
            Attribute attr = (Attribute) entry;
            assertEquals(attr.getKey(), entry.getKey());
            assertEquals(attr.getValue(), entry.getValue());
        }
    }

    @Test
    public void testCreateAttributesFromElementWithEmptyKey() throws IOException {
        Element el = new Element("input");
        el.setAttribute("checked", "");
        el.setAttribute("", "value");
        Attributes attrs = Element.asAttributes(el);
        assertEquals(2, attrs.size());
        Iterator<Map.Entry<String, Object>> it = attrs.iterator();
        Map.Entry<String, Object> entry1 = it.next();
        Map.Entry<String, Object> entry2 = it.next();
        assertEquals("checked", entry1.getKey());
        assertEquals(true, entry1.getValue());
        assertEquals("", entry2.getKey());
        assertEquals("value", entry2.getValue());

        for (Map.Entry<String, Object> entry : attrs) {
            Attribute attr = (Attribute) entry;
            assertEquals(attr.getKey(), entry.getKey());
            assertEquals(attr.getValue(), entry.getValue());
        }
    }

    @Test
    public void testCreateAttributesFromElementWithAttributes() throws IOException {
        Element el = new Element("div");
        el.attr("key1", "value1");
        el.attr("key2", "value2");
        el.attr("class", "class1 class2 class3");
        Attributes attrs = Element.asAttributes(el);
        assertEquals(4, attrs.size());
        Iterator<Map.Entry<String, Object>> it = attrs.iterator();
        Map.Entry<String, Object> entry1 = it.next();
        Map.Entry<String, Object> entry2 = it.next();
        Map.Entry<String, Object> entry3 = it.next();
        Map.Entry<String, Object> entry4 = it.next();
        assertEquals("key1", entry1.getKey());
        assertEquals("value1", entry1.getValue());
        assertEquals("key2", entry2.getKey());
        assertEquals("value2", entry2.getValue());
        assertEquals("class", entry3.getKey());
        assertEquals(Elements.class, entry3.getValue().getClass());
        assertEquals("class1", ((Elements) entry3.getValue()).get(0).tagName());
        assertEquals("class2", ((Elements) entry3.getValue()).get(1).tagName());
        assertEquals("class3", ((Elements) entry3.getValue()).get(2).tagName());

        for (Map.Entry<String, Object> entry : attrs) {
            Attribute attr = (Attribute) entry;
            assertEquals(attr.getKey(), entry.getKey());
            assertEquals(attr.getValue(), entry.getValue());
        }
    }

    @Test
    public void testCreateAttributesFromElementWithAttributesAndTextNode() throws IOException {
        Element el = new Element("div");
        el.attr("key1", "value1");
        el.attr("key2", "value2");
        el.text("This is text");
        Attributes attrs = Element.asAttributes(el);
        assertEquals(4, attrs.size());
        Iterator<Map.Entry<String, Object>> it = attrs.iterator();
        Map.Entry<String, Object> entry1 = it.next();
        Map.Entry<String, Object> entry2 = it.next();
        Map.Entry<String, Object> entry3 = it.next();
        Map.Entry<String, Object> entry4 = it.next();
        assertEquals("key1", entry1.getKey());
        assertEquals("value1", entry1.getValue());
        assertEquals("key2", entry2.getKey());
        assertEquals("value2", entry2.getValue());
        assertEquals("class", entry3.getKey());
        assertEquals(Elements.class, entry3.getValue().getClass());
        assertEquals("key", ((TextNode) entry4.getValue()).text(), "key");
        assertEquals("This is text", ((TextNode) entry4.getValue()).text());

        for (Map.Entry<String, Object> entry : attrs) {
            Attribute attr = (Attribute) entry;
            assertEquals(attr.getKey(), entry.getKey());
            assertEquals(attr.getValue(), entry.getValue());
        }
    }
}