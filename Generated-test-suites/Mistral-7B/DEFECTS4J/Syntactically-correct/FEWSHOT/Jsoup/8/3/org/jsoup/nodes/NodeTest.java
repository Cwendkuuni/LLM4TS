package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node = new Node(baseUri, attributes);
        assertEquals("test", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.hasAttr("class"));
        assertEquals("test", node.attr("class"));
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.setBaseUri("http://test.com");
        assertEquals("http://test.com", node.baseUri());
    }

    @Test
    public void testGetAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attr("href", "relative.html");
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testGetAbsUrlWithBaseUriInAttribute() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attr("abs:href", "relative.html");
        assertEquals("http://example.com/relative.html", node.absUrl("abs:href"));
    }

    @Test
    public void testGetAbsUrlNullAttribute() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals("", node.absUrl(null));
    }

    @Test
    public void testGetAbsUrlMalformedUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attr("abs:href", "malformed://example.com");
        assertEquals("", node.absUrl("abs:href"));
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child = new Node(baseUri, new Attributes());
        parent.addChild(child);
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri, new Attributes());
        Node child2 = new Node(baseUri, new Attributes());
        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child = new Node(baseUri, new Attributes());
        parent.addChild(child);
        parent.removeChild(child);
        assertTrue(parent.childNodes().isEmpty());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node oldChild = new Node(baseUri, new Attributes());
        Node newChild = new Node(baseUri, new Attributes());
        parent.addChild(oldChild);
        parent.replaceChild(newChild, oldChild);
        assertEquals(newChild, parent.childNode(0));
        assertTrue(oldChild.childNodes().isEmpty());
    }

    @Test
    public void testSetParentNode() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Node child = new Node(baseUri1, new Attributes());
        Node parent1 = new Node(baseUri2, new Attributes());
        child.setParentNode(parent1);
        assertEquals(parent1, child.parent());
    }
}