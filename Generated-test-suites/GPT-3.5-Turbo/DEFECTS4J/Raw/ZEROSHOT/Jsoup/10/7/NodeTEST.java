import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {
    
    private Node node;
    private Node parent;
    private Attributes attributes;
    
    @Before
    public void setUp() {
        attributes = new Attributes();
        node = new TestNode("baseUri", attributes);
        parent = new TestNode("parentBaseUri");
        parent.appendChild(node);
    }
    
    @Test
    public void testAttrAndGetAttr() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }
    
    @Test
    public void testAttributes() {
        assertEquals(attributes, node.attributes());
    }
    
    @Test
    public void testRemoveAttr() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }
    
    @Test
    public void testBaseUri() {
        assertEquals("baseUri", node.baseUri());
        node.setBaseUri("newBaseUri");
        assertEquals("newBaseUri", node.baseUri());
    }
    
    @Test
    public void testChildNodes() {
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(node);
        assertEquals(childNodes, node.childNodes());
    }
    
    @Test
    public void testParent() {
        assertEquals(parent, node.parent());
    }
    
    @Test
    public void testOwnerDocument() {
        assertNull(node.ownerDocument());
        node.setBaseUri("baseUri");
        assertTrue(node instanceof Document);
        assertEquals(node, node.ownerDocument());
        
        Node child = new TestNode();
        node.appendChild(child);
        assertEquals(node, child.ownerDocument());
    }
    
    private class TestNode extends Node {
        
        public TestNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }
        
        public TestNode(String baseUri) {
            super(baseUri);
        }
        
        public TestNode() {
            super();
        }
        
        @Override
        public String nodeName() {
            return "TestNode";
        }
        
        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        
        }
        
        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
        
        }
    }
}