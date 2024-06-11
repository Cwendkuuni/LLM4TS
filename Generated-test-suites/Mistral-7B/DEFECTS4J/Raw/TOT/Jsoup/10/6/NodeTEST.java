package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";

        // Test empty Node constructor
        Node emptyNode = new Node();
        assertEquals("", emptyNode.nodeName());
        assertNull(emptyNode.attributes());
        assertNull(emptyNode.baseUri());
        assertEquals(0, emptyNode.childNodes().size());

        // Test Node constructor with baseUri and attributes
        Node nodeWithBaseUriAndAttributes = new Node(baseUri, new AttributesImpl());
        assertEquals(baseUri, nodeWithBaseUriAndAttributes.baseUri());
        assertEquals(1, nodeWithBaseUriAndAttributes.childNodes().size());
        assertNull(nodeWithBaseUriAndAttributes.childNodes().get(0));
        assertEquals(baseUri, nodeWithBaseUriAndAttributes.absUrl("nodeName"));

        // Test Node constructor without baseUri and with attributes
        Node nodeWithoutBaseUriAndAttributes = new Node(new AttributesImpl());
        assertNull(nodeWithoutBaseUriAndAttributes.baseUri());
        assertEquals(0, nodeWithoutBaseUriAndAttributes.childNodes().size());
        assertNull(nodeWithoutBaseUriAndAttributes.attributes());

        // Test Node constructor without baseUri or attributes
        Node nodeWithoutBaseUriOrAttributes = new Node();
        assertNull(nodeWithoutBaseUriOrAttributes.baseUri());
        assertEquals(0, nodeWithoutBaseUriOrAttributes.childNodes().size());
        assertNull(nodeWithoutBaseUriOrAttributes.attributes());

        // Test setting baseUri and attributes
        Node nodeWithBaseUriAndAttributesSet = new Node(baseUri, new AttributesImpl());
        nodeWithBaseUriAndAttributesSet.setBaseUri("http://example.org");
        nodeWithBaseUriAndAttributesSet.attr("test", "value");
        assertEquals("http://example.org", nodeWithBaseUriAndAttributesSet.baseUri());
        assertEquals("value", nodeWithBaseUriAndAttributesSet.attr("test"));

        // Test getting and setting attribute values
        Node nodeWithAttribute = new Element("div");
        nodeWithAttribute.attr("test", "value");
        assertEquals("value", nodeWithAttribute.attr("test"));
        nodeWithAttribute.attr("test", "new value");
        assertEquals("new value", nodeWithAttribute.attr("test"));

        // Test getting non-existent attribute
        Node nodeWithoutAttribute = new Element("div");
        assertEquals("", nodeWithoutAttribute.attr("test"));

        // Test removing attribute
        Node nodeWithRemovedAttribute = new Element("div").attr("test", "value");
        nodeWithRemovedAttribute.removeAttr("test");
        assertEquals("", nodeWithRemovedAttribute.attr("test"));

        // Test getting baseUri and setting it to a malformed URL
        Node nodeWithMalformedBaseUri = new Node("http://example.com", new AttributesImpl());
        try {
            nodeWithMalformedBaseUri.setBaseUri("invalid://example.com");
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // expected
        }

        // Test getting and setting baseUri with a valid URL
        Node nodeWithValidBaseUri = new Node("http://example.com", new AttributesImpl());
        Node nodeWithNewBaseUri = new Node("http://example.org");
        nodeWithNewBaseUri.setBaseUri(nodeWithValidBaseUri.baseUri());
        assertEquals("http://example.com", nodeWithValidBaseUri.baseUri());
        assertEquals("http://example.org", nodeWithNewBaseUri.baseUri());

        // Test getting and setting childNodes
        Node parentNode = new Element("div");
        Node childNode = new Element("p");
        parentNode.appendChild(childNode);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(parentNode, childNode.parent());
        Node newChildNode = new Element("span");
        parentNode.appendChild(newChildNode);
        assertEquals(2, parentNode.childNodes().size());

        // Test getting childNode by index
        Node thirdChildNode = parentNode.childNode(1);
        assertEquals("p", thirdChildNode.nodeName());
        Node fourthChildNode = parentNode.childNode(2);
        assertEquals("span", fourthChildNode.nodeName());

        // Test getting empty childNodes
        Node emptyParentNode = new Element("div");
        assertEquals(0, emptyParentNode.childNodes().size());

        // Test getting unmodifiable childNodes list
        assertEquals(Collections.unmodifiableList(parentNode.childNodes()), parentNode.childNodes());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String attributeValue = "http://example.org/path";

        // Test relative URL with no baseUri
        Node nodeWithoutBaseUri = new Element("div");
        assertEquals("", nodeWithoutBaseUri.absUrl("attribute"));

        // Test relative URL with baseUri
        Node nodeWithBaseUri = new Element("div").attr("baseUri", baseUri);
        assertEquals(baseUri + "/path", nodeWithBaseUri.absUrl("attribute"));

        // Test absolute URL with no baseUri
        Node nodeWithAbsoluteUrl = new Element("div").attr("attribute", attributeValue);
        assertEquals(attributeValue, nodeWithAbsoluteUrl.absUrl("attribute"));

        // Test absolute URL with baseUri
        Node nodeWithBaseUriAndAbsoluteUrl = new Element("div").attr("baseUri", baseUri).attr("attribute", attributeValue);
        assertEquals(baseUri + "/path", nodeWithBaseUriAndAbsoluteUrl.absUrl("attribute"));

        // Test empty attribute
        Node nodeWithEmptyAttribute = new Element("div").attr("attribute", "");
        assertEquals("", nodeWithEmptyAttribute.absUrl("attribute"));

        // Test invalid URL in attribute
        Node nodeWithInvalidUrlAttribute = new Element("div").attr("attribute", "invalid://example.com");
        assertEquals("", nodeWithInvalidUrlAttribute.absUrl("attribute"));
    }
}