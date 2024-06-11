package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeWithoutAttributesConstructor() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals("http://example.com", node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testDefaultNodeConstructor() {
        Node node = new Node();
        assertNull(node.baseUri());
        assertNull(node.attributes());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeName() {
        // abstract method, can't test directly
    }

    @Test
    public void testAttrMethod() {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        String attributeValue = "example";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Node(baseUri, attributes);
        assertEquals(attributeValue, node.attr(attributeKey));
    }

    @Test
    public void testAttrMethodWithEmptyString() {
        String baseUri = "http://example.com";
        String attributeKey = "href";
        Node node = new Node(baseUri, new Attributes());
        assertEquals("", node.attr(attributeKey));
    }

    @Test
    public void testAttrMethodWithAbsoluteUrlAttribute() {
        String baseUri = "http://example.com";
        String absoluteAttributeValue = "http://example.com/about";
        Attributes attributes = new Attributes();
        attributes.put("href", absoluteAttributeValue);
        Node node = new Node(baseUri, attributes);
        assertEquals(absoluteAttributeValue, node.absUrl("href"));
    }

    @Test
    public void testAttrMethodWithRelativeUrlAttribute() {
        String baseUri = "http://example.com/index.html";
        String relativeAttributeValue = "contact.html";
        Attributes attributes = new Attributes();
        attributes.put("href", relativeAttributeValue);
        Node node = new Node(baseUri, attributes);
        assertEquals("http://example.com/contact.html", node.absUrl("href"));
    }

    @Test
    public void testAttrMethodWithNonExistentAttribute() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        assertEquals("", node.attr("nonexistentattribute"));
    }

    @Test
    public void testAttributesMethod() {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        String attributeValue = "example";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Node(baseUri, attributes);
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testAttributesMethodWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        assertEquals(Collections.emptyMap(), node.attributes());
    }

    @Test
    public void testHasAttrMethod() {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        String attributeValue = "example";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr(attributeKey));
    }

    @Test
    public void testHasAttrMethodWithNonExistentAttribute() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        assertFalse(node.hasAttr("nonexistentattribute"));
    }

    @Test
    public void testRemoveAttrMethod() {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        String attributeValue = "example";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Node(baseUri, attributes);
        node.removeAttr(attributeKey);
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testSetBaseUriMethod() {
        String baseUri = "http://example.com";
        String newBaseUri = "http://jsoup.org";
        Node node = new Node(baseUri, new Attributes());
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriWithNullBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        node.setBaseUri(null);
    }

    @Test
    public void testGetChildNodeByIndex() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(childNode1, childNode2), children);
    }

    @Test
    public void testChildNodesAsArray() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        Node[] children = parentNode.childNodesAsArray();
        assertEquals(Arrays.asList(childNode1, childNode2).toArray(), children);
    }

    @Test
    public void testGetParentNode() {
        Node childNode = new Node();
        Node parentNode = new Node();
        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testGetParentNodeIsNull() {
        Node node = new Node();
        assertNull(node.parent());
    }

    @Test
    public void testGetOwnerDocument() {
        Node node = new Node();
        Document document = new Document();
        document.setRootElement(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemoveMethod() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node nodeToRemove = new Node(baseUri, new Attributes());
        parentNode.addChildren(nodeToRemove);
        parentNode.remove();
        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testReplaceWithMethod() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node nodeToReplace = new Node(baseUri, new Attributes());
        Node nodeToReplaceWith = new Node(baseUri, new Attributes());
        parentNode.addChildren(nodeToReplace);
        parentNode.replaceWith(nodeToReplaceWith);
        assertEquals(nodeToReplaceWith, parentNode.childNode(0));
        assertEquals(nodeToReplace, nodeToReplaceWith.parentNode());
    }

    @Test
    public void testSetParentNodeMethod() {
        Node childNode = new Node();
        Node parentNode = new Node();
        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parentNode());
    }

    @Test
    public void testReplaceChildMethod() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node nodeToReplace = new Node(baseUri, new Attributes());
        Node nodeToReplaceWith = new Node(baseUri, new Attributes());
        parentNode.addChildren(nodeToReplace);
        parentNode.replaceChild(nodeToReplace, nodeToReplaceWith);
        assertEquals(nodeToReplaceWith, parentNode.childNode(0));
        assertEquals(nodeToReplace, nodeToReplaceWith.parentNode());
    }

    @Test
    public void testRemoveChildMethod() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node nodeToRemove = new Node(baseUri, new Attributes());
        parentNode.addChildren(nodeToRemove);
        parentNode.removeChild(nodeToRemove);
        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testAddChildrenMethod() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(childNode1, childNode2), children);
    }

    @Test
    public void testAddChildrenWithIndexMethod() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());
        parentNode.addChildren(1, childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(Arrays.asList(childNode2, childNode1), children);
    }

    @Test
    public void testToStringMethod() {
        String baseUri = "http://example.com";
        String nodeName = "div";
        Attributes attributes = new Attributes();
        attributes.put("class", "box");
        Node node = new Node(baseUri, nodeName, attributes) {
            @Override
            public String nodeName() {
                return nodeName;
            }
        };
        assertEquals("<div class=\"box\"></div>", node.toString());
    }

    @Test
    public void testEqualsMethod() {
        Node node1 = new Node();
        Node node2 = new Node();
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCodeMethod() {
        Node node1 = new Node();
        Node node2 = new Node();
        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        assertFalse(hashCode1 == hashCode2);
    }

    @Test
    public void testCloneMethod() throws CloneNotSupportedException {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        String attributeValue = "example";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };
        Node clonedNode = node.clone();
        assertEquals("div", clonedNode.nodeName());
        assertEquals(attributes, clonedNode.attributes());
        assertEquals(baseUri, clonedNode.baseUri());
        assertTrue(clonedNode.childNodes().isEmpty());
    }
}