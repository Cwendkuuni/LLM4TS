package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {
                // noop
            }

            @Override
            protected List<Node> ensureChildNodes() {
                throw new UnsupportedOperationException("Leaf Nodes do not have child nodes.");
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
        leafNode.coreValue("value");
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("key"));
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr("key"));
        leafNode.attr("key", "new value");
        assertEquals("new value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("key"));
        leafNode.coreValue("value");
        assertNotNull(leafNode.removeAttr("key"));
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));
        leafNode.coreValue("value");
        assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
        leafNode.setParent(new Node() {
            @Override
            public String baseUri() {
                return "parentBaseUri";
            }
        });
        assertEquals("parentBaseUri", leafNode.baseUri());
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