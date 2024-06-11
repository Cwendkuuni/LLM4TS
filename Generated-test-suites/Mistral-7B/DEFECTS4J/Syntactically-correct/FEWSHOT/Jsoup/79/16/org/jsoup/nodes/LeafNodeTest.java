package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new ElementNode("root");
        assertFalse(node.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        node.setValue(attributes);

        assertTrue(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode node = new ElementNode("root");
        Attributes attributes = new Attributes();
        node.setValue(attributes);
        attributes.put("attr1", "value1");

        assertSame(attributes, node.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode node = new ElementNode("root");
        assertNull(node.value);
        node.ensureAttributes();
        assertSame(new Attributes(), node.value);

        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        node.setValue(attributes);
        node.ensureAttributes();
        assertSame(attributes, node.value);
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new ElementNode("root", "value");
        assertEquals("value", node.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode node = new ElementNode("root");
        node.coreValue("newValue");
        assertEquals("newValue", node.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode node = new ElementNode("root");
        node.attr("attr1", "value1");
        assertEquals("value1", node.attr("attr1"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode node = new ElementNode("root");
        node.attr("attr1", "value1");
        assertEquals("value1", node.attr("attr1"));

        node.attr("attr1", "value2");
        assertEquals("value2", node.attr("attr1"));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new ElementNode("root");
        assertFalse(node.hasAttr("attr1"));

        node.attr("attr1", "value1");
        assertTrue(node.hasAttr("attr1"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new ElementNode("root");
        node.attr("attr1", "value1");
        assertTrue(node.hasAttr("attr1"));
        node.removeAttr("attr1");
        assertFalse(node.hasAttr("attr1"));
    }

    @Test
    public void testAttrWithSameNameAsNodeName() {
        LeafNode node = new ElementNode("attr", "value");
        assertEquals("value", node.attr("attr"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodes() {
        LeafNode node = new ElementNode("root");
        node.ensureChildNodes();
    }
}