import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testCoreValue2() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertNull(leafNode.attr("testAttr"));
    }

    @Test
    public void testAttr2() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        leafNode.attr("testAttr", "testValue");
        assertEquals("testValue", leafNode.attr("testAttr"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertFalse(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertNull(leafNode.removeAttr("testAttr"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertNull(leafNode.absUrl("testUrl"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        leafNode.doSetBaseUri("test");
        // No operation is performed, so no assertion needed
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testName";
            }
        };
        leafNode.ensureChildNodes();
    }

}