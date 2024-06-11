import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();

        assertNull(leafNode.attributes());
        
        leafNode.attr("key1", "value1");
        assertTrue(leafNode.hasAttributes());
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNodeImpl();

        assertFalse(leafNode.hasAttributes());

        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();

        assertNull(leafNode.coreValue());

        leafNode.coreValue("value1");
        assertEquals("value1", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        assertNull(leafNode.attr("key1"));

        leafNode.attr("key1", "value1");
        assertEquals("value1", leafNode.attr("key1"));
        assertNull(leafNode.attr("key2"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        assertFalse(leafNode.hasAttr("key1"));

        leafNode.attr("key1", "value1");
        assertTrue(leafNode.hasAttr("key1"));
        assertFalse(leafNode.hasAttr("key2"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        assertFalse(leafNode.hasAttr("key1"));

        leafNode.attr("key1", "value1");
        assertTrue(leafNode.hasAttr("key1"));

        leafNode.removeAttr("key1");
        assertFalse(leafNode.hasAttr("key1"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();

        assertNull(leafNode.absUrl("key1"));

        leafNode.attr("key1", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("key1"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();

        assertEquals("", leafNode.baseUri());

        Element parent = new Element("parent");
        parent.appendChild(leafNode);
        parent.setBaseUri("http://example.com");

        assertEquals("http://example.com", leafNode.baseUri());
    }

    private class LeafNodeImpl extends LeafNode {
        @Override
        public String nodeName() {
            return "leafNode";
        }
    }
}