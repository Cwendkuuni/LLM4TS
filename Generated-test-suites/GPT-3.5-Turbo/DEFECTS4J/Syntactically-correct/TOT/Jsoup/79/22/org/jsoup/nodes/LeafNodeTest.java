import static org.junit.Assert.*;
import org.junit.Test;

public class LeafNodeTest {
    
    @Test
    public void testHasAttributes() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertFalse(node.hasAttributes());
        
        node.attr("key", "value");
        assertTrue(node.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertNotNull(node.attributes());
        assertFalse(node.hasAttributes());
        
        node.attr("key", "value");
        assertTrue(node.hasAttributes());
        assertNotNull(node.attributes());
    }
    
    @Test
    public void testCoreValue() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(node.coreValue());
        
        node.coreValue("value");
        assertEquals("value", node.coreValue());
    }
    
    @Test
    public void testAttr() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(node.attr("key"));
        
        node.attr("key", "value");
        assertEquals("value", node.attr("key"));
        
        assertNull(node.attr("key2"));
        
        node.attr("key", "new value");
        assertEquals("new value", node.attr("key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttrWithNullKey() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        node.attr(null, "value");
    }
    
    @Test
    public void testHasAttr() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertFalse(node.hasAttr("key"));
        
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("key2"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(node.removeAttr("key"));
        
        node.attr("key", "value");
        assertNotNull(node.removeAttr("key"));
        assertNull(node.removeAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(node.absUrl("key"));
        
        node.attr("key", "http://www.example.com/");
        assertEquals("http://www.example.com/", node.absUrl("key"));
        
        node.attr("key", "/relative");
        assertEquals("http://www.example.com/relative", node.absUrl("key"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertEquals("", node.baseUri());
        
        node.parent(new Element("parent"));
        assertEquals("", node.baseUri());
        
        node.parent().baseUri("http://www.example.com/");
        assertEquals("http://www.example.com/", node.baseUri());
    }
    
    @Test
    public void testDoSetBaseUri() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        node.doSetBaseUri("http://www.example.com/");
        // No operation should be performed, so no assertions needed
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        assertEquals(0, node.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode node = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
        
        node.ensureChildNodes();
    }
}