package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {
    private Document document;
    private Element element;
    private Node node;
    
    @Before
    public void setUp() {
        document = new Document("");
        element = new Element("div", "");
        node = new Node("") {
            @Override
            public String nodeName() {
                return null;
            }
            @Override
            public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {                
            }
            @Override
            public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {                
            }
        };
    }
    
    @Test
    public void testAttr() {
        node.attr("test", "value");
        assertTrue(node.hasAttr("test"));
        assertEquals("value", node.attr("test"));
        assertEquals("", node.attr("nonexistentAttr"));
        assertFalse(node.hasAttr("nonexistentAttr"));
    }

    @Test
    public void testAttributes() {
        assertNotNull(node.attributes());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testBaseUri() {
        assertNotNull(node.baseUri());
        assertEquals("", node.baseUri());
        node.setBaseUri("/");
        assertEquals("/", node.baseUri());
    }
   
    @Test
    public void testChildNodes() {
        assertTrue(node.childNodes().isEmpty());
        assertTrue(element.childNodes().isEmpty());
    }
    
    @Test
    public void testSiblingNodes() {
        assertTrue(node.siblingNodes().isEmpty());
        assertTrue(element.siblingNodes().isEmpty());
    }
    
    @Test
    public void testNextSibling() {
        assertNull(node.nextSibling());
        assertNull(element.nextSibling());
    }
    
    @Test 
    public void testPreviousSibling() {
        assertNull(node.previousSibling());
        assertNull(element.previousSibling());
    }
    
    @Test
    public void testChildNode() {
        assertNull(node.childNode(0));
        assertNull(element.childNode(0));      
    }
    
    @Test
    public void testParent() {
        assertNull(node.parent());
        assertNull(element.parent());
    }
    
    @Test
    public void testNextSiblingAndPreviousSibling() {
        assertNull(node.nextSibling());
        assertNull(node.previousSibling());
        assertNull(element.nextSibling());
        assertNull(element.previousSibling());
    }
    
    @Test
    public void testOuterHtml() {
        assertNotNull(node.outerHtml());
        assertNotNull(element.outerHtml());
        assertEquals("", node.outerHtml());
        assertEquals("<div></div>", element.outerHtml());
    }
}