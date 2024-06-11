
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TestLeafNode();
        assertFalse(leafNode.hasAttributes());

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TestLeafNode();
        assertEquals("", leafNode.attributes().toString());

        leafNode.attr("key", "value");
        assertEquals("key=\"value\"", leafNode.attributes().toString());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TestLeafNode();
        assertEquals("", leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TestLeafNode();
        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TestLeafNode();
        assertEquals("", leafNode.removeAttr("key").toString());

        leafNode.attr("key", "value");
        assertEquals("", leafNode.removeAttr("key").toString());
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TestLeafNode();
        assertEquals("", leafNode.absUrl("key"));

        leafNode.attr("key", "value");
        assertEquals("", leafNode.absUrl("key"));
    }

    private static class TestLeafNode extends LeafNode {
        @Override
        public String nodeName() {
            return "test";
        }
    }
}
