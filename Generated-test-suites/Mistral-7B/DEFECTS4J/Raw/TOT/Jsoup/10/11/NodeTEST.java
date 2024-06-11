package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import java.net.MalformedURLException;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        assertEquals("element1", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parentNode());
        assertNotNull(node.attributes());
        assertEquals(0, node.childNodes().size());
    }

    @Test
    public void testNodeCreationWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parentNode());
        assertNull(node.attributes());
        assertEquals(0, node.childNodes().size());
    }

    @Test
    public void testNodeCreationWithNullAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, null);

        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parentNode());
        assertNull(node.attributes());
        assertEquals(0, node.childNodes().size());
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        attributes.put("class", "element-class");
        Node node = new Node(baseUri, attributes);

        assertEquals("element1", node.attr("id"));
        assertEquals("element-class", node.attr("class"));
        assertEquals("", node.attr("nonexistent"));
        assertEquals("", node.attr("abs:href")); // default behavior
    }

    @Test
    public void testGetAbsoluteUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "relative.html";
        Attributes attributes = new Attributes();
        attributes.put("href", relativeUrl);
        Node node = new Node(baseUri, attributes);

        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testGetAbsoluteUrlWithBaseUriAlreadyUrl() {
        String baseUri = "http://example.com/relative.html";
        String relativeUrl = "absolute.html";
        Attributes attributes = new Attributes();
        attributes.put("href", relativeUrl);
        Node node = new Node(baseUri, attributes);

        assertEquals("http://example.com/absolute.html", node.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testGetAbsoluteUrlWithMalformedUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "malformed:url";
        Attributes attributes = new Attributes();
        attributes.put("href", relativeUrl);
        Node node = new Node(baseUri, attributes);

        node.absUrl("href");
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);

        Node retrievedChildNode = parentNode.childNode(0);

        assertEquals(childNode, retrievedChildNode);
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("id", "newId");

        assertEquals("newId", node.attr("id"));
    }

    @Test
    public void testSetAttributeWithNullValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("id", null);

        assertEquals("", node.attr("id"));
    }

    @Test
    public void testSetAttributeWithEmptyValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("id", "");

        assertEquals("", node.attr("id"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");

        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testSetBaseUri() {
        String oldBaseUri = "http://example.com";
        String newBaseUri = "http://newexample.com";
        Node node = new Node(oldBaseUri, new Attributes());

        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriWithNullBaseUri() {
        Node node = new Node();

        node.setBaseUri(null);
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node originalNode = new Node(baseUri, attributes);
        Node clonedNode = originalNode.clone();

        assertEquals(originalNode.nodeName(), clonedNode.nodeName());
        assertEquals(originalNode.baseUri(), clonedNode.baseUri());
        assertNotSame(originalNode.parentNode(), clonedNode.parentNode());
        assertNotSame(originalNode.attributes(), clonedNode.attributes());
        assertEquals(originalNode.childNodes().size(), clonedNode.childNodes().size());

        for (int i = 0; i < originalNode.childNodes().size(); i++) {
            Node originalChildNode = originalNode.childNode(i);
            Node clonedChildNode = clonedNode.childNode(i);

            assertEquals(originalChildNode.nodeName(), clonedChildNode.nodeName());
            assertEquals(originalChildNode.baseUri(), clonedChildNode.baseUri());
            assertNotSame(originalChildNode, clonedChildNode);
            assertNotSame(originalChildNode.parentNode(), clonedChildNode.parentNode());
            assertNotSame(originalChildNode.attributes(), clonedChildNode.attributes());

            if (originalChildNode instanceof Element) {
                Element originalElement = (Element) originalChildNode;
                Element clonedElement = (Element) clonedChildNode;

                assertEquals(originalElement.tagName(), clonedElement.tagName());
                assertNotSame(originalElement.attributes(), clonedElement.attributes());
            }
        }
    }

    @Test
    public void testClonedNodeWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        Node clonedParentNode = parentNode.clone();
        Node clonedChildNode = clonedParentNode.childNode(0);

        assertEquals(parentNode.nodeName(), clonedParentNode.nodeName());
        assertEquals(parentNode.baseUri(), clonedParentNode.baseUri());
        assertNotSame(parentNode, clonedParentNode);
        assertNotSame(parentNode.attributes(), clonedParentNode.attributes());
        assertEquals(parentNode.childNodes().size(), clonedParentNode.childNodes().size());
        assertEquals(childNode, clonedChildNode);
        assertNotSame(parentNode, clonedChildNode.parentNode());
        assertNotSame(parentNode.attributes(), clonedChildNode.attributes());
    }
}