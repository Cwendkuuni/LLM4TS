package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testCreateNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertNull(node.childNodes());
    }

    @Test
    public void testCreateNodeWithoutAttributes() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode());
        assertNull(node.childNodes());
    }

    @Test
    public void testCreateNodeWithoutBaseUri() {
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", attributes);
        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertNull(node.childNodes());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("myClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));
        node.attr("class", "newClass");
        assertEquals("newClass", node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String href = "relative.html";
        Attributes attributes = new Attributes();
        Node node = new Element("a", baseUri, attributes);
        node.attr("href", href);
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistentAttr"));
    }

    @Test
    public void testAddChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.addChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode, childNodes.get(0));
        assertEquals(parentNode, childNode.parentNode());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
        assertEquals(parentNode, childNode1.parentNode());
        assertEquals(parentNode, childNode2.parentNode());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());
        assertNull(childNode.parentNode());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, attributes);
        Node newChild = new Element("p", baseUri, attributes);
        parentNode.addChild(oldChild);
        parentNode.replaceChild(oldChild, newChild);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(newChild, childNodes.get(0));
        assertEquals(parentNode, newChild.parentNode());
        assertEquals(parentNode, oldChild.parentNode());
        assertNull(oldChild.nextSibling());
        assertNull(oldChild.previousSibling());
        assertEquals(newChild, parentNode.childNode(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Element("p", baseUri, attributes);
        Node parentNode = new Element("div", baseUri, attributes);
        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parentNode());
        assertNull(childNode.nextSibling());
        assertNull(childNode.previousSibling());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node1 = new Element("p", baseUri, attributes);
        Node node2 = new Element("p", baseUri, attributes);
        Node node3 = new Element("p", baseUri, attributes);
        List<Node> siblings = new ArrayList<>();
        siblings.add(node1);
        siblings.add(node2);
        siblings.add(node3);
        parentNode.setSiblingIndex(1);
        assertEquals(1, node1.siblingIndex());
        assertEquals(2, node2.siblingIndex());
        assertEquals(3, node3.siblingIndex());
    }

    @Test
    public void testNextSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node1 = new Element("p", baseUri, attributes);
        Node node2 = new Element("p", baseUri, attributes);
        Node node3 = new Element("p", baseUri, attributes);
        List<Node> siblings = new ArrayList<>();
        siblings.add(node1);
        siblings.add(node2);
        siblings.add(node3);
        parentNode.setSiblingIndex(1);
        node1.setSiblingIndex(0);
        node2.setSiblingIndex(2);
        assertEquals(node2, node1.nextSibling());
        assertEquals(null, node1.previousSibling());
        assertEquals(node1, node2.previousSibling());
    }

    @Test
    public void testPreviousSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node1 = new Element("p", baseUri, attributes);
        Node node2 = new Element("p", baseUri, attributes);
        Node node3 = new Element("p", baseUri, attributes);
        List<Node> siblings = new ArrayList<>();
        siblings.add(node1);
        siblings.add(node2);
        siblings.add(node3);
        parentNode.setSiblingIndex(1);
        node1.setSiblingIndex(0);
        node2.setSiblingIndex(2);
        assertEquals(node1, node2.previousSibling());
        assertEquals(null, node2.nextSibling());
        assertEquals(node2, node1.nextSibling());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", baseUri, attributes);
        node.outerHtml("test");
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"myClass\">test</div>");
        assertEquals(expected.toString(), node.outerHtml());
    }
}