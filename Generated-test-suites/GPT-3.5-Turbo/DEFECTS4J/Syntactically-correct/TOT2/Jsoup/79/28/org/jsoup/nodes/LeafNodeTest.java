import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetValue() {
        LeafNode leafNode = new ConcreteLeafNode();
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertEquals("", leafNode.attr("key"));
    }

    @Test
    public void testAttrKeyEqualsNodeName() {
        LeafNode leafNode = new ConcreteLeafNode();
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertNotNull(leafNode.removeAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertNotNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new ConcreteLeafNode();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new ConcreteLeafNode();
        leafNode.ensureChildNodes();
    }

    private static class ConcreteLeafNode extends LeafNode {
        @Override
        public String nodeName() {
            return "test";
        }
    }
}