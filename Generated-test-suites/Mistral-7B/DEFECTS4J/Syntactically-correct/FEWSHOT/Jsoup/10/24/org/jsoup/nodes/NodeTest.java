package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        assertEquals("testNode", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
        assertNotNull(node.attributes());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithNullAttributes() {
        String baseUri = "http://test.com";
        Node node = new Node(baseUri);
        assertEquals(baseUri, node.baseUri());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
        assertNull(node.attributes());
    }

    @Test
    public void testDefaultNodeConstructor() {
        Node node = new Node();
        assertNull(node.baseUri());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeName() {
        Node node = new Node() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        assertEquals("testNode", node.nodeName());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        assertEquals("testNode", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttr"));
        assertEquals(baseUri, node.absUrl("testUri"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Node("", attributes);
        assertEquals(attributes, node.attributes());
        assertEquals("testNode", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttr"));

        attributes.put("class", "testClass");
        assertEquals("testClass", node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://test.com";
        Node node = new Node(baseUri, new Attributes());
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "http://newtest.com";
        Node node = new Node("", new Attributes());
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://test.com";
        String relativeUrl = "testRelative.html";
        Node node = new Node(baseUri, new Attributes());
        assertEquals("", node.absUrl(relativeUrl));
        Node relativeNode = new Node("", new Attributes());
        relativeNode.attributes().put("href", relativeUrl);
        node.addChild(relativeNode);
        assertEquals("http://test.com/testRelative.html", node.absUrl(relativeUrl));
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node("", new Attributes());
        parentNode.addChild(childNode);
        Node retrievedChildNode = parentNode.childNode(0);
        assertSame(childNode, retrievedChildNode);
    }

    @Test
    public void testChildren() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node("", new Attributes());
        Node childNode2 = new Node("", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        List<Node> expectedChildren = new ArrayList<>(Arrays.asList(childNode1, childNode2));
        assertEquals(expectedChildren, children);
        Node retrievedChildNode1 = children.get(0);
        Node retrievedChildNode2 = children.get(1);
        assertSame(childNode1, retrievedChildNode1);
        assertSame(childNode2, retrievedChildNode2);
    }

    @Test
    public void testChildrenAsArray() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node("", new Attributes());
        Node childNode2 = new Node("", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        Node[] childrenAsArray = parentNode.childNodesAsArray();
        Node[] expectedChildrenAsArray = new Node[] {childNode1, childNode2};
        assertArrayEquals(expectedChildrenAsArray, childrenAsArray);
    }

    @Test
    public void testParent() {
        String baseUri = "http://test.com";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "testParent");
        Node parentNode = new Node(baseUri, parentAttributes);
        Node childNode = new Node("", new Attributes());
        parentNode.addChild(childNode);
        Node retrievedParentNode = childNode.parent();
        assertSame(parentNode, retrievedParentNode);
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        assertEquals(node, node.ownerDocument());
        Document document = new Document(baseUri);
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node("", new Attributes());
        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(Collections.emptyList(), children);
        childNode.setParentNode(parentNode);
        parentNode.addChild(childNode);
        children = parentNode.childNodes();
        assertEquals(Arrays.asList(childNode), children);
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node("", new Attributes());
        Node newChildNode = new Node("", new Attributes());
        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(newChildNode), children);
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://test.com";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "testParent");
        Node parentNode = new Node(baseUri, parentAttributes);
        Node childNode = new Node("", new Attributes());
        childNode.setParentNode(parentNode);
        parentNode.addChild(childNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(childNode), children);
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node("", new Attributes());
        Node newChildNode = new Node("", new Attributes());
        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(newChildNode), children);
        assertEquals(parentNode, newChildNode.parentNode());
        assertEquals(oldChildNode, newChildNode.nextSibling());
        assertEquals(newChildNode, oldChildNode.previousSibling());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node("", new Attributes());
        Node newChildNode = new Node("", new Attributes());
        parentNode.addChild(oldChildNode);
        parentNode.addChild(newChildNode);
        parentNode.removeChild(oldChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(newChildNode), children);
        assertNull(oldChildNode.parentNode());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node("", new Attributes());
        Node childNode2 = new Node("", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        List<Node> expectedChildren = new ArrayList<>(Arrays.asList(childNode1, childNode2));
        assertEquals(expectedChildren, children);
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node("", new Attributes());
        Node childNode2 = new Node("", new Attributes());
        parentNode.addChildren(1, childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        List<Node> expectedChildren = new ArrayList<>(Arrays.asList(childNode1, childNode2));
        assertEquals(expectedChildren, children);
        Node retrievedChildNode1 = children.get(0);
        Node retrievedChildNode2 = children.get(1);
        assertSame(childNode1, retrievedChildNode1);
        assertSame(childNode2, retrievedChildNode2);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node("", new Attributes());
        parentNode.addChild(childNode);
        StringBuilder accumulator = new StringBuilder();
        parentNode.outerHtml(accumulator);
        String expectedHtml = "<html><head></head><body>" +
                "<node id=\"testNode\">" +
                "<child></child>" +
                "</node>" +
                "</body></html>";
        assertEquals(expectedHtml, accumulator.toString());
    }

    @Test
    public void testOuterHtmlWithTextChild() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node parentNode = new Node(baseUri, attributes);
        TextNode textChild = new TextNode("Test text");
        parentNode.addChild(textChild);
        StringBuilder accumulator = new StringBuilder();
        parentNode.outerHtml(accumulator);
        String expectedHtml = "<html><head></head><body>" +
                "<node id=\"testNode\">Test text</node>" +
                "</body></html>";
        assertEquals(expectedHtml, accumulator.toString());
    }

    @Test
    public void testOuterHtmlWithNestedChildren() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node("", new Attributes());
        Node child2 = new Node("", new Attributes());
        parentNode.addChild(child1);
        child1.addChild(child2);
        StringBuilder accumulator = new StringBuilder();
        parentNode.outerHtml(accumulator);
        String expectedHtml = "<html><head></head><body>" +
                "<node id=\"testNode\">" +
                "<node></node>" +
                "</node>" +
                "</body></html>";
        assertEquals(expectedHtml, accumulator.toString());
    }

    @Test
    public void testOuterHtmlWithCustomIndentation() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setIndentAmount(4);
        Node parentNode = new Node(baseUri, attributes);
        StringBuilder accumulator = new StringBuilder();
        parentNode.outerHtml(accumulator, 2, outputSettings);
        String expectedHtml = "\n<html>\n<head>\n</head>\n<body>\n" +
                "<node id=\"testNode\">\n</node>\n</body>\n</html>";
        assertEquals(expectedHtml, accumulator.toString());
    }
}