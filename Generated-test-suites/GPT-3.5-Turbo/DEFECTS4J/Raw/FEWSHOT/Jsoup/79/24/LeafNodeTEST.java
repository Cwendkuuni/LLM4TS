package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {
    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNodeImpl();
    }

    private class LeafNodeImpl extends LeafNode {
        @Override
        public String nodeName() {
            return null;
        }

        @Override
        public Node clone() {
            return null;
        }
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        Validate.notNull("key");

        assertEquals("value", leafNode.attr("key"));
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.removeAttr("key"));
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));
        leafNode.attr("key", "value");
        assertEquals("absUrl", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        leafNode.doSetBaseUri("baseUri");
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}