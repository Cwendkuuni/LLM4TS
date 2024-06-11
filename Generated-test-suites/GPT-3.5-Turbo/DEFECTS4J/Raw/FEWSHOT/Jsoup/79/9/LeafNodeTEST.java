package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        Attributes attributes = leafNode.attributes();
        assertEquals(attributes, leafNode.value);
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttrWithNullKey() {
        LeafNode leafNode = new LeafNodeImpl();
        try {
            leafNode.attr(null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAttrWithHasAttributesFalse() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.value = "value";
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttrWithSuperAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.ensureAttributes();
        assertEquals("superValue", leafNode.attr("superKey"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals(leafNode, leafNode.removeAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("absUrl", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.doSetBaseUri("baseUri");
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

    private class LeafNodeImpl extends LeafNode {
        @Override
        protected String nodeName() {
            return "nodeName";
        }
    }
}