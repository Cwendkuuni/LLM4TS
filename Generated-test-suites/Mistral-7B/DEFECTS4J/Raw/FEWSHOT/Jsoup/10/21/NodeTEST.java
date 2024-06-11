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
    public void testCreateNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("Node", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());

        assertEquals("Node", node2.nodeName());
        assertEquals(baseUri, node2.baseUri());
        assertNull(node2.attributes());

        assertEquals("Node", node3.nodeName());
        assertNull(node3.baseUri());
        assertNull(node3.attributes());
    }

    @Test
    public void testBasicAttrAccess() {
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");

        Node node = new Node("", attributes);

        assertEquals("", node.attr(""));
        assertEquals("foo", node.attr("class"));
        assertNull(node.attr("non-existent"));

        attributes.put("id", "bar");
        assertEquals("", node.attr(""));
        assertEquals("foo", node.attr("class"));
        assertEquals("bar", node.attr("id"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("href", "/about");

        Node node = new Node(baseUri, attributes);

        assertEquals("", node.absUrl(""));
        assertEquals("about", node.absUrl("href"));
        assertEquals("http://example.com/about", node.absUrl("href"));
        assertEquals("", node.absUrl("non-existent"));

        attributes.put("href", "relative.html");
        assertEquals("relative.html", node.absUrl("href"));
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
        assertEquals("", node.absUrl("non-existent"));

        String relativeBase = "relative.html";
        Node relativeNode = new Node(relativeBase, attributes);
        assertEquals("", relativeNode.absUrl(""));
        assertEquals("about", relativeNode.absUrl("href"));
        assertEquals("about", relativeNode.absUrl("href")); // relative to relativeBase
        assertEquals("", relativeNode.absUrl("non-existent"));

        Node absoluteNode = new Node("http://example.com/absolute.html", attributes);
        assertEquals("http://example.com/absolute.html", absoluteNode.absUrl(""));
        assertEquals("about", absoluteNode.absUrl("href"));
        assertEquals("http://example.com/about", absoluteNode.absUrl("href"));
    }

    @Test
    public void testAttributeAccessWithPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("href", "http://example.com/about");

        Node node = new Node("", attributes);

        assertEquals("http://example.com/about", node.absUrl("abs:href"));
        assertEquals("", node.attr(""));
        assertEquals("foo", node.attr("class"));
        assertEquals("http://example.com/about", node.absUrl("abs:href"));
        assertNull(node.attr("non-existent"));
        assertNull(node.absUrl("non-existent"));
    }

    @Test
    public void testAttributeRemoval() {
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");

        Node node = new Node("", attributes);

        assertEquals("foo", node.attr("class"));
        node.removeAttr("class");
        assertEquals("", node.attr("class"));
    }

    @Test
    public void testChildNodes() {
        Node parent = new Node("", new Attributes());

        Node child1 = new Node("", new Attributes());
        Node child2 = new Node("", new Attributes());

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));

        Node child3 = new Node("", new Attributes());
        parent.addChildren(0, child3);

        children = parent.childNodes();
        assertEquals(3, children.size());
        assertSame(child3, children.get(0));
        assertSame(child1, children.get(1));
        assertSame(child2, children.get(2));

        Node child4 = new Node("", new Attributes());
        parent.addChildren(child4);

        children = parent.childNodes();
        assertEquals(4, children.size());
        assertSame(child4, children.get(3));
        assertSame(child3, children.get(2));
        assertSame(child1, children.get(1));
        assertSame(child2, children.get(0));

        Node grandChild = new Node("", new Attributes());
        child1.addChildren(grandChild);

        children = parent.childNodes();
        assertEquals(5, children.size());
        assertSame(grandChild, children.get(4));
        assertSame(child4, children.get(3));
        assertSame(child3, children.get(2));
        assertSame(child1, children.get(1));
        assertSame(child2, children.get(0));

        List<Node> childNodesAsArray = parent.childNodesAsArray();
        assertEquals(4, childNodesAsArray.length);
        assertSame(child4, childNodesAsArray[2]);
        assertSame(child3, childNodesAsArray[1]);
        assertSame(child1, childNodesAsArray[0]);
        assertSame(child2, childNodesAsArray[3]);

        parent.removeChild(child1);

        children = parent.childNodes();
        assertEquals(3, children.size());
        assertSame(child3, children.get(1));
        assertSame(child2, children.get(0));
        assertSame(child4, children.get(2));

        parent.removeChild(child3);

        children = parent.childNodes();
        assertEquals(2, children.size());
        assertSame(child2, children.get(0));
        assertSame(child4, children.get(1));
    }

    @Test
    public void testParentNode() {
        Node parent = new Node("", new Attributes());
        Node child1 = new Node("", new Attributes());

        child1.setParentNode(parent);
        assertEquals(parent, child1.parent());

        ArrayList<Node> children = new ArrayList<>(2);
        children.add(child1);
        parent.setChildNodes(children);

        assertEquals(parent, child1.parent());

        Node child2 = new Node("", new Attributes());
        children.set(0, child2);
        assertEquals(parent, child1.parent());
        assertEquals(parent, child2.parent());

        parent.removeChild(child1);
        assertEquals(null, child1.parent());

        parent.removeChild(child2);
        assertEquals(null, child2.parent());
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Node("", new Attributes());
        Node sibling1 = new Node("", new Attributes());
        Node node = new Node("", new Attributes());
        Node sibling2 = new Node("", new Attributes());

        parent.setChildNodes(Arrays.asList(sibling1, node, sibling2));

        assertEquals(1, node.siblingIndex());
        assertEquals(2, sibling2.siblingIndex());
        assertEquals(0, sibling1.siblingIndex());

        parent.removeChild(sibling1);

        assertEquals(0, node.siblingIndex());
        assertEquals(-1, sibling1.siblingIndex());

        parent.addChildren(sibling1, sibling2);

        assertEquals(1, node.siblingIndex());
        assertEquals(2, sibling2.siblingIndex());
        assertEquals(0, sibling1.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accumulator = new StringBuilder();

        Node node = new Elements.TextNode("Hello, World!");
        node.outerHtml(accumulator);

        assertEquals("Hello, World!\n", accumulator.toString());
    }

    @Test
    public void testOuterHtmlWithSettings() {
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setIndentAmount(2);

        Node node = new Elements.TextNode("Hello, World!");
        node.outerHtml(accumulator, 0, settings);

        assertEquals("\nHello, World!\n", accumulator.toString());

        node.outerHtmlHead(accumulator, 1, settings);
        node.outerHtmlTail(accumulator, 1, settings);

        assertEquals("\n<text>\n<text>\n", accumulator.toString());
    }

    @Test
    public void testToString() {
        Node node = new Elements.TextNode("Hello, World!");
        assertEquals("Hello, World!\n", node.outerHtml());
    }

    @Test
    public void testEquals() {
        Node node1 = new Elements.TextNode("Hello, World!");
        Node node2 = new Elements.TextNode("Hello, World!");
        Node node3 = new Elements.TextNode("Hello, World!");
        Node node4 = new Elements.TextNode("Goodbye, World!");

        assertEquals(node1, node1);
        assertEquals(node1, node2);
        assertEquals(node1, node3);

        assertFalse(node1.equals(null));
        assertFalse(node1.equals(node4));
    }

    @Test
    public void testHashcode() {
        Node node1 = new Elements.TextNode("Hello, World!");
        Node node2 = new Elements.TextNode("Hello, World!");
        Node node3 = new Elements.TextNode("Hello, World!");
        Node node4 = new Elements.TextNode("Goodbye, World!");

        int hash1 = node1.hashCode();
        int hash2 = node2.hashCode();
        int hash3 = node3.hashCode();
        int hash4 = node4.hashCode();

        assertNotEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
        assertEquals(hash1, hash2);
        assertEquals(hash1, hash3);
        assertNotEquals(hash1, hash4);
    }

    @Test
    public void testClone() {
        Node original = new Elements.TextNode("Hello, World!");
        Node clone = original.clone();

        assertEquals("Hello, World!\n", original.outerHtml());
        assertEquals("Hello, World!\n", clone.outerHtml());

        assertEquals(original, original);
        assertEquals(clone, clone);
        assertNotEquals(original, clone);
    }
}