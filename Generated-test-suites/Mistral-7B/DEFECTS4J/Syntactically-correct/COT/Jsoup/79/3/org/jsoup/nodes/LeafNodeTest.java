import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("testAttribute", "testValue");
            }
        };

        assertTrue(leafNode.hasAttributes());

        leafNode = new LeafNode();
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("testAttribute", "testValue");
            }
        };

        Attributes attributes = leafNode.attributes();
        assertEquals("testValue", attributes.get("testAttribute"));

        leafNode = new LeafNode();
        assertNull(leafNode.attributes());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureAttributesWhenNoAttributes() {
        LeafNode leafNode = new LeafNode();
        leafNode.ensureAttributes();
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());

        leafNode = new LeafNode();
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("testAttribute", "testValue");
        assertEquals("testValue", leafNode.attr("testAttribute"));

        leafNode = new LeafNode();
        assertNull(leafNode.attr("testAttribute"));

        leafNode = new LeafNode();
        leafNode.attr("nodeName", "testValue");
        assertEquals("testValue", leafNode.attr("nodeName"));
    }

    @Test
    public void testAttrWithDuplicateKey() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("testAttribute", "testValue");
        leafNode.attr("testAttribute", "newTestValue");

        assertEquals("newTestValue", leafNode.attr("testAttribute"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("testAttribute", "testValue");
            }
        };

        assertTrue(leafNode.hasAttr("testAttribute"));

        leafNode = new LeafNode();
        assertFalse(leafNode.hasAttr("testAttribute"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("testAttribute", "testValue");
            }
        };

        leafNode.removeAttr("testAttribute");
        assertFalse(leafNode.hasAttr("testAttribute"));
    }

    @Test
    public void testAttrWithEmptyStringValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("testAttribute", "");
        assertEquals("", leafNode.attr("testAttribute"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrWithNullKey() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr(null, "testValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrWithNullValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("testAttribute", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode();
        assertEquals(0, leafNode.childNodeSize());
    }
}