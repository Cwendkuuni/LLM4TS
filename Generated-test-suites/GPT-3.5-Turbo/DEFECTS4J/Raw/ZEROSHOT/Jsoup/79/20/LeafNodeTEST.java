import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {
    
    private LeafNode leafNode;
    
    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
    }
    
    @After
    public void tearDown() {
        leafNode = null;
    }
    
    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());
        
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }
    
    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }
    
    @Test
    public void testAttr() {
        assertNull(leafNode.attr("key"));
        
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr("leafNode"));
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }
    
    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
        
        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttr("leafNode"));
        
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("key"));
        
        leafNode.coreValue("value");
        assertNull(leafNode.removeAttr("leafNode"));
        
        leafNode.attr("key", "value");
        assertNotNull(leafNode.removeAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));
        
        leafNode.attr("key", "http://example.com");
        
        assertEquals("http://example.com", leafNode.absUrl("key"));
    }
    
    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
        
        leafNode.setParent(new Node() {
            @Override
            public String nodeName() {
                return "parent";
            }
            
            @Override
            public String baseUri() {
                return "http://example.com/";
            }
        });
        
        assertEquals("http://example.com/", leafNode.baseUri());
    }
    
    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://example.com/");
        // No operation, so no need to test further
    }
    
    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
        // Should throw UnsupportedOperationException
    }
}