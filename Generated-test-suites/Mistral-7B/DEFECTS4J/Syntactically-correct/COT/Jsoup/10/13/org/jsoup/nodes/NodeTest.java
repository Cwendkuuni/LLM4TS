package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {
    private Node node;
    private String baseUri = "http://example.com";
    private Attributes attributes;
    
    @Before
    public void setUp() {
        node = new Element("div");
        node.setBaseUri(baseUri);
        attributes = new Attributes();
        node.setAttributes(attributes);
        node.setAttribute("test", "value");
    }

    

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("value", node.attr("test"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testAttributes() {
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("test"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("https://example.com");
        assertEquals("https://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", node.absUrl("nonexistent"));
        URL expected = new URL("http://example.com/relative");
        node.setAttribute("abs:href", "relative");
        assertEquals(expected.toExternalForm(), node.absUrl("href"));
        expected = new URL("http://example.com/relative.html");
        node.setAttribute("href", "relative.html");
        assertEquals(expected.toExternalForm(), node.absUrl("abs:href"));
    }

    @Test
    public void testChildNode() {
        Node child = new Element("p");
        node.appendChild(child);
        assertEquals(child, node.childNode(0));
    }

    @Test
    public void testChildren() {
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        node.appendChild(child1);
        node.appendChild(child2);
        List<Node> expected = Arrays.asList(child1, child2);
        assertEquals(expected, node.childNodes());
    }

    @Test
    public void testParent() {
        Node parent = new Element("body");
        parent.appendChild(node);
        assertEquals(parent, node.parent());
        assertNull(node.parent()); // default constructor
    }

    @Test
    public void testOwnerDocument() {
        Node parent = new Element("body");
        parent.appendChild(node);
        assertEquals(parent, node.ownerDocument());
        assertNull(node.ownerDocument()); // default constructor
    }

    @Test
    public void testRemove() {
        Node parent = new Element("body");
        parent.appendChild(node);
        node.remove();
        assertFalse(parent.hasChildNodes());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Element("body");
        Node child1 = new Element("p");
        parent.appendChild(child1);
        Node child2 = new Element("p");
        parent.replaceChild(child1, child2);
        assertFalse(parent.hasChildNodes(child1));
        assertTrue(parent.hasChildNodes(child2));
    }

    @Test
    public void testSetParentNode() {
        Node parent1 = new Element("body");
        Node parent2 = new Element("body");
        node.setParentNode(parent1);
        parent1.removeChild(node);
        node.setParentNode(parent2);
        assertEquals(parent2, node.parentNode);
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Element("body");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.appendChild(child1);
        parent.replaceChild(child1, child2);
        assertFalse(parent.hasChildNodes(child1));
        assertTrue(parent.hasChildNodes(child2));
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Element("body");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.removeChild(child1);
        assertFalse(parent.hasChildNodes(child1));
        assertTrue(parent.hasChildNodes(child2));
    }

    @Test
    public void testAddChildren() {
        Node parent = new Element("body");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(child1, child2);
        List<Node> expected = Arrays.asList(child1, child2);
        assertEquals(expected, parent.childNodes());
    }

    @Test
    public void testAddChildrenAtIndex() {
        Node parent = new Element("body");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(1, child1, child2);
        List<Node> expected = new ArrayList<Node>(Collections.nCopies(2, child1));
        expected.set(0, child2);
        assertEquals(expected, parent.childNodes());
    }

    @Test
    public void testReparentChild() {
        Node parent1 = new Element("body");
        Node child = new Element("p");
        parent1.appendChild(child);
        Node parent2 = new Element("body");
        node.reparentChild(child);
        parent2.appendChild(child);
        assertEquals(parent2, child.parentNode);
    }

    @Test
    public void testReindexChildren() {
        Node parent = new Element("body");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        Node child3 = new Element("p");
        parent.addChildren(child1, child2, child3);
        node.reindexChildren();
        List<Node> expected = Arrays.asList(child1, child2, child3);
        assertEquals(expected, parent.childNodes());
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new Element("body");
        Node sibling1 = new Element("p");
        Node node = new Element("p");
        Node sibling2 = new Element("p");
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        List<Node> expected = Arrays.asList(sibling1, node, sibling2);
        assertEquals(expected, node.siblingNodes());
    }

    @Test
    public void testNextSibling() {
        Node parent = new Element("body");
        Node sibling1 = new Element("p");
        Node node = new Element("p");
        Node sibling2 = new Element("p");
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        assertEquals(sibling2, node.nextSibling());
        assertNull(node.nextSibling(Collections.singletonList(parent)));
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new Element("body");
        Node sibling1 = new Element("p");
        Node node = new Element("p");
        Node sibling2 = new Element("p");
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        assertEquals(sibling1, node.previousSibling());
        assertNull(node.previousSibling(Collections.singletonList(parent)));
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Element("body");
        Node sibling1 = new Element("p");
        Node node = new Element("p");
        Node sibling2 = new Element("p");
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        assertEquals(1, node.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        Node parent = new Element("body");
        Node sibling1 = new Element("p");
        Node node = new Element("p");
        Node sibling2 = new Element("p");
        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);
        node.setSiblingIndex(0);
        List<Node> expected = Arrays.asList(node, sibling1, sibling2);
        assertEquals(expected, parent.childNodes());
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        String expected = "\n<div></div>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testClone() {
        Node clone = node.clone();
        assertEquals(node.nodeName(), clone.nodeName());
        assertFalse(node.equals(clone));
        assertEquals(node.toString(), clone.toString());
    }

    
}