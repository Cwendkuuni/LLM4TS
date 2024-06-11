package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        assertFalse(node.hasAttributes());

        Attributes attributes = new Attributes();
        node.value = attributes;
        assertTrue(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        assertNull(node.attributes());

        Attributes attributes = new Attributes();
        node.value = attributes;
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        node.coreValue("foo");
        assertEquals("foo", node.coreValue());
    }

    @Test
    public void testCoreValueSetting() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        node.coreValue("foo");
        node.coreValue("bar");
        assertEquals("bar", node.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        node.attr("test", "foo");
        assertEquals("foo", node.attr("test"));
    }

    @Test
    public void testAttrSetting() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        node.attr("test", "foo");
        node.attr("test", "bar");
        assertEquals("bar", node.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        assertFalse(node.hasAttr("test"));

        node.attr("test", "foo");
        assertTrue(node.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        node.attr("test", "foo");
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testAttrGetNullKey() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        assertNull(node.attr(null));
    }

    @Test
    public void testAttrGetEmptyStringKey() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        assertSame(EmptyString, node.attr(""));
    }

    @Test
    public void testAttrGetUnknownNameKey() {
        LeafNode node = new Elements.SingleElement<LeafNode>(new LeafNode());
        node.value = new Attributes();
        assertNull(node.attr("unknown"));
    }
}