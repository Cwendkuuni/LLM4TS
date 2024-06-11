
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new SubLeafNode(null);

        assertFalse(node.hasAttributes());

        node.attr("key", "value");

        assertTrue(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode node = new SubLeafNode(null);

        assertFalse(node.hasAttributes());

        node.attributes();

        assertTrue(node.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new SubLeafNode(null);

        assertNull(node.coreValue());

        node.coreValue("value");

        assertEquals("value", node.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode node = new SubLeafNode(null);

        assertNull(node.attr("key"));

        node.attr("key", "value");

        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testNodeAttr() {
        LeafNode node = new SubLeafNode(null);

        assertNull(node.attr("nodeName"));

        node.attr("nodeName", "value");

        assertEquals("value", node.attr("nodeName"));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new SubLeafNode(null);

        assertFalse(node.hasAttr("key"));

        node.attr("key", "value");

        assertTrue(node.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new SubLeafNode(null);

        assertNull(node.removeAttr("key"));

        node.attr("key", "value");

        assertEquals("value", node.removeAttr("key"));
        assertNull(node.attr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode node = new SubLeafNode(null);

        assertNull(node.absUrl("key"));

        node.attr("key", "http://example.com");

        assertEquals("http://example.com", node.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode node = new SubLeafNode(null);

        assertEquals("", node.baseUri());

        Node parent = new SubLeafNode(null);

        parent.attr("key", "http://example.com");
        node.parentNode = parent;

        assertEquals("http://example.com", node.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode node = new SubLeafNode(null);

        node.ensureChildNodes();
    }

    private static class SubLeafNode extends LeafNode {
        public SubLeafNode(Object value) {
            this.value = value;
        }

        @Override
        public String nodeName() {
            return "nodeName";
        }
    }

}
