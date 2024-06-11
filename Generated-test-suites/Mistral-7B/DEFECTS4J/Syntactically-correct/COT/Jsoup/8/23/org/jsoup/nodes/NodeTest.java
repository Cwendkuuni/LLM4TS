package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class NodeTest {

    @Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "some-class");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals("some-class", node.attr("class"));
    }

    @Test
    public void testNodeWithOnlyBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeWithoutBaseUriOrAttributes() {
        Node node = new TextNode("");

        assertEquals("", node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "some-class");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("some-class", node.attr("class"));
        assertEquals("", node.attr("non-existent-attribute"));
    }

    @Test
    public void testGetAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "relative-url.html";
        Node node = new Element("a", baseUri, null);
        node.attributes().put("href", relativeUrl);

        String expectedAbsUrl = "http://example.com/relative-url.html";
        assertEquals(expectedAbsUrl, node.absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAbsUrlWithNullBaseUri() {
        String relativeUrl = "relative-url.html";
        Node node = new Element("a", null, null);
        node.attributes().put("href", relativeUrl);

        node.absUrl("href");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAbsUrlWithNullAttributeKey() {
        String baseUri = "http://example.com";
        Node node = new Element("a", baseUri, null);

        node.absUrl(null);
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.addChild(childNode);

        Node node = parentNode.childNode(0);
        assertEquals("p", node.nodeName());
        assertEquals(parentNode, node.parent());
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("span", baseUri, attributes);
        parentNode.addChild(childNode1);
        parentNode.addChild(childNode2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals("p", children.get(0).nodeName());
        assertEquals("span", children.get(1).nodeName());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node firstChild = new Element("p", baseUri, attributes);
        Node secondChild = new Element("span", baseUri, attributes);
        parentNode.addChild(firstChild);
        parentNode.addChild(secondChild);

        parentNode.removeChild(firstChild);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals("span", children.get(0).nodeName());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, attributes);
        Node newChild = new Element("span", baseUri, attributes);
        parentNode.addChild(oldChild);

        parentNode.replaceChild(newChild, oldChild);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals("span", children.get(0).nodeName());
        assertEquals(parentNode, newChild.parent());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);

        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("span", baseUri, attributes);
        Node child3 = new Element("a", baseUri, attributes);

        parentNode.addChildren(child1, child2, child3);
        List<Node> children = parentNode.childNodes();
        assertEquals(3, children.size());
        assertEquals("p", children.get(0).nodeName());
        assertEquals("span", children.get(1).nodeName());
        assertEquals("a", children.get(2).nodeName());
    }

}