package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {
    // Test constructor

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Node(baseUri, attributes);

        assertEquals("example", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertFalse(node.hasAttr("nonExistent"));
        assertEquals("", node.attr("nonExistent"));
        assertTrue(node.hasAttr("class"));
        assertEquals("example", node.attr("class"));
    }

    @Test
    public void testNodeConstructorWithoutBaseUriAndAttributes() {
        Node node = new Node();

        assertTrue(childNodes.isEmpty());
        assertNull(node.attributes());
        assertNull(node.baseUri());
    }

    // Test getters and setters

    @Test
    public void testGettersAndSettersBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());

        assertEquals(baseUri, node.baseUri());

        String newBaseUri = "http://another-example.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testGettersAndSettersAttributes() {
        Node node = new Node();

        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        node.setAttributes(attributes);
        assertEquals(attributes, node.attributes());

        Attributes newAttributes = new Attributes();
        newAttributes.put("class", "newExample");
        node.setAttributes(newAttributes);
        assertEquals(newAttributes, node.attributes());

        assertFalse(node.hasAttr("nonExistent"));
        assertEquals("", node.attr("nonExistent"));
        assertTrue(node.hasAttr("class"));
        assertEquals("newExample", node.attr("class"));
    }

    @Test
    public void testGettersAndSettersChildren() {
        Node parentNode = new Node();
        Node node1 = new Element("div");
        Node node2 = new Element("p");

        parentNode.addChildren(node1, node2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(node1, children.get(0));
        assertEquals(node2, children.get(1));

        Node firstChild = parentNode.childNode(0);
        assertEquals(node1, firstChild);

        Node lastChild = parentNode.childNode(1);
        assertEquals(node2, lastChild);

        Node removedChild = parentNode.removeChild(node1);
        children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(node2, children.get(0));

        parentNode.addChildren(node1);
        children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(node1, children.get(0));
        assertEquals(node2, children.get(1));
    }

    // Test attributes

    @Test
    public void testAttributesSetAndGet() {
        Node node = new Node();

        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        node.setAttributes(attributes);
        assertEquals(attributes, node.attributes());
        assertEquals("example", node.attr("class"));

        attributes.remove("class");
        node.setAttributes(attributes);
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAttributesSetAndGetWithNullAttributes() {
        Node node = new Node();

        node.setAttributes(null);
        assertNull(node.attributes());
        assertFalse(node.hasAttr("nonExistent"));
        assertEquals("", node.attr("nonExistent"));
    }

    @Test
    public void testAttributesSetAndGetWithEmptyAttributes() {
        Node node = new Node();

        Attributes attributes = new Attributes();
        node.setAttributes(attributes);
        assertNull(node.attributes());
        assertFalse(node.hasAttr("nonExistent"));
        assertEquals("", node.attr("nonExistent"));
    }

    @Test
    public void testAttributesSetAndGetWithAbsUrl() {
        Node node = new Node();
        String baseUri = "http://example.com";

        Attributes attributes = new Attributes();
        attributes.put("abs:href", "relative.html");
        node.setAttributes(attributes);
        URL absUrl = node.absUrl("href");
        assertEquals("http://example.com/relative.html", absUrl.toExternalForm());
    }

    // Test exceptions

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBaseUri() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullAttributes() {
        new Node("http://example.com", null);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlWithMalformedBaseUri() {
        Node node = new Node();
        node.setBaseUri("invalid");
        String attributeKey = "abs:href";
        String expectedErrorMessage = "java.net.MalformedURLException: invalid";
        String result = node.absUrl(attributeKey);
        assertEquals("", result);
        try {
            node.absUrl(attributeKey);
        } catch (MalformedURLException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAddChildrenWithNullChildren() {
        Node node = new Node();
        node.addChildren(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveChildWithInvalidIndex() {
        Node node = new Element("div");
        node.addChildren(new Element("p"));
        node.removeChild(1);
    }

    // Test inner HTML

    @Test
    public void testOuterHtml() {
        Node node = new Element("div");
        node.outerHtml("test");

        assertEquals("<div></div>", node.outerHtml());

        Node childNode = new Element("p");
        childNode.outerHtml("child");

        node.addChildren(childNode);

        assertEquals("<div><p>child</p></div>", node.outerHtml());
    }
}

// 
// 