import org.jsoup.nodes.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertFalse(leafNode.hasAttributes());
        
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
        
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertNotNull(leafNode.attributes());
        
        leafNode.attr("key", "value");
        assertNotNull(leafNode.attributes());
        
        leafNode.removeAttr("key");
        assertNotNull(leafNode.attributes());
        
        assertFalse(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertNull(leafNode.coreValue());
        
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
        
        leafNode.coreValue(null);
        assertNull(leafNode.coreValue());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertNull(leafNode.attr("key"));
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        
        leafNode.removeAttr("key");
        assertNull(leafNode.attr("key"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertFalse(leafNode.hasAttr("key"));
        
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        
        leafNode.attr("key", "value");
        leafNode.removeAttr("key");
        
        assertFalse(leafNode.hasAttr("key"));
        assertNull(leafNode.attr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertEquals("", leafNode.absUrl("key"));
        
        leafNode.attr("abs:href", "https://www.example.com");
        assertEquals("https://www.example.com", leafNode.absUrl("href"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertEquals("", leafNode.baseUri());
        
        Document doc = new Document("");
        doc.appendChild(leafNode);
        assertEquals("", leafNode.baseUri());
    }
    
    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        
        leafNode.setBaseUri("https://www.example.com");
        assertNull(leafNode.getBaseUri());
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeImpl();
        
        leafNode.ensureChildNodes();
    }
    
    private class LeafNodeImpl extends LeafNode {
        @Override
        public String nodeName() {
            return "node";
        }
        
        @Override
        public String toString() {
            return nodeName();
        }
    }
}