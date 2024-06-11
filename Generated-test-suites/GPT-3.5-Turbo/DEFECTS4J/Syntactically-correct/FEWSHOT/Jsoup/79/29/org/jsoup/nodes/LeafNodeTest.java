package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());

        Attributes attributes = leafNode.attributes();
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.attr("testNode", "value");
        assertEquals("value", leafNode.coreValue());

        leafNode.coreValue("newValue");
        assertEquals("newValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        assertEquals("value", leafNode.attr("testNode"));

        leafNode.attr("key", "newValue");
        assertEquals("newValue", leafNode.attr("key"));
        assertEquals("newValue", leafNode.attr("testNode"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        assertTrue(leafNode.hasAttr("testNode"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));

        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
        assertFalse(leafNode.hasAttr("testNode"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));

        leafNode.attr("key", "http://www.example.com");
        assertEquals("http://www.example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        assertTrue(leafNode.baseUri().isEmpty());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://www.example.com");
        assertTrue(leafNode.baseUri().isEmpty());
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