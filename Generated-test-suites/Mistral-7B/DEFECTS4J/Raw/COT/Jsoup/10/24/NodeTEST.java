package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Node(baseUri, attributes);

        assertEquals("elementId", node.attr("id"));
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeConstructorWithoutBaseUriOrAttributes() {
        Node node = new Node();

        assertNull(node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testNodeAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Node(baseUri, attributes);

        node.attr("id", "newId");
        assertEquals("newId", node.attr("id"));
        assertEquals("elementId", node.attr("oldId"));
        assertNull(node.attr("nonExistentAttr"));
    }

    @Test
    public void testNodeRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testNodeAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        String relativeUrl = "relativeUrl";
        node.attr("href", relativeUrl);
        String absoluteUrl = node.absUrl("href");

        assertEquals("http://example.com" + relativeUrl, absoluteUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testNodeAbsUrlMalformedUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        String invalidUrl = "/invalid:url";
        node.attr("abs:href", invalidUrl);
        node.absUrl("abs:href");
    }

    @Test
    public void testNodeChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        Node retrievedChildNode = parentNode.childNode(0);

        assertSame(childNode, retrievedChildNode);
    }

    @Test
    public void testNodeChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testNodeParent() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri1, attributes);
        Node parentNode1 = new Node(baseUri2, attributes);
        Node parentNode2 = new Node(baseUri2, attributes);

        childNode.setParentNode(parentNode1);
        Node retrievedParentNode1 = childNode.parent();
        assertSame(parentNode1, retrievedParentNode1);

        childNode.setParentNode(parentNode2);
        Node retrievedParentNode2 = childNode.parent();
        assertSame(parentNode2, retrievedParentNode2);
    }

    @Test
    public void testNodeRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);

        List<Node> children = parentNode.childNodes();
        assertFalse(children.contains(childNode));
    }

    @Test
    public void testNodeReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);

        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);

        List<Node> children = parentNode.childNodes();
        assertFalse(children.contains(oldChildNode));
        assertTrue(children.contains(newChildNode));
    }

    @Test
    public void testNodeSetParentNode() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri1, attributes);
        Node parentNode1 = new Node(baseUri2, attributes);
        Node parentNode2 = new Node(baseUri2, attributes);

        childNode.setParentNode(parentNode1);
        Node retrievedParentNode1 = childNode.parent();
        assertSame(parentNode1, retrievedParentNode1);

        childNode.setParentNode(parentNode2);
        Node retrievedParentNode2 = childNode.parent();
        assertSame(parentNode2, retrievedParentNode2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeSetParentNodeNull() {
        Node childNode = new Node();
        childNode.setParentNode(null);
    }

    @Test
    public void testNodeSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);

        parentNode.addChildren(node1, node2);
        node1.setSiblingIndex(1);

        assertEquals(1, node1.siblingIndex());
    }

    @Test
    public void testNodeOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Node(baseUri, attributes);

        StringBuilder expectedOutput = new StringBuilder();
        node.outerHtml(expectedOutput);

        String actualOutput = node.outerHtml();
        assertEquals(expectedOutput.toString(), actualOutput);
    }
}