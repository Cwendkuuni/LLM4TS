import org.jsoup.nodes.LeafNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = Mockito.mock(LeafNode.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        String value = "test";
        leafNode.coreValue(value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        String key = "key";
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr(key));
        assertEquals("", leafNode.attr("differentKey"));
    }

    @Test
    public void testSetAttr() {
        String key = "key";
        String value = "value";
        leafNode.attr(key, value);
        assertEquals(value, leafNode.attr(key));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        assertEquals(leafNode.absUrl("key"), "");
        leafNode.attr("key", "http://example.com");
        assertEquals(leafNode.absUrl("key"), "http://example.com");
    }

    @Test
    public void testBaseUri() {
        assertEquals(leafNode.baseUri(), "");
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://example.com");
        assertEquals(leafNode.baseUri(), "");
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(leafNode.childNodeSize(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}