package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    private LeafNode node;

    @Before
    public void setUp() {
        node = new Elements.SingleElement<Element>("test");
    }

    @Test
    public void testHasAttributes() {
        assertFalse(node.hasAttributes());

        node.coreValue("test value");
        node.attr("test", "attribute value");
        assertTrue(node.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(node.attributes());

        node.coreValue("test value");
        node.attr("test", "attribute value");
        Attributes attributes = node.attributes();
        assertEquals("test", attributes.getClass().getSimpleName());
        assertEquals("test value", attributes.get("test"));
        assertNull(attributes.get("non-existent"));
    }

    @Test
    public void testEnsureAttributes() {
        node.coreValue("test value");
        node.ensureAttributes();
        assertSame(node.value, node.attributes());
    }

    @Test
    public void testCoreValue() {
        node.coreValue("test value");
        assertEquals("test value", node.coreValue());

        node.attr("test", "attribute value");
        assertEquals("test value", node.coreValue());

        node.coreValue("new test value");
        assertEquals("new test value", node.coreValue());
    }

    @Test
    public void testAttr() {
        node.attr("test", "attribute value");
        assertEquals("attribute value", node.attr("test"));

        node.coreValue("test value");
        assertEquals("test value", node.attr("test"));

        node.attr("test", null);
        assertNull(node.attr("test"));

        node.attr("test", "new attribute value");
        assertEquals("new attribute value", node.attr("test"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(node.hasAttr("test"));

        node.coreValue("test value");
        assertTrue(node.hasAttr("test"));

        node.attr("test", "attribute value");
        assertTrue(node.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        node.coreValue("test value");
        node.attr("test", "attribute value");
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));

        node.attr("test", "new attribute value");
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));

        node.coreValue("test value");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(node.absUrl("test"));

        node.attr("href", "http://test.com");
        assertEquals("http://test.com", node.absUrl("href"));

        node.coreValue("test value");
        assertNull(node.absUrl("test"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", node.baseUri());

        node.parent(new Elements.SingleElement<Element>("parent"));
        node.parent().attr("baseUrl", "http://base.com");
        assertEquals("http://base.com", node.baseUri());

        node.coreValue("test value");
        assertEquals("", node.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        node.setBaseUri("http://test.com");
    }

    @Test
    public void testChildNodeSize() {
        assertEquals(0, node.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        node.ensureChildNodes();
    }
}