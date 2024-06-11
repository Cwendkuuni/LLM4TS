package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.junit.Test;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode node = new TextNode("Hello World");
        assertFalse(node.hasAttributes());

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        attrNode.attr("id", "test");
        assertTrue(attrNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new TextNode("Hello World");
        assertEquals("Hello World", node.coreValue());

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        assertEquals("Hello World", attrNode.coreValue());

        LeafNode attrMapNode = new Element("html");
        attrMapNode.value = new AttributesImpl();
        attrMapNode.value.put("id", "test");
        assertNull(attrMapNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode node = new TextNode("Hello World");
        node.coreValue("Hello New");
        assertEquals("Hello New", node.coreValue());

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        attrNode.coreValue("Hello New");
        assertEquals("Hello New", attrNode.coreValue());

        LeafNode attrMapNode = new Element("html");
        attrMapNode.value = new AttributesImpl();
        attrMapNode.value.put("id", "test");
        attrMapNode.coreValue("Hello New");
        assertEquals("Hello New", attrMapNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode node = new TextNode("Hello World");
        assertEquals("", node.attr("id"));

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        attrNode.attr("id", "test");
        assertEquals("test", attrNode.attr("id"));

        LeafNode attrMapNode = new Element("html");
        attrMapNode.value = new AttributesImpl();
        attrMapNode.value.put("id", "test");
        assertEquals("test", attrMapNode.attr("id"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode node = new TextNode("Hello World");
        node.attr("id", "test");
        assertEquals("test", node.attr("id"));

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        attrNode.attr("id", "test");
        assertEquals("test", attrNode.attr("id"));

        LeafNode attrMapNode = new Element("html");
        attrMapNode.value = new AttributesImpl();
        attrMapNode.value.put("id", "test");
        attrMapNode.attr("id", "newTest");
        assertEquals("newTest", attrMapNode.attr("id"));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new TextNode("Hello World");
        assertFalse(node.hasAttr("id"));

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        attrNode.attr("id", "test");
        assertTrue(attrNode.hasAttr("id"));

        LeafNode attrMapNode = new Element("html");
        attrMapNode.value = new AttributesImpl();
        attrMapNode.value.put("id", "test");
        assertTrue(attrMapNode.hasAttr("id"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new TextNode("Hello World");
        node.attr("id", "test");
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        attrNode.attr("id", "test");
        attrNode.removeAttr("id");
        assertFalse(attrNode.hasAttr("id"));

        LeafNode attrMapNode = new Element("html");
        attrMapNode.value = new AttributesImpl();
        attrMapNode.value.put("id", "test");
        attrMapNode.removeAttr("id");
        assertFalse(attrMapNode.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode node = new TextNode("Hello World");
        assertEquals("", node.absUrl("id"));

        LeafNode attrNode = new Element("html");
        attrNode.coreValue("Hello World");
        attrNode.attr("id", "test");
        assertEquals("", attrNode.absUrl("id"));

        LeafNode attrMapNode = new Element("html");
        attrMapNode.value = new AttributesImpl();
        attrMapNode.value.put("id", "test");
        assertEquals("", attrMapNode.absUrl("id"));
    }

    @Test
    public void testBaseUri() {
        LeafNode node = new TextNode("Hello World");
        assertEquals("", node.baseUri());

        LeafNode parentNode = new Element("body");
        TextNode childNode = new TextNode("Hello World");
        parentNode.add(childNode);
        assertEquals("", childNode.baseUri());

        Element grandParentNode = new Element("html");
        parentNode.add(grandParentNode);
        grandParentNode.add(parentNode);
        assertEquals("", childNode.baseUri());

        Element parentWithBaseUriNode = new Element("body");
        parentWithBaseUriNode.baseUri("/test.html");
        TextNode childNodeWithBaseUri = new TextNode("Hello World");
        parentWithBaseUriNode.add(childNodeWithBaseUri);
        assertEquals("/test.html", childNodeWithBaseUri.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode node = new TextNode("Hello World");
        node.setBaseUri("/test.html");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode node = new TextNode("Hello World");
        assertEquals(0, node.childNodeSize());
    }

    @Test
    public void testEnsureChildNodes() {
        LeafNode node = new TextNode("Hello World");
        assertThrows(UnsupportedOperationException.class, () -> node.ensureChildNodes());
    }
}