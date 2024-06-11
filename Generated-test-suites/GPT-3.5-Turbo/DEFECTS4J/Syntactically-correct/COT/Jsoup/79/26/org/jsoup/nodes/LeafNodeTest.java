
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Test with no attributes
        LeafNode leafNode = new TestLeafNode();
        assertFalse(leafNode.hasAttributes());

        // Test with attributes
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        // Test with no attributes
        LeafNode leafNode = new TestLeafNode();
        assertNull(leafNode.attributes());

        // Test with attributes
        leafNode.attr("key", "value");
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TestLeafNode();
        assertNull(leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttrWithNullKey() {
        LeafNode leafNode = new TestLeafNode();
        try {
            leafNode.attr(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new TestLeafNode();
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));

        leafNode.attr("key", "new_value");
        assertEquals("new_value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TestLeafNode();
        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TestLeafNode();
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));

        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TestLeafNode();
        assertNull(leafNode.absUrl("key"));

        leafNode.attr("key", "https://example.com/");
        assertEquals("https://example.com/", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TestLeafNode();
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new TestLeafNode();
        leafNode.doSetBaseUri("https://example.com/");
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TestLeafNode();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TestLeafNode();
        leafNode.ensureChildNodes();
    }

    private static class TestLeafNode extends LeafNode {
        @Override
        public String nodeName() {
            return "testNode";
        }
    }
}
