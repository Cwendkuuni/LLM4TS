
import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {
  
    private Node node;
    
    @Before
    public void setUp() {
        node = new TestNode("http://example.com", new Attributes());
    }
  
    @Test
    public void testNodeName() {
        assertEquals("TestNode", node.nodeName());
    }
    
    @Test
    public void testAttr() {
        // Test with attribute present
        node.attr("key", "value");
        assertEquals("value", node.attr("key"));
        
        // Test with attribute not present
        assertEquals("", node.attr("fakeKey"));
    }
    
    @Test
    public void testAttributes() {
        assertTrue(node.attributes().isEmpty());
        
        node.attr("key", "value");
        assertFalse(node.attributes().isEmpty());
        assertEquals("value", node.attributes().get("key"));
    }
    
    @Test
    public void testHasAttr() {
        // Test with attribute present
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        
        // Test with attribute not present
        assertFalse(node.hasAttr("fakeKey"));
    }
    
    @Test
    public void testRemoveAttr() {
        // Test with attribute present
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
        
        // Test with attribute not present
        assertFalse(node.hasAttr("fakeKey"));
        node.removeAttr("fakeKey");
        assertFalse(node.hasAttr("fakeKey"));
    }
    
    @Test
    public void testBaseUri() {
        assertEquals("http://example.com", node.baseUri());
        
        node.setBaseUri("http://example.org");
        assertEquals("http://example.org", node.baseUri());
    }
    
    @Test
    public void testAbsUrl() {
        // Test with attribute present and relative URL
        node.attr("href", "/page.html");
        assertEquals("http://example.com/page.html", node.absUrl("href"));
        
        // Test with attribute present and absolute URL
        node.attr("href", "http://example.org/page.html");
        assertEquals("http://example.org/page.html", node.absUrl("href"));
        
        // Test with attribute not present
        assertEquals("", node.absUrl("fakeAttr"));
    }
    
    @Test
    public void testChildNode() {
        Node child = new TestNode();
        node.addChildren(child);
        
        assertEquals(child, node.childNode(0));
    }
    
    @Test
    public void testChildNodes() {
        assertTrue(node.childNodes().isEmpty());
        
        Node child1 = new TestNode();
        Node child2 = new TestNode();
        
        node.addChildren(child1, child2);
        
        assertFalse(node.childNodes().isEmpty());
        assertEquals(child1, node.childNodes().get(0));
        assertEquals(child2, node.childNodes().get(1));
    }
    
    @Test
    public void testParent() {
        assertNull(node.parent());
        
        Node parent = new TestNode();
        parent.addChildren(node);
        
        assertEquals(parent, node.parent());
    }
    
    @Test
    public void testOwnerDocument() {
        assertNull(node.ownerDocument());
        
        Document document = new TestDocument();
        node.setParentNode(document);
        
        assertEquals(document, node.ownerDocument());
    }
    
    @Test
    public void testRemove() {
        Node parent = new TestNode();
        parent.addChildren(node);
        
        assertEquals(1, parent.childNodes().size());
        assertEquals(parent, node.parent());
        
        node.remove();
        
        assertEquals(0, parent.childNodes().size());
        assertNull(node.parent());
    }
    
    @Test
    public void testReplaceWith() {
        Node parent = new TestNode();
        parent.addChildren(node);
        
        assertEquals(1, parent.childNodes().size());
        assertEquals(parent, node.parent());
        
        Node newNode = new TestNode();
        node.replaceWith(newNode);
        
        assertEquals(1, parent.childNodes().size());
        assertEquals(parent, newNode.parent());
        assertEquals(newNode, parent.childNodes().get(0));
        assertNull(node.parent());
    }
    
    private static class TestNode extends Node {
        
        private String nodeName;
        
        public TestNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }
        
        public TestNode() {
            super();
        }
        
        public String nodeName() {
            return nodeName;
        }
        
        protected void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
        
        protected void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        
    }
    
    private static class TestDocument extends Document {
        
        @Override
        protected void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
        
        @Override
        protected void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        
    }
}
