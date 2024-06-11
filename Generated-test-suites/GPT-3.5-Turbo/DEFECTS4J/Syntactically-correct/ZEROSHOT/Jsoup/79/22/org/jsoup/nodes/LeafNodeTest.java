package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertFalse(leafNode.hasAttributes());
        assertEquals(Attributes.class, leafNode.attributes().getClass());
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testEnsureAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttrWithNoAttributes() {
        assertNull(leafNode.attr("testKey"));
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.attr("testNode"));
    }

    @Test
    public void testAttrWithAttributes() {
        leafNode.attr("testAttribute", "testValue");
        assertEquals("testValue", leafNode.attr("testAttribute"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("testAttribute"));
        leafNode.attr("testAttribute", "testValue");
        assertTrue(leafNode.hasAttr("testAttribute"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("testAttribute", "testValue");
        assertTrue(leafNode.hasAttr("testAttribute"));
        leafNode.removeAttr("testAttribute");
        assertFalse(leafNode.hasAttr("testAttribute"));
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", leafNode.absUrl("testKey"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("testBaseUri");
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