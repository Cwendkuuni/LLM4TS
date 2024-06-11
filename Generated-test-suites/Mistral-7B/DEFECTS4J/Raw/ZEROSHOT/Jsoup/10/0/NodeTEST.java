package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Node abstract class.
 */
public class NodeTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    /**
     * Test constructor with baseUri and attributes.
     */
    @org.junit.Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.hasAttr("id"));
        assertEquals("elementId", node.attr("id"));
    }

    /**
     * Test constructor without baseUri.
     */
    @org.junit.Test
    public void testNodeWithoutBaseUri() {
        Node node = new TextNode("Test text");

        assertEquals("", node.baseUri());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    /**
     * Test default constructor.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Node node = new Node();

        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    /**
     * Test getNodeName method.
     */
    @org.junit.Test
    public void testgetNodeName() {
        Node node = new Element("div");

        assertEquals("div", node.nodeName());
    }

    /**
     * Test getAttribute method.
     */
    @org.junit.Test
    public void testGetAttribute() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        attributes.put("class", "elementClass");

        Node node = new Element("div", baseUri, attributes);

        assertEquals("elementId", node.attr("id"));
        assertEquals("elementClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttribute"));

        node.removeAttr("id");
        assertEquals("", node.attr("id"));
    }

    /**
     * Test getAllAttributes method.
     */
    @org.junit.Test
    public void testGetAllAttributes() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        attributes.put("class", "elementClass");

        Node node = new Element("div", baseUri, attributes);

        Attributes actual = node.attributes();

        assertEquals("id", actual.get("id"));
        assertEquals("class", actual.get("class"));
        assertEquals(2, actual.size());
        assertFalse(actual.hasKey("nonExistentAttribute"));
    }

    /**
     * Test setAttribute method.
     */
    @org.junit.Test
    public void testSetAttribute() {
        String baseUri = "http://example.com/";
        Node node = new Element("div", baseUri);

        node.attr("id", "newId");
        node.attr("class", "newClass");

        assertEquals("newId", node.attr("id"));
        assertEquals("newClass", node.attr("class"));
    }

    /**
     * Test hasAttribute method.
     */
    @org.junit.Test
    public void testHasAttribute() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node = new Element("div", baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    /**
     * Test removeAttribute method.
     */
    @org.junit.Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node = new Element("div", baseUri, attributes);

        node.removeAttr("id");

        assertFalse(node.hasAttr("id"));
    }

    /**
     * Test baseUri method.
     */
    @org.junit.Test
    public void testBaseUri() {
        String baseUri = "http://example.com/";
        Node node = new Element("div", baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setBaseUri method.
     */
    @org.junit.Test
    public void testSetBaseUri() {
        String originalBaseUri = "http://example.com/";
        Node node = new Element("div", originalBaseUri);

        node.setBaseUri("http://newexample.com/");

        assertEquals("http://newexample.com/", node.baseUri());
    }

    /**
     * Test absUrl method.
     */
    @org.junit.Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");

        Node node = new Element("a", baseUri, attributes);

        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    /**
     * Test childNode method.
     */
    @org.junit.Test
    public void testChildNode() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = new Element("p", baseUri);

        parentNode.addChild(childNode);

        Node retrievedChildNode = parentNode.childNode(0);

        assertEquals(childNode, retrievedChildNode);
    }

    /**
     * Test childNodes method.
     */
    @org.junit.Test
    public void testChildNodes() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);

        Node childNode1 = new Element("p", baseUri);
        Node childNode2 = new Element("span", baseUri);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> childNodes = parentNode.childNodes();

        assertEquals(2, childNodes.size());
        assertTrue(childNodes.contains(childNode1));
        assertTrue(childNodes.contains(childNode2));
    }

    /**
     * Test getChildNodesAsArray method.
     */
    @org.junit.Test
    public void testGetChildNodesAsArray() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);

        Node childNode1 = new Element("p", baseUri);
        Node childNode2 = new Element("span", baseUri);

        parentNode.addChildren(childNode1, childNode2);

        Node[] childNodesArray = parentNode.childNodesAsArray();

        assertEquals(2, childNodesArray.length);
       assertTrue(Arrays.asList(childNodesArray).contains(childNode1));
        assertTrue(Arrays.asList(childNodesArray).contains(childNode2));
    }

    /**
     * Test parent method.
     */
    @org.junit.Test
    public void testParent() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = new Element("p", baseUri);

        parentNode.addChild(childNode);

        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parent());
    }

    /**
     * Test remove method.
     */
    @org.junit.Test
    public void testRemove() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = new Element("p", baseUri);

        parentNode.addChild(childNode);

        parentNode.removeChild(childNode);

        assertFalse(parentNode.hasChild(childNode));
    }

    /**
     * Test replaceChild method.
     */
    @org.junit.Test
    public void testReplaceChild() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node oldChildNode = new Element("p", baseUri);
        Node newChildNode = new Element("span", baseUri);

        parentNode.addChild(oldChildNode);

        parentNode.replaceChild(newChildNode, oldChildNode);

        assertEquals(1, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().contains(newChildNode));
        assertFalse(parentNode.childNodes().contains(oldChildNode));
    }

    /**
     * Test setParentNode method.
     */
    @org.junit.Test
    public void testSetParentNode() {
        String baseUri = "http://example.com/";
        Node parentNode1 = new Element("div", baseUri);
        Node childNode = new Element("p", baseUri);

        childNode.setParentNode(parentNode1);

        assertEquals(parentNode1, childNode.parent());

        Node parentNode2 = new Element("div", baseUri);
        childNode.setParentNode(parentNode2);

        assertEquals(parentNode2, childNode.parent());
    }

    /**
     * Test replaceChild method with null child.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testReplaceChildWithNullChild() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = null;

        parentNode.replaceChild(childNode, childNode);
    }

    /**
     * Test addChildren method.
     */
    @org.junit.Test
    public void testAddChildren() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);

        Node childNode1 = new Element("p", baseUri);
        Node childNode2 = new Element("span", baseUri);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> childNodes = parentNode.childNodes();

        assertEquals(2, childNodes.size());
        assertTrue(childNodes.contains(childNode1));
        assertTrue(childNodes.contains(childNode2));
    }

    /**
     * Test addChildren method with empty array.
     */
    @org.junit.Test
    public void testAddChildrenWithEmptyArray() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);

        parentNode.addChildren();

        assertTrue(parentNode.childNodes().isEmpty());
    }

    /**
     * Test addChildren method with null child.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testAddChildrenWithNullChild() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = null;

        parentNode.addChildren(childNode);
    }

    /**
     * Test remove method with null child.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testRemoveWithNullChild() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = null;

        parentNode.removeChild(childNode);
    }

    /**
     * Test siblingIndex method.
     */
    @org.junit.Test
    public void testSiblingIndex() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node firstChild = new Element("p", baseUri);
        Node secondChild = new Element("span", baseUri);

        parentNode.addChildren(firstChild, secondChild);

        assertEquals(0, firstChild.siblingIndex());
        assertEquals(1, secondChild.siblingIndex());
    }

    /**
     * Test outerHtml method.
     */
    @org.junit.Test
    public void testOuterHtml() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = new Element("p", baseUri).text("Test text");

        parentNode.addChild(childNode);

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("<div>\n");
        expectedOutput.append("<p>Test text</p>\n");
        expectedOutput.append("</div>\n");

        StringBuilder actualOutput = new StringBuilder();
        parentNode.outerHtml(actualOutput);

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testClone() {
        String baseUri = "http://example.com/";
        Node parentNode = new Element("div", baseUri);
        Node childNode = new Element("p", baseUri).text("Test text");

        parentNode.addChild(childNode);

        Node clonedNode = parentNode.clone();

        assertEquals(parentNode.nodeName(), clonedNode.nodeName());
        assertEquals(parentNode.childNodes().size(), clonedNode.childNodes().size());

        List<Node> parentChildNodes = parentNode.childNodes();
        List<Node> clonedChildNodes = clonedNode.childNodes();

        for (int i = 0; i < parentChildNodes.size(); i++) {
            Node parentChildNode = parentChildNodes.get(i);
            Node clonedChildNode = clonedChildNodes.get(i);

            assertEquals(parentChildNode.nodeName(), clonedChildNode.nodeName());
            assertEquals(parentChildNode.text(), clonedChildNode.text());
        }
    }
}