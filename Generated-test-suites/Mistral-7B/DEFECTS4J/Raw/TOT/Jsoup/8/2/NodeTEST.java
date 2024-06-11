package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");

        // Test protected constructor
        Node node1 = new Node(baseUri, attributes);
        assertEquals("test", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertNotNull(node1.attributes());
        assertTrue(node1.hasAttr("id"));
        assertEquals("test", node1.attr("id"));
        assertEquals("", node1.attr("nonExistentAttribute"));

        // Test default constructor
        Node node2 = new Node();
        assertEmpty(node2.childNodes());
        assertNull(node2.attributes());
        assertEquals(Collections.EMPTY_LIST, node2.childNodes());

        // Test setting and getting attributes
        Node node3 = new Node(baseUri, attributes);
        node3.attr("class", "testClass");
        assertEquals("testClass", node3.attr("class"));
        assertFalse(node3.hasAttr("nonExistentAttribute"));
        node3.removeAttr("id");
        assertFalse(node3.hasAttr("id"));

        // Test baseURI and absUrl
        assertEquals(baseUri, node3.baseUri());
        assertEquals(baseUri + "/test", node3.absUrl("id"));
        assertEquals("", node3.absUrl("nonExistentAttribute"));

        // Test childNodes
        Element element = new Element("div", attributes);
        Node child1 = new TextNode("Child 1");
        Node child2 = new TextNode("Child 2");
        node3.addChildren(element, child1, child2);
        Node[] children = node3.childNodesAsArray();
        assertEquals(3, children.length);
        assertEquals("div", children[0].nodeName());
        assertEquals("Child 1", children[1].outerHtml());
        assertEquals("Child 2", children[2].outerHtml());

        // Test parent
        Element parentElement = new Element("body", attributes);
        parentElement.addChildren(node3);
        assertEquals(parentElement, node3.parent());
        assertNull(node3.parent()); // after remove

        node3.remove();
        assertFalse(parentElement.childNodes().contains(node3));
    }

    // Add more test cases here as needed for specific methods or behavior of the Node class

    private void assertEmpty(List<Node> nodes) {
        assertEquals(0, nodes.size());
    }
}