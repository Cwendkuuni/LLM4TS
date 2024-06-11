package org.jsoup.nodes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new TestNode("http://example.com", new Attributes());
        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAttr() {
        Node node = new TestNode("http://example.com");
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new TestNode("http://example.com");
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUri() {
        Node node = new TestNode("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new TestNode("http://example.com");
        node.attr("href", "/page.html");
        assertEquals("http://example.com/page.html", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node parent = new TestNode("http://example.com");
        Node child1 = new TestNode("http://example.com");
        Node child2 = new TestNode("http://example.com");
        parent.addChildren(child1, child2);

        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testParent() {
        Node parent = new TestNode("http://example.com");
        Node child = new TestNode("http://example.com");
        parent.addChildren(child);

        assertEquals(parent, child.parent());
    }

    @Test
    public void testRemove() {
        Node parent = new TestNode("http://example.com");
        Node child = new TestNode("http://example.com");
        parent.addChildren(child);

        child.remove();
        assertNull(child.parent());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new TestNode("http://example.com");
        Node oldChild = new TestNode("http://example.com");
        Node newChild = new TestNode("http://example.com");
        parent.addChildren(oldChild);

        oldChild.replaceWith(newChild);
        assertEquals(newChild, parent.childNode(0));
        assertNull(oldChild.parent());
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new TestNode("http://example.com");
        Node sibling1 = new TestNode("http://example.com");
        Node sibling2 = new TestNode("http://example.com");
        parent.addChildren(sibling1, sibling2);

        assertTrue(parent.siblingNodes().contains(sibling1));
        assertTrue(parent.siblingNodes().contains(sibling2));
    }

    @Test
    public void testNextSibling() {
        Node parent = new TestNode("http://example.com");
        Node sibling1 = new TestNode("http://example.com");
        Node sibling2 = new TestNode("http://example.com");
        parent.addChildren(sibling1, sibling2);

        assertEquals(sibling2, sibling1.nextSibling());
        assertNull(sibling2.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new TestNode("http://example.com");
        Node sibling1 = new TestNode("http://example.com");
        Node sibling2 = new TestNode("http://example.com");
        parent.addChildren(sibling1, sibling2);

        assertEquals(sibling1, sibling2.previousSibling());
        assertNull(sibling1.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new TestNode("http://example.com");
        Node sibling1 = new TestNode("http://example.com");
        Node sibling2 = new TestNode("http://example.com");
        parent.addChildren(sibling1, sibling2);

        assertEquals(Integer.valueOf(0), sibling1.siblingIndex());
        assertEquals(Integer.valueOf(1), sibling2.siblingIndex());
    }

    @Test
    public void testClone() {
        Node parent = new TestNode("http://example.com");
        Node child = new TestNode("http://example.com");
        parent.addChildren(child);

        Node clonedNode = child.clone();
        assertEquals(child.baseUri(), clonedNode.baseUri());
        assertNull(clonedNode.parent());
    }

    // Additional test cases for edge cases and error scenarios can be added here

    private class TestNode extends Node {
        public TestNode(String baseUri) {
            super(baseUri);
        }

        public TestNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        @Override
        public String nodeName() {
            return "TestNode";
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
    }
}