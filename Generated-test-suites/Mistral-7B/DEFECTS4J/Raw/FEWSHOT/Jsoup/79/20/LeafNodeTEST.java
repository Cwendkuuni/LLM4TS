package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new Elements().html().get(0);
        assertFalse(leafNode.hasAttributes());

        Element element = new Element("div");
        element.attr("class", "test-class");
        assertTrue(element.hasAttributes());
    }

    @Test
    public void testGetAttributes() {
        Element element = new Element("div");
        element.attr("class", "test-class");
        element.attr("data-test", "test-value");

        Attributes attributes = element.attributes();
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("test-class", attributes.get("class"));
        assertEquals("test-value", attributes.get("data-test"));
        assertNull(attributes.get("non-existent-attribute"));
    }

    @Test
    public void testSetCoreValue() {
        Element element = new Element("div");
        assertEquals(EmptyString, element.attr(Element.TAG));

        element.coreValue("test-value");
        assertEquals("test-value", element.attr(Element.TAG));
    }

    @Test
    public void testSetCoreValueWithAttributes() {
        Element element = new Element("div");
        element.attr("class", "test-class");

        element.coreValue("test-value");
        assertEquals("test-value", element.attr(Element.TAG));
        assertEquals("test-class", element.attr("class"));
    }

    @Test
    public void testGetCoreValue() {
        Element element = new Element("div");
        element.coreValue("test-value");
        assertEquals("test-value", element.attr(Element.TAG));
    }

    @Test
    public void testSetCoreValueWithNull() {
        Element element = new Element("div");
        element.coreValue(null);
        assertNull(element.attr(Element.TAG));
    }

    @Test
    public void testGetAttr() {
        Element element = new Element("div");
        element.attr("class", "test-class");
        element.attr("data-test", "test-value");

        assertEquals("test-class", element.attr("class"));
        assertEquals("test-value", element.attr("data-test"));
        assertNull(element.attr("non-existent-attribute"));
    }

    @Test
    public void testSetAttr() {
        Element element = new Element("div");
        element.attr(new Attribute("class", "test-class"));
        element.attr(new Attribute("data-test", "test-value"));

        assertEquals("test-class", element.attr("class"));
        assertEquals("test-value", element.attr("data-test"));
    }

    @Test
    public void testHasAttr() {
        Element element = new Element("div");
        element.attr("class", "test-class");

        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = new Element("div");
        element.attr("class", "test-class");
        element.removeAttr("class");

        assertFalse(element.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        Element element = new Element("a", "href", "https://example.com");
        assertEquals("https://example.com", element.absUrl("href"));
    }

    @Test
    public void testBaseUri() {
        Document document = new Document();
        Element root = document.createElement("html");
        root.appendChild(document.createElement("head"));
        root.appendChild(document.createElement("body"));
        document.setRootElement(root);

        Element link = document.createElement("link", "href", "https://example.com");
        root.appendChild(link);

        Element element = new Element("a", "href", "/relative-path");
        element.parent = link;

        assertEquals("https://example.com/relative-path", element.absUrl("href"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        Element element = new Element("div");
        element.setBaseUri("http://example.com");
    }

    @Test
    public void testChildNodeSize() {
        Element element = new Element("div");
        assertEquals(0, element.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        Element element = new Element("div");
        element.ensureChildNodes();
    }
}