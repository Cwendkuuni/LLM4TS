package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        assertFalse(node.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        node.coreValue("test");
        assertTrue(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        assertNull(node.attributes());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        node.coreValue("test");
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("test");
        Attributes attributes = new Attributes();
        attributes.put("test2", "value2");
        node.attributes().put("test2", "value2");
        assertEquals(attributes, node.attributes());

        node.ensureAttributes();
        assertEquals(attributes, node.attributes());

        node.coreValue("test3");
        assertNull(node.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("value");
        assertEquals("value", node.attr(node.nodeName()));
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("value");
        node.coreValue("newValue");
        assertEquals("newValue", node.attr(node.nodeName()));
    }

    @Test
    public void testAttr() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("test");
        assertEquals("test", node.attr("test"));
        assertEquals("", node.attr("nonExistentAttr"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        node.attributes().put("test", "value");
        assertEquals("value", node.attr("test"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("test");
        node.attr("test", "value");
        assertEquals("value", node.attr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        node.attributes().put("test", "value");
        node.attr("test", "newValue");
        assertEquals("newValue", node.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("test");
        assertFalse(node.hasAttr("nonExistentAttr"));
        assertTrue(node.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("test");
        node.attributes().put("test", "value");
        node.removeAttr("test");
        assertNull(node.attr("test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("test");
        String baseUri = "baseUri";
        assertEquals(baseUri, node.absUrl(node.nodeName()));
    }

    @Test
    public void testBaseUri() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        assertEmpty(node.baseUri());

        Elements parent = new Elements.SingleElement<Element>("parent").first();
        parent.coreValue("parent");
        node.parent(parent);
        assertEquals(parent.baseUri(), node.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.coreValue("test");
        node.doSetBaseUri("baseUri");
        assertEquals("baseUri", node.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        assertEquals(0, node.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode node = new Elements.SingleElement<Element>("test").first();
        node.ensureChildNodes();
    }
}