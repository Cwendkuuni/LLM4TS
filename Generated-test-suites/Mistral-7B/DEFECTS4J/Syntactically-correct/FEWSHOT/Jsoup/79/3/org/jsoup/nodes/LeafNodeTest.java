package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttributes());

        Element element = new Element("test");
        element.attr("test", "value");
        assertTrue(element.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertNull(leafNode.attributes());

        Element element = new Element("test");
        element.attr("test", "value");
        Attributes attributes = element.attributes();
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("TestValue");
        Attributes attributes = leafNode.attributes();
        assertEquals("TestValue", attributes.get("test"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("TestValue");
        assertEquals("TestValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValueString() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("TestValue");
        leafNode.coreValue("NewTestValue");
        assertEquals("NewTestValue", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("test", "value");
        assertEquals("value", leafNode.attr("test"));
        assertNull(leafNode.attr("nonExistent"));
    }

    @Test
    public void testAttrStringString() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("test", "value");
        leafNode.attr("test", "newValue");
        assertEquals("newValue", leafNode.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr("test"));

        Element element = new Element("test");
        element.attr("test", "value");
        assertTrue(element.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("TestValue");
        leafNode.attr("test", "value");
        leafNode.removeAttr("test");
        assertNull(leafNode.attr("test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Test");
        assertNull(leafNode.absUrl("test"));

        Element element = new Element("img");
        element.attr("src", "test.html");
        assertEquals("test.html", element.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("Test");
        assertEmpty(leafNode.baseUri());

        Element parent = new Element("div");
        Element child = new Element("test");
        parent.appendChild(child);
        assertEquals(parent.baseUri(), child.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.ensureChildNodes();
    }
}