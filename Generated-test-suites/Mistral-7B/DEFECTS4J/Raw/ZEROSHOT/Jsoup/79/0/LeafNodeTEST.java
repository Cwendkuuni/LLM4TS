import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import static org.jsoup.helper.Validate.notNull;

public class LeafNodeTest {

    @Test
    public void testLeafNodeHasAttributes() {
        LeafNode leafNode = new TextNode("test");
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testLeafNodeGetAttributes() {
        LeafNode leafNode = new TextNode("test");
        Attributes attributes = leafNode.attributes();
        assertNull(attributes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLeafNodeEnsureAttributes() {
        LeafNode leafNode = new TextNode("test");
        leafNode.ensureAttributes();
    }

    @Test
    public void testLeafNodeCoreValue() {
        LeafNode leafNode = new TextNode("test");
        String coreValue = leafNode.coreValue();
        assertEquals("test", coreValue);
    }

    @Test
    public void testLeafNodeSetCoreValue() {
        LeafNode leafNode = new TextNode("test");
        leafNode.coreValue("newTest");
        String coreValue = leafNode.coreValue();
        assertEquals("newTest", coreValue);
    }

    @Test
    public void testLeafNodeAttr() {
        LeafNode leafNode = new TextNode("test");
        String attrValue = leafNode.attr("test");
        assertEquals("test", attrValue);
        String nodeName = leafNode.nodeName();
        assertEquals("test", attrValue); // check for node name as key
        assertNull(leafNode.attr("nonexistentKey"));
    }

    @Test
    public void testLeafNodeSetAttr() {
        LeafNode leafNode = new TextNode("test");
        leafNode.attr("key", "value");
        String attrValue = leafNode.attr("key");
        assertEquals("value", attrValue);
    }

    @Test
    public void testLeafNodeHasAttr() {
        LeafNode leafNode = new TextNode("test").attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        assertFalse(leafNode.hasAttr("nonexistentKey"));
    }

    @Test
    public void testLeafNodeRemoveAttr() {
        LeafNode leafNode = new TextNode("test").attr("key", "value");
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testLeafNodeAbsUrl() {
        LeafNode leafNode = new TextNode("test");
        String absUrl = leafNode.absUrl("test");
        assertEquals("", absUrl);
    }

    @Test
    public void testLeafNodeBaseUri() {
        LeafNode leafNode = new TextNode("test");
        leafNode.parent(new TextNode("parent"));
        String baseUri = leafNode.baseUri();
        assertEquals("parent", baseUri);
    }

    @Test
    public void testLeafNodeChildNodeSize() {
        LeafNode leafNode = new TextNode("test");
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLeafNodeEnsureChildNodes() {
        LeafNode leafNode = new TextNode("test");
        leafNode.ensureChildNodes();
    }

    @Test
    public void testLeafNodeValidateNotNull() {
        String key = null;
        try {
            new LeafNode().attr(key);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }
}