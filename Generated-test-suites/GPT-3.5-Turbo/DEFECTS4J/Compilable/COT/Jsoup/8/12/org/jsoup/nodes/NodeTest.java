package org.jsoup.nodes;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeName() {
        Node node = new TestNode();
        assertEquals("TestNode", node.nodeName());
    }

    @Test
    public void testAttr() {
        Node node = new TestNode();
        node.attr("key", "value");
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testAttributes() {
        Node node = new TestNode();
        node.attr("key", "value");
        assertEquals("value", node.attributes().get("key"));
    }

    @Test
    public void testHasAttr() {
        Node node = new TestNode();
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new TestNode();
        node.attr("key", "value");
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUri() {
        Node node = new TestNode();
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new TestNode();
        node.setBaseUri("http://example.com");
        node.attr("href", "/test.html");
        assertEquals("http://example.com/test.html", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node parent = new TestNode();
        Node child = new TestNode();
        parent.addChildren(child);
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testParent() {
        Node parent = new TestNode();
        Node child = new TestNode();
        parent.addChildren(child);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new TestNode();
        Node child1 = new TestNode();
        Node child2 = new TestNode();
        parent.addChildren(child1, child2);

        Node replacement = new TestNode();
        child1.replaceWith(replacement);

        assertEquals(replacement, parent.childNode(0));
        assertNull(parent.childNode(1));
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new TestNode();
        Node child1 = new TestNode();
        Node child2 = new TestNode();
        parent.addChildren(child1, child2);

        ArrayList<Node> siblings = new ArrayList<>(parent.siblingNodes());
        assertEquals(child1, siblings.get(0));
        assertEquals(child2, siblings.get(1));
    }

    @Test
    public void testNextSibling() {
        Node parent = new TestNode();
        Node child1 = new TestNode();
        Node child2 = new TestNode();
        parent.addChildren(child1, child2);
        assertEquals(child2, child1.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new TestNode();
        Node child1 = new TestNode();
        Node child2 = new TestNode();
        parent.addChildren(child1, child2);
        assertEquals(child1, child2.previousSibling());
    }

    @Test
    public void testOuterHtml() {
        Node node = new TestNode();
        String expectedHtml = "<TestNode></TestNode>";
        assertEquals(expectedHtml, node.outerHtml());
    }
    
    private static class TestNode extends Node {
        @Override
        public String nodeName() {
            return "TestNode";
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
            accum.append("<TestNode>");
        }

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
            accum.append("</TestNode>");
        }
    }
}