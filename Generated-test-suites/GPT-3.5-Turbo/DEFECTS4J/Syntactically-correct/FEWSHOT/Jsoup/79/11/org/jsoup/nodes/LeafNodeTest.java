import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testLeafNode";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());

        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertFalse(leafNode.hasAttributes());

        Attributes attributes = leafNode.attributes();
        assertTrue(leafNode.hasAttributes());
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttrWithDefaultValue() {
        assertEquals("", leafNode.attr("key"));

        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
        assertEquals("", leafNode.attr("nonExistingKey"));
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

        leafNode.attr("key", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUriWithParent() {
        Node parent = new Node() {
            @Override
            public String baseUri() {
                return "http://example.com";
            }

            @Override
            protected void doSetBaseUri(String baseUri) {
                // noop
            }
        };
        leafNode.setParent(parent);
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testBaseUriWithoutParent() {
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