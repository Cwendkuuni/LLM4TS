import org.jsoup.nodes.*;
import org.junit.*;

public class LeafNodeTest {

    // Test case for hasAttributes() method
    @Test
    public void testHasAttributes() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertFalse(leafNode1.hasAttributes());

        LeafNode leafNode2 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }

            @Override
            public Attributes attributes() {
                return new Attributes();
            }
        };

        Assert.assertTrue(leafNode2.hasAttributes());
    }

    // Test case for attributes() method
    @Test
    public void testAttributes() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertNull(leafNode1.attributes());

        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");

        LeafNode leafNode2 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }

            @Override
            public Object value() {
                return attributes;
            }
        };

        Assert.assertEquals(attributes, leafNode2.attributes());
    }

    // Test case for ensureAttributes() method
    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertFalse(leafNode1.hasAttributes());

        leafNode1.ensureAttributes();

        Assert.assertTrue(leafNode1.hasAttributes());
        Assert.assertNotNull(leafNode1.attributes());
    }

    // Test case for coreValue() method
    @Test
    public void testCoreValue() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertNull(leafNode1.coreValue());

        leafNode1.coreValue("testValue");

        Assert.assertEquals("testValue", leafNode1.coreValue());
    }

    // Test case for attr() method
    @Test
    public void testAttr() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertNull(leafNode1.attr("attribute1"));

        leafNode1.attr("attribute1", "value1");

        Assert.assertEquals("value1", leafNode1.attr("attribute1"));
    }

    // Test case for hasAttr() method
    @Test
    public void testHasAttr() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertFalse(leafNode1.hasAttr("attribute1"));

        leafNode1.attr("attribute1", "value1");

        Assert.assertTrue(leafNode1.hasAttr("attribute1"));
    }

    // Test case for removeAttr() method
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertNull(leafNode1.removeAttr("attribute1"));

        leafNode1.attr("attribute1", "value1");

        Assert.assertEquals("value1", leafNode1.removeAttr("attribute1"));
    }

    // Test case for absUrl() method
    @Test
    public void testAbsUrl() {
        LeafNode leafNode1 = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
        
        Assert.assertNull(leafNode1.absUrl("href"));

        leafNode1.attr("href", "/path");

        Assert.assertEquals("/path", leafNode1.absUrl("href"));
    }

}