package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        node = new Element("div");
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("id", "testId");
        assertEquals("testId", node.attr("id"));
    }

    @Test
    public void testAttributes() {
        assertNotNull(node.attributes());
    }

    @Test
    public void testHasAttr() {
        node.attr("class", "testClass");
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("class", "testClass");
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testChildNode() {
        Node childNode = new Element("span");
        node.addChildren(childNode);
        assertEquals(childNode, node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Node childNode1 = new Element("span");
        Node childNode2 = new Element("a");
        Node childNode3 = new Element("img");
        node.addChildren(childNode1, childNode2, childNode3);
        assertEquals(3, node.childNodes().size());
    }

    @Test
    public void testParent() {
        Node parent = new Element("body");
        parent.addChildren(node);
        assertEquals(parent, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document("");
        Node rootNode = document.createElement("html");
        document.appendChild(rootNode);
        rootNode.appendChild(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Element("body");
        parent.addChildren(node);
        node.remove();
        assertFalse(parent.childNodes().contains(node));
    }

    @Test
    public void testReplaceWith() {
        Node newNode = new Element("h1");
        Node parent = new Element("body");
        parent.addChildren(node);
        node.replaceWith(newNode);
        assertFalse(parent.childNodes().contains(node));
        assertTrue(parent.childNodes().contains(newNode));
    }

    @Test
    public void testSiblingNodes() {
        Node siblingNode1 = new Element("span");
        Node siblingNode2 = new Element("a");
        Node siblingNode3 = new Element("img");
        Node parent = new Element("body");
        parent.addChildren(siblingNode1, node, siblingNode2, siblingNode3);
        assertEquals(4, node.siblingNodes().size());
    }

    @Test
    public void testNextSibling() {
        Node siblingNode1 = new Element("span");
        Node siblingNode2 = new Element("a");
        Node siblingNode3 = new Element("img");
        Node parent = new Element("body");
        parent.addChildren(siblingNode1, node, siblingNode2, siblingNode3);
        assertEquals(siblingNode2, node.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node siblingNode1 = new Element("span");
        Node siblingNode2 = new Element("a");
        Node siblingNode3 = new Element("img");
        Node parent = new Element("body");
        parent.addChildren(siblingNode1, node, siblingNode2, siblingNode3);
        assertEquals(siblingNode1, node.previousSibling());
    }

}