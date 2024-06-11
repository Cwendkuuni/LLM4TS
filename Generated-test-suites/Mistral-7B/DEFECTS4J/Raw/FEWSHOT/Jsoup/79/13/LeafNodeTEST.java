package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttributes());

        Element element = new Element("html");
        element.attr("id", "test");
        assertTrue(element.hasAttributes());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals("", leafNode.attr("test"));

        Element element = new Element("html");
        element.attr("id", "test");
        assertEquals("test", element.attr("id"));

        leafNode.coreValue("Test2");
        assertEquals("Test2", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr(leafNode.nodeName(), "Test2");
        assertEquals("Test2", leafNode.attr(leafNode.nodeName()));

        Element element = new Element("html");
        element.attr("id", "test");
        assertEquals("test", element.attr("id"));
        element.attr("class", "test2");
        assertEquals("test2", element.attr("class"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr("test"));

        Element element = new Element("html");
        element.attr("id", "test");
        assertTrue(element.hasAttr("id"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("Test with id");
        leafNode.attr("id", "test");
        assertEquals("test", leafNode.attr("id"));

        leafNode.removeAttr("id");
        assertEquals("Test with id", leafNode.attr(leafNode.nodeName()));

        Element element = new Element("html");
        element.attr("id", "test");
        assertTrue(element.hasAttr("id"));
        element.removeAttr("id");
        assertFalse(element.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals("", leafNode.absUrl("test"));

        Element element = new Element("html");
        element.attr("src", "test.html");
        assertEquals("test.html", element.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals("", leafNode.baseUri());

        Element element = new Element("html");
        element.parent(new Element("body"));
        element.parent().parent().attr("base", "/test");
        assertEquals("/test", element.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.ensureChildNodes();
    }
}