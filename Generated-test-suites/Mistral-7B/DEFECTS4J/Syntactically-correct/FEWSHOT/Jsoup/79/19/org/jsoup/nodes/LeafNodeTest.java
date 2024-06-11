package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("foo");
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("tag");
        leafNodeWithAttributes.attr("attr", "value");
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("foo");
        assertNull(leafNode.attributes());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("tag");
        leafNodeWithAttributes.attr("attr1", "value1");
        leafNodeWithAttributes.attr("attr2", "value2");
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.coreValue("bar");
        assertEquals("bar", leafNode.attr(Node.NODE_NAME));
        assertEquals("bar", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("tag");
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        leafNodeWithAttributes.value = attributes;
        assertEquals(attributes, leafNodeWithAttributes.attributes());
        assertEquals("value1", leafNodeWithAttributes.attr("attr1"));
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("foo");
        assertEquals("foo", leafNode.attr(Node.NODE_NAME));
        leafNode.coreValue("bar");
        assertEquals("bar", leafNode.attr(Node.NODE_NAME));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("tag");
        leafNodeWithAttributes.attr("attr1", "value1");
        assertEquals("value1", leafNodeWithAttributes.attr("attr1"));
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.attr(Node.NODE_NAME, "bar");
        assertEquals("bar", leafNode.attr(Node.NODE_NAME));
        assertEquals("foo", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("tag");
        leafNodeWithAttributes.attr("attr1", "value1");
        leafNodeWithAttributes.attr("attr1", "value2");
        assertEquals("value2", leafNodeWithAttributes.attr("attr1"));
        assertEquals("value1", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("foo");
        assertFalse(leafNode.hasAttr(Node.NODE_NAME));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("tag");
        leafNodeWithAttributes.attr("attr", "value");
        assertTrue(leafNodeWithAttributes.hasAttr("attr"));
        assertFalse(leafNodeWithAttributes.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("tag");
        leafNodeWithAttributes.attr("attr1", "value1");
        leafNodeWithAttributes.removeAttr("attr1");
        assertNull(leafNodeWithAttributes.attr("attr1"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("foo");
        assertNull(leafNode.absUrl(Node.NODE_NAME));

        LeafNode leafNodeWithBaseUri = new Elements.SingleTagElement("tag") {
            @Override
            public String baseUri() {
                return "base";
            }
        };
        assertEquals("base:foo", leafNodeWithBaseUri.absUrl(Node.NODE_NAME));
    }

    @Test
    public void testBaseUri() {
        Element parent = new Elements.SingleTagElement("parent");
        TextNode leafNode = new TextNode("foo");
        parent.appendText(leafNode);
        assertEquals("", leafNode.baseUri());

        Element parentWithBaseUri = new Elements.SingleTagElement("parent") {
            @Override
            public String baseUri() {
                return "base";
            }
        };
        leafNode = new TextNode("foo");
        parentWithBaseUri.appendText(leafNode);
        assertEquals("base", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.ensureChildNodes();
    }
}