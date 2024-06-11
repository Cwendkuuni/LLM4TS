package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "nodeClass");

        Node node = new Node(baseUri, attributes);

        assertEquals("nodeClass", node.attr("class"));
        assertEquals("http://example.com", node.baseUri());

        // Test getters for other variables
        assertNull(node.parentNode);
        assertTrue(node.childNodes.isEmpty());
        assertNull(node.attributes());
        assertEquals("", node.attr("nonExistentAttribute"));
    }

    @Test
    public void testNodeWithoutBaseUriAndAttributes() {
        Node node = new Node();

        assertNull(node.parentNode);
        assertTrue(node.childNodes.isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testAbsUrlWithRelativeUrlAttribute() {
        String nodeBaseUri = "http://example.com";
        String attrValue = "relativeUrl.html";
        Node node = new Node(nodeBaseUri, new Attributes());
        node.attributes().put("abs:href", attrValue);

        String absUrl = node.absUrl("href");
        assertEquals("http://example.com/relativeUrl.html", absUrl);
    }

    @Test
    public void testAbsUrlWithAbsoluteUrlAttribute() {
        String nodeBaseUri = "http://example.com";
        String attrValue = "http://another-example.com/page.html";
        Node node = new Node(nodeBaseUri, new Attributes());
        node.attributes().put("href", attrValue);

        String absUrl = node.absUrl("href");
        assertEquals("http://another-example.com/page.html", absUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlWithInvalidUrlAttribute() {
        String nodeBaseUri = "http://example.com";
        String attrValue = "invalidUrl";
        Node node = new Node(nodeBaseUri, new Attributes());
        node.attributes().put("abs:href", attrValue);

        node.absUrl("abs:href");
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());

        Node child1 = new Node(baseUri, new Attributes());
        Node child2 = new Node(baseUri, new Attributes());

        parentNode.addChildren(child1, child2);

        List<Node> childNodes = parentNode.childNodes();
        List<Node> expectedChildren = new ArrayList<Node>(Arrays.asList(child1, child2));

        assertEquals(expectedChildren, childNodes);
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode = new Node(baseUri, new Attributes());

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parent()); // default constructor
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());

        parentNode.addChildren(childNode1, childNode2);

        parentNode.removeChild(childNode1);

        List<Node> childNodes = parentNode.childNodes();
        List<Node> expectedChildren = Arrays.asList(childNode2);

        assertEquals(expectedChildren, childNodes);
    }
}