import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.helper.Validate;
import java.util.List;
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
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.attributes().isEmpty());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.attr("leaf", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
        assertEquals("value", leafNode.attr("leaf"));
    }

    @Test
    public void testAttr() {
        assertEquals("", leafNode.attr("leaf"));
        leafNode.attr("leaf", "value");
        assertEquals("value", leafNode.attr("leaf"));
    }

    @Test
    public void testAttrWithInvalidKey() {
        try {
            leafNode.attr(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Key must not be null", e.getMessage());
        }
    }

    @Test
    public void testAttrWithNoAttributes() {
        assertEquals("", leafNode.attr("leaf"));
        assertEquals("default", leafNode.attr("default", "default"));
    }

    @Test
    public void testAttrWithExistingAttributeName() {
        leafNode.attr("leaf", "value");
        assertEquals("value", leafNode.attr("leaf"));
        leafNode.attr("leaf", "newValue");
        assertEquals("newValue", leafNode.attr("leaf"));
    }

    @Test
    public void testAttrWithNewAttributeName() {
        leafNode.attr("newAttr", "value");
        assertEquals("value", leafNode.attr("newAttr"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("leaf"));
        leafNode.attr("leaf", "value");
        assertTrue(leafNode.hasAttr("leaf"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("leaf", "value");
        assertTrue(leafNode.hasAttr("leaf"));
        leafNode.removeAttr("leaf");
        assertFalse(leafNode.hasAttr("leaf"));
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", leafNode.absUrl("leaf"));
        leafNode.attr("leaf", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("leaf"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        // No operation, should not throw any exception
        leafNode.doSetBaseUri("baseUri");
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