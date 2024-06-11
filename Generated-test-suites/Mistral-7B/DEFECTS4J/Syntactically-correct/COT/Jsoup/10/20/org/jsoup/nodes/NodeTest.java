package org.jsoup.nodes; // Update the package name if necessary

import org.junit.Test;
import org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);
        assertEquals("node1", node.attr("id"));
        assertEquals(baseUri, node.baseUri());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertNull(node.baseUri());
    }

    @Test
    public void testNodeAbstractMethods() {
        Node node = new Node();
        assertNull(node.nodeName());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullBaseUri() {
        String baseUri = null;
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = null;
        Node node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeGetSetBaseUri() {
        String baseUri = "http://example.com";
        String newBaseUri = "https://example.com";
        Node node = new Node(baseUri);

        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testNodeGetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "nodeClass");

        Node node = new Node(baseUri, attributes);
        assertEquals("node1", node.attr("id"));
        assertEquals("nodeClass", node.attr("class"));
        assertEquals("", node.attr("nonexistent"));
    }

    @Test
    public void testNodeSetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("id", "newId");
        assertEquals("newId", node.attr("id"));
    }

    @Test
    public void testNodeRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");
        assertEquals("", node.attr("id"));
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        String attributeKey = null;

        node.attr(attributeKey, "value");
    }

    @Test
    public void testNodeAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "/relative/path.html";
        Node node = new Node(baseUri);

        String absoluteUrl = node.absUrl(relativeUrl);
        assertTrue(absoluteUrl.startsWith("http://example.com/relative/path.html"));
    }

    @Test
    public void testNodeAbsUrlMalformedURL() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        String attributeKey = "abs:href";
        String relativeUrl = "relative.html";
        String absoluteUrl = node.absUrl(attributeKey);

        assertTrue(absoluteUrl.isEmpty());
    }

    @Test
    public void testNodeChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testNodeChildNodesWithNullParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node child = new Node(baseUri, attributes);
        child.setParentNode(null);

        assertTrue(child.childNodes().isEmpty());
        assertFalse(child.hasParentNode());
    }

    @Test
    public void testNodeSetParentNode() {
        String baseUri = "http://example.com";
        String parentBaseUri = "http://parent.example.com";
        Attributes attributes = new Attributes();

        Node child = new Node(baseUri, attributes);
        Node parent = new Node(parentBaseUri, attributes);

        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }
}