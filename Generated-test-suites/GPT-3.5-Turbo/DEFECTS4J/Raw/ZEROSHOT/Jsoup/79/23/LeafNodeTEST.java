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
                return "leafNode";
            }
        };
    }

    @Test
    public void testHasAttributesWithEmptyValue() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributesWithValue() {
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());
        leafNode.ensureAttributes();
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributesWithExistingValue() {
        leafNode.value = "value";
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes().hasKey("leafNode"));
        assertEquals("value", leafNode.attributes().get("leafNode"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttrWithNoAttributes() {
        assertNull(leafNode.attr("key"));
        leafNode.value = "value";
        assertEquals("value", leafNode.attr("leafNode"));
    }

    @Test
    public void testAttrWithAttributes() {
        leafNode.value = new Attributes();
        leafNode.attributes().put("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
        leafNode.value = new Attributes();
        leafNode.attributes().put("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("key"));
        leafNode.value = new Attributes();
        leafNode.attributes().put("key", "value");
        assertNotNull(leafNode.removeAttr("key"));
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));
        leafNode.value = new Attributes();
        leafNode.attributes().put("key", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUriWithParent() {
        Node parent = new Element("parent");
        parent.baseUri("http://example.com");
        leafNode.parent = parent;
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testBaseUriWithoutParent() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://example.com");
        // No assertion, just to cover the method
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