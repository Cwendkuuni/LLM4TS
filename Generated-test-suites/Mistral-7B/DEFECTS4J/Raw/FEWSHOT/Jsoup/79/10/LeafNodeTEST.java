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

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("hello");
        leafNodeWithAttributes.attr("id", "test");
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("foo");
        assertNull(leafNode.attributes());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("hello");
        leafNodeWithAttributes.attr("id", "test");
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertEquals("id", attributes.keySet().iterator().next());
        assertEquals("test", attributes.get("id"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("foo");
        assertEquals("foo", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("hello");
        assertEquals("hello", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testCoreValue_Setter() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.coreValue("bar");
        assertEquals("bar", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new TextNode("foo");
        leafNodeWithAttributes.value = new Attributes();
        leafNodeWithAttributes.coreValue("hello");
        assertEquals("hello", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("foo");
        assertEquals("", leafNode.attr(""));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("hello");
        leafNodeWithAttributes.attr("id", "test");
        assertEquals("test", leafNodeWithAttributes.attr("id"));
    }

    @Test
    public void testAttr_Setter() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.attr("id", "test");
        assertEquals("test", leafNode.attr("id"));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("hello");
        leafNodeWithAttributes.attr("id", "test");
        assertEquals("test", leafNodeWithAttributes.attr("id"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("foo");
        assertFalse(leafNode.hasAttr(""));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("hello");
        leafNodeWithAttributes.attr("id", "test");
        assertTrue(leafNodeWithAttributes.hasAttr("id"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("foo");
        assertNull(leafNode.removeAttr(""));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("hello");
        leafNodeWithAttributes.attr("id", "test");
        assertNull(leafNodeWithAttributes.removeAttr(""));
        assertNull(leafNodeWithAttributes.removeAttr("id"));
        assertFalse(leafNodeWithAttributes.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("foo");
        assertNull(leafNode.absUrl(""));

        LeafNode leafNodeWithBaseUri = new Elements.SingleTagElement("a") {
            @Override
            public String absUrl(String key) {
                return "http://example.com";
            }
        };
        leafNodeWithBaseUri.coreValue("foo");
        assertEquals("http://example.com/foo", leafNodeWithBaseUri.absUrl(""));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("foo");
        assertEquals("", leafNode.baseUri());

        Element parent = new Elements.SingleTagElement("div");
        LeafNode leafNodeWithParent = new TextNode("foo");
        parent.appendChild(leafNodeWithParent);
        assertEquals(parent.baseUri(), leafNodeWithParent.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.setBaseUri("http://example.com");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("foo");
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("foo");
        leafNode.ensureChildNodes();
    }
}