import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {
    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNodeImpl();
    }

    // Test hasAttributes method
    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    // Test attributes method
    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());
    }

    // Test coreValue method
    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
    }

    // Test coreValue with value set
    @Test
    public void testCoreValueWithValue() {
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    // Test attr method with null key
    @Test(expected = IllegalArgumentException.class)
    public void testAttrNullKey() {
        leafNode.attr(null);
    }

    // Test attr method without attributes
    @Test
    public void testAttrNoAttributes() {
        assertNull(leafNode.attr("key"));
    }

    // Test attr method with key equals to node name
    @Test
    public void testAttrWithNodeName() {
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr(leafNode.nodeName()));
    }

    // Test attr method with key not equals to node name
    @Test
    public void testAttrWithDifferentKey() {
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    // Test attr method when attributes already exist
    @Test
    public void testAttrWithExistingAttributes() {
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");
        assertEquals("value1", leafNode.attr("key1"));
        assertEquals("value2", leafNode.attr("key2"));
    }

    // Test hasAttr method
    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
    }

    // Test removeAttr method
    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr("key"));
    }

    // Test absUrl method
    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));
    }

    // Test baseUri method without parent
    @Test
    public void testBaseUriWithoutParent() {
        assertEquals("", leafNode.baseUri());
    }

    // Test baseUri method with parent
    @Test
    public void testBaseUriWithParent() {
        Node parent = new NodeImpl();
        parent.setBaseUri("parentUri");
        leafNode.setParentNode(parent);
        assertEquals("parentUri", leafNode.baseUri());
    }

    // Test doSetBaseUri method
    @Test
    public void testDoSetBaseUri() {
        // No-op, nothing to test
    }

    // Test childNodeSize method
    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    // Test ensureChildNodes method
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }

    private class LeafNodeImpl extends LeafNode {
        @Override
        public String nodeName() {
            return "leaf";
        }
    }
}