import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;
    private Attributes attributes;

    @Before
    public void setUp() {
        leafNode = new LeafNodeTestImpl();
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        leafNode.coreValue("coreValue");
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());

        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());

        leafNode.coreValue("value");
        assertTrue(leafNode.attributes() instanceof Attributes);
        assertEquals("value", leafNode.attributes().get("nodeName"));
    }

    @Test
    public void testEnsureAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());

        leafNode.coreValue("value");
        assertFalse(leafNode.hasAttributes());
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertEquals("value", leafNode.attributes().get("nodeName"));
    }

    @Test
    public void testCoreValue() {
        assertEquals("coreValue", leafNode.coreValue());

        leafNode.coreValue("newCoreValue");
        assertEquals("newCoreValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertEquals("key1", leafNode.attr("key1"));
        assertEquals(EmptyString, leafNode.attr("key2"));

        leafNode.coreValue("value");
        assertEquals(EmptyString, leafNode.attr("key1"));
        assertEquals("value", leafNode.attr("nodeName"));
    }

    @Test
    public void testAttrWithKeyAndValue() {
        assertEquals(leafNode, leafNode.attr("key1", "new value1"));
        assertEquals("new value1", leafNode.attr("key1"));

        leafNode.coreValue("value");
        assertEquals(leafNode, leafNode.attr("key3", "new value3"));
        assertEquals(EmptyString, leafNode.attr("key3"));
        assertTrue(leafNode.hasAttributes());
        assertEquals("value", leafNode.attributes().get("key3"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key1"));

        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.hasAttr("nodeName"));
        assertFalse(leafNode.hasAttr("key1"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttr("key1"));

        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttributes());
        assertNull(leafNode.removeAttr("key1"));
        assertTrue(leafNode.hasAttr("nodeName"));
        assertNull(leafNode.removeAttr("nodeName"));
        assertFalse(leafNode.hasAttr("nodeName"));
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", leafNode.absUrl("key1"));

        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttributes());
        assertNull(leafNode.absUrl("key1"));
        assertNull(leafNode.absUrl("nodeName"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());

        LeafNode parent = new LeafNodeTestImpl();
        parent.coreValue("parentValue");
        leafNode.setParentNode(parent);
        assertEquals("parentValue", leafNode.baseUri());

        parent.setParentNode(new LeafNodeTestImpl());
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("baseUri");
        // No operation to test
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
        // Exception should be thrown
    }

    private class LeafNodeTestImpl extends LeafNode {
        @Override
        public String nodeName() {
            return "nodeName";
        }
    }
}