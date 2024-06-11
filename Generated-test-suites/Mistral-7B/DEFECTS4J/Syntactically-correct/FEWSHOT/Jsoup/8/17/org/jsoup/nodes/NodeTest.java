package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class NodeTest {

    @Test
    public void testNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertNull(node.childNodes());
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node parentNode = new Element("body", baseUri, new Attributes());
        Node childNode = new Element("div", baseUri, attributes);
        parentNode.appendChild(childNode);
        assertEquals("body", parentNode.nodeName());
        assertEquals(baseUri, parentNode.baseUri());
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode, childNodes.get(0));
        assertEquals(parentNode, childNode.parentNode());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri, new Attributes());
        node.setBaseUri("http://test.com");
        assertEquals("http://test.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "images/image.jpg";
        Node node = new Element("img", baseUri, new Attributes().put("src", relativeUrl));
        String absolutUrl = node.absUrl("src");
        assertEquals("http://example.com/images/image.jpg", absolutUrl);
    }

    @Test
    public void testAbsUrlWithParentBaseUri() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "images/image.jpg");
        Node parentNode = new Element("a", baseUri, attributes);
        Node childNode = new Element("img", null, new Attributes());
        parentNode.appendChild(childNode);
        String absolutUrl = childNode.absUrl("href");
        assertEquals("http://example.com/images/image.jpg", absolutUrl);
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "test-class");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("test", node.attr("id"));
        assertEquals("test-class", node.attr("class"));
        assertEquals("", node.attr("non-existent-attribute"));
    }

    @Test
    public void testSetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, new Attributes());
        node.attr("id", "test");
        node.attr("class", "test-class");
        assertEquals("test", node.attr("id"));
        assertEquals("test-class", node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", baseUri, new Attributes());
        Node childNode1 = new Element("div", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
    }

    @Test
    public void testGetChildNodeByIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", baseUri, new Attributes());
        Node childNode1 = new Element("div", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        Node childNode = parentNode.childNode(0);
        assertEquals(childNode1, childNode);
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", baseUri, new Attributes());
        Node childNode1 = new Element("div", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        parentNode.removeChild(childNode1);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode2, childNodes.get(0));
    }
}