package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeHasAttr() {
        Node node = new Node("", new Attributes());
        node.attr("test", "attribute");
        assertTrue(node.hasAttr("test"));
    }

    @Test
    public void testNodeHasNoAttr() {
        Node node = new Node("", new Attributes());
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testNodeRemoveAttr() {
        Node node = new Node("", new Attributes());
        node.attr("test", "attribute");
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testNodeGetEmptyAttr() {
        Node node = new Node("", new Attributes());
        assertEquals("", node.attr("test"));
    }

    @Test
    public void testNodeGetAttrWithValue() {
        Node node = new Node("", new Attributes());
        node.attr("test", "attribute");
        assertEquals("attribute", node.attr("test"));
    }

    @Test
    public void testNodeGetAbsUrlFromEmptyBaseUri() {
        Node node = new Node("", new Attributes());
        assertEquals("", node.absUrl("test"));
    }

    @Test
    public void testNodeGetAbsUrlWithBaseUri() {
        Node node = new Node("http://example.com", new Attributes());
        assertEquals("http://example.com/test", node.absUrl("test"));
    }

    @Test
    public void testNodeGetAbsUrlWithRelativeUrl() {
        Node node = new Node("http://example.com", new Attributes());
        assertEquals("http://example.com/test", node.absUrl("test"));
        assertEquals("http://example.com/test2", node.absUrl("test2"));
    }

    @Test
    public void testNodeGetAbsUrlWithAbsUrl() {
        Node node = new Node("http://example.com", new Attributes());
        assertEquals("http://example.com/test", node.absUrl("test"));
        assertEquals("http://example.com/test", node.absUrl("http://example.com/test"));
    }

    @Test
    public void testNodeGetAbsUrlWithMalformedUrl() {
        Node node = new Node("http://example.com", new Attributes());
        try {
            node.absUrl("test:/test");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNodeGetChildNodeByIndex() {
        Node parent = new Node("", new Attributes());
        Node child1 = new Node("", new Attributes());
        Node child2 = new Node("", new Attributes());
        parent.childNodes.add(child1);
        parent.childNodes.add(child2);
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testNodeGetChildNodes() {
        Node parent = new Node("", new Attributes());
        Node child1 = new Node("", new Attributes());
        Node child2 = new Node("", new Attributes());
        parent.childNodes.add(child1);
        parent.childNodes.add(child2);
        List<Node> nodes = parent.childNodes();
        assertEquals(2, nodes.size());
        assertEquals(child1, nodes.get(0));
        assertEquals(child2, nodes.get(1));
    }

    @Test
    public void testNodeGetParentNode() {
        Node child = new Node("", new Attributes());
        Node parent = new Node("", new Attributes());
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
        assertEquals(null, child.parentNode);
    }

    @Test
    public void testNodeGetOwnerDocument() {
        Node child = new Node("", new Attributes());
        Node parent = new Node("", new Attributes());
        parent.setParentNode(null);
        child.setParentNode(parent);
        assertEquals(parent, child.ownerDocument());
        assertEquals(null, child.ownerDocument());
    }

    @Test
    public void testNodeRemove() {
        Node parent = new Node("", new Attributes());
        Node child = new Node("", new Attributes());
        parent.childNodes.add(child);
        parent.removeChild(child);
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testNodeReplaceWith() {
        Node parent = new Node("", new Attributes());
        Node oldChild = new Node("", new Attributes());
        Node newChild = new Node("", new Attributes());
        parent.childNodes.add(oldChild);
        parent.replaceWith(newChild);
        assertEquals(1, parent.childNodes().size());
        assertEquals(newChild, parent.childNode(0));
    }

    @Test
    public void testNodeSetParentNode() {
        Node child = new Node("", new Attributes());
        Node parent = new Node("", new Attributes());
        child.setParentNode(parent);
        assertEquals(parent, child.parentNode);
    }

    @Test
    public void testNodeReplaceChild() {
        Node parent = new Node("", new Attributes());
        Node oldChild = new Node("", new Attributes());
        Node newChild = new Node("", new Attributes());
        parent.childNodes.add(oldChild);
        parent.replaceChild(newChild, oldChild);
        assertEquals(1, parent.childNodes().size());
        assertEquals(newChild, parent.childNode(0));
    }

    @Test
    public void testNodeRemoveChild() {
        Node parent = new Node("", new Attributes());
        Node child = new Node("", new Attributes());
        parent.childNodes.add(child);
        parent.removeChild(child);
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testNodeAddChildren() {
        Node parent = new Node("", new Attributes());
        Node child1 = new Node("", new Attributes());
        Node child2 = new Node("", new Attributes());
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testNodeAddChildrenWithIndex() {
        Node parent = new Node("", new Attributes());
        Node child1 = new Node("", new Attributes());
        Node child2 = new Node("", new Attributes());
        parent.addChildren(0, child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }
}