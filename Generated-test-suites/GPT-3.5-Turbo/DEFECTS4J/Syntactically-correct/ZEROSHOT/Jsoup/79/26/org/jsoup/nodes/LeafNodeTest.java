import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "test";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(leafNode.hasAttributes());
        assertEquals(Attributes.class, attributes.getClass());
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttrWithoutAttributes() {
        assertNull(leafNode.attr("test"));
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr("test"));
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttrWithAttributes() {
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        assertNull(leafNode.attr("otherKey"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("test"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        assertFalse(leafNode.hasAttr("otherKey"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttr("test"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        leafNode.attr("key", "https://example.com");
        assertEquals("https://example.com", leafNode.absUrl("key"));
        assertNull(leafNode.absUrl("otherKey"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
        leafNode.setBaseUri("https://example.com");
        assertEquals("https://example.com", leafNode.baseUri());
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