package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {
    @Test
    public void testEmptyNode() {
        Node node = new Node();
        assertEquals("", node.baseUri());
        assertNotNull(node.attributes());
        assertEquals(0, node.childNodes().size());
        assertNull(node.parentNode());
        assertEquals("", node.nodeName());
        assertFalse(node.hasAttr("attr"));
        assertEquals("", node.attr("attr"));
        assertEquals("", node.absUrl("attr"));
    }

    @Test
    public void testNodeWithAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        Node node = new Node(baseUri, attributes);
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertEquals(0, node.childNodes().size());
        assertNull(node.parentNode());
        assertEquals("Node", node.nodeName());
        assertTrue(node.hasAttr("attr1"));
        assertEquals("value1", node.attr("attr1"));
        assertEquals("value1", node.absUrl("attr1"));
        assertTrue(node.hasAttr("attr2"));
        assertEquals("value2", node.attr("attr2"));
        assertEquals("value2", node.absUrl("attr2"));
        assertFalse(node.hasAttr("NonExistentAttr"));
        assertEquals("", node.absUrl("NonExistentAttr"));
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, new Attributes());
        Node child2 = new Node(baseUri, new Attributes());

        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        assertEquals(parentNode, child1.parent());
        assertEquals(parentNode, child2.parent());
    }

    @Test
    public void testNodeWithBaseUrl() throws MalformedURLException {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);

        String relUrl = "relative.html";
        String absoluteUrl = node.absUrl(relUrl);

        URL expectedAbsoluteUrl = new URL(baseUri, relUrl);
        assertEquals(expectedAbsoluteUrl.toExternalForm(), absoluteUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedBaseUrl() throws MalformedURLException {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);

        String relUrl = "relative.html";
        node.absUrl(relUrl);
    }

    @Test
    public void testNodeRemoval() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, new Attributes());
        Node child2 = new Node(baseUri, new Attributes());

        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());

        parentNode.removeChild(child1);

        children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));

        Node removedChild = parentNode.childNode(0);
        assertEquals(child1, removedChild);
        assertNull(child1.parentNode());
    }
}