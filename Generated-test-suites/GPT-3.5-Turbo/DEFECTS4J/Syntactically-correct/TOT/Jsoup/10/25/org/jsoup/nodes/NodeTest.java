
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        node = new NodeImpl("https://www.example.com", new Attributes());
    }

    @Test
    public void testNodeName() {
        assertEquals("Node", node.nodeName());
    }

    @Test
    public void testAttrExistence() {
        assertFalse(node.hasAttr("attributeKey"));

        node.attr("attributeKey", "attributeValue");
        assertTrue(node.hasAttr("attributeKey"));
    }

    @Test
    public void testAttr() {
        assertEquals("", node.attr("attributeKey"));

        node.attr("attributeKey", "attributeValue");
        assertEquals("attributeValue", node.attr("attributeKey"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("attributeKey", "attributeValue");
        assertTrue(node.hasAttr("attributeKey"));

        node.removeAttr("attributeKey");
        assertFalse(node.hasAttr("attributeKey"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("https://www.example.com", node.baseUri());

        node.setBaseUri("https://www.example2.com");
        assertEquals("https://www.example2.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", node.absUrl("attributeKey"));

        node.attr("attributeKey", "/path");
        assertEquals("https://www.example.com/path", node.absUrl("attributeKey"));
    }

    private class NodeImpl extends Node {
        public NodeImpl(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        public String nodeName() {
            return "Node";
        }

        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

        }

        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

        }
    }

}
