package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {
    
    @Test
    public void testHasAttributes() {
        // Test when value is an Attributes object
        LeafNode leafNode1 = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode1.value = new Attributes();
        assertTrue(leafNode1.hasAttributes());
        
        // Test when value is not an Attributes object
        LeafNode leafNode2 = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode2.value = "test";
        assertFalse(leafNode2.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        
        // Test when value is an Attributes object
        leafNode.value = new Attributes();
        assertEquals(leafNode.value, leafNode.attributes());
        
        // Test when value is not an Attributes object
        leafNode.value = "test";
        assertNotEquals(leafNode.value, leafNode.attributes());
    }
    
    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        
        // Test when hasAttributes() is true
        leafNode1.value = new Attributes();
        leafNode1.ensureAttributes();
        assertTrue(leafNode1.hasAttributes());
        
        // Test when hasAttributes() is false
        leafNode1.value = "test";
        leafNode1.ensureAttributes();
        assertTrue(leafNode1.hasAttributes());
        assertTrue(leafNode1.value instanceof Attributes);
        assertTrue(((Attributes) leafNode1.value).containsKey(leafNode1.nodeName()));
        assertEquals(leafNode1.value, ((Attributes) leafNode1.value).get(leafNode1.nodeName()));
    }
    
    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "test";
            }
        };
        
        // Test when value is an Attributes object
        leafNode.value = new Attributes();
        assertNull(leafNode.coreValue());
        
        // Test when value is a string
        leafNode.value = "test value";
        assertEquals("test value", leafNode.coreValue());
    }
    
    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "test";
            }
        };
        
        // Test when value is an Attributes object
        leafNode.value = new Attributes();
        assertNull(leafNode.coreValue());
        
        // Test when value is a string
        leafNode.coreValue("new value");
        assertEquals("new value", leafNode.coreValue());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "test";
            }
        };
        
        // Test when hasAttributes() is false and key equals nodeName()
        leafNode.value = "value";
        assertEquals("value", leafNode.attr("test"));
        
        // Test when hasAttributes() is false and key does not equal nodeName()
        leafNode.value = "value";
        assertEquals(EmptyString, leafNode.attr("other"));
        
        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        assertNull(leafNode.attr("test"));
    }
    
    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "test";
            }
        };
        
        // Test when hasAttributes() is false and key equals nodeName()
        leafNode.value = "value";
        leafNode.attr("test", "new value");
        assertEquals("new value", leafNode.value);
        
        // Test when hasAttributes() is false and key does not equal nodeName()
        leafNode.value = "value";
        leafNode.attr("other", "new value");
        assertEquals("value", leafNode.value);
        
        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        leafNode.attr("test", "new value");
        assertTrue(leafNode.value instanceof Attributes);
        assertTrue(((Attributes) leafNode.value).containsKey("test"));
        assertEquals("new value", ((Attributes) leafNode.value).get("test"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        
        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttr("test"));
        
        // Test when hasAttributes() is false
        leafNode.value = "value";
        assertFalse(leafNode.hasAttr("test"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        
        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        leafNode.removeAttr("test");
        assertFalse(((Attributes) leafNode.value).containsKey("test"));
        
        // Test when hasAttributes() is false
        leafNode.value = "value";
        leafNode.removeAttr("test");
        assertEquals("value", leafNode.value);
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        
        // Test when hasAttributes() is true
        leafNode.value = new Attributes();
        assertEquals("", leafNode.absUrl("test"));
        
        // Test when hasAttributes() is false
        leafNode.value = "value";
        assertEquals(EmptyString, leafNode.absUrl("test"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        
        // Test when hasParent() is true
        leafNode.parentNode = new Node() {
            @Override
            public String baseUri() {
                return "parent base URI";
            }
        };
        assertEquals("parent base URI", leafNode.baseUri());
        
        // Test when hasParent() is false
        assertNull(leafNode.baseUri());
    }
    
    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.doSetBaseUri("new base URI");
        // No assertion to be made as method does nothing
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return null;
            }
        };
        leafNode.ensureChildNodes();
    }
    
}