import org.jsoup.nodes.LeafNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
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

        // Add an attribute
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertEquals(0, leafNode.attributes().size());

        // Add attributes
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");
        assertEquals(2, leafNode.attributes().size());

        // Check if attributes are set correctly
        assertTrue(leafNode.hasAttr("key1"));
        assertTrue(leafNode.hasAttr("key2"));
        assertFalse(leafNode.hasAttr("key3"));
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("key"));

        // Add an attribute
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        assertEquals(1, leafNode.attributes().size());

        // Remove the attribute
        leafNode.removeAttr("key");
        assertNull(leafNode.attr("key"));
        assertEquals(0, leafNode.attributes().size());

        // Set and get an attribute with the same key as the nodeName
        leafNode.attr("leafNode", "value");
        assertEquals("value", leafNode.attr("leafNode"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        // Set and get coreValue
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));

        // Add an attribute
        leafNode.attr("key", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());

        // Set baseUri in parent node
        LeafNode parentNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "parentNode";
            }

            @Override
            public String baseUri() {
                return "http://example.com";
            }
        };

        leafNode.setParentNode(parentNode);
        assertEquals("http://example.com", leafNode.baseUri());
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