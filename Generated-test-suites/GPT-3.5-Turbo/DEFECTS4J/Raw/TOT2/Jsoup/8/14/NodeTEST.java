package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        // Test creating a new Node with base URI and attributes
        Attributes attributes = new Attributes();
        Node node = new Element("testBaseUri", attributes);
        assertNotNull(node);
        assertEquals("testBaseUri", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeAttr() {
        // Test setting and getting attributes
        Node node = new Element("testBaseUri");
        node.attr("key1", "value1");
        assertTrue(node.hasAttr("key1"));
        assertEquals("value1", node.attr("key1"));
    }

    @Test
    public void testNodeAbsUrl() {
        // Test getting absolute URL
        Node node = new Element("http://example.com");
        node.attr("href", "/page.html");
        assertEquals("http://example.com/page.html", node.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        // Test adding and retrieving child nodes
        Node parent = new Element("parent");
        Node child1 = new Element("child1");
        Node child2 = new Element("child2");
        parent.addChildren(child1, child2);
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testParentNode() {
        // Test getting parent node
        Node parent = new Element("parent");
        Node child = new Element("child");
        parent.addChildren(child);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testRemoveNode() {
        // Test removing a node from the DOM tree
        Node parent = new Element("parent");
        Node child = new Element("child");
        parent.addChildren(child);
        
        child.remove();
        assertNull(child.parent());
    }

    @Test
    public void testNextPreviousSibling() {
        // Test getting next and previous siblings
        Node parent = new Element("parent");
        Node child1 = new Element("child1");
        Node child2 = new Element("child2");
        parent.addChildren(child1, child2);
        
        assertEquals(child2, child1.nextSibling());
        assertEquals(child1, child2.previousSibling());
    }

}