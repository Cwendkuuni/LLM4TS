package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class NodeTest {

    @Test
    public void testAttributes() {
        Node node = new Element("div");
        node.attr("id", "test");
        node.attr("class", "test-class");
        node.attr("data-test", "test-data");

        assertEquals("test", node.attr("id"));
        assertEquals("test-class", node.attr("class"));
        assertEquals("test-data", node.attr("data-test"));
        assertNull(node.attr("non-existent-attribute"));

        Attributes attributes = node.attributes();
        assertEquals("id=\"test\" class=\"test-class\" data-test=\"test-data\"", attributes.toString());
    }

    @Test
    public void testHasAttr() {
        Node node = new Element("div");
        node.attr("id", "test");

        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("div");
        node.attr("id", "test");

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a", "http://example.com");
        node.attr("href", "/test");

        assertEquals("http://example.com/test", node.absUrl("href"));
        assertEquals("http://example.com", node.absUrl(""));
        assertNull(node.absUrl("non-existent-attribute"));
    }

    @Test
    public void testChildNodes() {
        Node parent = new Element("ul");

        Node child1 = new Element("li", "Child 1");
        Node child2 = new Element("li", "Child 2");
        Node child3 = new Element("li", "Child 3");

        parent.addChildren(child1, child2, child3);

        Node[] expectedChildren = new Node[]{child1, child2, child3};
        assertArrayEquals(expectedChildren, parent.childNodesAsArray());
    }

    @Test
    public void testParentNode() {
        Node child = new Element("li", "Child");
        Node parent = new Element("ul");

        child.setParentNode(parent);

        assertEquals(parent, child.parent());
        assertNull(child.parent()); // before setting parent
    }

    @Test
    public void testSiblingIndex() {
        Node first = new Element("li", "First");
        Node second = new Element("li", "Second");
        Node third = new Element("li", "Third");

        Node parent = new Element("ul");
        parent.addChildren(first, second, third);

        assertEquals(0, first.siblingIndex());
        assertEquals(1, second.siblingIndex());
        assertEquals(2, third.siblingIndex());
    }
}