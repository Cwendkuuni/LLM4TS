package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttributes());
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attributes());
        leafNode.attr("key", "value");
        assertNotNull(leafNode.attributes());
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attr("key"));
        leafNode.attr("key", "value");
        assertNull(leafNode.attr("invalid_key"));
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attr("key"));
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.absUrl("key"));
        leafNode.attr("key", "value");
        assertNull(leafNode.absUrl("key")); // Not supported for LeafNode
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.ensureChildNodes(); // Should throw UnsupportedOperationException
    }
}

class LeafNodeImpl extends LeafNode {
    @Override
    public String nodeName() {
        return "leaf";
    }

    @Override
    public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        // Not needed for testing
    }
}