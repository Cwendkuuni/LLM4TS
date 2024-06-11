package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Hello World");
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.attr("id", "test");
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Hello World");
        assertNull(leafNode.attributes());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.attr("id", "test");
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertEquals(1, attributes.size());
        assertEquals("test", attributes.get("id"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("Hello World");
        leafNode.coreValue("test");
        Attributes attributes = leafNode.attributes();
        assertEquals(1, attributes.size());
        assertEquals("test", attributes.get("test"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Hello World");
        assertEquals("Hello World", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("test");
        assertEquals("test", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new TextNode("Hello World");
        leafNode.coreValue("test");
        assertEquals("test", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.coreValue("test");
        assertEquals("test", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Hello World");
        assertEquals(null, leafNode.attr("test"));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.attr("id", "test");
        assertEquals("test", leafNodeWithAttributes.attr("id"));
        assertEquals(null, leafNodeWithAttributes.attr("name"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new Elements.SingleTagElement("div");
        leafNode.attr("id", "test");
        assertEquals("test", leafNode.attr("id"));

        LeafNode leafNodeWithExistingAttr = new Elements.SingleTagElement("div");
        leafNodeWithExistingAttr.attr("id", "test");
        leafNodeWithExistingAttr.attr("id", "newTest");
        assertEquals("newTest", leafNodeWithExistingAttr.attr("id"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Hello World");
        assertFalse(leafNode.hasAttr("test"));

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.attr("id", "test");
        assertTrue(leafNodeWithAttributes.hasAttr("id"));
        assertFalse(leafNodeWithAttributes.hasAttr("name"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        leafNodeWithAttributes.attr("id", "test");
        leafNodeWithAttributes.removeAttr("id");
        assertFalse(leafNodeWithAttributes.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Hello World");
        assertNull(leafNode.absUrl("test"));

        LeafNode leafNodeWithBaseUri = new Elements.SingleTagElement("img")
                .attr("src", "/test.html");
        Element parent = new Elements.SingleTagElement("div");
        parent.append(leafNodeWithBaseUri);
        Elements parentChildren = parent.children();
        assertEquals("/test.html", leafNodeWithBaseUri.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("Hello World");
        assertSame("", leafNode.baseUri());

        LeafNode leafNodeWithParent = new Elements.SingleTagElement("div")
                .attr("id", "parent")
                .append(new TextNode("Hello World"));
        assertEquals("parent", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Hello World");
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Hello World");
        leafNode.ensureChildNodes();
    }
}