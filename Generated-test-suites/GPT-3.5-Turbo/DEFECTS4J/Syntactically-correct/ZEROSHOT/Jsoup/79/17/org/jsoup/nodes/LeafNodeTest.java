import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertFalse(leafNode.hasAttributes());

        leafNode.coreValue("testValue");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.attributes());

        leafNode.coreValue("testValue");
        assertNotNull(leafNode.attributes());

        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.coreValue());

        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.coreValue());

        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());

        leafNode.coreValue("newValue");
        assertEquals("newValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.attr("key1"));
        assertNull(leafNode.attr("key2"));

        leafNode.coreValue("value1");
        assertEquals("value1", leafNode.attr("key1"));
        assertNull(leafNode.attr("key2"));

        leafNode.attr("key2", "value2");
        assertEquals("value1", leafNode.attr("key1"));
        assertEquals("value2", leafNode.attr("key2"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.attr("key1"));

        leafNode.coreValue("value1");
        assertEquals("value1", leafNode.attr("key1"));

        leafNode.removeAttr("key1");
        assertNull(leafNode.attr("key1"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertFalse(leafNode.hasAttr("key1"));

        leafNode.coreValue("value1");
        assertTrue(leafNode.hasAttr("key1"));

        leafNode.removeAttr("key1");
        assertFalse(leafNode.hasAttr("key1"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertNull(leafNode.absUrl("key1"));

        leafNode.coreValue("value1");
        assertNull(leafNode.absUrl("key1"));

        leafNode.attr("key1", "https://www.example.com");
        assertEquals("https://www.example.com", leafNode.absUrl("key1"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertEquals("", leafNode.baseUri());

        LeafNode parent = new LeafNodeTestImpl();
        parent.coreValue("https://www.example.com");
        leafNode.setParentNode(parent);
        assertEquals("https://www.example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNodeTestImpl();
        assertEquals("", leafNode.baseUri());

        leafNode.doSetBaseUri("https://www.example.com");
        assertEquals("", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeTestImpl();
        leafNode.ensureChildNodes();
    }

    private static class LeafNodeTestImpl extends LeafNode {

    }
}