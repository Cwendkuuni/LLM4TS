package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testNewInstance() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
        assertNull(node.parentNode());
        assertSame(Collections.emptyList(), node.childNodes());
        assertNull(node.attributes());
        assertNull(node.baseUri());
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testNewInstanceWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", "", attributes);
        assertEquals("div", node.nodeName());
        assertSame(Collections.emptyList(), node.childNodes());
        assertSame(attributes, node.attributes());
        assertEquals("test", node.attr("class"));
        assertTrue(node.hasAttr("class"));
        assertNull(node.baseUri());
    }

    @Test
    public void testNewInstanceWithBaseUri() {
        Node node = new Element("div", "", null, "http://example.com/");
        assertEquals("div", node.nodeName());
        assertNull(node.parentNode());
        assertSame(Collections.emptyList(), node.childNodes());
        assertNull(node.attributes());
        assertEquals("http://example.com/", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        Node node = new Element("div");
        node.setBaseUri("http://example.com/");
        assertEquals("http://example.com/", node.baseUri());
    }

    @Test
    public void testGetChildNodeByNameIndex() {
        Node parent = new Element("div");
        Node child1 = new TextNode("child1");
        Node child2 = new Element("p");
        parent.addChild(child1);
        parent.addChild(child2);

        Node child = parent.childNode(1);
        assertEquals("p", child.nodeName());
    }

    @Test
    public void testGetChildren() {
        Node parent = new Element("div");
        Node child1 = new TextNode("child1");
        Node child2 = new Element("p");
        parent.addChild(child1);
        parent.addChild(child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        for (Node node : children) {
            if (node == child1) {
                assertEquals("child1", node.nodeName());
            } else if (node == child2) {
                assertEquals("p", node.nodeName());
            }
        }
    }

    @Test
    public void testGetChildNodesAsArray() {
        Node parent = new Element("div");
        Node child1 = new TextNode("child1");
        Node child2 = new Element("p");
        parent.addChild(child1);
        parent.addChild(child2);

        Node[] children = parent.childNodesAsArray();
        assertEquals(2, children.length);
        for (Node node : Arrays.asList(children)) {
            if (node == child1) {
                assertEquals("child1", node.nodeName());
            } else if (node == child2) {
                assertEquals("p", node.nodeName());
            }
        }
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Element("div");
        Node child1 = new TextNode("child1");
        Node child2 = new Element("p");
        parent.addChild(child1);
        parent.addChild(child2);

        parent.removeChild(child1);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertSame(child2, children.get(0));
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Element("div");
        Node oldChild = new TextNode("oldChild");
        Node newChild = new Element("p");
        parent.addChild(oldChild);

        parent.replaceChild(oldChild, newChild);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertSame(newChild, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        Node child = new TextNode("child");
        Node parent = new Element("div");

        child.setParentNode(parent);
        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testGetParent() {
        Node child = new TextNode("child");
        Node parent = new Element("div");
        child.setParentNode(parent);

        Node parentFromChild = child.parent();
        assertEquals(parent, parentFromChild);
    }

    @Test
    public void testAddChildren() {
        Node parent = new Element("div");
        Node child1 = new TextNode("child1");
        Node child2 = new Element("p");

        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        for (Node node : children) {
            if (node == child1) {
                assertEquals("child1", node.nodeName());
            } else if (node == child2) {
                assertEquals("p", node.nodeName());
            }
        }
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Element("div");
        Node child1 = new TextNode("child1");
        Node child2 = new Element("p");

        parent.addChildren(0, child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        for (Node node : children) {
            if (node == child1) {
                assertEquals("child1", node.nodeName());
                assertEquals(0, node.siblingIndex());
            } else if (node == child2) {
                assertEquals("p", node.nodeName());
                assertEquals(1, node.siblingIndex());
            }
        }
    }
}