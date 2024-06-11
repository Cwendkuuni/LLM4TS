package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class NodeTest {

    @org.junit.Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Node(baseUri, attributes);
        assertEquals("elementId", node.attr("id"));
        assertEquals(baseUri, node.baseUri());

        Node nodeWithoutAttributes = new Node(baseUri);
        assertNull(nodeWithoutAttributes.attributes());
    }

    @org.junit.Test
    public void testNodeAttributeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Node(baseUri, attributes);

        node.attr("class", "exampleClass");
        assertEquals("exampleClass", node.attr("class"));

        assertFalse(node.hasAttr("nonexistentAttribute"));

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));

        String absUrl = node.absUrl("href");
        assertEquals("", absUrl);
    }

    @org.junit.Test
    public void testNodeChildMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parentElementId");
        Node parent = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        parent.addChild(child1);

        Node child2 = new Node(baseUri, attributes);
        parent.addChild(child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Node child3 = parent.childNode(1);
        assertEquals(child2, child3);

        Node[] childArray = parent.childNodesAsArray();
        assertArrayEquals(new Node[]{child1, child2}, childArray);

        assertEquals(parent, child1.parent());
        assertEquals(parent, child2.parent());
        assertEquals(null, child1.ownerDocument());
        assertEquals(null, child2.ownerDocument());
    }

    @org.junit.Test
    public void testNodeParentMethods() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "grandparentElementId");
        Node grandParent = new Node(baseUri, parentAttributes);

        Attributes attributes = new Attributes();
        attributes.put("id", "parentElementId");
        Node parentNode = new Node(baseUri, attributes);
        grandParent.addChild(parentNode);

        Node child = new Node(baseUri, attributes);
        parentNode.addChild(child);

        assertEquals(parentNode, child.parent());
        assertEquals(grandParent, parentNode.parent());
        assertEquals(grandParent, child.ownerDocument());

        child.remove();
        assertNull(parentNode.childNodes().get(0));
    }
}