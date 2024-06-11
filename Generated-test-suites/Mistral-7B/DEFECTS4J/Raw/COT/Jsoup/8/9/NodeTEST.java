package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test class for Node.java
 */
public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEmpty(node.childNodes());
    }

    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parentNode());
        assertEmpty(node.childNodes());
    }

    @Test
    public void testDefaultNodeConstructor() {
        Node node = new Node();
        assertEmpty(node.childNodes());
        assertNull(node.parentNode());
        assertNull(node.attributes());
    }

    @Test
    public void testgetNodeName() {
        // Since Node is an abstract class, we cannot test its nodeName() method directly.
        // Instead, we can test the behavior of the class by creating instances of other classes that extend Node
        Element element = new Element("div"); // Assume Element is a class that extends Node
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testAttrMethodWithValidAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        assertEquals("testNode", node.attr("id"));
    }

    @Test
    public void testAttrMethodWithInvalidAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        assertEquals("", node.attr("invalidKey"));
    }

    @Test
    public void testAttrMethodWithAbsUrlAttributeKey() {
        String baseUri = "http://example.com";
        String absoluteUrl = "http://example.com/test.html";
        Attributes attributes = new Attributes().put("abs:href", absoluteUrl);
        Node node = new Node(baseUri, attributes);
        assertEquals(absoluteUrl, node.absUrl("href"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttrMethodWithNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr(null);
    }

    @Test
    public void testAttributesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "testNode").put("class", "testClass");
        Node node = new Node(baseUri, attributes);
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testSetAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("id", "testNode");
        assertEquals("testNode", node.attr("id"));
        assertEquals(1, node.attributes().size());
    }

    @Test
    public void testHasAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("unknown"));
    }

    @Test
    public void testRemoveAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testBaseUriMethod() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUriMethod() {
        String baseUri = "http://example.com";
        Node node = new Node();
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriMethodWithNullBaseUri() {
        Node node = new Node();
        node.setBaseUri(null);
    }

    @Test
    public void testAbsUrlMethodWithValidAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("href", "test.html");
        Node node = new Node(baseUri, attributes);
        assertEquals("http://example.com/test.html", node.absUrl("href"));
    }

    @Test(expected = NullPointerException.class)
    public void testAbsUrlMethodWithNullAttributeKey() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        node.absUrl(null);
    }

    @Test
    public void testChildNodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);
        parentNode.addChild(childNode);
        Node retrievedChildNode = parentNode.childNode(0);
        assertEquals(childNode, retrievedChildNode);
    }

    @Test
    public void testChildrenMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testRemoveChildMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        parentNode.removeChild(childNode1);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(childNode2, children.get(0));
    }

    @Test
    public void testReplaceChildMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);
        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
    }

    @Test
    public void testSetParentNodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testRemoveMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node nodeToRemove = new Node(baseUri, attributes);
        parentNode.addChild(nodeToRemove);
        parentNode.remove();
        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWithMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);
        parentNode.addChild(oldChildNode);
        parentNode.replaceWith(newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
    }

    @Test
    public void testOwnerDocumentMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        Document document = new Document(baseUri);
        document.setBody(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testSetSiblingIndexMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node sibling1 = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChildren(sibling1, sibling2);
        sibling1.setSiblingIndex(0);
        sibling2.setSiblingIndex(1);
        List<Node> siblings = parentNode.siblingNodes();
        assertEquals(2, siblings.size());
        assertEquals(sibling1, siblings.get(0));
        assertEquals(sibling2, siblings.get(1));
    }

    @Test
    public void testNextSiblingMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node sibling1 = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChildren(sibling1, sibling2);
        sibling1.setSiblingIndex(0);
        sibling2.setSiblingIndex(1);
        Node nextSibling = sibling1.nextSibling();
        assertNull(nextSibling);
    }

    @Test
    public void testPreviousSiblingMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node sibling1 = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChildren(sibling1, sibling2);
        sibling1.setSiblingIndex(0);
        sibling2.setSiblingIndex(1);
        Node previousSibling = sibling2.previousSibling();
        assertNull(previousSibling);
    }

    @Test
    public void testOuterHtmlMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "testNode");
        StringBuilder accumulator = new StringBuilder();
        Node node = new Element("div", attributes).outerHtml(accumulator, 0, null);
        assertEquals("<div id=\"testNode\"></div>", node.outerHtml());
    }

    @Test
    public void testInnerHtmlMethod() {
        // Since Node is an abstract class, we cannot test its innerHtml() method directly.
        // Instead, we can test the behavior of the class by creating instances of other classes that extend Node
        Element element = new Element("div"); // Assume Element is a class that extends Node
        element.appendText("Test inner HTML");
        assertEquals("Test inner HTML", element.innerHtml());
    }

}