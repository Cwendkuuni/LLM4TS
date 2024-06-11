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

    Node node;
    Attributes attributes;
    String baseUri = "http://www.example.com";

    @Before
    public void setup() {
        attributes = new Attributes();
        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertNull(node.nodeName());
    }

    @Test(expected = AbstractMethodError.class)
    public void testAbstractMethodNodeName() {
        new Node() {}.nodeName();
    }

    @Test
    public void testAttr() {
        node.attr("key", "value");
        assertEquals("value", node.attr("key"));
        assertFalse(node.hasAttr("otherKey"));
        assertEquals("", node.attr("otherKey"));
        assertNull(node.attr("abs:href"));
        node.attr("abs:href", "http://www.example.com/test");
        assertEquals("http://www.example.com/test", node.absUrl("href"));
    }

    @Test
    public void testAttributes() {
        node.attr("key1", "value1");
        node.attr("key2", "value2");
        assertSame(attributes, node.attributes());
        assertEquals("value1", node.attributes().get("key1"));
        assertEquals("value2", node.attributes().get("key2"));
        assertFalse(node.attributes().hasKey("otherKey"));
        assertEquals("", node.attributes().get("otherKey"));
    }

    @Test
    public void testHasAttr() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("otherKey"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("key", "value");
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://www.example.org");
        assertEquals("http://www.example.org", node.baseUri());
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedUri() {
        node.absUrl("invalid:uri");
    }

    @Test
    public void testAbsUrlRelativeUrl() {
        node.attr("href", "relative.html");
        assertEquals("http://www.example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlAbsoluteUrl() {
        node.attr("href", "http://www.example.com/absolute.html");
        assertEquals("http://www.example.com/absolute.html", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node child = new Node(baseUri);
        node.addChild(child);
        Node retrievedChild = node.childNode(0);
        assertSame(child, retrievedChild);
    }

    @Test
    public void testChildren() {
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        node.addChildren(child1, child2);
        List<Node> children = node.childNodes();
        List<Node> expectedChildren = new ArrayList<>(Arrays.asList(child1, child2));
        assertEquals(expectedChildren, children);
    }

    @Test
    public void testChildNodesAsArray() {
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        node.addChildren(child1, child2);
        Node[] childrenAsArray = node.childNodesAsArray();
        Node[] expectedChildren = {child1, child2};
        assertArrayEquals(expectedChildren, childrenAsArray);
    }

    @Test
    public void testParent() {
        Node parent = new Node(baseUri);
        parent.addChild(node);
        assertSame(parent, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertNull(node.ownerDocument());
        Node parent = new Node(baseUri);
        parent.addChild(node);
        Document document = new Document();
        parent.setOwnerDocument(document);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Node(baseUri);
        parent.addChild(node);
        node.remove();
        List<Node> children = parent.childNodes();
        assertFalse(children.contains(node));
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Node(baseUri);
        parent.addChild(node1 = new Node(baseUri));
        Node replaceNode = new Node(baseUri);
        node.replaceWith(replaceNode);
        List<Node> children = parent.childNodes();
        assertFalse(children.contains(node1));
        assertTrue(children.contains(replaceNode));
    }

    @Test
    public void testSetParentNode() {
        Node parent1 = new Node(baseUri);
        Node parent2 = new Node(baseUri);
        node.setParentNode(parent1);
        node.setParentNode(parent2);
        List<Node> children1 = parent1.childNodes();
        List<Node> children2 = parent2.childNodes();
        assertFalse(children1.contains(node));
        assertTrue(children2.contains(node));
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Node(baseUri);
        Node out = new Node(baseUri);
        Node in = new Node(baseUri);
        parent.addChild(out);
        parent.replaceChild(out, in);
        List<Node> children = parent.childNodes();
        assertFalse(children.contains(out));
        assertTrue(children.contains(in));
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Node(baseUri);
        Node out = new Node(baseUri);
        parent.addChild(out);
        parent.removeChild(out);
        List<Node> children = parent.childNodes();
        assertFalse(children.contains(out));
    }

    @Test
    public void testAddChildren() {
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        Node[] expectedChildren = {child1, child2};
        assertEquals(children.size(), 2);
        assertEquals(expectedChildren, parent.childNodes().toArray());
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        parent.addChildren(1, child1, child2);
        List<Node> children = parent.childNodes();
        Node[] expectedChildren = {child2, child1};
        assertEquals(children.size(), 2);
        assertEquals(expectedChildren, parent.childNodes().toArray());
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Node(baseUri);
        Node sibling1 = new Node(baseUri);
        Node node = new Node(baseUri);
        Node sibling2 = new Node(baseUri);
        parent.addChild(sibling1);
        parent.addChild(node);
        parent.addChild(sibling2);
        assertEquals(0, node.siblingIndex());
        assertEquals(1, sibling1.siblingIndex());
        assertEquals(2, sibling2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder(100);
        node.outerHtml(accum);
        String outerHtml = accum.toString();
        assertTrue(outerHtml.contains("<node>"));
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder(100);
        node.outerHtmlHead(accum, 0, null);
        String outerHtmlHead = accum.toString();
        assertFalse(outerHtmlHead.contains("<node>"));
        assertFalse(outerHtmlHead.contains("</node>"));
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder(100);
        node.outerHtmlTail(accum, 0, null);
        String outerHtmlTail = accum.toString();
        assertFalse(outerHtmlTail.contains("<node>"));
        assertFalse(outerHtmlTail.contains("</node>"));
    }

    @Test
    public void testToString() {
        assertEquals(node.toString(), node.outerHtml());
    }

    @Test
    public void testEquals() {
        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri, attributes);
        Node node3 = new Node(baseUri);
        assertEquals(node1, node1);
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertEquals(node1, node1.clone());
        Node clonedNode = node1.clone();
        assertEquals(node1, clonedNode);
    }

    @Test
    public void testHashCode() {
        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri, attributes);
        Node node3 = new Node(baseUri);
        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();
        assertEquals(hashCode1, hashCode1);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}