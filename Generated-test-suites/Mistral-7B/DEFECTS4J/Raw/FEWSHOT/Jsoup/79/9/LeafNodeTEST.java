package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class LeafNodeTest {

    LeafNode node;

    @Before
    public void setup() {
        node = new LeafNode() {
            @Override
            public String nodeName() {
                return "test";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(node.hasAttributes());
        node.coreValue("value");
        assertTrue(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        node.coreValue("value");
        Attributes attributes = node.attributes();
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("test"));
        assertEquals("value", node.attr("test"));
    }

    @Test
    public void testEnsureAttributes() {
        node.coreValue("value");
        node.ensureAttributes();
        Attributes attributes = node.attributes();
        assertEquals("value", attributes.get("test"));
        assertEquals("value", node.attr("test"));
    }

    @Test
    public void testCoreValue() {
        node.coreValue("value");
        assertEquals("value", node.coreValue());
        node.coreValue("newValue");
        assertEquals("newValue", node.coreValue());
    }

    @Test
    public void testAttr() {
        node.coreValue("value");
        assertEquals("value", node.attr("test"));
        node.attr("test", "newValue");
        assertEquals("newValue", node.attr("test"));
        assertEquals("newValue", node.coreValue());
    }

    @Test
    public void testAttrWithNullKey() {
        node.coreValue("value");
        assertNull(node.attr(null));
    }

    @Test
    public void testAttrWithEmptyStringKey() {
        node.coreValue("value");
        assertEquals("value", node.attr(""));
    }

    @Test
    public void testAttrWithSameNameAsNodeName() {
        node.coreValue("value");
        node.attr("test", "newValue");
        assertEquals("newValue", node.attr("test"));
        assertEquals("newValue", node.coreValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        node.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        node.ensureChildNodes();
    }
}