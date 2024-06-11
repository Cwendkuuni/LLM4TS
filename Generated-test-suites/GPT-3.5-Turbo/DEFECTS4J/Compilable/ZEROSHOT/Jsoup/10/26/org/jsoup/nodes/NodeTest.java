package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        node = new Node() {
            @Override
            public String nodeName() {
                return null;
            }

            @Override
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

            }

            @Override
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

            }
        };
    }

    @Test
    public void testAttr() {
        assertNull(node.attr("attr"));
    }

    @Test
    public void testAttributes() {
        assertNotNull(node.attributes());
    }

    @Test
    public void testAttr1() {
        node.attr("attr", "value");
        assertTrue(node.hasAttr("attr"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(node.hasAttr("attr"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("attr");
    }

    @Test
    public void testBaseUri() {
        assertNull(node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("baseUri");
        assertEquals("baseUri", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", node.absUrl("attr"));
    }

    @Test
    public void testChildNode() {
        assertNull(node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        assertNotNull(node.childNodes());
    }

    @Test
    public void testSiblingNodes() {
        assertNotNull(node.siblingNodes());
    }

    @Test
    public void testNextSibling() {
        assertNull(node.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        assertNull(node.previousSibling());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", node.outerHtml());
    }

    @Test
    public void testEquals() {
        assertFalse(node.equals(null));
    }

    @Test
    public void testHashCode() {
        assertNotEquals(0, node.hashCode());
    }

    @Test
    public void testClone() {
        assertNotNull(node.clone());
    }
}