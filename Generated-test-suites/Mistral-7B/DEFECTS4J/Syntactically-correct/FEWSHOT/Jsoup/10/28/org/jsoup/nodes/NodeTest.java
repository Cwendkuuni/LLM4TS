package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertFalse(node.hasAttr("nonexistent"));
        assertEquals("", node.attr("nonexistent"));
        assertTrue(node.hasAttr("class"));
        assertEquals("test", node.attr("class"));
        assertEquals("test", node.attr("class", "newValue"));
        assertFalse(node.hasAttr("class", "oldValue"));
        assertEquals("", node.removeAttr("class"));
        assertEquals("", node.attr("abs:href"));
        assertEquals("http://example.com/path", node.absUrl("path"));
        assertNull(node.childNode(0));
        assertNull(node.childNodes().get(0));
        assertNull(node.parent());
        assertNull(node.ownerDocument());

        Node childNode = new TextNode("child");
        node.addChildren(childNode);
        Node child = node.childNode(0);
        assertEquals("child", child.toString());
        assertEquals(child, node.childNodes().get(0));
        assertEquals(child, node.getFirstChild());
        assertEquals(child, node.getLastChild());
        assertEquals(child, node.nextSibling());
        assertEquals(node, child.previousSibling());
        assertEquals(child, node.removeChild(0));
        assertEquals(1, node.childNodes().size());
        assertEquals(1, node.childNodesAsArray().length);
        assertNull(node.removeChild(1));
    }

    @Test
    public void testElementMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("id", "testId");

        Element element = new Element("div", baseUri, attributes);

        assertEquals("div", element.nodeName());
        assertEquals(baseUri, element.baseUri());
        assertEquals(attributes, element.attributes());
        assertEquals("test", element.attr("class"));
        assertEquals("testId", element.attr("id"));
        assertTrue(element.hasAttr("class"));
        assertTrue(element.hasAttr("id"));
        assertEquals("test newValue", element.attr("class", "newValue"));
        assertEquals("test newValue", element.attr("class"));
        assertEquals("", element.attr("nonexistent"));
        assertEquals("", element.removeAttr("class"));
        assertEquals("", element.removeAttr("id"));

        Element childElement = new Element("p", baseUri, attributes);
        element.addChildren(childElement);
        Element child = (Element) element.childNode(0);
        assertEquals("p", child.nodeName());
        assertEquals(child, element.getFirstChild());
        assertEquals(child, element.getLastChild());
        assertEquals(child, element.nextSibling());
        assertEquals(element, child.previousSibling());

        Element grandChildElement = new Element("span", baseUri, attributes);
        childElement.addChildren(grandChildElement);
        Element grandChild = (Element) childElement.getFirstChild();
        assertEquals("span", grandChild.nodeName());
        assertEquals(grandChild, childElement.getFirstChild());
        assertEquals(grandChild, childElement.getLastChild());
        assertEquals(childElement, grandChild.previousSibling());
        assertEquals(child, grandChild.nextSibling());

        assertEquals(childElement, element.removeChild(0));
        assertEquals(1, element.childNodes().size());
        assertEquals(1, element.childNodesAsArray().length);
        assertEquals(grandChildElement, childElement.removeChild(0));
        assertEquals(0, childElement.childNodes().size());
        assertEquals(0, childElement.childNodesAsArray().length);
    }
}