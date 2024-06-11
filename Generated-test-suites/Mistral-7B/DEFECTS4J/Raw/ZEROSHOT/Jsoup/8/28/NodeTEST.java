package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Node.java
 */
public class NodeTest {

    private Node testNode;
    private Document testDoc;

    @Before
    public void setup() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "test");
        testNode = new Element("div", baseUri, attributes);
        testDoc = new Document(baseUri);
        testDoc.body().appendElement(testNode);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", testNode.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("test", testNode.attr("id"));
        assertEquals("", testNode.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        assertEquals("id:test", testNode.attributes().toString());
    }

    @Test
    public void testSetAttr() {
        testNode.attr("class", "testClass");
        assertEquals("class:testClass", testNode.attributes().toString());
    }

    @Test
    public void testHasAttr() {
        assertTrue(testNode.hasAttr("id"));
        assertFalse(testNode.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        testNode.attr("class", "testClass");
        testNode.removeAttr("class");
        assertFalse(testNode.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, testNode.baseUri());
        testNode.setBaseUri("http://example2.com");
        assertEquals("http://example2.com", testNode.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String linkHref = "link.html";
        testNode.attr("href", linkHref);
        String absUrl = testNode.absUrl("href");
        String expectedAbsUrl = "http://example.com/link.html";
        assertEquals(expectedAbsUrl, absUrl);
    }

    @Test
    public void testAbsUrlRelativeToAbsUri() {
        String linkHref = "../link.html";
        testNode.baseUri("http://example2.com");
        testNode.attr("href", linkHref);
        String absUrl = testNode.absUrl("href");
        String expectedAbsUrl = "http://example2.com/link.html";
        assertEquals(expectedAbsUrl, absUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbsUrlMalformedUri() {
        String linkHref = "javascript:alert('Hello')";
        testNode.attr("href", linkHref);
        testNode.absUrl("href");
    }

    @Test
    public void testChildNode() {
        Element childElement = testNode.appendElement("p");
        Node expectedChildNode = childElement;
        Node actualChildNode = testNode.childNode(0);
        assertEquals(expectedChildNode, actualChildNode);
    }

    @Test
    public void testChildNodes() {
        Element pElement = testNode.appendElement("p");
        TextNode textNode = pElement.textNode();
        List<Node> expectedChildNodes = Arrays.asList(pElement, textNode);
        List<Node> actualChildNodes = testNode.childNodes();
        assertEquals(expectedChildNodes, actualChildNodes);
    }

    @Test
    public void testChildNodesAsArray() {
        Element pElement = testNode.appendElement("p");
        TextNode textNode = pElement.textNode();
        Node[] expectedChildNodes = {pElement, textNode};
        Node[] actualChildNodes = testNode.childNodesAsArray();
        assertArrayEquals(expectedChildNodes, actualChildNodes);
    }

    @Test
    public void testParent() {
        Element parentElement = testDoc.body();
        testNode.setParentNode(parentElement);
        Node expectedParentNode = parentElement;
        Node actualParentNode = testNode.parent();
        assertEquals(expectedParentNode, actualParentNode);
    }

    @Test
    public void testOwnerDocument() {
        Document expectedOwnerDocument = testDoc;
        Document actualOwnerDocument = testNode.ownerDocument();
        assertEquals(expectedOwnerDocument, actualOwnerDocument);
    }

    @Test
    public void testSetParentNode() {
        Element newParentElement = testDoc.createElement("body");
        testNode.setParentNode(newParentElement);
        assertEquals(newParentElement, testNode.parent());
    }

    @Test
    public void testReplaceChild() {
        Element newChildElement = testDoc.createElement("p");
        testNode.replaceChild(testNode.childNode(0), newChildElement);
        Node expectedNewChildNode = newChildElement;
        Node actualNewChildNode = testNode.childNode(0);
        assertEquals(expectedNewChildNode, actualNewChildNode);
    }

    @Test
    public void testRemoveChild() {
        Element childElement = testNode.appendElement("p");
        testNode.removeChild(childElement);
        List<Node> expectedChildNodes = Collections.emptyList();
        List<Node> actualChildNodes = testNode.childNodes();
        assertEquals(expectedChildNodes, actualChildNodes);
    }

    @Test
    public void testAddChildren() {
        Element newChildElement1 = testDoc.createElement("p");
        Element newChildElement2 = testDoc.createElement("p");
        testNode.addChildren(newChildElement1, newChildElement2);
        List<Node> expectedChildNodes = Arrays.asList(newChildElement1, newChildElement2);
        List<Node> actualChildNodes = testNode.childNodes();
        assertEquals(expectedChildNodes, actualChildNodes);
    }

    @Test
    public void testAddChildrenWithIndex() {
        Element newChildElement1 = testDoc.createElement("p");
        Element newChildElement2 = testDoc.createElement("p");
        testNode.addChildren(1, newChildElement1, newChildElement2);
        List<Node> expectedChildNodes = Arrays.asList(newChildElement1, testNode.childNode(0), newChildElement2);
        List<Node> actualChildNodes = testNode.childNodes();
        assertEquals(expectedChildNodes, actualChildNodes);
    }

    @Test
    public void testSiblingNodes() {
        Element siblingElement = testDoc.createElement("div");
        testNode.parentNode().appendChild(siblingElement);
        testNode.parentNode().insertBefore(testNode, siblingElement);
        List<Node> expectedSiblingNodes = Arrays.asList(siblingElement, testNode);
        List<Node> actualSiblingNodes = testNode.siblingNodes();
        assertEquals(expectedSiblingNodes, actualSiblingNodes);
    }

    @Test
    public void testNextSibling() {
        Element siblingElement = testDoc.createElement("div");
        testNode.parentNode().appendChild(siblingElement);
        testNode.parentNode().insertBefore(testNode, siblingElement);
        Node expectedNextSibling = siblingElement;
        Node actualNextSibling = testNode.nextSibling();
        assertEquals(expectedNextSibling, actualNextSibling);
    }

    @Test
    public void testPreviousSibling() {
        Element siblingElement = testDoc.createElement("div");
        testNode.parentNode().appendChild(testNode);
        testNode.parentNode().insertBefore(siblingElement, testNode);
        Node expectedPreviousSibling = siblingElement;
        Node actualPreviousSibling = testNode.previousSibling();
        assertEquals(expectedPreviousSibling, actualPreviousSibling);
    }

    @Test
    public void testSiblingIndex() {
        Element siblingElement = testDoc.createElement("div");
        testNode.parentNode().appendChild(testNode);
        testNode.parentNode().insertBefore(siblingElement, testNode);
        int expectedSiblingIndex = 0;
        int actualSiblingIndex = testNode.siblingIndex();
        assertEquals(expectedSiblingIndex, actualSiblingIndex);
    }

    @Test
    public void testSetSiblingIndex() {
        Element siblingElement1 = testDoc.createElement("div");
        Element siblingElement2 = testDoc.createElement("div");
        testNode.parentNode().appendChild(siblingElement1);
        testNode.parentNode().insertBefore(testNode, siblingElement1);
        testNode.parentNode().insertBefore(siblingElement2, testNode);
        testNode.setSiblingIndex(1);
        Node expectedNextSibling = siblingElement1;
        Node actualNextSibling = testNode.nextSibling();
        assertEquals(expectedNextSibling, actualNextSibling);
    }
}