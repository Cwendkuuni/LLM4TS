package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new TestLeafNode();
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());

        leafNode.attr("testAttr", "testValue");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());

        leafNode.attr("testAttr", "testValue");
        assertNotNull(leafNode.attributes());
        assertEquals("testValue", leafNode.attributes().get("testAttr"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.coreValue("testValue");
        assertNotNull(leafNode.coreValue());
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("testAttr"));

        leafNode.attr("testAttr", "testValue");
        assertEquals("testValue", leafNode.attr("testAttr"));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("testAttr"));

        leafNode.attr("testAttr", "testValue");
        assertNotNull(leafNode.removeAttr("testAttr"));
        assertNull(leafNode.attr("testAttr"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("testAttr"));

        leafNode.attr("testAttr", "testValue");
        assertTrue(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("testAttr"));

        leafNode.attr("testAttr", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("testAttr"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());

        LeafNode parent = new TestLeafNode();
        parent.attr("baseUri", "http://example.com");
        leafNode.setParent(parent);
        assertEquals("http://example.com", leafNode.baseUri());
    }

    private class TestLeafNode extends LeafNode {
    }
}