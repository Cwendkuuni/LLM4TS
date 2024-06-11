package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "https://www.example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("element1", node1.nodeName());
        assertEquals("https://www.example.com/", node1.baseUri());
        assertNotNull(node1.attributes());
        assertEquals("", node2.nodeName());
        assertNull(node2.attributes());
        assertTrue(node3.childNodes().isEmpty());

        // Test setting base uri and attributes
        node1.setBaseUri("https://www.example2.com/");
        node1.attr("class", "newClass");
        node1.attr("data-test", "newTest");

        assertEquals("https://www.example2.com/", node1.baseUri());
        assertEquals("newClass", node1.attr("class"));
        assertEquals("newTest", node1.attr("data-test"));

        // Test getting absolute URL from relative URL
        String href = node1.absUrl("href");
        assertEquals("https://www.example2.com/href", href);

        String src = node1.absUrl("src");
        assertNull(src); // no "src" attribute

        // Test getting child nodes
        Node child1 = new Node(baseUri, attributes);
        node1.addChildren(child1);
        assertEquals(1, node1.childNodes().size());
        assertEquals(child1, node1.childNode(0));

        // Test setting and getting parent node
        Node parent = new Node(baseUri);
        node1.setParentNode(parent);
        assertEquals(parent, node1.parent());
        parent.removeChild(node1);
        assertNull(node1.parent());

        // Test removing a node from the DOM tree
        parent.addChildren(node1);
        parent.remove();
        assertNull(node1.ownerDocument());
    }

    @Test
    public void testMalformedURL() {
        String baseUri = "https://www.example.com/";
        Attributes attributes = new Attributes();

        try {
            Node node = new Node(baseUri, attributes);
            node.absUrl("javascrip:alert('Hello')");
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // expected
        }
    }
}