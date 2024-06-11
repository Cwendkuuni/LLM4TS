
import static org.junit.Assert.*;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes_ReturnsTrueWhenValueIsInstanceOfAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_ReturnsFalseWhenValueIsNotInstanceOfAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = "some value";
        
        assertFalse(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes_ReturnsAttributesObjectWhenValueIsAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }

    @Test
    public void testAttributes_ReturnsEmptyAttributesObjectWhenValueIsNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = null;
        
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }
    
    @Test
    public void testAttr_ReturnsAttributeValueWhenKeyIsSameAsNodeNameAndValueIsString() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.value = "some value";
        
        assertEquals("some value", leafNode.attr("nodeName"));
    }

    @Test
    public void testAttr_ReturnsEmptyStringWhenKeyIsNotSameAsNodeNameAndValueIsNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.value = null;
        
        assertEquals("", leafNode.attr("otherKey"));
    }
    
    @Test
    public void testAttr_ReturnsAttributeValueUsingSuperClassWhenKeyIsNotSameAsNodeNameAndValueIsAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.value = new Attributes();
        
        assertEquals("", leafNode.attr("otherKey"));
    }
    
    @Test
    public void testAttr_ChangesValueToNewValueWhenKeyIsSameAsNodeNameAndValueIsNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.value = null;
        
        leafNode.attr("nodeName", "new value");
        
        assertEquals("new value", leafNode.value);
    }

    @Test
    public void testAttr_ChangesValueToNewAttributesObjectWhenKeyIsNotSameAsNodeName() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "nodeName";
            }
        };
        leafNode.value = "some value";
        
        leafNode.attr("otherKey", "new value");
        
        assertTrue(leafNode.value instanceof Attributes);
    }
    
    @Test
    public void testHasAttr_ReturnsTrueWhenAttributesContainsKey() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("key", "value");
        
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testHasAttr_ReturnsFalseWhenAttributesDoesNotContainKey() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("key", "value");
        
        assertFalse(leafNode.hasAttr("otherKey"));
    }
    
    @Test
    public void testRemoveAttr_RemovesKeyFromAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("key", "value");
        
        leafNode.removeAttr("key");
        
        assertFalse(((Attributes) leafNode.value).hasKey("key"));
    }
    
    @Test
    public void testAbsUrl_ReturnsAbsoluteUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.value = "relative/url";
        
        assertEquals("http://www.example.com/relative/url", leafNode.absUrl("key"));
    }
    
    @Test
    public void testBaseUri_ReturnsEmptyStringWhenParentIsNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.parentNode = null;
        
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_ReturnsParentBaseUriWhenParentIsNotNull() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        Node parentNode = new Node() {
            @Override
            public String baseUri() {
                return "http://www.example.com";
            }
        };
        leafNode.parentNode = parentNode;
        
        assertEquals("http://www.example.com", leafNode.baseUri());
    }
    
    @Test
    public void testDoSetBaseUri_NoOperation() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        
        leafNode.doSetBaseUri("http://www.example.com");
    }
    
    @Test
    public void testChildNodeSize_ReturnsZero() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_ThrowsUnsupportedOperationException() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        
        leafNode.ensureChildNodes();
    }
}
