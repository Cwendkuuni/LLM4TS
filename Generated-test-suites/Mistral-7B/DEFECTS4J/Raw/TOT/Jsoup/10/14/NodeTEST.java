package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");

        // Test default constructor
        Node node1 = new Node();
        assertEquals("", node1.nodeName());
        assertNull(node1.parentNode);
        assertTrue(node1.childNodes().isEmpty());
        assertNull(node1.attributes());
        assertEquals("", node1.baseUri());
        assertEquals(-1, node1.siblingIndex());

        // Test constructor with baseUri and attributes
        Node node2 = new Node(baseUri, attributes);
        assertEquals("", node2.nodeName());
        assertNull(node2.parentNode);
        assertTrue(node2.childNodes().isEmpty());
        assertEquals(attributes, node2.attributes());
        assertEquals(baseUri, node2.baseUri());
        assertEquals(-1, node2.siblingIndex());

        // Test constructor without baseUri or attributes
        Node node3 = new Node();
        assertEquals("", node3.nodeName());
        assertNull(node3.parentNode);
        assertTrue(node3.childNodes().isEmpty());
        assertNull(node3.attributes());
        assertEquals("", node3.baseUri());
        assertEquals(-1, node3.siblingIndex());

        // Test getters and setters
        node2.setBaseUri("http://test.com");
        assertEquals("http://test.com", node2.baseUri());

        node2.setAttributes(new Attributes().put("class", "test"));
        assertEquals("class", node2.attr("class"));
        assertEquals(1, node2.childNodes().size());

        // Test abstract methods
        assertFalse(node1.hasAttr("id"));
        assertEquals("", node1.attr("id"));

        node2.setAttribute("id", "test2");
        assertTrue(node2.hasAttr("id"));
        assertEquals("test2", node2.attr("id"));

        node2.setBaseUri("http://test2.com");
        assertEquals("http://test2.com", node2.absUrl("href"));
        assertEquals("http://example.com/href", node2.absUrl("abs:href"));

        // Test getChildNode and childNodes
        Node child1 = new Node();
        node2.appendChild(child1);
        Node child2 = new Node();
        node2.appendChild(child2);
        assertEquals(2, node2.childNodes().size());
        assertEquals(child1, node2.childNode(0));
        assertEquals(child2, node2.childNode(1));

        // Test removeChild
        node2.removeChild(child1);
        assertEquals(1, node2.childNodes().size());

        // Test clone
        Node clone1 = node3.clone();
        assertEquals(node3, clone1);

        Node clone2 = node2.clone();
        assertNotSame(node2, clone2);
        assertEquals(node2, clone2.ownerDocument());
        assertEquals(node2.childNodes(), clone2.childNodes());
        assertEquals(node2.attributes(), clone2.attributes());
    }
}