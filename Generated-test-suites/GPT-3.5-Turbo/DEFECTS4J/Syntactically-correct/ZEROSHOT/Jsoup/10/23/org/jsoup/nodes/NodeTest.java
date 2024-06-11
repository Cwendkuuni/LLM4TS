package org.jsoup.nodes;

import org.jsoup.select.NodeVisitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    private Node node;
    private Node parent;

    @Before
    public void setUp() {
        List<Node> childNodes = new ArrayList<Node>();
        childNodes.add(new Element("p"));
        childNodes.add(new TextNode("Hello World"));
        Attributes attributes = new Attributes();
        attributes.put("class", "container");
        node = new Element("div", "http://example.com", attributes);
        node.addChildren(childNodes.toArray(new Node[0]));
        parent = new Element("body");
        parent.appendChild(node);
    }

    @Test
    public void testAttr() {
        assertFalse(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertEquals("container", node.attr("class"));
        assertEquals("", node.attr("id"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        assertEquals("container", attributes.get("class"));
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() throws MalformedURLException {
        String absUrl = node.absUrl("href");
        assertEquals("http://example.com", absUrl);
    }

    @Test
    public void testChildNode() {
        Node childNode = node.childNode(0);
        assertNotNull(childNode);
        assertEquals("p", childNode.nodeName());
    }

    @Test
    public void testChildNodes() {
        List<Node> childNodes = node.childNodes();
        assertNotNull(childNodes);
        assertEquals(2, childNodes.size());
    }

    @Test
    public void testParent() {
        Node parentNode = node.parent();
        assertNotNull(parentNode);
        assertEquals("body", parentNode.nodeName());
    }

    @Test
    public void testOwnerDocument() {
        Document document = node.ownerDocument();
        assertNotNull(document);
    }

    @Test
    public void testRemove() {
        node.remove();
        assertNull(node.parent());
    }

    @Test
    public void testReplaceWith() {
        Node newNode = new Element("span");
        node.replaceWith(newNode);
        assertNull(node.parent());
        assertEquals(newNode, parent.childNodes().get(0));
    }

    @Test
    public void testSiblingNodes() {
        List<Node> siblingNodes = node.siblingNodes();
        assertNotNull(siblingNodes);
        assertEquals(1, siblingNodes.size());
    }

    @Test
    public void testNextSibling() {
        Node nextSibling = node.nextSibling();
        assertNull(nextSibling);
    }

    @Test
    public void testPreviousSibling() {
        Node previousSibling = node.previousSibling();
        assertNull(previousSibling);
    }

    @Test
    public void testSiblingIndex() {
        Integer siblingIndex = node.siblingIndex();
        assertNotNull(siblingIndex);
        assertEquals(0, siblingIndex.intValue());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = node.outerHtml();
        assertNotNull(outerHtml);
        assertFalse(outerHtml.isEmpty());
    }

    @Test
    public void testClone() {
        Node cloneNode = node.clone();
        assertNotNull(cloneNode);
        assertNotSame(node, cloneNode);
    }
}