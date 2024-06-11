import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNodeImpl();
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        String value = "test value";
        leafNode.coreValue(value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttrWhenNoAttributes() {
        String key = "testKey";
        String value = "testValue";
        assertNull(leafNode.attr(key));
        leafNode.attr(key, value);
        assertEquals(value, leafNode.attr(key));
    }

    @Test
    public void testAttrWhenHasAttributes() {
        leafNode.ensureAttributes();
        String key = "testKey";
        String value = "testValue";
        leafNode.attr(key, value);
        assertEquals(value, leafNode.attr(key));
    }

    @Test
    public void testHasAttr() {
        leafNode.ensureAttributes();
        String key = "testKey";
        assertFalse(leafNode.hasAttr(key));
        leafNode.attr(key, "");
        assertTrue(leafNode.hasAttr(key));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.ensureAttributes();
        String key = "testKey";
        String value = "testValue";
        leafNode.attr(key, value);
        assertEquals(value, leafNode.attr(key));
        leafNode.removeAttr(key);
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    public void testAbsUrl() {
        leafNode.ensureAttributes();
        String key = "testKey";
        assertNull(leafNode.absUrl(key));
    }

    @Test
    public void testBaseUri() {
        assertTrue(leafNode.baseUri().isEmpty());
    }

    @Test
    public void testDoSetBaseUri() {
        // No operation, cannot be directly tested
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }

    private class LeafNodeImpl extends LeafNode {
        @Override
        public String nodeName() {
            return "testNodeName";
        }
    }
}