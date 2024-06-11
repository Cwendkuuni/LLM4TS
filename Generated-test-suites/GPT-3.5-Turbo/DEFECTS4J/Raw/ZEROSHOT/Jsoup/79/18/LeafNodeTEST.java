import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertFalse(leafNode.hasAttributes());
        assertTrue(leafNode.attributes() instanceof Attributes);
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr(null));

        assertNull(leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        assertEquals("", leafNode.attr("nodeName"));

        leafNode.attr("nodeName", "nodeValue");
        assertEquals("nodeValue", leafNode.attr("nodeName"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr(null));

        assertFalse(leafNode.hasAttr("key"));

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        assertFalse(leafNode.hasAttr("nodeName"));

        leafNode.attr("nodeName", "nodeValue");
        assertTrue(leafNode.hasAttr("nodeName"));
    }

    @Test
    public void testRemoveAttr() {
        assertNull(leafNode.removeAttr(null));

        assertNull(leafNode.removeAttr("key"));

        leafNode.attr("key", "value");
        assertEquals(leafNode, leafNode.removeAttr("key"));

        assertNull(leafNode.attr("key"));

        leafNode.attr("nodeName", "nodeValue");
        assertEquals(leafNode, leafNode.removeAttr("nodeName"));

        assertNull(leafNode.attr("nodeName"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl(null));

        assertNull(leafNode.absUrl("key"));

        leafNode.attr("key", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        leafNode.setBaseUri("http://example.com");
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}