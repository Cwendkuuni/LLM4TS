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

        Element element = new Element("TestElement");
        assertTrue(element.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertNull(leafNode.attributes());

        Element element = new Element("TestElement");
        Attributes attributes = element.attributes();
        assertEquals(0, attributes.size());

        attributes.put("test", "testValue");
        assertEquals("test", element.nodeName());
        assertEquals("testValue", element.attr("test"));
        assertEquals("testValue", element.coreValue());
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.attr("nodeName"));

        Attributes attributes = leafNode.attributes();
        assertEquals("testValue", attributes.get("nodeName"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals("Test", leafNode.coreValue());

        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());

        Element element = new Element("TestElement");
        assertEquals("TestElement", element.nodeName());
        assertEquals("TestElement", element.coreValue());
        element.coreValue("testValue");
        assertEquals("testValue", element.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals("", leafNode.attr("test"));

        Element element = new Element("TestElement");
        assertNull(element.attr("test"));
        element.attr("test", "testValue");
        assertEquals("testValue", element.attr("test"));
        assertEquals("testValue", element.coreValue());
    }

    @Test
    public void testAttrWithValue() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("test", "testValue");
        assertEquals("testValue", leafNode.attr("test"));
        assertEquals("testValue", leafNode.coreValue());

        Element element = new Element("TestElement");
        assertEquals("", element.attr("test"));
        element.attr("test", "testValue");
        assertEquals("testValue", element.attr("test"));
        assertEquals("testElement testValue", element.toString());
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr("test"));

        Element element = new Element("TestElement");
        assertFalse(element.hasAttr("test"));
        element.attr("test", "testValue");
        assertTrue(element.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = new Element("TestElement");
        element.attr("test", "testValue");
        assertTrue(element.hasAttr("test"));
        element.removeAttr("test");
        assertFalse(element.hasAttr("test"));
    }

    @Test
    public void testAbsUrl() {
        Element element = new Element("img");
        element.absUrl("src");
        fail("This method should not be called for LeafNodes.");
    }

    @Test
    public void testBaseUri() {
        Element parent = new Element("parent");
        Element child = new TextNode("child");
        parent.appendChild(child);
        assertEquals("", child.baseUri());

        Element grandParent = new Element("grandParent");
        grandParent.appendChild(parent);
        assertEquals(grandParent.baseUri(), child.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Test");
        assertEquals(0, leafNode.childNodeSize());

        Element element = new Element("TestElement");
        assertEquals(0, element.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.ensureChildNodes();
    }
}