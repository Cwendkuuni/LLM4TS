import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {
                // Do nothing
            }

            @Override
            protected List<Node> ensureChildNodes() {
                throw new UnsupportedOperationException("Leaf Nodes do not have child nodes.");
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

        leafNode.coreValue("test");
        assertTrue(leafNode.hasAttributes());
        assertEquals("test", leafNode.attributes().get("nodeName"));
    }

    @Test
    public void testCoreValue() {
        leafNode.coreValue("test");
        assertEquals("test", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("test"));
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr("nodeName"));
    }

    @Test
    public void testAttrWithKeyValue() {
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
        assertEquals("value", leafNode.attributes().get("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("test"));
        leafNode.attr("test", "value");
        assertTrue(leafNode.hasAttr("test"));
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
        leafNode.attr("href", "https://www.example.com");
        assertEquals("https://www.example.com", leafNode.absUrl("href"));
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