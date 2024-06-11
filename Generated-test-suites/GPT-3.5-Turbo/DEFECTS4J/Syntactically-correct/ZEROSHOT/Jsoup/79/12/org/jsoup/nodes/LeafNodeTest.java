package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class LeafNodeTest {
    
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttributes());
        
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attributes());
        
        leafNode.attr("key", "value");
        assertNotNull(leafNode.attributes());
    }
    
    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());
        
        leafNode.attr("key", "new value");
        assertEquals("new value", leafNode.coreValue());
    }
    
    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());
        
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
        
        leafNode.coreValue("new value");
        assertEquals("new value", leafNode.coreValue());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attr("key"));
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        
        leafNode.attr("key", "new value");
        assertEquals("new value", leafNode.attr("key"));
    }
    
    @Test
    public void testAttrWithNullKey() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attr(null));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttr("key"));
        
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals(leafNode, leafNode.removeAttr("key"));
        
        leafNode.attr("key", "value");
        assertEquals(leafNode, leafNode.removeAttr("key"));
        assertFalse(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.absUrl("key"));
        
        leafNode.attr("key", "value");
        assertNull(leafNode.absUrl("key"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("", leafNode.baseUri());
        
        Node parent = new LeafNodeImpl();
        parent.attr("key", "value");
        leafNode.parent = parent;
        assertEquals("value", leafNode.baseUri());
    }
    
    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.doSetBaseUri("baseUri");
        // No operation is performed, so no assertions are needed
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
}

class LeafNodeImpl extends LeafNode {
    // Implement abstract methods
}