package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {
                // no operation
            }
        };
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
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("test");
        assertEquals("test", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("test"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("test"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("test"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
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