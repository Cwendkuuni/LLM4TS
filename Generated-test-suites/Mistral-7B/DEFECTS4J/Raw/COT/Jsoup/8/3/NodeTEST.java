package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Node class.
 */
public class NodeTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    // Test for constructor with baseUri and attributes

    @org.junit.Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        assertEquals("node1", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Attributes attributes = new Attributes();
        Node node = new Node(null, attributes);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, null);
    }

    // Test for constructor without arguments

    @org.junit.Test
    public void testDefaultConstructor() {
        Node node = new Node();
        assertTrue(Collections.EMPTY_LIST.equals(node.childNodes()));
        assertNull(node.attributes());
        assertNull(node.baseUri());
    }

    // Test for abstract methods nodeName()

    @org.junit.Test(expected = AbstractMethodError.class)
    public void testAbstractNodeNameMethod() {
        Node node = new Node();
        node.nodeName();
    }

    // Test for attr() method

    @org.junit.Test
    public void testAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        assertEquals("", node.attr("nonExistentAttr"));
        assertEquals("node1", node.attr("id"));
        assertEquals("", node.attr(""));
        assertEquals("", node.attr(null));
    }

    @org.junit.Test
    public void testAbsUrlMethod() {
        String baseUri = "http://example.com";
        String relativeUrl = "relativeUrl";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        String absUrl = node.absUrl("relativeUrl");
        assertTrue(absUrl.startsWith(baseUri));
        String absoluteUrl = "http://example.com/absoluteUrl";
        attributes.put("href", absoluteUrl);
        absUrl = node.absUrl("href");
        assertTrue(absUrl.equals(absoluteUrl));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMalformedURLExceptionInAbsUrlMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.absUrl("malformedUrl");
    }

    // Test for getChildNode() method

    @org.junit.Test
    public void testGetChildNodeByIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(child1, child2);
        Node child = parentNode.childNode(0);
        assertEquals(child1, child);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildNodeOutOfBounds() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(child1, child2);
        parentNode.childNode(3);
    }

    // Test for getChildren() method

    @org.junit.Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(child1, child2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    // Test for remove() method

    @org.junit.Test
    public void testRemoveNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(child1, child2);
        parentNode.remove();
        assertEquals(Collections.EMPTY_LIST, parentNode.childNodes());
    }

    @org.junit.Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(child1, child2);
        parentNode.removeChild(child1);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));
    }

    // Test for addChildren() method

    @org.junit.Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(child1, child2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChildren(null);
    }

    @org.junit.Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(1, child1, child2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    // Test for parent() method

    @org.junit.Test
    public void testGetParentNode() {
        String baseUri1 = "http://example1.com";
        Attributes attributes1 = new Attributes();
        Node parentNode1 = new Node(baseUri1, attributes1);
        String baseUri2 = "http://example2.com";
        Attributes attributes2 = new Attributes();
        Node child1 = new Node(baseUri1, attributes1);
        Node child2 = new Node(baseUri2, attributes2);
        child1.setParentNode(parentNode1);
        child2.setParentNode(child1);
        assertEquals(parentNode1, child2.parent());
        assertNull(child1.parent());
    }

    // Test for ownerDocument() method

    @org.junit.Test
    public void testGetOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Document document = new Document();
        document.setBaseUri(baseUri);
        parentNode.setOwnerDocument(document);
        assertEquals(document, parentNode.ownerDocument());
        assertNull(document.ownerDocument());
    }

    // Test for hasAttr() method

    @org.junit.Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    // Test for setAttr() method

    @org.junit.Test
    public void testSetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("id", "node1");
        assertEquals("node1", node.attr("id"));
    }

    // Test for removeAttr() method

    @org.junit.Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    // Test for toString() method

    @org.junit.Test
    public void testToString() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        assertEquals("", node.toString());
    }

    // Test for equals() and hashCode() methods

    @org.junit.Test
    public void testEqualsAndHashCode() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node node1 = new Node(baseUri, attributes1);
        Node node2 = new Node(baseUri, attributes2);
        Node node3 = new Node(baseUri, attributes1);
        assertEquals(node1, node3);
        // different parents
        Node parentNode1 = new Node(baseUri, attributes1);
        Node parentNode2 = new Node(baseUri, attributes2);
        Node node4 = new Node(baseUri, attributes1);
        node4.setParentNode(parentNode2);
        assertNotEquals(node1, node4);
        // different child nodes
        Node child1 = new TextNode("child1");
        Node child2 = new TextNode("child2");
        parentNode1.addChildren(child1, child2);
        Node node5 = new Node(baseUri, attributes1);
        Node node6 = new Node(baseUri, attributes1);
        node6.addChildren(new TextNode("child3"));
        assertNotEquals(parentNode1, node6);
        // different attributes
        attributes1.put("id1", "value1");
        attributes2.put("id2", "value2");
        Node node7 = new Node(baseUri, attributes1);
        Node node8 = new Node(baseUri, attributes2);
        assertNotEquals(node7, node8);
        // same node
        Node node9 = node1;
        assertEquals(node1, node9);
    }
}