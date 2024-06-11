
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        boolean hasAttributes = leafNode.hasAttributes();
        assertFalse(hasAttributes);
    }

    @Test
    public void testAttributes() {
        leafNode.value = new Attributes();
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        Object value = leafNode.value;
        assertNotNull(value);
        assertTrue(value instanceof Attributes);

        leafNode.ensureAttributes();
        Object newValue = leafNode.value;
        assertEquals(value, newValue);
    }

    @Test
    public void testCoreValue() {
        String coreValue = leafNode.coreValue();
        assertNull(coreValue);

        leafNode.attr("test", "value");
        coreValue = leafNode.coreValue();
        assertEquals("value", coreValue);
    }

    @Test
    public void testCoreValueSet() {
        leafNode.coreValue("new value");

        String coreValue = leafNode.coreValue();
        assertEquals("new value", coreValue);
    }

    @Test
    public void testAttr() {
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");

        String value1 = leafNode.attr("key1");
        assertEquals("value1", value1);

        String value2 = leafNode.attr("key2");
        assertEquals("value2", value2);

        String value3 = leafNode.attr("key3");
        assertEquals("", value3);
    }

    @Test
    public void testAttrWithNullKey() {
        try {
            leafNode.attr(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testAttrWithValue() {
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.value);
    }

    @Test
    public void testHasAttr() {
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");

        boolean hasAttr1 = leafNode.hasAttr("key1");
        assertTrue(hasAttr1);

        boolean hasAttr2 = leafNode.hasAttr("key2");
        assertTrue(hasAttr2);

        boolean hasAttr3 = leafNode.hasAttr("key3");
        assertFalse(hasAttr3);
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("key1", "value1");
        leafNode.attr("key2", "value2");

        leafNode.removeAttr("key1");
        boolean hasAttr1 = leafNode.hasAttr("key1");
        assertFalse(hasAttr1);

        leafNode.removeAttr("key2");
        boolean hasAttr2 = leafNode.hasAttr("key2");
        assertFalse(hasAttr2);
    }

    @Test
    public void testAbsUrl() {
        leafNode.attr("absUrl", "http://www.example.com");
        String absUrl = leafNode.absUrl("absUrl");
        assertEquals("http://www.example.com", absUrl);
    }

    @Test
    public void testBaseUri() {
        leafNode.setParentNode(new Node() {
            @Override
            public String baseUri() {
                return "http://www.example.com";
            }
        });

        String baseUri = leafNode.baseUri();
        assertEquals("http://www.example.com", baseUri);
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://www.example.com");
        // No-op
    }

    @Test
    public void testChildNodeSize() {
        int childNodeSize = leafNode.childNodeSize();
        assertEquals(0, childNodeSize);
    }

    @Test
    public void testEnsureChildNodes() {
        try {
            leafNode.ensureChildNodes();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e.getMessage());
        }
    }
}

