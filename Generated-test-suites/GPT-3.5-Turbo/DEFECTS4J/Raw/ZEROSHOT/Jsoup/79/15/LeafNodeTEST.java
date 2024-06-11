import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
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

        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());

        leafNode.value = new Attributes();
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertFalse(leafNode.hasAttributes());

        leafNode.value = "value";
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertEquals("value", leafNode.attributes().get("leafNode"));
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());

        leafNode.value = "value";
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        leafNode.coreValue("value");
        assertEquals("value", leafNode.attr("leafNode"));
    }

    @Test
    public void testAttr() {
        leafNode.value = null;
        assertEquals("default", leafNode.attr("default"));
        assertNull(leafNode.attr("leafNode"));

        leafNode.value = new Attributes();
        assertNull(leafNode.attr("default"));
        assertNull(leafNode.attr("leafNode"));

        leafNode.attr("leafNode", "value");
        assertEquals("value", leafNode.attr("leafNode"));
    }

    @Test
    public void testAttrWithMultipleAttributes() {
        leafNode.value = new Attributes();
        leafNode.attr("attribute1", "value1");
        leafNode.attr("attribute2", "value2");

        assertEquals("value1", leafNode.attr("attribute1"));
        assertEquals("value2", leafNode.attr("attribute2"));
    }

    @Test
    public void testAttrSet() {
        leafNode.value = null;
        leafNode.attr("leafNode", "value");
        assertEquals("value", leafNode.attr("leafNode"));

        leafNode.value = "default";
        leafNode.attr("leafNode", "value");
        assertEquals("value", leafNode.attr("leafNode"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("leafNode"));

        leafNode.value = new Attributes();
        assertFalse(leafNode.hasAttr("leafNode"));

        leafNode.attr("leafNode", "value");
        assertTrue(leafNode.hasAttr("leafNode"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.value = new Attributes();
        leafNode.attr("leafNode", "value");
        assertTrue(leafNode.hasAttr("leafNode"));

        leafNode.removeAttr("leafNode");
        assertFalse(leafNode.hasAttr("leafNode"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("leafNode"));

        leafNode.value = new Attributes();
        assertEquals("", leafNode.absUrl("leafNode"));

        leafNode.attr("leafNode", "https://www.example.com");
        assertEquals("https://www.example.com", leafNode.absUrl("leafNode"));
    }

    @Test
    public void testBaseUri() {
        assertTrue(leafNode.baseUri().isEmpty());

        LeafNode parent = new LeafNode() {
            @Override
            public String nodeName() {
                return "parent";
            }
        };
        parent.value = new Attributes();
        parent.attr("parent", "https://www.example.com");
        leafNode.setParentNode(parent);

        assertEquals("https://www.example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        // Do nothing, no implementation to test
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