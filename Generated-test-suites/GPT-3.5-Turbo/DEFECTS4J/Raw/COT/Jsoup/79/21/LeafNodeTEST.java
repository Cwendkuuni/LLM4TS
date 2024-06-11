import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertFalse(node.hasAttributes(), "hasAttributes should return false when value is not an instance of Attributes");
    }
    
    @Test
    public void testAttributes() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertNull(node.value, "value should be null initially");
        assertTrue(node.attributes() instanceof Attributes, "attributes should be an instance of Attributes");
        assertNotNull(node.value, "value should be initialized as an instance of Attributes");
    }
    
    @Test
    public void testEnsureAttributes() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        node.ensureAttributes();
        assertNotNull(node.value, "value should be initialized as an instance of Attributes");
    }
    
    @Test
    public void testCoreValue() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertNull(node.coreValue(), "coreValue should return null initially");
        
        node.attr("test", "value");
        assertEquals("value", node.coreValue(), "coreValue should return the value of the attribute named test");
    }
    
    @Test
    public void testCoreValue2() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        node.coreValue("value");
        assertEquals("value", node.attr("test"), "coreValue should set the value of the attribute named test");
    }
    
    @Test
    public void testAttr() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertNull(node.attr("key"), "attr should return null when value is not an instance of Attributes and key is not equal to nodeName");
        
        node.attr("test", "value");
        assertEquals("value", node.attr("test"), "attr should return the value of the attribute named test");
    }
    
    @Test
    public void testAttr2() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"), "hasAttr should return true when attribute with key exists");
        assertFalse(node.hasAttr("nonexistent"), "hasAttr should return false when attribute with key doesn't exist");
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertFalse(node.hasAttr("key"), "hasAttr should return false when attribute with key doesn't exist");
        
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"), "hasAttr should return true when attribute with key exists");
        
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"), "hasAttr should return false after removing attribute with key");
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertNull(node.absUrl("key"), "absUrl should return null when value is not an instance of Attributes");
        
        node.attr("test", "https://example.com");
        assertEquals("https://example.com", node.absUrl("test"), "absUrl should return the absolute URL of the attribute named test");
    }
    
    @Test
    public void testBaseUri() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertEquals("", node.baseUri(), "baseUri should return an empty string when node has no parent");
        
        node.setParent(new Element("parent"));
        assertEquals("", node.baseUri(), "baseUri should return an empty string when node has a parent");
    }
    
    @Test
    public void testDoSetBaseUri() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertDoesNotThrow(() -> node.doSetBaseUri("https://example.com"), "doSetBaseUri should not throw UnsupportedOperationException");
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertEquals(0, node.childNodeSize(), "childNodeSize should always return 0");
    }
    
    @Test
    public void testEnsureChildNodes() {
        LeafNode node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
        
        assertThrows(UnsupportedOperationException.class, () -> node.ensureChildNodes(), "ensureChildNodes should throw UnsupportedOperationException");
    }
}