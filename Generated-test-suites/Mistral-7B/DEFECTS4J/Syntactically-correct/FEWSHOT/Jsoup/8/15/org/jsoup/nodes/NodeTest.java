package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Element node = new Element("div");
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUriWithEmptyString() {
        Element node = new Element("div");
        node.setBaseUri("");
        assertNull(node.baseUri());
    }

    @Test
    public void testSetBaseUriWithNull() {
        Element node = new Element("div");
        node.setBaseUri(null);
        assertNull(node.baseUri());
    }

    @Test
    public void testAbsUrlWithEmptyAttribute() {
        Element node = new Element("a");
        assertEquals("", node.absUrl(""));
    }

    @Test
    public void testAbsUrlWithAbsoluteAttribute() {
        Element node = new Element("a");
        node.attr("href", "http://example.com");
        assertEquals("http://example.com", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithRelativeAttribute() {
        Element node = new Element("a");
        node.attr("href", "test.html");
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        assertEquals("http://example.com/test.html", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithInvalidUrlAttribute() {
        Element node = new Element("a");
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        assertEquals("", node.absUrl(""));
    }

    @Test
    public void testAttr() {
        Element node = new Element("div");
        node.attr("class", "foo");
        assertEquals("foo", node.attr("class"));
        assertEquals("", node.attr("non-existent-attribute"));
    }

    @Test
    public void testAttrWithEmptyString() {
        Element node = new Element("div");
        node.attr("class", "");
        assertEquals("", node.attr("class"));
    }

    @Test
    public void testAttrWithNull() {
        Element node = new Element("div");
        node.attr("class", null);
        assertNull(node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        Element node = new Element("div");
        node.attr("class", "foo");
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        Element node = new Element("div");
        node.attr("class", "foo");
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAddChild() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChild(child);
        Node node = parent.childNodes().get(0);
        assertEquals(child, node);
    }

    @Test
    public void testAddChildren() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.addChildren(child1, child2);
        List<Node> nodes = parent.childNodes();
        assertEquals(2, nodes.size());
        assertEquals(child1, nodes.get(0));
        assertEquals(child2, nodes.get(1));
    }

    @Test
    public void testReplaceChild() {
        Element parent = new Element("div");
        Element oldChild = new Element("p");
        Element newChild = new Element("p");
        parent.addChild(oldChild);
        parent.replaceChild(oldChild, newChild);
        List<Node> nodes = parent.childNodes();
        assertEquals(1, nodes.size());
        assertEquals(newChild, nodes.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChild(child);
        parent.removeChild(child);
        List<Node> nodes = parent.childNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testSetParentNode() {
        Element child = new Element("p");
        Element parent = new Element("div");
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testSetSiblingIndex() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element sibling2 = new Element("p");
        Element test = new Element("p");
        parent.addChildren(sibling1, sibling2);
        parent.addChild(test);
        test.setSiblingIndex(1);
        assertEquals(sibling2, test.previousSibling());
        assertEquals(sibling1, test.nextSibling());
    }

    @Test
    public void testToString() {
        Element node = new Element("div");
        assertEquals("<div></div>", node.toString());
    }
}