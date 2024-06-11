package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "my-class");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
    }

    @Test
    public void testConstructorWithEmptyArguments() {
        Node node = new Node();
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
        assertNull(node.baseUri());
        assertNull(node.parentNode());
    }

    @Test
    public void testConstructorWithoutBaseUriOrAttributes() {
        Node node = new Node();
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
        assertEquals("", node.baseUri());
        assertNull(node.parentNode());
    }

    @Test
    public void testGetNodeName() {
        Node node = new Element("p");
        assertEquals("p", node.nodeName());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "my-class");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("my-class", node.attr("class"));
        assertEquals("", node.attr("non-existent-attribute"));
        node.attr("new-attribute", "value");
        assertEquals("value", node.attr("new-attribute"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "my-class");
        Node node = new Element("div", baseUri, attributes);
        assertEquals(attributes, node.attributes());
        Attributes newAttributes = new Attributes();
        newAttributes.put("new-class", "new-value");
        node.attributes().putAll(newAttributes);
        assertEquals("new-value", node.attr("new-class"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "my-class");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "my-class");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri, new Attributes());
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://another-example.com");
        assertEquals("http://another-example.com", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", "", new Attributes());
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Element("a", baseUri, new Attributes().put("href", "relative.html"));
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
        assertEquals("", node.absUrl("non-existent-attribute"));
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", "", new Attributes());
        parentNode.addChild(childNode);
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", "", new Attributes());
        Node childNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(childNode1, childNode2), children);
    }

    @Test
    public void testChildNodesAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", "", new Attributes());
        Node childNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        Node[] childNodes = parentNode.childNodesAsArray();
        assertEquals(Arrays.asList(childNode1, childNode2).toArray(), childNodes);
    }

    @Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", "", new Attributes());
        parentNode.addChild(childNode);
        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", "", new Attributes());
        parentNode.addChild(childNode);
        assertEquals(parentNode, childNode.ownerDocument());
        if (parentNode instanceof Document) {
            Document document = (Document) parentNode;
            assertEquals(document, childNode.ownerDocument());
        } else {
            fail("parentNode is not a Document");
        }
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", "", new Attributes());
        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);
        assertEquals(0, parentNode.childNodes().size());
        assertEquals(null, childNode.parent());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode = new Element("p", "", new Attributes());
        Node newChildNode = new Element("p", "", new Attributes().put("class", "new-class"));
        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        assertEquals(newChildNode, parentNode.childNode(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Element("p", "", new Attributes());
        Node parentNode1 = new Element("div", baseUri, attributes);
        Node parentNode2 = new Element("div", baseUri, attributes);
        childNode.setParentNode(parentNode1);
        parentNode1.addChild(childNode);
        assertEquals(parentNode1, childNode.parent());
        childNode.setParentNode(parentNode2);
        assertEquals(parentNode2, childNode.parent());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode = new Element("p", "", new Attributes());
        Node newChildNode = new Element("p", "", new Attributes().put("class", "new-class"));
        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        assertEquals(newChildNode, parentNode.childNode(0));
        assertEquals(oldChildNode, parentNode.childNodes().get(1));
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", "", new Attributes());
        Node childNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        parentNode.removeChild(childNode1);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode2, parentNode.childNode(0));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", "", new Attributes());
        Node childNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        assertEquals(Arrays.asList(childNode1, childNode2), parentNode.childNodes());
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node newChildNode = new Element("p", "", new Attributes().put("class", "new-class"));
        Node childNode1 = new Element("p", "", new Attributes());
        parentNode.addChildren(1, newChildNode);
        parentNode.addChildren(childNode1);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(childNode1, newChildNode), children.subList(0, 2));
    }

    @Test
    public void testSiblingNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node siblingNode1 = new Element("p", "", new Attributes());
        Node nodeUnderTest = new Element("p", "", new Attributes());
        Node siblingNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(siblingNode1, nodeUnderTest, siblingNode2);
        List<Node> siblings = parentNode.siblingNodes();
        assertEquals(Arrays.asList(siblingNode1, nodeUnderTest, siblingNode2), siblings);
    }

    @Test
    public void testNextSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node siblingNode1 = new Element("p", "", new Attributes());
        Node nodeUnderTest = new Element("p", "", new Attributes());
        Node siblingNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(siblingNode1, nodeUnderTest, siblingNode2);
        assertEquals(siblingNode2, nodeUnderTest.nextSibling());
        assertEquals(null, nodeUnderTest.previousSibling());
    }

    @Test
    public void testPreviousSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node siblingNode1 = new Element("p", "", new Attributes());
        Node nodeUnderTest = new Element("p", "", new Attributes());
        Node siblingNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(siblingNode1, nodeUnderTest, siblingNode2);
        assertEquals(siblingNode1, nodeUnderTest.previousSibling());
        assertEquals(null, nodeUnderTest.nextSibling());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node siblingNode1 = new Element("p", "", new Attributes());
        Node nodeUnderTest = new Element("p", "", new Attributes());
        Node siblingNode2 = new Element("p", "", new Attributes());
        parentNode.addChildren(siblingNode1, nodeUnderTest, siblingNode2);
        nodeUnderTest.setSiblingIndex(1);
        List<Node> siblings = parentNode.siblingNodes();
        assertEquals(Arrays.asList(siblingNode1, nodeUnderTest, siblingNode2), siblings);
        assertEquals(siblingNode1, nodeUnderTest.previousSibling());
        assertEquals(siblingNode2, nodeUnderTest.nextSibling());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("p", baseUri, attributes).setText("Hello, world!");
        StringBuilder stringBuilder = new StringBuilder();
        node.outerHtml(stringBuilder);
        assertEquals("<p>" + node.outerHtml() + "</p>", stringBuilder.toString());
    }

    @Test
    public void testEquals() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node1 = new Element("p", baseUri, attributes).setText("Hello, world!");
        Node node2 = new Element("p", baseUri, attributes).setText("Hello, world!");
        Node node3 = new Element("p", baseUri, attributes).setText("Goodbye, world!");
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(""));
        assertFalse(node1.equals(null));
    }

    @Test
    public void testHashCode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node1 = new Element("p", baseUri, attributes).setText("Hello, world!");
        Node node2 = new Element("p", baseUri, attributes).setText("Hello, world!");
        Node node3 = new Element("p", baseUri, attributes).setText("Goodbye, world!");
        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();
        assertTrue(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node originalNode = new Element("p", baseUri, attributes).setText("Hello, world!");
        Node clonedNode = originalNode.clone();
        assertEquals(originalNode.nodeName(), clonedNode.nodeName());
        assertEquals(originalNode.attributes(), clonedNode.attributes());
        assertEquals(originalNode.baseUri(), clonedNode.baseUri());
        assertEquals(originalNode.childNodes(), clonedNode.childNodes());
        assertNotNull(clonedNode.parent());
        assertNull(clonedNode.parentNode());
    }
}