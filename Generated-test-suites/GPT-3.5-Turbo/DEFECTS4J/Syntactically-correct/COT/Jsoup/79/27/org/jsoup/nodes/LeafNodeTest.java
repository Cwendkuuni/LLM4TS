
import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testSetAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertNull(leafNode.removeAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
            @Override
            public Node parent() {
                return null;
            }
        };
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.doSetBaseUri("baseUri");
        // No assertion, just checking for exception
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return null;
            }
        };
        leafNode.ensureChildNodes();
    }
}
