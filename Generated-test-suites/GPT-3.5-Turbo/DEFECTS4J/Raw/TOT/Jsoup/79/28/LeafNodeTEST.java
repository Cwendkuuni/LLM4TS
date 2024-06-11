import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Test
    public void testHasAttributes_true() {
        leafNode = new LeafNode() {
            @Override
            protected boolean hasAttributes() {
                return true;
            }
        };
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_false() {
        leafNode = new LeafNode() {
            @Override
            protected boolean hasAttributes() {
                return false;
            }
        };
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        leafNode = new LeafNode() {
            @Override
            public Attributes attributes() {
                return new Attributes();
            }
        };
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes_hasAttributes() {
        leafNode = new LeafNode() {
            @Override
            protected boolean hasAttributes() {
                return true;
            }
        };
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testEnsureAttributes_noAttributes() {
        leafNode = new LeafNode() {
            @Override
            protected boolean hasAttributes() {
                return false;
            }
        };
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        leafNode = new LeafNode() {
            @Override
            String coreValue() {
                return "value";
            }
        };
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_setValue() {
        leafNode = new LeafNode() {
            @Override
            void coreValue(String value) {
                // do nothing
            }
        };
        String newValue = "new value";
        leafNode.coreValue(newValue);
        assertEquals(newValue, leafNode.coreValue());
    }

    @Test
    public void testAttr_noAttributes() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "default";
            }
        };
        String key = "test";
        assertEquals("default", leafNode.attr(key));
    }

    @Test
    public void testAttr_hasAttributes() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }
        };
        String key = "test";
        assertEquals("value", leafNode.attr(key));
    }

    @Test
    public void testAttr_keyIsNull() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }
        };
        String key = null;
        try {
            leafNode.attr(key);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testAttr_keyEqualsNodeName() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }
        };
        String key = "test";
        assertEquals("value", leafNode.attr(key));
    }

    @Test
    public void testAttr_keyEqualsNodeName_hasAttributes() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }

            @Override
            protected boolean hasAttributes() {
                return true;
            }
        };
        String key = "test";
        assertEquals("value", leafNode.attr(key));
    }

    @Test
    public void testAttr_invalidKey_hasAttributes() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }

            @Override
            protected boolean hasAttributes() {
                return true;
            }
        };
        String key = "invalid";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_invalidKey_noAttributes() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }
        };
        String key = "invalid";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_invalidKey_nullNodeName() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }

            @Override
            String nodeName() {
                return null;
            }
        };
        String key = "invalid";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_validKey_nullNodeName() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "value";
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }

            @Override
            String nodeName() {
                return null;
            }
        };
        String key = null;
        try {
            leafNode.attr(key);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testAttr_invalidKey_nullValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return null;
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "invalid";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_validKey_nullValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return null;
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "test";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_keyEqualsNodeName_hasAttributes_nullValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return null;
            }

            @Override
            protected boolean hasAttributes() {
                return true;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "test";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_keyEqualsNodeName_hasAttributes_invalidValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "invalid";
            }

            @Override
            protected boolean hasAttributes() {
                return true;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "test";
        assertEquals("invalid", leafNode.attr(key));
    }

    @Test
    public void testAttr_invalidKey_hasAttributes_nullValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return null;
            }

            @Override
            protected boolean hasAttributes() {
                return true;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "invalid";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_invalidKey_hasAttributes_invalidValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "invalid";
            }

            @Override
            protected boolean hasAttributes() {
                return true;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "invalid";
        assertEquals("invalid", leafNode.attr(key));
    }

    @Test
    public void testAttr_invalidKey_noAttributes_nullValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return null;
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "invalid";
        assertNull(leafNode.attr(key));
    }

    @Test
    public void testAttr_invalidKey_noAttributes_invalidValue() {
        leafNode = new LeafNode() {
            @Override
            public String attr(String key) {
                return "invalid";
            }

            @Override
            protected boolean hasAttributes() {
                return false;
            }

            @Override
            String nodeName() {
                return "test";
            }
        };
        String key = "invalid";
        assertEquals("invalid", leafNode.attr(key));
    }

    @Test
    public void testAttr_absUrl() {
        String url = "https://www.example.com";
        leafNode = new LeafNode() {
            @Override
            public String absUrl(String key) {
                return url;
            }
        };
        assertEquals(url, leafNode.absUrl("test"));
    }

    @Test
    public void testBaseUri_hasParent() {
        leafNode = new LeafNode() {
            @Override
            protected boolean hasParent() {
                return true;
            }

            @Override
            public Node parent() {
                return new Node() {
                    @Override
                    public String baseUri() {
                        return "https://www.example.com";
                    }
                };
            }
        };
        assertEquals("https://www.example.com", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_noParent() {
        leafNode = new LeafNode() {
            @Override
            protected boolean hasParent() {
                return false;
            }
        };
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        String baseUri = "https://www.example.com";
        leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {
                // do nothing
            }
        };
        leafNode.doSetBaseUri(baseUri);
        // Noop method, nothing to verify
        assertTrue(true);
    }

    @Test
    public void testChildNodeSize() {
        leafNode = new LeafNode() {
            @Override
            public int childNodeSize() {
                return 5;
            }
        };
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode = new LeafNode() {
            @Override
            protected List<Node> ensureChildNodes() {
                throw new UnsupportedOperationException("Leaf Nodes do not have child nodes.");
            }
        };
        leafNode.ensureChildNodes();
    }
}