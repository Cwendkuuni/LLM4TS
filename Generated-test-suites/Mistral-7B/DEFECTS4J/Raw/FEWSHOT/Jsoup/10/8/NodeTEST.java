package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attribute;
import org.junit.Test;

/**
 * Test cases for Node class.
 */
public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("div");
        assertNotNull(node);
        assertEquals("div", node.nodeName());
        assertNull(node.parentNode());
        assertNull(node.attributes());
        assertNull(node.baseUri());
        assertFalse(node.hasChildren());
        assertFalse(node.hasParent());

        Node textNode = new TextNode("Hello World");
        assertNotNull(textNode);
        assertEquals("#text", textNode.nodeName());
        assertNull(textNode.parentNode());
        assertNull(textNode.attributes());
        assertNull(textNode.baseUri());
        assertTrue(textNode.hasText());
        assertFalse(textNode.hasChildren());
        assertFalse(textNode.hasParent());
    }

    @Test
    public void testSetBaseUri() {
        Element node = new Element("div");
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testSetBaseUriInvalid() {
        Element node = new Element("div");
        try {
            node.setBaseUri(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
        try {
            node.setBaseUri("");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAttributes() {
        Element node = new Element("div");
        node.attr("class", "myclass");
        Attribute attr = node.attr("class");
        assertEquals("myclass", attr.getValue());
        assertEquals("class", attr.getKey());
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
        assertEquals("", node.attr("nonexistent"));
    }

    @Test
    public void testAttributesClone() {
        Element node = new Element("div");
        node.attr("class", "myclass");
        Attributes attrs = node.attributes();
        Element clonedNode = node.cloneNode(true);
        Attributes clonedAttrs = clonedNode.attributes();
        assertEquals(attrs, clonedAttrs);
    }

    @Test
    public void testAttrs() {
        Element node = new Element("div");
        node.attr("class", "myclass");
        assertEquals("class=\"myclass\"", node.attr("class"));
        assertEquals("class", node.attrKey("class"));
        assertEquals("myclass", node.attrValue("class"));
    }

    @Test
    public void testHasAttr() {
        Element node = new Element("div");
        node.attr("class", "myclass");
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttr() {
        Element node = new Element("div");
        node.attr("class", "myclass");
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        Element node = new Element("a", "href=\"/foo\"");
        assertEquals("", node.absUrl("nonexistent"));
        assertEquals("http://www.example.com/foo", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlInvalidBaseUri() {
        Element node = new Element("a", "href=\"/foo\"");
        try {
            node.absUrl(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
        try {
            node.absUrl("");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChildNodesEmpty() {
        Element parent = new Element("div");
        assertTrue(parent.childNodes().isEmpty());
    }

    @Test
    public void testChildNodes() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.appendChild(child);
        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(child, childNodes.get(0));
    }

    @Test
    public void testChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.appendChild(child);
        Node node = parent.childNode(0);
        assertEquals(child, node);
    }

    @Test
    public void testChildNodeInvalidIndex() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.appendChild(child);
        try {
            parent.childNode(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReplaceChild() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.appendChild(child1);
        parent.replaceChild(child1, child2);
        assertEquals(child2, parent.childNode(0));
        assertEquals(child1, parent.removeChild(child2));
    }

    @Test
    public void testRemoveChild() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.appendChild(child1);
        parent.appendChild(child2);
        Node removedChild = parent.removeChild(child1);
        assertEquals(child2, parent.childNode(0));
        assertEquals(child1, removedChild);
    }

    @Test
    public void testRemoveChildNull() {
        Element parent = new Element("div");
        try {
            parent.removeChild(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testAddChildren() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.addChildren(child1, child2);
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testAddChildrenNull() {
        Element parent = new Element("div");
        try {
            parent.addChildren(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}