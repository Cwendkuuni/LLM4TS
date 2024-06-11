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
                return "leafNode";
            }
        };
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
        leafNode.coreValue("test");
        assertEquals("test", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("key"));
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttrWithExistingAttributes() {
        leafNode.coreValue("existingValue");
        assertEquals("existingValue", leafNode.attr("leafNode"));
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));
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
}