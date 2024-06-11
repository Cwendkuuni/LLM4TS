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
        element.attr("test", "attribute");
        assertTrue(element.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertNull(leafNode.attributes());

        Element element = new Element("test");
        element.attr("test", "attribute");
        Attributes attributes = element.attributes();
        assertEquals("attribute", attributes.get("test"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("Test Attribute");
        Attributes attributes = leafNode.attributes();
        assertEquals("Test Attribute", attributes.get("test"));
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("Test Attribute");
        assertEquals("Test Attribute", leafNode.attr("test"));

        Element element = new Element("test");
        assertEquals("", element.attr("test"));
        element.attr("test", "attribute");
        assertEquals("attribute", element.attr("test"));
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("Test Attribute");
        leafNode.attr("test", "new attribute");
        assertEquals("new attribute", leafNode.attr("test"));

        Element element = new Element("test");
        assertEquals("", element.attr("test"));
        element.attr("test", "attribute");
        element.attr("test", "new attribute");
        assertEquals("new attribute", element.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr("test"));

        Element element = new Element("test");
        assertFalse(element.hasAttr("nonexistent"));
        assertTrue(element.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("Test Attribute");
        leafNode.attr("test", "new attribute");
        leafNode.removeAttr("test");
        assertNull(leafNode.attr("test"));

        Element element = new Element("test");
        element.attr("test", "attribute");
        element.removeAttr("test");
        assertNull(element.attr("test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Test");
        assertNull(leafNode.absUrl("test"));

        Element element = new Element("img");
        element.absUrl("src");
        assertTrue(element.absUrl("src").startsWith("http://"));
    }

    @Test
    public void testBaseUri() {
        TextNode leafNode = new TextNode("Test");
        Element parent = new Element("parent");
        Element grandParent = new Element("grandparent");
        parent.add(grandParent);
        grandParent.add(parent);
        grandParent.add(leafNode);
        assertEquals("", leafNode.baseUri());
        assertEquals(grandParent.baseUri(), leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.setBaseUri("baseUri");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.ensureChildNodes();
    }
}