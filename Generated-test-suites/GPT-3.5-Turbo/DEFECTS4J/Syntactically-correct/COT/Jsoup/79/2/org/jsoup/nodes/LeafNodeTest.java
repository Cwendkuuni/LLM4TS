import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Node;
import org.jsoup.Jsoup;
import java.util.List;

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

        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        Assert.assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };

        Assert.assertNull(leafNode.attributes());

        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        Assert.assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        leafNode.value = "value";

        Assert.assertEquals("value", leafNode.attr("leafNode"));
        Assert.assertEquals("", leafNode.attr("otherAttr"));
    }

    // Additional test cases for edge cases and exception handling scenarios

    /**
     * Test the case where the value is null or not an instance of Attributes.
     */
    @Test
    public void testAttrEdgeCase() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };

        Assert.assertEquals("", leafNode.attr("leafNode"));
        Assert.assertEquals("", leafNode.attr("otherAttr"));

        leafNode.value = null;
        Assert.assertEquals("", leafNode.attr("leafNode"));
        Assert.assertEquals("", leafNode.attr("otherAttr"));

        leafNode.value = "value";
        Assert.assertEquals("value", leafNode.attr("leafNode"));
        Assert.assertEquals("", leafNode.attr("otherAttr"));
    }

    /**
     * Test the case where the key equals the nodeName.
     */
    @Test
    public void testAttrKeyEqualsNodeName() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };
        leafNode.value = null;

        Assert.assertEquals("", leafNode.attr("leafNode"));
        Assert.assertEquals("", leafNode.attr("otherAttr"));

        leafNode.attr("leafNode", "value");
        Assert.assertEquals("value", leafNode.attr("leafNode"));
        Assert.assertEquals("", leafNode.attr("otherAttr"));
    }

    @Test
    public void testAttrKeyNotEqualsNodeName() {
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

        Assert.assertNull(leafNode.removeAttr("key"));
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        Assert.assertNotNull(leafNode.removeAttr("key"));
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

        Assert.assertEquals("", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };

        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leafNode";
            }
        };

        leafNode.doSetBaseUri("baseUri");
        // Test that doSetBaseUri does nothing, so the baseUri remains empty
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