import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.value = new Attributes();
        
        boolean result = leafNode.hasAttributes();
        
        assertTrue(result);
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.value = new Attributes();
        
        Attributes attributes = leafNode.attributes();
        
        assertNotNull(attributes);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.attr("test", "value");
        
        String result = leafNode.coreValue();
        
        assertEquals("value", result);
    }

    @Test
    public void testCoreValueWithNoAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        
        String result = leafNode.coreValue();
        
        assertNull(result);
    }

    @Test
    public void testCoreValueSet() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.coreValue("value");
        
        String result = leafNode.attr("test");
        
        assertEquals("value", result);
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.attr("test", "value");
        
        String result = leafNode.attr("test");
        
        assertEquals("value", result);
    }

    @Test
    public void testAttrWithNoAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        
        String result = leafNode.attr("test");
        
        assertEquals("test", result);
    }

    @Test
    public void testAttrWithSuperAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.attr("test", "value");
        leafNode.attr("other", "value");
        
        String result = leafNode.attr("other");
        
        assertEquals("value", result);
    }

    @Test
    public void testAttrWithSuperAttrButDifferentKey() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.attr("test", "value");
        leafNode.attr("other", "value");
        
        String result = leafNode.attr("test");
        
        assertNull(result);
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.attr("test", "value");
        
        boolean result = leafNode.hasAttr("test");
        
        assertTrue(result);
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.attr("test", "value");
        
        Node result = leafNode.removeAttr("test");
        
        assertNull(result.attr("test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.attr("test", "http://example.com/");
        
        String result = leafNode.absUrl("test");
        
        assertEquals("http://example.com/", result);
    }

    @Test
    public void testBaseUriWithParent() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        leafNode.parentNode = new LeafNode() {
            @Override
            String nodeName() {
                return "parent";
            }
        };
        
        String result = leafNode.baseUri();
        
        assertEquals("", result);
    }

    @Test
    public void testBaseUriWithNoParent() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        
        String result = leafNode.baseUri();
        
        assertEquals("", result);
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        
        leafNode.doSetBaseUri("http://example.com");
        
        // Noop, nothing to test
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        
        int result = leafNode.childNodeSize();
        
        assertEquals(0, result);
    }

    @Test
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "test";
            }
        };
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
        	leafNode.ensureChildNodes();
        });
        assertEquals("Leaf Nodes do not have child nodes.", exception.getMessage());
    }
}