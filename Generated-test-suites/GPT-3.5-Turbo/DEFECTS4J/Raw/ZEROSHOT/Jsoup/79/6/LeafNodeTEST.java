import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LeafNodeTest {
  
    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNodeName";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        Assert.assertNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        Assert.assertTrue(leafNode.hasAttributes());
        Assert.assertNotNull(leafNode.attributes());
        Assert.assertNull(leafNode.attributes().get("testNodeName"));
    }

    @Test
    public void testCoreValue() {
        Assert.assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        leafNode.coreValue("testValue");
        Assert.assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttrWithNoAttributes() {
        String value = leafNode.attr("testAttr");
        Assert.assertEquals("", value);
    }

    @Test
    public void testAttrWithAttributes() {
        leafNode.attr("testAttr", "testValue");
        String value = leafNode.attr("testAttr");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testAttrWithNullKey() {
        try {
            leafNode.attr(null, "testValue");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Key must not be null", e.getMessage());
        }
    }

    @Test
    public void testHasAttr() {
        Assert.assertFalse(leafNode.hasAttr("testAttr"));
        leafNode.attr("testAttr", "testValue");
        Assert.assertTrue(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("testAttr", "testValue");
        Assert.assertTrue(leafNode.hasAttr("testAttr"));
        leafNode.removeAttr("testAttr");
        Assert.assertFalse(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testAbsUrl() {
        leafNode.attr("abs:testAttr", "https://www.example.com");
        String absUrl = leafNode.absUrl("testAttr");
        Assert.assertEquals("https://www.example.com", absUrl);
    }

    @Test
    public void testBaseUriWithParent() {
        LeafNode parent = new LeafNode() {
            @Override
            public String nodeName() {
                return "parentNodeName";
            }
        };
        parent.attr("abs:baseUri", "https://www.example.com");
        leafNode.setParentNode(parent);
        String baseUri = leafNode.baseUri();
        Assert.assertEquals("https://www.example.com", baseUri);
    }

    @Test
    public void testBaseUriWithoutParent() {
        String baseUri = leafNode.baseUri();
        Assert.assertEquals("", baseUri);
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("https://www.example.com");
        // No assertion necessary as it is a noop method
    }

    @Test
    public void testChildNodeSize() {
        int size = leafNode.childNodeSize();
        Assert.assertEquals(0, size);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        List<Node> childNodes = leafNode.ensureChildNodes();
        Assert.fail("UnsupportedOperationException should have been thrown.");
    }
}