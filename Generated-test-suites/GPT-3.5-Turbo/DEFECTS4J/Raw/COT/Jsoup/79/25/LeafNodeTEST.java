
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode();
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode();
        assertNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode();
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.coreValue("test");
        assertEquals("test", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode();
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testSetAttr() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode();
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("key", "value");
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode();
        assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode();
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode();
        // Method should not have any effect, so no direct tests needed
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode();
        leafNode.ensureChildNodes();
    }

}
