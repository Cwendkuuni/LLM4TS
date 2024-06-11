package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeName() {
        // create a new Element node with nodeName "div"
        Element element = new Element("div");
        // assert that the node name is "div"
        assertEquals("div", element.nodeName());

        // create a new DocumentType node with nodeName "doctype"
        DocumentType documentType = new DocumentType("doctype");
        // assert that the node name is "doctype"
        assertEquals("doctype", documentType.nodeName());

        // create a new TextNode node with nodeName "#text"
        TextNode textNode = new TextNode("#text");
        // assert that the node name is "#text"
        assertEquals("#text", textNode.nodeName());
    }

    @Test
    public void testAttr() {
        // create a new Element node with attributes
        Element element = new Element("div");
        element.attr("id", "demo");
        element.attr("class", "container");

        // assert that the attribute values are correct
        assertEquals("demo", element.attr("id"));
        assertEquals("container", element.attr("class"));
        assertEquals("", element.attr("data-name"));

        // create a new DocumentType node
        DocumentType documentType = new DocumentType("doctype");

        // assert that there are no attributes
        assertEquals("", documentType.attr("id"));
        assertEquals("", documentType.attr("class"));
        assertEquals("", documentType.attr("data-name"));

        // create a new TextNode node
        TextNode textNode = new TextNode("#text");

        // assert that there are no attributes
        assertEquals("", textNode.attr("id"));
        assertEquals("", textNode.attr("class"));
        assertEquals("", textNode.attr("data-name"));
    }

    @Test
    public void testHasAttr() {
        // create a new Element node with attributes
        Element element = new Element("div");
        element.attr("id", "demo");
        element.attr("class", "container");

        // assert that the element has the attributes
        assertTrue(element.hasAttr("id"));
        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr("data-name"));

        // create a new DocumentType node
        DocumentType documentType = new DocumentType("doctype");

        // assert that the document type has no attributes
        assertFalse(documentType.hasAttr("id"));
        assertFalse(documentType.hasAttr("class"));
        assertFalse(documentType.hasAttr("data-name"));

        // create a new TextNode node
        TextNode textNode = new TextNode("#text");

        // assert that the text node has no attributes
        assertFalse(textNode.hasAttr("id"));
        assertFalse(textNode.hasAttr("class"));
        assertFalse(textNode.hasAttr("data-name"));
    }

    @Test
    public void testBaseUri() {
        // create a new Element node with base URI
        Element element = new Element("div");
        element.setBaseUri("http://www.example.com");

        // assert that the base URI is correct
        assertEquals("http://www.example.com", element.baseUri());

        // create a new DocumentType node
        DocumentType documentType = new DocumentType("doctype");

        // assert that the base URI is empty
        assertEquals("", documentType.baseUri());

        // create a new TextNode node
        TextNode textNode = new TextNode("#text");

        // assert that the base URI is empty
        assertEquals("", textNode.baseUri());
    }

    // Add more test cases for other methods in the Node class

}