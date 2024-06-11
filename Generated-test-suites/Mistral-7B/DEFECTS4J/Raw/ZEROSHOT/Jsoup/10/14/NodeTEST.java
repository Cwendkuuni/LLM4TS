package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.util.Collections;
import java.util.List;

/**
 * Test file for Node class
 */
public class NodeTest {

    /**
     * Test Node constructor with baseUri and attributes.
     */
    @org.junit.Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);

        assertEquals("node1", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertEquals(0, node.childNodes().size());
        assertNull(node.parentNode());
    }

    /**
     * Test Node constructor without baseUri, with attributes.
     */
    @org.junit.Test
    public void testNodeConstructorWithoutBaseUriAndWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("id", "node2");
        Node node = new Node();
        node.attributes(attributes);

        assertEquals("", node.nodeName());
        assertNull(node.baseUri());
        assertEquals(attributes, node.attributes());
        assertEquals(0, node.childNodes().size());
        assertNull(node.parentNode());
    }

    /**
     * Test Node constructor without baseUri or attributes.
     */
    @org.junit.Test
    public void testNodeConstructorWithoutBaseUriOrAttributes() {
        Node node = new Node();

        assertEquals("", node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.attributes());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.parentNode());
    }

    /**
     * Test attr method with existing attribute.
     */
    @org.junit.Test
    public void testAttrWithExistingAttribute() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node3");
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        assertEquals("test-class", node.attr("class"));
    }

    /**
     * Test attr method with non-existent attribute.
     */
    @org.junit.Test
    public void testAttrWithNonExistentAttribute() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        assertEquals("", node.attr("non-existent-attribute"));
    }

    /**
     * Test attr method with baseUrl prefix.
     */
    @org.junit.Test
    public void testAttrWithBaseUrlPrefix() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative-url.html");
        Node node = new Node(baseUri, attributes);

        assertEquals("https://example.com/relative-url.html", node.absUrl("href"));
    }

    /**
     * Test attributes method.
     */
    @org.junit.Test
    public void testAttributesMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node4");
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        List<String> attributesList = new ArrayList<>();
        attributesList.add("id");
        attributesList.add("class");

        assertEquals(attributesList, node.attributes().names());
    }

    /**
     * Test attr method with set and get attribute.
     */
    @org.junit.Test
    public void testAttrWithSetAndGetAttribute() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("id", "node5");
        assertEquals("node5", node.attr("id"));
    }

    /**
     * Test hasAttr method with existing attribute.
     */
    @org.junit.Test
    public void testHasAttrWithExistingAttribute() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node6");
        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
    }

    /**
     * Test hasAttr method with non-existent attribute.
     */
    @org.junit.Test
    public void testHasAttrWithNonExistentAttribute() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    /**
     * Test removeAttr method.
     */
    @org.junit.Test
    public void testRemoveAttrMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node7");
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    /**
     * Test baseUri method.
     */
    @org.junit.Test
    public void testBaseUriMethod() {
        String baseUri = "https://example.com";
        Node node = new Node(baseUri, new Attributes());

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setBaseUri method.
     */
    @org.junit.Test
    public void testSetBaseUriMethod() {
        String baseUri = "https://example.com";
        Node node = new Node();

        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test childNode method.
     */
    @org.junit.Test
    public void testChildNodeMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Element("div", attributes);
        parentNode.addChild(childNode);

        Node node = parentNode.childNode(0);
        assertEquals(childNode, node);
    }

    /**
     * Test childNodes method.
     */
    @org.junit.Test
    public void testChildNodesMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Element("div", attributes);
        Node childNode2 = new Element("div", attributes);
        parentNode.addChildren(childNode1, childNode2);

        List<Node> nodeList = parentNode.childNodes();
        assertEquals(2, nodeList.size());
        assertTrue(nodeList.contains(childNode1));
        assertTrue(nodeList.contains(childNode2));
    }

    /**
     * Test childNodesAsArray method.
     */
    @org.junit.Test
    public void testChildNodesAsArrayMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Element("div", attributes);
        Node childNode2 = new Element("div", attributes);
        parentNode.addChildren(childNode1, childNode2);

        Node[] nodeArray = parentNode.childNodesAsArray();
        assertEquals(2, nodeArray.length);
        assertTrue(nodeArray[0] instanceof Element);
        assertTrue(nodeArray[1] instanceof Element);
    }

    /**
     * Test parent method.
     */
    @org.junit.Test
    public void testParentMethod() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("body", parentAttributes);
        Node childNode = new Element("div", new Attributes().put("class", "test-class"));
        parentNode.addChild(childNode);

        Node node = childNode;
        assertEquals(parentNode, node.parent());
    }

    /**
     * Test ownerDocument method.
     */
    @org.junit.Test
    public void testOwnerDocumentMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node document = new Document(baseUri, new Attributes());
        Node childNode = new Element("div", attributes);
        document.addChild(childNode);

        Node node = childNode;
        assertEquals(document, node.ownerDocument());
    }

    /**
     * Test remove method.
     */
    @org.junit.Test
    public void testRemoveMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", attributes);
        Node childNode = new Element("div", new Attributes().put("class", "test-class"));
        parentNode.addChild(childNode);

        Node node = childNode;
        parentNode.removeChild(node);
        assertEquals(0, parentNode.childNodes().size());
        assertNull(node.parentNode());
    }

    /**
     * Test replaceWith method.
     */
    @org.junit.Test
    public void testReplaceWithMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", attributes);
        Node oldChildNode = new Element("div", new Attributes().put("class", "old-class"));
        Node newChildNode = new Element("div", new Attributes().put("class", "new-class"));
        parentNode.addChild(oldChildNode);

        Node node = oldChildNode;
        parentNode.replaceChild(node, newChildNode);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newChildNode, parentNode.childNode(0));
    }

    /**
     * Test setParentNode method.
     */
    @org.junit.Test
    public void testSetParentNodeMethod() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("body", parentAttributes);
        Node node = new Element("div", new Attributes().put("class", "test-class"));

        node.setParentNode(parentNode);
        assertEquals(parentNode, node.parentNode());
    }

    /**
     * Test replaceChild method.
     */
    @org.junit.Test
    public void testReplaceChildMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", attributes);
        Node oldChildNode = new Element("div", new Attributes().put("class", "old-class"));
        Node newChildNode = new Element("div", new Attributes().put("class", "new-class"));
        parentNode.addChild(oldChildNode);

        parentNode.replaceChild(oldChildNode, newChildNode);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newChildNode, parentNode.childNode(0));
    }

    /**
     * Test removeChild method.
     */
    @org.junit.Test
    public void testRemoveChildMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", attributes);
        Node oldChildNode = new Element("div", new Attributes().put("class", "old-class"));
        parentNode.addChild(oldChildNode);

        parentNode.removeChild(oldChildNode);
        assertEquals(0, parentNode.childNodes().size());
        assertNull(oldChildNode.parentNode());
    }

    /**
     * Test addChildren method.
     */
    @org.junit.Test
    public void testAddChildrenMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", attributes);
        Node childNode1 = new Element("div", attributes);
        Node childNode2 = new Element("div", attributes);

        parentNode.addChildren(childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().contains(childNode1));
        assertTrue(parentNode.childNodes().contains(childNode2));
    }

    /**
     * Test addChildren method with index.
     */
    @org.junit.Test
    public void testAddChildrenMethodWithIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("body", attributes);
        Node childNode1 = new Element("div", attributes);
        Node childNode2 = new Element("div", attributes);

        parentNode.addChildren(1, childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(1));
        assertEquals(childNode2, parentNode.childNode(2));
    }

    /**
     * Test outerHtml method.
     */
    @org.junit.Test
    public void testOuterHtmlMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node8");
        Node node = new Element("div", attributes);
        node.text("Test outerHTML node");

        StringBuilder sb = new StringBuilder();
        node.outerHtml(sb);

        String expected = "<div id=\"node8\">Test outerHTML node</div>";
        assertEquals(expected, sb.toString());
    }

    /**
     * Test equals method.
     */
    @org.junit.Test
    public void testEqualsMethod() {
        String baseUri = "https://example.com";
        Attributes attributes1 = new Attributes();
        attributes1.put("id", "node9");
        Attributes attributes2 = new Attributes();
        attributes2.put("id", "node9");

        Node node1 = new Node(baseUri, attributes1);
        Node node2 = new Node(baseUri, attributes2);

        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testCloneMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node10");
        Node node = new Element("div", attributes);
        node.text("Test cloned node");

        Node clonedNode = node.clone();

        assertEquals(node, clonedNode);
        assertNotSame(node, clonedNode);

        Node childNode = new Element("p", new Attributes().put("class", "test-class"));
        node.addChild(childNode);

        assertEquals(2, node.childNodes().size());
        assertEquals(1, clonedNode.childNodes().size());

        Node clonedChildNode = clonedNode.childNode(0);
        assertEquals(childNode, clonedChildNode);
        assertNotSame(childNode, clonedChildNode);

        assertNotSame(node, clonedChildNode);
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToStringMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node11");
        Node node = new Element("div", attributes);
        node.text("Test toString node");

        String expected = "<div id=\"node11\">Test toString node</div>";
        assertEquals(expected, node.toString());
    }
}