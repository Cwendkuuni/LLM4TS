package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node1 = new Element("div", baseUri, attributes);
        Node node2 = new Element("p", baseUri);

        assertEquals("div", node1.nodeName());
        assertEquals("http://example.com", node1.baseUri());
        assertEquals(attributes, node1.attributes());
        assertNull(node2.baseUri());
        assertNull(node2.attributes());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "about.html");
        Node node = new Element("a", baseUri, attributes);

        assertEquals("about.html", node.attr("href"));
        assertEquals("", node.attr("nonExistentAttr"));
        assertEquals("http://example.com/about.html", node.absUrl("href"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals(attributes, node.attributes());
        assertEquals(1, node.attributes().size());
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));

        node.attr("title", "Test title");
        assertEquals("Test title", node.attr("title"));
        assertEquals(2, node.attributes().size());

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertEquals(1, node.attributes().size());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "images/logo.png";
        Node node = new Element("img", baseUri, null);
        node.attr("src", relativeUrl);

        assertEquals("", node.absUrl(""));
        assertEquals("http://example.com", node.absUrl(""));
        assertEquals("images/logo.png", node.absUrl("src"));
        assertEquals("http://example.com/images/logo.png", node.absUrl("src"));
        assertEquals("", node.absUrl("nonExistentAttr"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Node parentNode = new Element("div", baseUri, null);
        Node childNode1 = new Element("p", baseUri, null);
        Node childNode2 = new Element("a", baseUri, null);
        childNode2.attr("href", "about.html");

        parentNode.addChildren(childNode1, childNode2);

        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));

        Node siblingNode = new Element("p", baseUri, null);
        parentNode.addChild(siblingingNode);

        assertEquals(3, parentNode.childNodes().size());
        assertEquals(siblingNode, parentNode.childNode(2));
    }

    @Test
    public void testParentNode() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Node childNode = new Element("p", baseUri1, null);
        Node parentNode1 = new Element("div", baseUri2, null);
        parentNode1.addChild(childNode);

        assertEquals(parentNode1, childNode.parent());
        assertEquals(null, childNode.parent());

        Node childNode2 = new Element("span", baseUri1, null);
        childNode.addChild(childNode2);

        assertEquals(childNode, childNode2.parent());
        assertEquals(null, childNode2.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri, null);

        Document document = new Document(baseUri);
        document.setRootElement(node);

        assertEquals(document, node.ownerDocument());
        assertEquals(node, document.getRootElement());
    }

    @Test(expected = AssertionError.class)
    public void testRemoveChildNullParent() {
        Node node = new Element("div", null, null);
        node.removeChild(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildNodeNegativeIndex() {
        String baseUri = "http://example.com";
        Node parentNode = new Element("div", baseUri, null);
        parentNode.addChildren(new Element("p", baseUri), new Element("a", baseUri));

        parentNode.getChildNode(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildNodeIndexOutOfRange() {
        String baseUri = "http://example.com";
        Node parentNode = new Element("div", baseUri, null);
        parentNode.addChildren(new Element("p", baseUri), new Element("a", baseUri));

        parentNode.getChildNode(2);
    }
}