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
            public String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void hasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void attributes() {
        assertNotNull(leafNode.attributes());
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void coreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void attr() {
        assertNull(leafNode.attr("testKey"));
        leafNode.attr("testKey", "testValue");
        assertEquals("testValue", leafNode.attr("testKey"));
    }

    @Test
    public void hasAttr() {
        assertFalse(leafNode.hasAttr("testKey"));
        leafNode.attr("testKey", "testValue");
        assertTrue(leafNode.hasAttr("testKey"));
    }

    @Test
    public void removeAttr() {
        leafNode.attr("testKey", "testValue");
        assertTrue(leafNode.hasAttr("testKey"));
        leafNode.removeAttr("testKey");
        assertFalse(leafNode.hasAttr("testKey"));
    }

    @Test
    public void absUrl() {
        assertNull(leafNode.absUrl("testKey"));
    }

    @Test
    public void baseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void doSetBaseUri() {
        leafNode.doSetBaseUri("testBaseUri");
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void childNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ensureChildNodes() {
        leafNode.ensureChildNodes();
    }
}