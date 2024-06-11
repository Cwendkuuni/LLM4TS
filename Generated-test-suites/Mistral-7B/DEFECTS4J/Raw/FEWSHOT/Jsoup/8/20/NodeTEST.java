package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {
    @Test
    public void testNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertEquals("test", node.attr("id"));
        assertEquals(baseUri, node.baseUri());
        assertEquals(0, node.siblingIndex());
        assertNull(node.parentNode());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertEquals("", node.nodeName());

        Node node2 = new Node(baseUri);
        assertEquals(baseUri, node2.baseUri());
        assertEquals(null, node2.attributes());
        assertEquals(0, node2.siblingIndex());
        assertNull(node2.parentNode());
        assertEquals(Collections.emptyList(), node2.childNodes());
        assertEquals("", node2.nodeName());

        Node node3 = new Node();
        assertEquals(null, node3.baseUri());
        assertEquals(null, node3.attributes());
        assertEquals(-1, node3.siblingIndex());
        assertNull(node3.parentNode());
        assertEquals(Collections.emptyList(), node3.childNodes());
        assertEquals("", node3.nodeName());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        node.attr("id", "newId");
        assertEquals("newId", node.attr("id"));

        node.attr("abs:href", "http://example2.com");
        assertEquals("http://example2.com", node.absUrl("href"));

        node.attr("foo", "bar");
        assertEquals("bar", node.attr("foo"));
        assertEquals("", node.attr("nonExistentAttr"));

        node.removeAttr("id");
        assertEquals("", node.attr("id"));

        node.removeAttr("abs:href");
        assertEquals("", node.absUrl("href"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("abs:href"));
        assertFalse(node.hasAttr("nonExistentAttr"));

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));

        node.removeAttr("abs:href");
        assertFalse(node.hasAttr("abs:href"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        List<Map.Entry<String, String>> attributesList = new ArrayList<>(node.attributes().entrySet());
        assertEquals("id", attributesList.get(0).getKey());
        assertEquals("test", attributesList.get(0).getValue());

        node.removeAttr("id");
        List<Map.Entry<String, String>> attributesList2 = new ArrayList<>(node.attributes().entrySet());
        assertTrue(attributesList2.isEmpty());
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);

        Node childNode1 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));

        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode2);

        List<Node> childNodes2 = parentNode.childNodes();
        assertEquals(2, childNodes2.size());
        assertEquals(childNode1, childNodes2.get(0));
        assertEquals(childNode2, childNodes2.get(1));

        parentNode.removeChild(childNode1);
        List<Node> childNodes3 = parentNode.childNodes();
        assertEquals(1, childNodes3.size());
        assertEquals(childNode2, childNodes3.get(0));
    }

    @Test
    public void testParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri);
        parentNode.addChild(childNode);

        assertEquals(parentNode, childNode.parentNode());
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test(expected = NullPointerException.class)
    public void testNullParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = null;
        childNode.setParentNode(parentNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeChildIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes);
        parentNode.addChild(childNode1);

        parentNode.removeChild(childNode1);
        parentNode.setSiblingIndex(1);
        parentNode.removeChild(parentNode.childNode(0));

        try {
            parentNode.childNode(-1);
        } catch (IndexOutOfBoundsException e) {
            fail("Expected IllegalArgumentException");
        }
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri);

        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parentNode());

        parentNode.removeChild(childNode);
        assertNull(childNode.parentNode());

        parentNode.addChild(childNode);
        assertEquals(parentNode, childNode.parentNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetParentNodeTwice() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri);

        childNode.setParentNode(parentNode);
        parentNode.removeChild(childNode);
        childNode.setParentNode(parentNode);
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(0));

        parentNode.replaceChild(childNode1, childNode2);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode2, parentNode.childNode(0));
        assertEquals(childNode1, childNode2.previousSibling());
    }

    @Test(expected = IllegalStateException.class)
    public void testReplaceChildSelf() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        childNode.setParentNode(parentNode);

        try {
            childNode.replaceChild(childNode, childNode);
        } catch (IllegalStateException e) {
            assertEquals("The node cannot be a parent of itself", e.getMessage());
        }
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));

        parentNode.removeChild(childNode1);
        assertEquals(1, parentNode.childNodes().size());
        assertNull(parentNode.childNode(0).previousSibling());
        assertEquals(childNode2, parentNode.childNode(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveChildNull() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = null;

        try {
            parentNode.removeChild(childNode);
        } catch (IllegalArgumentException e) {
            assertEquals("The node to be removed is null", e.getMessage());
        }
    }
}