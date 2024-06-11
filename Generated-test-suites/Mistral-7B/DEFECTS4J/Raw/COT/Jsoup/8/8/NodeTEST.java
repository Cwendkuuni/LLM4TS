package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test class for Node.
 */
public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        assertNotNull(node);
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithoutAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertNotNull(node);
        assertEquals(baseUri, node.baseUri());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testDefaultConstructor() {
        Node node = new Node();
        assertNotNull(node);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeName() {
        Node node = new TextNode("Test");
        assertEquals("Test", node.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testNullNodeName() {
        new Node("", null) {
            @Override
            public String nodeName() {
                throw new AssertionError();
            }
        };
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        Node node = new Node(baseUri, attributes);
        assertEquals("testValue", node.attr("test"));
        assertEquals("", node.attr("nonExistentAttr"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "/relative/path";
        Node node = new Node(baseUri, new Attributes());
        node.attr("href", relativeUrl);
        assertEquals("http://example.com/relative/path", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistentAttr"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        node.attr("abs:url", "invalid:url");
    }

    @Test
    public void testGetChildNodeByIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        Node childNode = parentNode.childNode(0);
        assertEquals(childNode1, childNode);
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        parentNode.removeChild(childNode1);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertFalse(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }
}