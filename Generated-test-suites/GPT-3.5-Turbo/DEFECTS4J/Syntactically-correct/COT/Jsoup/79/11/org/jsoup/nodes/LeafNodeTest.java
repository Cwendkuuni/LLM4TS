import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

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
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(leafNode.hasAttributes());
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        String value = "test";
        leafNode.coreValue(value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttrSingleValue() {
        String key = "key";
        String value = "value";
        leafNode.attr(key, value);
        assertTrue(leafNode.hasAttr(key));
        assertEquals(value, leafNode.attr(key));
    }

    @Test
    public void testAttrSameKey() {
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";
        leafNode.attr(key, value1);
        assertTrue(leafNode.hasAttr(key));
        assertEquals(value1, leafNode.attr(key));

        leafNode.attr(key, value2);
        assertEquals(value2, leafNode.attr(key));
    }

    @Test
    public void testAttrDifferentKey() {
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        leafNode.attr(key1, value1);
        assertTrue(leafNode.hasAttr(key1));
        assertEquals(value1, leafNode.attr(key1));

        leafNode.attr(key2, value2);
        assertTrue(leafNode.hasAttr(key2));
        assertEquals(value2, leafNode.attr(key2));
    }

    @Test
    public void testHasAttr() {
        String key = "key";
        assertFalse(leafNode.hasAttr(key));

        leafNode.attr(key, "value");
        assertTrue(leafNode.hasAttr(key));
    }

    @Test
    public void testRemoveAttr() {
        String key = "key";
        leafNode.attr(key, "value");
        assertTrue(leafNode.hasAttr(key));

        leafNode.removeAttr(key);
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    public void testAbsUrl() {
        String key = "key";
        assertNull(leafNode.absUrl(key));

        leafNode.attr(key, "value");
        assertNull(leafNode.absUrl(key));
    }

    @Test
    public void testBaseUri() {
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

    private static class LeafNodeImpl extends LeafNode {
        @Override
        public String nodeName() {
            return "test";
        }
    }
}