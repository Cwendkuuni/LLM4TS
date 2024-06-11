package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new TextNode("Test");
        assertFalse(node.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        assertTrue(nodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode node = new TextNode("Test");
        assertNull(node.attributes());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        assertEquals(attributes, nodeWithAttributes.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new TextNode("Test");
        assertEquals("Test", node.coreValue());

        Attributes attributes = new Attributes();
        attributes.put("nodeName", "TestValue");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        assertEquals("Test", nodeWithAttributes.coreValue());
    }

    @Test
    public void testCoreValueAssignment() {
        LeafNode node = new TextNode("Test");
        node.coreValue("TestValue");
        assertEquals("TestValue", node.coreValue());

        Attributes attributes = new Attributes();
        attributes.put("nodeName", "TestValue");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        nodeWithAttributes.coreValue("NewTestValue");
        assertEquals("NewTestValue", nodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode node = new TextNode("Test");
        assertEquals("Test", node.attr("nodeName"));
        assertTrue(node.hasAttributes());
        node.coreValue("TestValue");
        assertEquals("TestValue", node.attr("nodeName"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        assertEquals("value", nodeWithAttributes.attr("test"));
    }

    @Test
    public void testAttrAssignment() {
        LeafNode node = new TextNode("Test");
        node.attr("test", "value");
        assertEquals("value", node.attr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        nodeWithAttributes.attr("test", "newValue");
        assertEquals("newValue", nodeWithAttributes.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new TextNode("Test");
        assertFalse(node.hasAttr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        assertTrue(nodeWithAttributes.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new TextNode("Test");
        node.attr("test", "value");
        node = node.removeAttr("test");
        assertFalse(node.hasAttributes());
        assertNull(node.attr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        nodeWithAttributes = nodeWithAttributes.removeAttr("test");
        assertFalse(nodeWithAttributes.hasAttributes());
        assertNull(nodeWithAttributes.attr("test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode node = new TextNode("Test");
        assertNull(node.absUrl("key"));

        Attributes attributes = new Attributes();
        attributes.put("href", "http://test.com");
        LeafNode nodeWithAttributes = new TextNode("Test", attributes);
        assertEquals("http://test.com", nodeWithAttributes.absUrl("href"));
    }

    @Test
    public void testBaseUri() {
        Element parent = new Element("Parent");
        TextNode node = new TextNode("Test", new Attributes().put("nodeName", "TestValue"));
        parent.appendText(node);
        assertEquals("", node.baseUri());

        Element grandParent = new Element("GrandParent");
        parent.appendElement(grandParent);
        grandParent.appendText(new TextNode("BaseUrl", new Attributes().put("baseUri", "http://baseurl.com")));
        assertEquals("http://baseurl.com", node.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode node = new TextNode("Test");
        node.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode node = new TextNode("Test");
        node.ensureChildNodes();
    }
}