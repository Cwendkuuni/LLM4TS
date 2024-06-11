package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertEquals("elementId", node.attr("id"));
        assertEquals(Collections.emptyList(), node.childNodes());
        assertEquals(node, node.parent());
        assertEquals(null, node.ownerDocument());
    }

    @Test
    public void testNodeCreationWithoutBaseUri() {
        Attributes attributes = new Attributes();
        Node node = new Element("div", attributes);
        assertEquals("div", node.nodeName());
        assertEquals(null, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertEquals(null, node.parent());
        assertEquals(null, node.ownerDocument());
    }

    @Test
    public void testNodeCreationWithoutAttributes() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(new Attributes(), node.attributes());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertEquals(node, node.parent());
        assertEquals(null, node.ownerDocument());
    }

    @Test
    public void testNodeCreationWithoutBaseUriOrAttributes() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
        assertEquals(null, node.baseUri());
        assertEquals(null, node.attributes());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertEquals(null, node.parent());
        assertEquals(null, node.ownerDocument());
    }

    @Test
    public void testNodeCreationWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new TextNode("Hello World");
        parentNode.appendChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode, childNodes.get(0));
        assertEquals(parentNode, childNode.parent());
        assertEquals(parentNode, childNode.ownerDocument());
    }

    @Test
    public void testNodeCreateWithExistingChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element parentNode = new Element("div", baseUri, attributes);
        TextNode childNode1 = new TextNode("Hello World 1");
        TextNode childNode2 = new TextNode("Hello World 2");
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
        assertEquals(parentNode, childNode1.parent());
        assertEquals(parentNode, childNode2.parent());
        assertEquals(parentNode, childNode1.ownerDocument());
        assertEquals(parentNode, childNode2.ownerDocument());
    }

    @Test
    public void testNodeSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element parentNode = new Element("div", baseUri, attributes);
        Element childNode = new Element("p", baseUri, attributes);
        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parent());
        assertEquals(parentNode, childNode.ownerDocument());
    }

    @Test
    public void testNodeReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element parentNode = new Element("div", baseUri, attributes);
        TextNode oldChildNode = new TextNode("Old Text");
        TextNode newChildNode = new TextNode("New Text");
        parentNode.appendChild(oldChildNode);
        parentNode.replaceChild(newChildNode, oldChildNode);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(newChildNode, childNodes.get(0));
        assertEquals(parentNode, newChildNode.parent());
        assertEquals(parentNode, newChildNode.ownerDocument());
    }

    @Test
    public void testNodeRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element parentNode = new Element("div", baseUri, attributes);
        TextNode childNode = new TextNode("Child Text");
        parentNode.appendChild(childNode);
        parentNode.removeChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());
        assertEquals(null, childNode.parent());
        assertEquals(null, childNode.ownerDocument());
    }

    @Test
    public void testNodeAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element parentNode = new Element("div", baseUri, attributes);
        Element childNode1 = new Element("p", baseUri, attributes);
        Element childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
        assertEquals(parentNode, childNode1.parent());
        assertEquals(parentNode, childNode2.parent());
        assertEquals(parentNode, childNode1.ownerDocument());
        assertEquals(parentNode, childNode2.ownerDocument());
    }

    @Test
    public void testNodeAbsUrl() {
        String baseUri = "http://example.com";
        String attributeValue = "http://example.org/image.jpg";
        Attributes attributes = new Attributes();
        attributes.put("src", attributeValue);
        Element node = new Element("img", baseUri, attributes);
        String absUrl = node.absUrl("src");
        assertEquals(new URL("http://example.org/image.jpg"), new URL(absUrl));
    }

    @Test
    public void testNodeAbsUrlInvalidUrl() {
        String baseUri = "http://example.com";
        String attributeValue = "image.jpg";
        Attributes attributes = new Attributes();
        attributes.put("src", attributeValue);
        Element node = new Element("img", baseUri, attributes);
        String absUrl = node.absUrl("src");
        assertEquals("", absUrl);
    }

    @Test
    public void testNodeAbsUrlEmptyAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element node = new Element("a", baseUri, attributes);
        String absUrl = node.absUrl("href");
        assertEquals("", absUrl);
    }

    @Test
    public void testNodeHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Element node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testNodeAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Element node = new Element("div", baseUri, attributes);
        assertEquals("elementId", node.attr("id"));
        assertEquals("", node.attr("class"));
    }

    @Test
    public void testNodeAttrWithPrefix() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "http://example.org");
        Element node = new Element("a", baseUri, attributes);
        assertEquals(new URL("http://example.org"), node.absUrl("abs:href"));
    }

    @Test
    public void testNodeSetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Element node = new Element("div", baseUri, attributes);
        node.attr("class", "example");
        assertEquals("example", node.attr("class"));
    }

    @Test
    public void testNodeRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Element node = new Element("div", baseUri, attributes);
        node.removeAttr("class");
        assertEquals(new Attributes(), node.attributes());
    }
}