package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testNewNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNewNodeWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertTrue(node.attributes().isEmpty());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNewNodeWithoutBaseUriOrAttributes() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertTrue(node.attributes().isEmpty());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testGetNodeName() {
        Node node = new Element("p");
        assertEquals("p", node.nodeName());
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("element1", node.attr("id"));
        assertEquals("", node.attr("non-existent-attribute"));
    }

    @Test
    public void testGetAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Element("div", baseUri, attributes);
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri);
        node.attr("class", "myClass");
        assertEquals("myClass", node.attr("class"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        String newBaseUri = "http://new-example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("a", baseUri, attributes);
        node.attr("href", "relative.html");
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithBaseUriInAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("base", "http://new-example.com");
        Node node = new Element("a", baseUri, attributes);
        node.attr("href", "relative.html");
        assertEquals("http://new-example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithRelativeUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("a", baseUri, attributes);
        assertEquals("", node.absUrl(""));
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode);
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        Node[] children = parentNode.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(childNode1, children[0]);
        assertEquals(childNode2, children[1]);
    }

    @Test
    public void testParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode);
        assertEquals(parentNode, childNode.parentNode());
        assertEquals(null, childNode.parentNode());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        Document ownerDocument = new Document(baseUri);
        ownerDocument.appendChild(node);
        assertEquals(ownerDocument, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode);
        parentNode.removeChild(childNode);
        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, attributes);
        Node newChild = new Element("span", baseUri, attributes);
        parentNode.appendChild(oldChild);
        parentNode.replaceChild(oldChild, newChild);
        assertEquals(newChild, parentNode.childNode(0));
        assertTrue(oldChild.childNodes().isEmpty());
        assertTrue(newChild.childNodes().isEmpty());
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode1 = new Element("div", baseUri, attributes);
        Node parentNode2 = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        childNode.setParentNode(parentNode1);
        parentNode1.appendChild(childNode);
        childNode.setParentNode(parentNode2);
        assertTrue(parentNode2.childNodes().isEmpty());
        assertTrue(parentNode1.childNodes().size() > 0);
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, attributes);
        Node newChild = new Element("span", baseUri, attributes);
        parentNode.appendChild(oldChild);
        parentNode.replaceChild(oldChild, newChild);
        assertEquals(newChild, parentNode.childNode(0));
        assertTrue(oldChild.childNodes().isEmpty());
        assertTrue(newChild.childNodes().isEmpty());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode);
        parentNode.removeChild(childNode);
        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(1, childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(1));
        assertEquals(childNode2, children.get(2));
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        Node clonedNode = node.clone();
        assertEquals(node, clonedNode);
        assertNotSame(node, clonedNode);
        assertEquals(node.nodeName(), clonedNode.nodeName());
        assertEquals(node.baseUri(), clonedNode.baseUri());
        assertEquals(node.attributes(), clonedNode.attributes());
        assertFalse(node.childNodes().isEmpty());
        assertTrue(clonedNode.childNodes().isEmpty());
    }

    @Test
    public void testCloneWithParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        parentNode.appendChild(node);
        Node clonedNode = node.clone();
        Node clonedParent = new Element("div", baseUri, attributes);
        clonedParent.appendChild(clonedNode);
        assertEquals(parentNode, clonedParent);
        assertEquals(parentNode.childNodes().get(0), clonedParent.childNodes().get(0));
        assertEquals(node, clonedNode);
        assertEquals(node.childNodes().get(0), clonedNode.childNodes().get(0));
    }
}