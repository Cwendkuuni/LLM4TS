
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNodeName";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testEnsureAttributes() {
        assertFalse(leafNode.hasAttributes());

        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes().isEmpty());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.attr("testNodeName", "testValue");

        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        leafNode.coreValue("testValue");

        assertEquals("testValue", leafNode.coreValue());
        assertEquals(1, leafNode.attributes().size());
        assertEquals("testValue", leafNode.attributes().get("testNodeName"));
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("testKey"));

        leafNode.attr("testKey", "testValue");

        assertEquals("testValue", leafNode.attr("testKey"));
    }

    @Test
    public void testAttrCaseSensitive() {
        assertNull(leafNode.attr("testKey"));

        leafNode.attr("testKey", "testValue");

        assertNull(leafNode.attr("TestKey"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("testKey"));

        leafNode.attr("testKey", "testValue");

        assertTrue(leafNode.hasAttr("testKey"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("testKey", "testValue");

        assertTrue(leafNode.hasAttr("testKey"));

        leafNode.removeAttr("testKey");

        assertFalse(leafNode.hasAttr("testKey"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("testKey"));

        leafNode.attr("testKey", "https://example.com");

        assertEquals("https://example.com", leafNode.absUrl("testKey"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        // noop, nothing to test
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
