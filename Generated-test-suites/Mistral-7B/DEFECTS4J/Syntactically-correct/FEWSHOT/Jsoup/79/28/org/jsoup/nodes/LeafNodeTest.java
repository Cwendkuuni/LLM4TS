package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
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
        TextNode textNode = new TextNode("Hello World");
        Element element = new Element("div");
        assertEquals(false, textNode.hasAttributes());
        assertEquals(true, element.hasAttributes());
    }

    @Test
    public void testAttributes() {
        TextNode textNode = new TextNode("Hello World");
        Element element = new Element("div");

        element.attr("id", "test");
        element.attr("class", "testClass");
        Attributes attributes = element.attributes();

        assertEquals("test", attributes.get("id"));
        assertEquals("testClass", attributes.get("class"));
        assertEquals(2, attributes.size());

        TextNode textNodeWithAttr = new TextNode("Hello World").attr("data-test", "value");
        Attributes attributesOfTextNode = textNodeWithAttr.attributes();
        assertEquals("value", attributesOfTextNode.get("data-test"));
        assertEquals(1, attributesOfTextNode.size());

        TextNode textNodeWithoutAttr = new TextNode("Hello World");
        Attributes attributesOfTextNodeWithoutAttr = textNodeWithoutAttr.attributes();
        assertNull(attributesOfTextNodeWithoutAttr.get("data-test"));
        assertEquals(0, attributesOfTextNodeWithoutAttr.size());
    }

    @Test
    public void testEnsureAttributes() {
        TextNode textNode = new TextNode("Hello World");
        textNode.ensureAttributes();
        assertNull(textNode.attributes().get("data-test"));
        assertEquals(0, textNode.attributes().size());

        Element element = new Element("div");
        element.ensureAttributes();
        assertTrue(element.hasAttributes());
        assertEquals(0, element.attributes().size());

        Element elementWithAttributes = new Element("div").attr("id", "test").attr("class", "testClass");
        elementWithAttributes.ensureAttributes();
        assertEquals("test", elementWithAttributes.attributes().get("id"));
        assertEquals("testClass", elementWithAttributes.attributes().get("class"));
        assertEquals(2, elementWithAttributes.attributes().size());
    }

    @Test
    public void testCoreValue() {
        TextNode textNode = new TextNode("Hello World");
        assertEquals("Hello World", textNode.coreValue());

        Element element = new Element("div").text("Hello World");
        assertEquals("Hello World", element.coreValue());

        Element elementWithAttr = new Element("div").attr("id", "test").text("Hello World");
        assertEquals("Hello World", elementWithAttr.coreValue());
        assertEquals("test", elementWithAttr.attr("id"));
    }

    @Test
    public void testCoreValueSetting() {
        TextNode textNode = new TextNode("Hello World");
        textNode.coreValue("New Value");
        assertEquals("New Value", textNode.getWholeText());

        Element element = new Element("div").text("Hello World");
        element.coreValue("New Value");
        assertEquals("New Value", element.getWholeText());
        assertEquals("", element.attr("data-test")); // no attribute was set
    }

    @Test
    public void testAttr() {
        TextNode textNode = new TextNode("Hello World");
        textNode.attr("data-test", "value");
        assertEquals("value", textNode.attributes().get("data-test"));

        Element element = new Element("div").attr("id", "test");
        assertEquals("test", element.attr("id"));
        assertEquals("test", element.attributes().get("id"));

        Element elementWithExistingAttr = new Element("div")
                .attr("id", "test")
                .attr("data-test", "value");
        assertEquals("test", element.attr("id"));
        assertEquals("test", element.attributes().get("id"));
        assertEquals("value", element.attributes().get("data-test"));
        assertEquals(2, element.attributes().size());

        Element elementWithEmptyAttr = new Element("div").attr("data-test", "");
        assertEquals("", element.attributes().get("data-test"));
        assertEquals(1, element.attributes().size());
    }

    @Test
    public void testRemoveAttr() {
        TextNode textNodeWithAttr = new TextNode("Hello World").attr("data-test", "value");
        textNodeWithAttr.removeAttr("data-test");
        assertNull(textNodeWithAttr.attributes().get("data-test"));
        assertEquals(0, textNodeWithAttr.attributes().size());

        Element elementWithAttr = new Element("div").attr("id", "test").attr("class", "testClass");
        elementWithAttr.removeAttr("class");
        assertEquals("test", elementWithAttr.attributes().get("id"));
        assertEquals(1, elementWithAttr.attributes().size());
    }

    @Test
    public void testGetBaseUri() {
        Element element = new Element("div");
        assertEquals("", element.baseUri());

        Element parent = new Element("body").add(element);
        assertEquals("", element.baseUri());
        assertEquals("", parent.baseUri());

        Element grandParent = new Element("html").add(parent);
        assertEquals("", element.baseUri());
        assertEquals("", parent.baseUri());
        assertEquals("", grandParent.baseUri());
        assertEquals("/", grandParent.absUrl("/"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        TextNode textNode = new TextNode("Hello World");
        textNode.childNodeSize(); // UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        TextNode textNode = new TextNode("Hello World");
        textNode.ensureChildNodes(); // UnsupportedOperationException
    }

}