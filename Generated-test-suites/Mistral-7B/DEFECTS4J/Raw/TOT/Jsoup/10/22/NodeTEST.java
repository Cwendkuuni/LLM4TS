package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attributes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    private Node node;
    private Document doc;

    @Before
    public void setup() throws MalformedURLException {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        doc = new Document(baseUri);
        node = new Element(doc, "testElement", attributes);
    }

    @Test
    public void testNodeName() {
        assertEquals("testElement", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("class", "testClass");
        assertEquals("testClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        attributes.put("testAttr", "testValue");
        assertEquals("testValue", node.attr("testAttr"));
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testHasAttr() {
        node.attr("testAttr", "testValue");
        assertTrue(node.hasAttr("testAttr"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("testAttr", "testValue");
        node.removeAttr("testAttr");
        assertFalse(node.hasAttr("testAttr"));
    }

    @Test
    public void testBaseUri() {
        node.setBaseUri(new URL("http://subdomain.example.com/"));
        assertEquals("http://subdomain.example.com/", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        node.attr("href", "relative.html");
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
        node.setBaseUri(new URL("http://subdomain.example.com/"));
        assertEquals("http://subdomain.example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Node child1 = new TextNode("Child 1");
        Node child2 = new TextNode("Child 2");

        node.addChildren(child1, child2);

        List<Node> childNodes = node.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));

        List<Node> childNodesAsArray = node.childNodesAsArray();
        assertEquals(2, childNodesAsArray.length);
        assertEquals(child1, childNodesAsArray[0]);
        assertEquals(child2, childNodesAsArray[1]);
    }

    @Test
    public void testParent() {
        doc.appendElement("body");
        doc.body().appendElement(node);

        assertEquals(doc.body(), node.parent());
        assertNull(node.parent()); // after remove

        node.remove();
    }

    @Test
    public void testOwnerDocument() {
        assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        doc.appendElement("body");
        doc.body().appendElement(node);
        node.remove();

        List<Node> childNodes = doc.body().childNodes();
        assertEquals(0, childNodes.indexOf(node));
        assertTrue(childNodes.isEmpty());
    }

    @Test
    public void testReplaceWith() {
        Node replacement = new TextNode("Replacement");

        doc.appendElement("body");
        doc.body().appendElement(node);
        doc.body().replaceChild(node, replacement);

        assertEquals(replacement, doc.body().childNodes().get(0));
    }

    @Test
    public void testSetParentNode() {
        Node newParent = new Element(doc, "newParent");
        node.setParentNode(newParent);

        assertEquals(newParent, node.parentNode);
    }

    @Test
    public void testReplaceChild() {
        Node oldChild = new TextNode("Old Child");
        Node newChild = new TextNode("New Child");

        doc.appendElement("body");
        doc.body().appendChild(oldChild);
        doc.body().replaceChild(oldChild, newChild);

        assertEquals(newChild, doc.body().childNodes().get(0));
    }

    @Test
    public void testRemoveChild() {
        Node oldChild = new TextNode("Old Child");
        Node newChild = new TextNode("New Child");

        doc.appendElement("body");
        doc.body().appendChild(oldChild);
        doc.body().removeChild(oldChild);
        doc.body().appendChild(newChild);

        assertEquals(newChild, doc.body().childNodes().get(0));
    }

    @Test
    public void testAddChildren() {
        Node newChild1 = new TextNode("New Child 1");
        Node newChild2 = new TextNode("New Child 2");

        doc.appendElement("body");
        doc.body().addChildren(newChild1, newChild2);

        List<Node> childNodes = doc.body().childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(newChild1, childNodes.get(0));
        assertEquals(newChild2, childNodes.get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node newChild1 = new TextNode("New Child 1");
        Node newChild2 = new TextNode("New Child 2");

        doc.appendElement("body");
        doc.body().addChildren(1, newChild1, newChild2);

        List<Node> childNodes = doc.body().childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(newChild1, childNodes.get(0));
        assertEquals(newChild2, childNodes.get(1));
    }

    @Test
    public void testOuterHtml() {
        String expected = "<testElement id=\"testNode\">Test</testElement>";
        node.outerHtml(new StringBuilder(expected.length()));

        assertEquals(expected, node.outerHtml());

        Attributes newAttributes = new Attributes();
        newAttributes.put("test", "test");
        node.attributes = newAttributes;

        expected = "<testElement id=\"testNode\" test=\"test\">Test</testElement>";
        node.outerHtml(new StringBuilder(expected.length()));

        assertEquals(expected, node.outerHtml());
    }
}