import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;

public class LeafNodeTest {
    
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        Attributes attributes = leafNode.attributes();
        
        assertNotNull(attributes);
        assertEquals(0, attributes.size());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertNull(leafNode.attr("key"));
    }
    
    @Test
    public void testSetAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        
        leafNode.attr("key", "value");
        
        assertEquals("value", leafNode.attr("key"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertFalse(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        
        leafNode.attr("key", "value");
        
        leafNode.removeAttr("key");
        
        assertFalse(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertNull(leafNode.absUrl("key"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        
        assertEquals("", leafNode.baseUri());
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
        // implement abstract methods if necessary
    }
}