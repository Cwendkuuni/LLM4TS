
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        Assert.assertFalse(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        Attributes attributes = leafNode.attributes();
        
        Assert.assertTrue(leafNode.hasAttributes());
        Assert.assertEquals(attributes, leafNode.attributes());
    }
    
    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        leafNode.coreValue("value");
        
        Assert.assertEquals("value", leafNode.coreValue());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        leafNode.attr("key", "value");
        
        Assert.assertEquals("value", leafNode.attr("key"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        Assert.assertFalse(leafNode.hasAttr("key"));
        
        leafNode.attr("key", "value");
        
        Assert.assertTrue(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        leafNode.attr("key", "value");
        leafNode.removeAttr("key");
        
        Assert.assertFalse(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        leafNode.attr("href", "/example");
        
        Assert.assertEquals("http://example.com/example", leafNode.absUrl("href"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        leafNode.attr("href", "/example");
        
        Assert.assertEquals("", leafNode.baseUri());
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        Assert.assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        
        leafNode.ensureChildNodes();
    }
}
