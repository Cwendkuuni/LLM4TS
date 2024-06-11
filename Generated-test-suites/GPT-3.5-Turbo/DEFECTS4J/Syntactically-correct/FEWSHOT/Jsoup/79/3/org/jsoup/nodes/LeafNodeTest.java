package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private static class LeafNodeImpl extends LeafNode {

        @Override
        public String nodeName() {
            return "test";
        }
    }

    @Test
    public void testHasAttributes() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertFalse(node1.hasAttributes());

        node1.value = new Attributes();
        assertTrue(node1.hasAttributes());

        node1.value = "test";
        assertFalse(node1.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.attributes());

        node1.value = new Attributes();
        assertNotNull(node1.attributes());

        node1.value = "test";
        assertNull(node1.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.coreValue());

        node1.value = "test";
        assertEquals("test", node1.coreValue());

        node1.value = new Attributes();
        assertNull(node1.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.coreValue());

        node1.coreValue("test");
        assertEquals("test", node1.value);
    }

    @Test
    public void testAttr() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.attr("key"));

        node1.value = new Attributes();
        assertNull(node1.attr("key"));

        node1.value = "value";
        assertNull(node1.attr("key"));

        node1.value = "key";
        assertNull(node1.attr("key"));

        node1.value = new Attributes();
        node1.value.put("key", "value");
        assertEquals("value", node1.attr("key"));
    }

    @Test
    public void testSetAttr() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.value);

        node1.attr("key", "value");
        assertEquals("value", node1.value);

        node1.attr("key", "new value");
        assertEquals("new value", node1.value);
    }

    @Test
    public void testHasAttr() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertFalse(node1.hasAttr("key"));

        node1.value = new Attributes();
        assertFalse(node1.hasAttr("key"));

        node1.value = "value";
        assertFalse(node1.hasAttr("key"));

        node1.value = "key";
        assertFalse(node1.hasAttr("key"));

        node1.value = new Attributes();
        node1.value.put("key", "value");
        assertTrue(node1.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.value);

        node1.removeAttr("key");
        assertNull(node1.value);

        node1.value = new Attributes();
        assertNull(node1.value);

        node1.value = "value";
        assertNull(node1.value);

        node1.value = "key";
        assertNull(node1.value);

        node1.value = new Attributes();
        node1.value.put("key", "value");
        assertEquals(1, node1.attributes().size());

        node1.removeAttr("key");
        assertEquals(0, node1.attributes().size());
    }

    @Test
    public void testAbsUrl() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.absUrl("key"));

        node1.value = new Attributes();
        assertNull(node1.absUrl("key"));

        node1.value = "value";
        assertNull(node1.absUrl("key"));

        node1.value = "key";
        assertNull(node1.absUrl("key"));

        node1.value = new Attributes();
        node1.value.put("key", "value");
        assertNull(node1.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertEquals("", node1.baseUri());

        node1.setParentNode(new Element("parent"));
        assertEquals("", node1.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertNull(node1.baseUri());

        node1.doSetBaseUri("baseUri");
        assertNull(node1.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        assertEquals(0, node1.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNodeImpl node1 = new LeafNodeImpl();
        node1.ensureChildNodes();
    }
}