package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myclass");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("myclass", node1.attributes().get("class"));
        assertEquals(baseUri, node1.baseUri());
        assertEquals(Collections.emptyList(), node2.childNodes());
        assertNull(node2.attributes());
        assertTrue(node3.childNodes().isEmpty());
        assertNull(node3.attributes());
    }

    @Test
    public void testNodeName() {
        Element element = new Element("div");
        assertSame(element.nodeName(), "div");
    }

    @Test
    public void testAttr() {
        Element element = new Element("a");
        element.attr("href", "http://example.com");
        assertEquals("http://example.com", element.attr("href"));

        element.attr("class", "myclass");
        assertEquals("myclass", element.attr("class"));

        assertEquals("", element.attr("nonExistent"));
    }

    @Test
    public void testHasAttr() {
        Element element = new Element("a");
        element.attr("href", "http://example.com");
        assertTrue(element.hasAttr("href"));
        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr("nonExistent"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = new Element("a");
        element.attr("href", "http://example.com");
        element.removeAttr("href");
        assertFalse(element.hasAttr("href"));
    }

    @Test
    public void testAbsUrl() {
        Document doc = new Document();
        Element link = new Element("a").attr("href", "relative.html");

        assertEquals("", link.absUrl(""));
        assertEquals("relative.html", link.absUrl("href"));
        assertEquals("http://example.com/relative.html", link.absUrl("href", doc.baseUri()));
    }

    @Test
    public void testChildNodes() {
        Document doc = new Document();

        Element parent = new Element("div");
        parent.addChildren(new Element("p"), new Element("p"));

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(children.get(0).getClass(), Element.class);
        assertEquals(children.get(1).getClass(), Element.class);
    }

    @Test
    public void testSetParentNode() {
        Document doc = new Document();
        Element parent = new Element("div");
        Element child = new Element("p");

        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testReplaceChild() {
        Document doc = new Document();
        Element parent = new Element("div");
        Element oldChild = new Element("p");
        Element newChild = new Element("p");

        parent.addChild(oldChild);
        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
        assertEquals(newChild, oldChild.parent());
        assertEquals(oldChild, parent.childNode(1));
    }

    @Test
    public void testRemoveChild() {
        Document doc = new Document();
        Element parent = new Element("div");
        Element child = new Element("p");

        parent.addChild(child);
        parent.removeChild(child);
        assertEquals(0, parent.childNodes().size());
        assertEquals(null, child.parent());
    }

    @Test
    public void testAddChildren() {
        Document doc = new Document();
        Element parent = new Element("div");

        parent.addChildren(new Element("p"), new Element("p"), new Element("p"));

        List<Node> children = parent.childNodes();
        assertEquals(3, children.size());
        for (Node node : children) {
            assertEquals(Element.class, node.getClass());
        }
    }

    @Test
    public void testToString() {
        Element element = new Element("p");
        element.text("Hello World!");

        assertEquals("<p>Hello World!</p>", element.toString());
    }
}