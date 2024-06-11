package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        // Test when value is null
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttributes());

        // Test when value is an instance of Attributes
        Attributes attributes = new Attributes();
        leafNode = new LeafNodeImpl(attributes);
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attributes());

        Attributes attributes = new Attributes();
        leafNode = new LeafNodeImpl(attributes);
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttributes());
        assertNull(leafNode.attributes());

        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertNotNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());

        String value = "Test Value";
        leafNode.coreValue(value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        String key = "key";
        String value = "value";
        assertNull(leafNode.attr(key));

        leafNode.attr(key, value);
        assertEquals(value, leafNode.attr(key));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        String key = "key";
        assertFalse(leafNode.hasAttr(key));

        leafNode.attr(key, "value");
        assertTrue(leafNode.hasAttr(key));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();

        String key = "key";
        assertNull(leafNode.removeAttr(key));

        leafNode.attr(key, "value");
        assertNotNull(leafNode.removeAttr(key));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();

        String key = "key";
        assertNull(leafNode.absUrl(key));

        leafNode.attr(key, "value");
        assertNull(leafNode.absUrl(key));
    }

    @Test
    public void testBaseUri() {
        // Test when parent is null
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("", leafNode.baseUri());

        // Test when parent is not null
        Node parent = new Element("div");
        parent.setBaseUri("http://example.com");
        leafNode.setParentNode(parent);
        assertEquals("http://example.com", leafNode.baseUri());
    }

    class LeafNodeImpl extends LeafNode {
        LeafNodeImpl() {
            super();
        }

        LeafNodeImpl(Attributes attributes) {
            super();
            this.value = attributes;
        }

        @Override
        public String nodeName() {
            return "";
        }
    }
}