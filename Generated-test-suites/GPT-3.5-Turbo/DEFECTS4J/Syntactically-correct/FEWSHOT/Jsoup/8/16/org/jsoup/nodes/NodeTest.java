package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {
    @Test
    public void testNodeName() {
        assertEquals("#text", new TextNode("").nodeName());
        assertEquals("p", new Element("p").nodeName());
        assertEquals("", new Document("").nodeName());
    }

    @Test
    public void testAttr() {
        Node node = new Element("div");
        node.attr("id", "myId");
        assertEquals("myId", node.attr("id"));
        assertNull(node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        Node node = new Element("div");
        node.attr("id", "myId");
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("div");
        node.attr("id", "myId");
        node.attr("class", "myClass");
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("div");
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a");
        node.attr("href", "/foo");
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com/foo", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testParent() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.addChildren(child);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testOwnerDocument() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.addChildren(child);
        assertEquals(parent, child.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.addChildren(child);
        child.remove();
        assertNull(child.parent());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");
        parent.addChildren(child1);
        child1.replaceWith(child2);
        assertNull(child1.parent());
        assertEquals(child2, parent.childNode(0));
    }
}