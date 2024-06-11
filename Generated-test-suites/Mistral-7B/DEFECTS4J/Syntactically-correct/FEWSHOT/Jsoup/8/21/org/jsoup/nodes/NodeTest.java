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
    public void testNewNode() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertEquals("example", node.attr("class"));
    }

    @Test
    public void testNewNodeEmpty() {
        Node node = new Node();
        assertNull(node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNewNodeWithoutBaseUriOrAttributes() {
        Node node = new Node();
        assertNull(node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("example", node.attr("class"));
        assertEquals("", node.attr("non_existent_attr"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non_existent_attr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://www.example.com";
        String href = "/about.html";
        Node node = new Element("a", baseUri, new Attributes().put("href", href));
        String absUrl = node.absUrl("href");
        assertEquals("http://www.example.com/about.html", absUrl);
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        String baseUri = "http://www.example.com";
        String href = "https://www.google.com";
        Node node = new Element("a", baseUri, new Attributes().put("href", href));
        String absUrl = node.absUrl("href");
        assertEquals(href, absUrl);
    }

    @Test
    public void testAbsUrlWithEmptyAttribute() {
        String baseUri = "http://www.example.com";
        Node node = new Element("img", baseUri, new Attributes());
        String absUrl = node.absUrl("src");
        assertEquals("", absUrl);
    }

    @Test
    public void testAbsUrlWithMissingAttribute() {
        String baseUri = "http://www.example.com";
        Node node = new Element("div", baseUri, new Attributes());
        String absUrl = node.absUrl("non_existent_attr");
        assertEquals("", absUrl);
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode);
        Node childNodeFromParent = parentNode.childNode(0);
        assertEquals(childNode, childNodeFromParent);
    }

    @Test
    public void testChildren() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    @Test
    public void testChildNodesAsArray() {
        String baseUri = "http://www.example.com";
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
    public void testParent() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node childNode = new Element("p", baseUri, new Attributes());
        parentNode.appendChild(childNode);
        assertEquals(parentNode, childNode.parent());
        assertEquals(null, childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.ownerDocument() instanceof Document);
    }

    @Test
    public void testRemove() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node childNode = new Element("p", baseUri, new Attributes());
        parentNode.appendChild(childNode);
        parentNode.removeChild(childNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node oldChildNode = new Element("p", baseUri, new Attributes());
        Node newChildNode = new Element("span", baseUri, new Attributes());
        parentNode.appendChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri1 = "http://www.example1.com";
        String baseUri2 = "http://www.example2.com";
        Attributes attributes = new Attributes();
        Node childNode = new Element("p", baseUri1, attributes);
        Node parentNode1 = new Element("div", baseUri1, attributes);
        Node parentNode2 = new Element("div", baseUri2, attributes);
        parentNode1.appendChild(childNode);
        childNode.setParentNode(parentNode2);
        assertEquals(parentNode2, childNode.parent());
        assertEquals(null, childNode.parent());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node oldChildNode = new Element("p", baseUri, new Attributes());
        Node newChildNode = new Element("span", baseUri, new Attributes());
        parentNode.appendChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node childNode1 = new Element("p", baseUri, new Attributes());
        Node childNode2 = new Element("p", baseUri, new Attributes());
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        parentNode.removeChild(childNode1);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(childNode2, children.get(0));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://www.example.com";
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
    public void testSiblingNodes() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node firstChild = new Element("p", baseUri, new Attributes());
        Node secondChild = new Element("p", baseUri, new Attributes());
        Node thirdChild = new Element("p", baseUri, new Attributes());
        parentNode.appendChild(firstChild);
        parentNode.appendChild(secondChild);
        parentNode.appendChild(thirdChild);
        List<Node> siblings = firstChild.siblingNodes();
        assertEquals(2, siblings.size());
        assertTrue(siblings.contains(secondChild));
        assertTrue(siblings.contains(thirdChild));
    }

    @Test
    public void testNextSibling() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node firstChild = new Element("p", baseUri, new Attributes());
        Node secondChild = new Element("p", baseUri, new Attributes());
        Node thirdChild = new Element("p", baseUri, new Attributes());
        parentNode.appendChild(firstChild);
        parentNode.appendChild(secondChild);
        parentNode.appendChild(thirdChild);
        Node nextSibling = firstChild.nextSibling();
        assertEquals(secondChild, nextSibling);
        Node nextSiblingOfLastChild = thirdChild.nextSibling();
        assertEquals(null, nextSiblingOfLastChild);
    }

    @Test
    public void testPreviousSibling() {
        String baseUri = "http://www.example.com";
        Attributes parentAttributes = new Attributes();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node firstChild = new Element("p", baseUri, new Attributes());
        Node secondChild = new Element("p", baseUri, new Attributes());
        Node thirdChild = new Element("p", baseUri, new Attributes());
        parentNode.appendChild(firstChild);
        parentNode.appendChild(secondChild);
        parentNode.appendChild(thirdChild);
        Node previousSibling = secondChild.previousSibling();
        assertEquals(firstChild, previousSibling);
        Node previousSiblingOfFirstChild = parentNode.childNodes().get(0).previousSibling();
        assertEquals(null, previousSiblingOfFirstChild);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        StringBuilder accum = new StringBuilder(32 * 1024);
        node.outerHtml(accum);
        String outerHtml = accum.toString();
        assertTrue(outerHtml.startsWith("<div>"));
        assertTrue(outerHtml.endsWith("</div>"));
    }

    @Test
    public void testToString() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        String toString = node.toString();
        assertTrue(toString.startsWith("<div>"));
        assertTrue(toString.endsWith("</div>"));
    }
}