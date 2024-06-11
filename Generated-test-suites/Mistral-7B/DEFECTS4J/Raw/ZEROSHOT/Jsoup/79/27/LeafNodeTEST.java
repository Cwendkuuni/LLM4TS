package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;

import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Test text");
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl());
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Test text");
        assertNull(leafNode.attributes());

        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertEquals("value", attributes.get("data-test"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("Test text");
        assertNull(leafNode.value);

        leafNode.ensureAttributes();
        assertTrue(leafNode.value instanceof Attributes);
        Attributes attributes = (Attributes) leafNode.value;
        assertEquals(leafNode.nodeName(), attributes.get(leafNode.nodeName()));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Test text");
        assertEquals("Test text", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        assertEquals("Test text", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new TextNode("Test text");
        leafNode.coreValue("New test text");
        assertEquals("New test text", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        leafNodeWithAttributes.coreValue("New test text");
        assertEquals("New test text", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test text");
        assertEquals(leafNode.nodeName(), leafNode.attr("nodeName"));
        assertEquals(EmptyString, leafNode.attr("nonexistentAttr"));

        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        assertEquals("value", leafNodeWithAttributes.attr("data-test"));
        assertEquals(EmptyString, leafNodeWithAttributes.attr("nonexistentAttr"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new TextNode("Test text");
        leafNode.attr("nodeName", "newNodeName");
        assertEquals("newNodeName", leafNode.nodeName());

        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        leafNodeWithAttributes.attr("data-test", "newValue");
        assertEquals("newValue", leafNodeWithAttributes.attr("data-test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test text");
        assertFalse(leafNode.hasAttr("nonexistentAttr"));

        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        assertTrue(leafNodeWithAttributes.hasAttr("data-test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        leafNodeWithAttributes.removeAttr("data-test");
        assertFalse(leafNodeWithAttributes.hasAttr("data-test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNodeWithAttributes = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        assertEquals(EmptyString, leafNodeWithAttributes.absUrl(""));
    }

    @Test
    public void testBaseUri() {
        Node parentNode = new TextNode("Parent text");
        LeafNode leafNode = new TextNode("Test text", new AttributesImpl().put("data-test", "value"));
        leafNode.parent(parentNode);
        assertEquals(parentNode.baseUri(), leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Test text");
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Test text");
        leafNode.ensureChildNodes();
    }
}