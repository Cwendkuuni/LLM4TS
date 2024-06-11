package org.jsoup.nodes; // Import the class under test

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertNotNull(node.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testNullNodeName() {
        Node emptyNode = new Node();
        try {
            emptyNode.nodeName();
        } catch (AssertionError e) {
            // Expected assertion error
        }
    }

    @Test
    public void testAttr() {
        assertEquals("value1", node.attr("key1"));
        assertEquals("", node.attr("nonExistentAttr"));
        assertNull(node.attr("nonExistentAttr").trim());
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        assertEquals("value1", attributes.get("key1"));
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonExistentAttr"));
    }

    @Test
    public void testSetAttr() {
        node.attr("key2", "value2");
        Attributes attributes = node.attributes();
        assertEquals("value2", attributes.get("key2"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("key1"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("key2", "value2");
        node.removeAttr("key2");
        Attributes attributes = node.attributes();
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "http://newbase.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test(expected = AssertionError.class)
    public void testNullBaseUri() {
        node.setBaseUri(null);
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", node.absUrl("nonExistentAttr"));
        assertEquals("http://example.com/attribute1", node.absUrl("attribute1"));
        assertEquals("http://example.com/attribute2", node.absUrl("abs:attribute2"));
    }

    @Test(expected = AssertionError.class)
    public void testNullAttributeKey() {
        try {
            node.absUrl(null);
        } catch (AssertionError e) {
            // Expected assertion error
        }
    }

    @Test
    public void testChildNode() {
        Node childNode = new Node("child");
        node.childNodes.add(childNode);
        assertEquals(childNode, node.childNode(0));
    }

    @Test
    public void testChildren() {
        Node childNode1 = new Node("child1");
        Node childNode2 = new Node("child2");
        node.addChildren(childNode1, childNode2);
        List<Node> children = node.childNodes();
        assertThat(children, hasSize(3));
        assertThat(children, contains(node, childNode1, childNode2));
    }

    @Test
    public void testParent() {
        Node parentNode = new Node("parent");
        parentNode.addChildren(node);
        assertEquals(parentNode, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Node("parent");
        parentNode.addChildren(node);
        assertTrue(parentNode.childNodes().contains(node));
        node.remove();
        assertFalse(parentNode.childNodes().contains(node));
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new Node("parent");
        Node replacementNode = new Node("replacement");
        Node childNode = node;
        parentNode.addChildren(childNode);
        parentNode.replaceChild(childNode, replacementNode);
        assertEquals(replacementNode, parentNode.childNode(0));
    }

    @Test
    public void testClone() {
        Node clonedNode = node.clone();
        assertEquals(node, clonedNode);
        clonedNode.attr("key3", "value3");
        assertNotSame(node, clonedNode);
        assertEquals(3, clonedNode.childNodes().size());
        Node clonedChildNode = clonedNode.childNodes().get(0);
        assertNotSame(node.childNodes().get(0), clonedChildNode);
        assertNotSame(clonedNode, clonedChildNode.getParent());
    }
}