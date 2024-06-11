
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class LeafNodeTest {
    
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertFalse(leafNode.hasAttributes());
        
        leafNode.coreValue("testValue");
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(leafNode.attributes());
        
        leafNode.coreValue("testValue");
        assertNotNull(leafNode.attributes());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(leafNode.attr("testKey"));
        
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.attr("testNode"));
    }
    
    @Test
    public void testAttrWithKeyNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        try {
            leafNode.attr(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("key must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testAttrWithValueNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        leafNode.attr("testNode", null);
        assertNull(leafNode.attr("testNode"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertFalse(leafNode.hasAttr("testKey"));
        
        leafNode.coreValue("testValue");
        assertTrue(leafNode.hasAttr("testNode"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(leafNode.removeAttr("testKey"));
        
        leafNode.coreValue("testValue");
        assertNotNull(leafNode.removeAttr("testNode"));
        assertNull(leafNode.attributes());
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertNull(leafNode.absUrl("testKey"));
        
        leafNode.coreValue("testValue");
        assertNull(leafNode.absUrl("testNode"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertEquals("", leafNode.baseUri());
        
        ParentNode parentNode = new ParentNode() {
            @Override
            public String nodeName() {
                return "testParent";
            }
        };
        parentNode.appendChild(leafNode);
        assertEquals("", leafNode.baseUri()); // Base URI is inherited from parent node
        
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        leafNode.ensureChildNodes();
    }
    
    
}
