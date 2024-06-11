package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            protected String outerHtmlHead() {
                return null;
            }

            @Override
            protected String outerHtmlTail() {
                return null;
            }

            @Override
            public String nodeName() {
                return null;
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
        assertTrue(attributes instanceof Attributes);
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttrWithoutAttributes() {
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testAttrWithAttributeNameMatch() {
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr("nodeName"));
    }

    @Test
    public void testAttrWithAttributeNameMismatch() {
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testAttrWithValue() {
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testHasAttr() {
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        assertFalse(leafNode.hasAttr("notKey"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        leafNode.attr("url", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("url"));
    }

    @Test
    public void testBaseUriWithParent() {
        assertFalse(leafNode.hasParent());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://example.com");
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