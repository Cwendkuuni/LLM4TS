package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    

    @Test
    public void testConstructorWithBaseUriAndAttributes() {
        Node node = new Node("http://example.com", new Attributes());
        assertNotNull(node.parentNode);
        assertNotNull(node.childNodes);
        assertNotNull(node.attributes);
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testConstructorWithBaseUriOnly() {
        Node node = new Node("http://example.com");
        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertNull(node.parentNode);
        assertNull(node.childNodes);
    }

    @Test
    public void testDefaultConstructor() {
        Node node = new Node();
        assertNull(node.parentNode);
        assertNotNull(node.childNodes);
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes);
        assertEquals(0, node.siblingIndex());
    }

    @Test
    public void testNodeNameAbstraction() {
        assertFalse(node instanceof Node);
        assertTrue(node instanceof Element);
        assertTrue(node instanceof TextNode);
    }

    @Test
    public void testGetAttrSimple() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        Node node = new Node("baseUri", attributes);
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testGetAttrAbsUrl() {
        Attributes attributes = new Attributes();
        attributes.put("abs:testKey", "http://example.com/test");
        Node node = new Node("baseUri", attributes);
        assertEquals("http://example.com/test", node.absUrl("abs:testKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttrNullBaseUri() {
        Node node = new Node();
        node.absUrl("testKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttrNullAttributeKey() {
        Node node = new Node("baseUri", new Attributes());
        node.absUrl(null);
    }

    @Test(expected = MalformedURLException.class)
    public void testGetAbsUrlMalformedAttributeValue() {
        Attributes attributes = new Attributes();
        attributes.put("abs:testKey", "javascript:alert('Hello')");
        Node node = new Node("baseUri", attributes);
        node.absUrl("abs:testKey");
    }

    @Test
    public void testGetChildNodeByIndex() {
        Node parentNode = new Node("baseUri");
        Node child1 = new Node("child1", parentNode.new Attributes());
        Node child2 = new Node("child2", parentNode.new Attributes());
        parentNode.addChildren(child1, child2);
        Node child = parentNode.childNode(0);
        assertEquals("child1", child.nodeName());
    }

    @Test
    public void testGetChildren() {
        Node parentNode = new Node("baseUri");
        Node child1 = new Node("child1", parentNode.new Attributes());
        Node child2 = new Node("child2", parentNode.new Attributes());
        parentNode.addChildren(child1, child2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).nodeName());
        assertEquals("child2", children.get(1).nodeName());
    }

    @Test
    public void testChildNodesAsArray() {
        Node parentNode = new Node("baseUri");
        Node child1 = new Node("child1", parentNode.new Attributes());
        Node child2 = new Node("child2", parentNode.new Attributes());
        parentNode.addChildren(child1, child2);
        Node[] children = parentNode.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals("child1", children[0].nodeName());
        assertEquals("child2", children[1].nodeName());
    }

    @Test
    public void testGetParent() {
        Node parentNode = new Node("baseUri");
        Node child = new Node("child", parentNode.new Attributes());
        parentNode.addChild(child);
        assertEquals(parentNode, child.parent());
    }

    @Test
    public void testGetOwnerDocument() {
        Node parentNode = new Node("baseUri");
        Node child = new Node("child", parentNode.new Attributes());
        parentNode.addChild(child);
        assertEquals(parentNode, child.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Node("baseUri");
        Node child1 = new Node("child1", parentNode.new Attributes());
        Node child2 = new Node("child2", parentNode.new Attributes());
        parentNode.addChildren(child1, child2);
        parentNode.removeChild(child1);
        assertFalse(parentNode.hasChildNodes());
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new Node("baseUri");
        Node oldChild1 = new Node("child1", parentNode.new Attributes());
        Node oldChild2 = new Node("child2", parentNode.new Attributes());
        Node newChild = new Node("newChild", parentNode.new Attributes());
        parentNode.addChildren(oldChild1, oldChild2);
        parentNode.replaceChild(oldChild1, newChild);
        assertEquals("newChild", parentNode.childNode(0).nodeName());
    }

    @Test
    public void testSetParentNode() {
        Node parentNode1 = new Node("baseUri1");
        Node child = new Node("child", parentNode1.new Attributes());
        parentNode1.addChild(child);
        Node parentNode2 = new Node("baseUri2");
        child.setParentNode(parentNode2);
        assertEquals(parentNode2, child.parent());
        assertFalse(parentNode1.hasChildNodes());
    }

    @Test
    public void testReplaceChild() {
        Node parentNode = new Node("baseUri");
        Node oldChild1 = new Node("child1", parentNode.new Attributes());
        Node oldChild2 = new Node("child2", parentNode.new Attributes());
        Node newChild = new Node("newChild", parentNode.new Attributes());
        parentNode.addChildren(oldChild1, oldChild2);
        parentNode.replaceChild(oldChild1, newChild);
        assertEquals("newChild", parentNode.childNode(0).nodeName());
    }

    
}