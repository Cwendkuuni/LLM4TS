package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

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
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
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
        assertNull(leafNode.attr("key"));
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        leafNode.removeAttr("key");
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.absUrl("key"));
        leafNode.attr("key", "http://www.example.com");
        assertEquals("http://www.example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("", leafNode.baseUri());
        leafNode.setParent(new NodeImpl());
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.setBaseUri("http://www.example.com");
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.ensureChildNodes();
    }
    
    private static class LeafNodeImpl extends LeafNode {
        
        @Override
        public String nodeName() {
            return "leaf";
        }
        
    }
}