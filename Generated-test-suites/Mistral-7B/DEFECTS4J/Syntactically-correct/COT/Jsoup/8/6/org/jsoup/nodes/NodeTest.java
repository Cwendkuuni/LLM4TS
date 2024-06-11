package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Node.java
 */
public class NodeTest {

    private Node root;
    private Node parent;
    private Node child1;
    private Node child2;
    private Node child3;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "root");
        root = new Element(baseUri, "html", attributes);
        parent = new Element(baseUri, "body", attributes);
        root.appendChild(parent);
        attributes = new Attributes();
        attributes.put("class", "child1");
        child1 = new Element(baseUri, "div", attributes);
        parent.appendChild(child1);
        attributes.put("class", "child2");
        child2 = new Element(baseUri, "div", attributes);
        parent.appendChild(child2);
        attributes.put("class", "child3");
        child3 = new Element(baseUri, "div", attributes);
        parent.appendChild(child3);
        attributes = new Attributes();
        child1.attributes().put("id", "child1");
        TextNode textNode = new TextNode("Text content");
        child1.appendChild(textNode);
    }

    @Test
    public void testNodeName() {
        assertEquals("html", root.nodeName());
        assertEquals("body", parent.nodeName());
        assertEquals("div", child1.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("root", root.attr("id"));
        assertEquals("child1", child1.attr("id"));
        assertNull(child1.attr("nonExistingAttr"));
        assertEquals("example.com", root.absUrl(""));
        assertEquals("http://example.com/child1", child1.absUrl(""));
        assertEquals("http://example.com/child1#text-content", child1.absUrl("id"));
        assertEquals("text-content", child1.childNodes().get(0).nodeName());
    }

    @Test
    public void testAttributes() {
        assertEquals("class=child1 id=child1", child1.outerHtml());
        assertNull(root.attributes());
        Attributes attributes = root.attributes();
        attributes.put("test", "value");
        assertEquals("test=value", root.outerHtml());
    }

    @Test
    public void testHasAttr() {
        assertTrue(child1.hasAttr("id"));
        assertTrue(child1.hasAttr("class"));
        assertFalse(child1.hasAttr("nonExistingAttr"));
    }

    @Test
    public void testRemoveAttr() {
        assertSame(child1, child1.removeAttr("id"));
        assertFalse(child1.hasAttr("id"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://example.com", root.baseUri());
        root.setBaseUri("http://another-example.com");
        assertEquals("http://another-example.com", root.baseUri());
    }

    @Test
    public void testChildNode() {
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
        assertEquals(child3, parent.childNode(2));
        assertNull(parent.childNode(3));
    }

    @Test
    public void testChildNodes() {
        List<Node> children = Arrays.asList(child1, child2, child3);
        assertEquals(children, parent.childNodes());
        assertEquals(3, parent.childNodes().size());
    }

    @Test
    public void testChildNodesAsArray() {
        Node[] children = parent.childNodesAsArray();
        assertArrayEquals(new Node[] { child1, child2, child3 }, children);
    }

    @Test
    public void testParent() {
        assertEquals(root, child1.parent());
        assertNull(textNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertEquals(root, child1.ownerDocument());
        assertNull(textNode.ownerDocument());
    }

    @Test
    public void testRemove() {
        List<Node> childrenBeforeRemoval = new ArrayList<Node>(parent.childNodes());
        parent.remove();
        assertEquals(0, root.childNodes().size());
        assertEquals(0, root.childNodesAsArray().length);
        assertNull(parent);
    }

    @Test
    public void testReplaceWith() {
        Node nodeToReplace = new Element(baseUri, "span", attributes());
        parent.replaceWith(nodeToReplace);
        assertEquals(nodeToReplace, parent);
        assertEquals(nodeToReplace, root.childNodes().get(0));
    }

    @Test
    public void testAddChild() {
        Node newNode = new Element(baseUri, "div", attributes());
        parent.addChildren(newNode);
        assertEquals(newNode, parent.getLastChild());
        assertEquals(newNode, parent.childNodes().get(3));
        assertEquals(4, parent.childNodes().size());
    }

    @Test
    public void testSetParentNode() {
        Node newNode = new Element(baseUri, "div", attributes());
        parent.setParentNode(newNode);
        assertEquals(newNode, parent.parentNode);
        assertEquals(newNode, newNode.childNodes().get(0));
    }

    @Test
    public void testReplaceChild() {
        Node nodeToReplace = child1;
        Node newNode = new Element(baseUri, "span", attributes());
        parent.replaceChild(nodeToReplace, newNode);
        assertEquals(newNode, parent.getChildNodes().get(0));
        assertEquals(newNode, nodeToReplace.getParentNode());
        assertEquals(0, nodeToReplace.getSiblingIndex());
        assertEquals(1, newNode.getSiblingIndex());
    }

    @Test
    public void testRemoveChild() {
        Node nodeToRemove = child1;
        parent.removeChild(nodeToRemove);
        assertEquals(child2, parent.getChildNodes().get(0));
        assertEquals(child2, root.childNodes().get(0));
        assertNull(nodeToRemove.getParentNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNull() {
        parent.addChildren((Node[]) null);
    }

    @Test
    public void testAddChildrenDuplicate() {
        Node nodeToAdd = new Element(baseUri, "div", attributes());
        parent.addChildren(nodeToAdd, nodeToAdd);
        assertEquals(2, parent.childNodes().size());
    }

    @Test
    public void testSiblingNodes() {
        List<Node> siblings = parent.siblingNodes();
        assertEquals(Collections.emptyList(), siblings);
    }

    @Test
    public void testNextSibling() {
        Node nextSibling = child2;
        assertEquals(child2, child1.nextSibling());
        child2.remove();
        assertEquals(child3, child1.nextSibling());
        child1.remove();
        assertNull(child2.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node previousSibling = child2;
        assertEquals(child1, child2.previousSibling());
        child2.remove();
        assertEquals(null, child1.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        assertEquals(0, child1.siblingIndex());
        assertEquals(1, child2.siblingIndex());
        assertEquals(2, child3.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        child2.setSiblingIndex(3);
        assertEquals(3, child2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("<html><head></head><body><div class='child1' id='child1'>Text content</div></body></html>", root.outerHtml());
    }

    @Test
    public void testOuterHtmlWithEmptyAttributes() {
        Attributes emptyAttributes = new Attributes();
        Node emptyNode = new Element(baseUri, "div", emptyAttributes);
        assertEquals("<div></div>", emptyNode.outerHtml());
    }

    @Test
    public void testOuterHtmlWithCustomOutputSettings() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(false);
        StringBuilder sb = new StringBuilder();
        root.outerHtml(sb, 0, settings);
        assertEquals("<html><body><div class=\"child1\" id=\"child1\">Text content</div></body></html>", sb.toString());
    }
}