package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {};
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {};
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {};
        assertNull(leafNode.coreValue());
        leafNode.coreValue("test");
        assertEquals("test", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {};
        assertEquals("test", leafNode.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {};
        assertFalse(leafNode.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {};
        assertNotNull(leafNode.removeAttr("test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {};
        assertNotNull(leafNode.absUrl("test"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {};
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {};
        leafNode.doSetBaseUri("test");
        // no functionality to test
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {};
        leafNode.ensureChildNodes();
    }

}