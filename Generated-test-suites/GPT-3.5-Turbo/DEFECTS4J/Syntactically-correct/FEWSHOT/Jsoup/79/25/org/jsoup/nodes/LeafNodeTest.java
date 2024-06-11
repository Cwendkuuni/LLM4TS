package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertFalse(leafNode.hasAttributes());
        
        leafNode.attr("key", "value");
        
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertNull(leafNode.attributes());
        
        leafNode.attr("key", "value");
        
        assertEquals(1, leafNode.attributes().size());
        assertEquals("value", leafNode.attributes().get("key"));
    }
    
    @Test
    public void testCoreValue() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertNull(leafNode.coreValue());
        
        leafNode.attr("key", "value");
        
        assertEquals("value", leafNode.coreValue());
        
        leafNode.attr("key2", "value2");
        
        assertEquals("value", leafNode.coreValue());
    }
    
    @Test
    public void testCoreValueSet() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        leafNode.coreValue("value");
        
        assertEquals("value", leafNode.coreValue());
        
        leafNode.attr("key", "value");
        
        assertEquals("value", leafNode.coreValue());
        
        leafNode.coreValue("value2");
        
        assertEquals("value2", leafNode.coreValue());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertNull(leafNode.attr("key"));
        
        leafNode.attr("key", "value");
        
        assertEquals("value", leafNode.attr("key"));
        
        leafNode.attr("key2", "value2");
        
        assertEquals("value", leafNode.attr("key"));
        assertEquals("value2", leafNode.attr("key2"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertFalse(leafNode.hasAttr("key"));
        
        leafNode.attr("key", "value");
        
        assertTrue(leafNode.hasAttr("key"));
        
        assertFalse(leafNode.hasAttr("key2"));
        
        leafNode.attr("key2", "value2");
        
        assertTrue(leafNode.hasAttr("key"));
        assertTrue(leafNode.hasAttr("key2"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertNull(leafNode.removeAttr("key"));
        
        leafNode.attr("key", "value");
        
        assertEquals("value", leafNode.removeAttr("key"));
        
        assertNull(leafNode.removeAttr("key"));
        
        leafNode.attr("key2", "value2");
        
        assertEquals("value2", leafNode.removeAttr("key2"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertNull(leafNode.absUrl("key"));
        
        leafNode.attr("key", "value");
        
        assertEquals("value", leafNode.absUrl("key"));
        
        leafNode.attr("key2", "value2");
        
        assertNull(leafNode.absUrl("key2"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertEquals("", leafNode.baseUri());
        
        LeafNode parent = new ConcreteLeafNode();
        parent.attr("key", "value");
        leafNode.setParent(parent);
        
        assertEquals("value", leafNode.baseUri());
    }
    
    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        leafNode.doSetBaseUri("baseUri");
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new ConcreteLeafNode();
        
        leafNode.ensureChildNodes();
    }
    
    private static class ConcreteLeafNode extends LeafNode {
        @Override
        public String nodeName() {
            return "leafNode";
        }
    }
}