package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Node.
 */
public class NodeTest {

    /**
     * Test constructor with baseUri and attributes.
     */
    @Test
    public void testNodeConstructionWithBaseUriAndAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node");

        Node node = new Node(baseUri, attributes);

        assertEquals("node", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    /**
     * Test constructor without baseUri, attributes, or children.
     */
    @Test
    public void testNodeConstructionWithoutArgs() {
        Node node = new Node();

        assertTrue(node.childNodes().isEmpty());
        assertNull(node.parentNode());
        assertNull(node.baseUri());
        assertNull(node.attributes());
    }

    /**
     * Test getNodeName method.
     */
    @Test
    public void testgetNodeName() {
        Node node = new Node();
        assertEquals("Node", node.nodeName());

        Node element = new Element("div");
        assertEquals("div", element.nodeName());
    }

    /**
     * Test getAttribute method.
     */
    @Test
    public void testAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node");
        attributes.put("class", "example");

        Node node = new Node(baseUri, attributes);

        assertEquals("node", node.attr("id"));
        assertEquals("example", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));

        String relUrl = "relative.html";
        assertEquals("", node.absUrl("nonExistentAttr"));
        assertEquals(baseUri + relUrl, node.absUrl("abs:" + relUrl));

        String urlStr = baseUri + "/images/" + "image.jpg";
        URL url = new URL(urlStr);
        assertEquals(url.toExternalForm(), node.absUrl("img:src"));

        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    /**
     * Test getAttributes method.
     */
    @Test
    public void testAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node");
        attributes.put("class", "example");

        Node node = new Node(baseUri, attributes);

        Attributes nodeAttributes = node.attributes();

        assertEquals("node", nodeAttributes.get("id"));
        assertEquals("example", nodeAttributes.get("class"));
        assertTrue(nodeAttributes.isEmpty());
    }

    /**
     * Test setting attribute.
     */
    @Test
    public void testSetAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("testAttr", "testValue");

        assertEquals("testValue", node.attr("testAttr"));
    }

    /**
     * Test checking for attribute existence.
     */
    @Test
    public void testHasAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node");

        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    /**
     * Test removing attribute.
     */
    @Test
    public void testRemoveAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node");
        attributes.put("class", "example");

        Node node = new Node(baseUri, attributes);

        node.removeAttr("class");

        assertFalse(node.hasAttr("class"));
    }

    /**
     * Test getting baseUri method.
     */
    @Test
    public void testBaseUri() {
        String baseUri = "https://example.com";
        Node node = new Node(baseUri, new Attributes());

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setting baseUri method.
     */
    @Test
    public void testSetBaseUri() {
        String oldBaseUri = "https://example.com";
        String newBaseUri = "https://another-example.com";
        Node node = new Node(oldBaseUri, new Attributes());

        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }

    /**
     * Test getting child node by index.
     */
    @Test
    public void testChildNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new TextNode("Child 1");
        Node childNode2 = new TextNode("Child 2");

        parentNode.addChildren(childNode1, childNode2);

        Node testChildNode = parentNode.childNode(0);

        assertEquals(childNode1, testChildNode);
    }

    /**
     * Test getting list of children.
     */
    @Test
    public void testChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new TextNode("Child 1");
        Node childNode2 = new TextNode("Child 2");

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    /**
     * Test getting children as an array.
     */
    @Test
    public void testChildrenAsArray() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new TextNode("Child 1");
        Node childNode2 = new TextNode("Child 2");

        parentNode.addChildren(childNode1, childNode2);

        Node[] children = parentNode.childNodesAsArray();

        assertEquals(2, children.length);
        assertTrue(Arrays.asList(children).contains(childNode1));
        assertTrue(Arrays.asList(children).contains(childNode2));
    }

    /**
     * Test getting parent node.
     */
    @Test
    public void testParent() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node();

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
    }

    /**
     * Test getting document associated with this node.
     */
    @Test
    public void testOwnerDocument() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node();

        childNode.setParentNode(parentNode);

        Document document = childNode.ownerDocument();

        assertNotNull(document);
        assertEquals(parentNode, document);
    }

    /**
     * Test removing node from DOM tree.
     */
    @Test
    public void testRemove() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new TextNode("Child Node");

        parentNode.addChildren(childNode);
        parentNode.removeChild(childNode);

        assertTrue(parentNode.childNodes().isEmpty());
    }

    /**
     * Test replacing node in DOM tree.
     */
    @Test
    public void testReplaceWith() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new TextNode("Old Child Node");
        Node newChildNode = new TextNode("New Child Node");

        parentNode.addChildren(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);

        assertEquals(newChildNode, parentNode.childNode(0));
    }

    /**
     * Test edge case: setting parent node of a node to itself.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingParentNodeToSelf() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.setParentNode(node);
    }

    /**
     * Test edge case: replacing child node with itself.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReplacingChildWithSelf() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node node = new TextNode("Child Node");

        parentNode.addChildren(node);
        parentNode.replaceChild(node, node);
    }

    /**
     * Test edge case: removing child node that doesn't exist.
     */
    @Test
    public void testRemovingNonExistentChildNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        parentNode.removeChild(new TextNode("Non-existent child node"));

        assertTrue(parentNode.childNodes().isEmpty());
    }

    /**
     * Test edge case: adding children with null values.
     */
    @Test(expected = NullPointerException.class)
    public void testAddingChildrenWithNullValues() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        parentNode.addChildren(null);
    }
}