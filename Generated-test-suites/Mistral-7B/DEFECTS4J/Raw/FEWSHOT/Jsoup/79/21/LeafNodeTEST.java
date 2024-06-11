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
        LeafNode node = new Elements.SingleTagElement("div") {};
        assertFalse(node.hasAttributes());

        LeafNode nodeWithAttributes = new AttributesImpl();
        nodeWithAttributes.put("class", "foo");
        assertTrue(nodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        assertNull(node.attributes());

        LeafNode nodeWithAttributes = new AttributesImpl();
        nodeWithAttributes.put("class", "foo");
        assertEquals(nodeWithAttributes.attributes(), nodeWithAttributes.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        node.value = "foo";

        node.ensureAttributes();
        assertEquals(node.value, new AttributesImpl());
        assertEquals(node.attributes().size(), 1);
        assertEquals(node.attributes().get("class"), "foo");
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        node.coreValue("foo");
        assertEquals(node.coreValue(), "foo");
    }

    @Test
    public void testAttr() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        node.attr("class", "foo");
        assertEquals(node.attr("class"), "foo");

        node = new AttributesImpl();
        node.put("class", "foo");
        assertEquals(node.attr("class"), "foo");
    }

    @Test
    public void testAttrWithValue() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        node.attr("class", "foo", "bar");
        assertEquals(node.attr("class"), "bar");

        LeafNode nodeWithInitialValue = new AttributesImpl();
        nodeWithInitialValue.put("class", "foo");
        nodeWithInitialValue.attr("class", "bar");
        assertEquals(nodeWithInitialValue.attr("class"), "bar");
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        assertFalse(node.hasAttr("class"));

        LeafNode nodeWithClassAttribute = new AttributesImpl();
        nodeWithClassAttribute.put("class", "foo");
        assertTrue(nodeWithClassAttribute.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode nodeWithClassAttribute = new AttributesImpl();
        nodeWithClassAttribute.put("class", "foo");

        LeafNode nodeWithoutClassAttribute = nodeWithClassAttribute.removeAttr("class");
        assertFalse(nodeWithClassAttribute.hasAttr("class"));
        assertSame(nodeWithoutClassAttribute, nodeWithClassAttribute);
    }

    @Test
    public void testAbsUrl() {
        LeafNode node = new Elements.SingleTagElement("img") {};
        node.absUrl("src");
        assertNull(node.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new Elements.SingleTagElement("div") {};
        assertEmptyString(leafNode.baseUri());

        Node parentNode = new Elements.SingleTagElement("body") {};
        leafNode.parent(parentNode);
        assertEquals(parentNode.baseUri(), leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        node.setBaseUri("http://example.com");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        assertEquals(node.childNodeSize(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode node = new Elements.SingleTagElement("div") {};
        node.ensureChildNodes();
    }

    private void assertEmptyString(String string) {
        assertTrue(string.isEmpty());
        if (!string.equals("")) {
            fail("Expected an empty string, but got: " + string);
        }
    }
}