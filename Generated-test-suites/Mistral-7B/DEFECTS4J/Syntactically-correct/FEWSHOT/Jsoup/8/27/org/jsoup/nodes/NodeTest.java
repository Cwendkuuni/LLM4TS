package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testNewNode() {
        String baseUri = "http://example.com/";
        Node node = new Node(baseUri);

        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parent());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNewNodeWithAttributes() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Node(baseUri, attributes);

        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parent());
        assertTrue(node.childNodes().isEmpty());
        assertEquals("myClass", node.attr("class"));
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com/";
        Node node = new Node(baseUri);

        node.setBaseUri("http://test.com/");
        assertEquals("http://test.com/", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/";
        Node node = new Node(baseUri);
        node.attr("href", "about.html");

        assertEquals("", node.absUrl(""));
        assertEquals("about.html", node.absUrl("href"));
        assertEquals("http://example.com/about.html", node.absUrl("href"));
        assertEquals("http://test.com/about.html", node.absUrl("href", "http://test.com/"));
        assertEquals("", node.absUrl("abs:href"));
        assertEquals("", node.absUrl("abs:href", "http://test.com/"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com/";
        Node parent = new Node(baseUri);

        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Node grandChild = new Node(baseUri);
        child1.addChildren(grandChild);

        children = parent.childNodes();
        assertEquals(3, children.size());
    }

    @Test
    public void testAddAndRemoveChildren() {
        String baseUri = "http://example.com/";
        Node parent = new Node(baseUri);

        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());

        parent.removeChild(child1);

        children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));

        parent.remove();

        children = parent.childNodes();
        assertTrue(children.isEmpty());
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com/";
        Node child = new Node(baseUri);
        Node parent = new Node(baseUri);

        child.setParentNode(parent);
        assertEquals(parent, child.parent());

        parent.removeChild(child);
        assertNull(child.parent());
    }
}