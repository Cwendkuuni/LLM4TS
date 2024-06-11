package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Hello World");
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div");
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Hello World");
        assertNull(leafNode.attributes());

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div").attr("class", "myClass");
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertEquals("class", attributes.getClass().getSimpleName());
        assertEquals("myClass", attributes.get("class"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("Hello World");

        leafNode.ensureAttributes();
        assertNull(leafNode.value);

        LeafNode leafNodeWithAttributes = new Elements.SingleTagElement("div").attr("class", "myClass");
        leafNodeWithAttributes.ensureAttributes();
        assertEquals("myClass", ((Attributes) leafNodeWithAttributes.value).get("class"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Hello World");
        assertEquals("Hello World", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new TextNode("Hello World");
        leafNode.coreValue("New Value");
        assertEquals("New Value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Hello World");

        String value = "newValue";
        assertEquals(leafNode.coreValue(), leafNode.attr(nodeName()));
        leafNode.attr(nodeName(), value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new TextNode("Hello World");

        String key = "testKey";
        String value = "newValue";
        leafNode.attr(key, value);
        assertEquals(value, leafNode.attr(key));
        assertEquals(key, leafNode.nodeName());
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Hello World");
        assertFalse(leafNode.hasAttr("testKey"));

        LeafNode leafNodeWithAttribute = new Elements.SingleTagElement("div").attr("class", "myClass");
        assertTrue(leafNodeWithAttribute.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("Hello World").attr("testKey", "testValue");
        assertEquals("testValue", leafNode.attr("testKey"));

        leafNode.removeAttr("testKey");
        assertNull(leafNode.attr("testKey"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Hello World");
        assertEquals("", leafNode.absUrl(nodeName()));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("Hello World").parent(new Elements.SingleTagElement("body"));
        assertEquals("", leafNode.baseUri());

        Element parentWithBaseUri = new Elements.SingleTagElement("html").attr("base", "/test");
        leafNode = leafNode.parent(parentWithBaseUri);
        assertEquals("/test", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode leafNode = new TextNode("Hello World");
        leafNode.setBaseUri("http://example.com");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Hello World");
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Hello World");
        leafNode.ensureChildNodes();
    }
}