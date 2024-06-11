import org.jsoup.helper.Validate;
import org.jsoup.nodes.LeafNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Assert.assertNotNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        Assert.assertNotNull(leafNode.attributes());
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
    public void testAttr() {
        Assert.assertNull(leafNode.attr("testAttribute"));
    }

    @Test
    public void testSetAttr() {
        leafNode.attr("testAttribute", "testValue");
        Assert.assertEquals("testValue", leafNode.attr("testAttribute"));
    }

    @Test
    public void testHasAttr() {
        Assert.assertFalse(leafNode.hasAttr("testAttribute"));
    }

    @Test
    public void testRemoveAttr() {
        Assert.assertNotNull(leafNode.removeAttr("testAttribute"));
    }

    @Test
    public void testAbsUrl() {
        Assert.assertNull(leafNode.absUrl("testAttribute"));
    }

    @Test
    public void testBaseUri() {
        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("testBaseUri");
        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        Assert.assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}