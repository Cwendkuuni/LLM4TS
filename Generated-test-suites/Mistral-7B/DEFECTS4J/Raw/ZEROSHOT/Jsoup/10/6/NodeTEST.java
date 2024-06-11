package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("testNode", node1.nodeName());
        assertEquals("http://example.com", node1.baseUri());
        assertEquals(attributes, node1.attributes());
        assertNull(node2.attributes());
        assertNull(node3.attributes());
    }

    @Test
    public void testAttributeGetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        attributes.put("class", "testClass");

        Node node = new Node(baseUri, attributes);

        assertEquals("testNode", node.nodeName());
        assertEquals("http://example.com", node.baseUri());
        assertEquals("testNode", node.attr("id"));
        assertEquals("testClass", node.attr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));
        assertEquals("", node.attr("nonExistentAttr"));
        assertNull(node.attributes().get("nonExistentAttr"));
    }

    @Test
    public void testAttributeSetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("id", "testNode");
        node.attr("class", "testClass");

        assertEquals("testNode", node.attr("id"));
        assertEquals("testClass", node.attr("class"));
        assertEquals("testNode", node.attributes().get("id"));
        assertEquals("testClass", node.attributes().get("class"));
    }

    @Test
    public void testAttributeRemoval() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        attributes.put("class", "testClass");

        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");
        node.removeAttr("class");

        assertFalse(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("id"));
        assertEquals("", node.attr("class"));
        assertNull(node.attributes().get("id"));
        assertNull(node.attributes().get("class"));
    }

    @Test
    public void testBaseUriGetters() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals("http://example.com", node.baseUri());
        node.setBaseUri("http://newexample.com");
        assertEquals("http://newexample.com", node.baseUri());
    }

    @Test
    public void testChildNodesGetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();

        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Node child3 = new Node(baseUri, attributes);
        parent.addChildren(child3);

        children = parent.childNodes();

        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
    }

    @Test
    public void testParentNodeGetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node child = new Node(baseUri, attributes);

        child.setParentNode(parent);

        assertEquals(parent, child.parent());
        assertNull(child.parent());

        parent.addChildren(child);

        assertEquals(child, parent.childNodes().get(0));
    }

    @Test
    public void testAbsUrlGetter() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);
        node.attr("href", "relative.html");

        assertEquals("", node.absUrl("nonExistentAttr"));
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedException() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);
        node.attr("abs:href", "malformed://example.com");

        node.absUrl("abs:href");
    }
}