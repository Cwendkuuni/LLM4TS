package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    private Node node;

    @Before
    public void setup() {
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        node = new NodeImpl("baseUri", attributes);
    }

    @Test
    public void testNodeName() {
        assertEquals("Node", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("value1", node.attr("attr1"));
        assertEquals("value2", node.attr("attr2"));
        assertEquals("", node.attr("attr3"));
    }

    @Test
    public void testAttributes() {
        assertEquals(2, node.attributes().size());
        assertTrue(node.attributes().hasKey("attr1"));
        assertTrue(node.attributes().hasKey("attr2"));
        assertFalse(node.attributes().hasKey("attr3"));
    }

    @Test
    public void testAttrWithAbsoluteUrl() {
        node.attr("abs:href", "http://example.com");
        assertEquals("http://example.com", node.absUrl("href"));
    }

    @Test
    public void testAttrWithRelativeUrl() {
        node.setBaseUri("http://example.com");
        node.attr("href", "/test");
        assertEquals("http://example.com/test", node.absUrl("href"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("attr1"));
        assertTrue(node.hasAttr("attr2"));
        assertFalse(node.hasAttr("attr3"));
    }
    
    @Test
    public void testRemoveAttr() {
        node.removeAttr("attr1");
        assertFalse(node.hasAttr("attr1"));
        assertTrue(node.hasAttr("attr2"));
    }
    
    @Test
    public void testBaseUri() {
        assertEquals("baseUri", node.baseUri());
    }
    
    @Test
    public void testSetBaseUri() {
        node.setBaseUri("newBaseUri");
        assertEquals("newBaseUri", node.baseUri());
    }
    
    @Test
    public void testChildNode() {
        Node child1 = new NodeImpl("child1");
        Node child2 = new NodeImpl("child2");
        node.addChildren(child1, child2);
        
        assertEquals(child1, node.childNode(0));
        assertEquals(child2, node.childNode(1));
    }
    
    @Test
    public void testChildNodes() {
        List<Node> children = node.childNodes();
        assertEquals(0, children.size());
        
        Node child1 = new NodeImpl("child1");
        Node child2 = new NodeImpl("child2");
        node.addChildren(child1, child2);
        
        children = node.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }
    
    @Test
    public void testParent() {
        Node parent = new NodeImpl("parent");
        parent.addChildren(node);
        
        assertEquals(parent, node.parent());
    }
    
    @Test
    public void testRemove() {
        Node parent = new NodeImpl("parent");
        parent.addChildren(node);
        
        node.remove();
        assertFalse(parent.childNodes().contains(node));
        assertNull(node.parent());
    }
    
    @Test
    public void testReplaceWith() {
        Node parent = new NodeImpl("parent");
        parent.addChildren(node);
        
        Node newNode = new NodeImpl("newNode");
        node.replaceWith(newNode);
        assertFalse(parent.childNodes().contains(node));
        assertTrue(parent.childNodes().contains(newNode));
        assertNull(node.parent());
        assertEquals(parent, newNode.parent());
    }

    private static class NodeImpl extends Node {

        protected NodeImpl(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        protected NodeImpl(String baseUri) {
            super(baseUri);
        }

        protected NodeImpl() {
            super();
        }

        @Override
        public String nodeName() {
            return "Node";
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
            // This method is not used in Node, so an implementation is not necessary.
        }

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
            // This method is not used in Node, so an implementation is not necessary.
        }
    }
}