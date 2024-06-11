import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;
    private Attributes attributes;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            String nodeName() {
                return "testName";
            }
        };
        attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        leafNode.value = attributes;
    }

    @Test
    public void testHasAttributes() {
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        // Make sure attributes are already set
        assertNotNull(leafNode.attributes());

        // Reset value to null and ensure attributes are created again
        leafNode.value = null;
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertEquals("testName", leafNode.attr("testName"));
    }

    @Test
    public void testCoreValue() {
        assertEquals("value1", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        assertEquals("value1", leafNode.coreValue());

        leafNode.coreValue("newValue");
        assertEquals("newValue", leafNode.coreValue());
    }

    @Test
    public void testAttrWithValue() {
        assertEquals("value1", leafNode.attr("attribute1"));
        assertEquals("", leafNode.attr("nonExistingAttribute"));

        leafNode.attr("attribute1", "newValue");
        assertEquals("newValue", leafNode.attr("attribute1"));
        assertEquals("", leafNode.attr("nonExistingAttribute"));
    }

    @Test
    public void testAttrWithoutValue() {
        assertNull(leafNode.attr(null));
        assertNull(leafNode.attr(""));

        leafNode.attr("attribute1", null);
        assertNull(leafNode.attr("attribute1"));
    }

    @Test
    public void testAttrWithNoAttributes() {
        leafNode.value = null;

        assertEquals("testName", leafNode.attr("testName"));
        assertEquals("", leafNode.attr("nonExistingAttribute"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(leafNode.hasAttr("attribute1"));
        assertFalse(leafNode.hasAttr("nonExistingAttribute"));
    }

    @Test
    public void testRemoveAttr() {
        assertTrue(leafNode.hasAttr("attribute1"));
        assertFalse(leafNode.hasAttr("nonExistingAttribute"));

        leafNode.removeAttr("attribute1");
        assertFalse(leafNode.hasAttr("attribute1"));
        assertFalse(leafNode.hasAttr("nonExistingAttribute"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("attribute1"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        // No operation, nothing to test
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