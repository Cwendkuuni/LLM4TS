package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elemId");

        // Test default constructor
        Node node1 = new Node();
        assertEquals("", node1.nodeName());
        assertNull(node1.parentNode);
        assertNotNull(node1.attributes());
        assertEquals(Collections.emptyList(), node1.childNodes());
        assertEquals(baseUri, node1.baseUri());
        assertNull(node1.attributes().get("id"));

        // Test constructor with baseUri and attributes
        Node node2 = new Node(baseUri, attributes);
        assertEquals("", node2.nodeName());
        assertNull(node2.parentNode);
        assertEquals(attributes, node2.attributes());
        assertEquals(Collections.EMPTY_LIST, node2.childNodes());
        assertEquals(baseUri, node2.baseUri());

        // Test constructor without arguments
        Node node3 = new Node();
        assertEquals("", node3.nodeName());
        assertNull(node3.parentNode);
        assertNull(node3.attributes());
        assertEquals(Collections.EMPTY_LIST, node3.childNodes());
        assertEquals("", node3.baseUri());
    }

    @Test
    public void testNodeName() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elemId");
        attributes.put("class", "myClass");

        Node node = new Element("div", baseUri, attributes);

        assertEquals("elemId", node.attr("id"));
        assertEquals("myClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));

        String absUrl = node.absUrl("href");
        assertEquals("", absUrl);
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("id", "elemId");
        attributes.put("class", "myClass");

        Node node = new Element("div", null, attributes);

        assertEquals(attributes, node.attributes());

        attributes.put("newAttribute", "newValue");
        assertEquals(attributes, node.attributes());

        assertEquals("elemId", node.attr("id"));
        assertEquals("myClass", node.attr("class"));
        assertEquals("newValue", node.attr("newAttribute"));
        assertEquals("", node.attr("nonExistentAttr"));
    }

    @Test
    public void testHasAttr() {
        Attributes attributes = new Attributes();
        attributes.put("id", "elemId");

        Node node = new Element("div", null, attributes);

        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        Attributes attributes = new Attributes();
        attributes.put("id", "elemId");

        Node node = new Element("div", null, attributes);

        assertEquals("elemId", node.attr("id"));
        node.removeAttr("id");
        assertEquals("", node.attr("id"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");

        Node node = new Element("a", baseUri, attributes);

        String absUrl = node.absUrl("href");
        assertEquals("http://example.com/relative.html", absUrl);

        String baseUri2 = "http://other-example.com";
        node.setBaseUri(baseUri2);
        String absUrl2 = node.absUrl("href");
        assertEquals("http://other-example.com/relative.html", absUrl2);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";

        Node parentNode = new Element("div", baseUri, new Attributes());

        Node child1 = new Element("p", baseUri, new Attributes());
        Node child2 = new TextNode("Text node");
        Node child3 = new Element("span", baseUri, new Attributes());

        parentNode.addChildren(child1, child2, child3);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(3, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
        assertEquals(child3, childNodes.get(2));

        Node[] childNodesArray = parentNode.childNodesAsArray();
        assertArrayEquals(new Node[]{child1, child2, child3}, childNodesArray);
    }

    @Test
    public void testGetParent() {
        Node parentNode = new Element("div", null, new Attributes());
        Node child = new Element("p", null, new Attributes());

        child.setParentNode(parentNode);

        assertEquals(parentNode, child.parent());
        assertNull(parentNode.parent());
    }

    @Test
    public void testGetDocument() {
        Node node = new Element("div", null, new Attributes());

        Document document = new Document();
        document.appendChild(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Element("div", null, new Attributes());
        Node child = new Element("p", null, new Attributes());

        parentNode.appendChild(child);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());

        parentNode.removeChild(child);

        childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new Element("div", null, new Attributes());
        Node oldChild = new Element("p", null, new Attributes());
        Node newChild = new Element("span", null, new Attributes());

        parentNode.appendChild(oldChild);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(oldChild, childNodes.get(0));

        parentNode.replaceChild(oldChild, newChild);

        childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(newChild, childNodes.get(0));
    }

    @Test
    public void testSetParentNode() {
        Node child = new Element("p", null, new Attributes());
        Node parent = new Element("div", null, new Attributes());

        child.setParentNode(parent);

        assertEquals(parent, child.parent());
        assertNull(parent.parent());
    }

    @Test
    public void testSetSiblingIndex() {
        Node node1 = new Element("p", null, new Attributes());
        Node node2 = new Element("p", null, new Attributes());

        List<Node> siblings = new ArrayList<>();
        siblings.add(node1);
        siblings.add(node2);

        Node parent = new Element("div", null, new Attributes());
        parent.setChildren(siblings);

        node1.setSiblingIndex(0);
        node2.setSiblingIndex(1);

        assertEquals(0, node1.siblingIndex());
        assertEquals(1, node2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";

        Node parentNode = new Element("div", baseUri, new Attributes());
        Node child1 = new Element("p", baseUri, new Attributes()).html("Text 1");
        Node child2 = new TextNode("Text 2").html("Text 2");

        parentNode.addChildren(child1, child2);

        StringBuilder accum = new StringBuilder();
        parentNode.outerHtml(accum);

        String expected = "<div>" +
                "<p>Text 1</p>" +
                "Text 2" +
                "</div>";

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadAndTail() {
        String baseUri = "http://example.com";

        Node parentNode = new Element("div", baseUri, new Attributes());
        Node child = new Element("p", baseUri, new Attributes()).html("Text");

        parentNode.outerHtmlHead(new StringBuilder(), 0, null);
        parentNode.outerHtmlTail(new StringBuilder(), 0, null);

        String expected = "<div>" +
                "Text" +
                "</div>";

        StringBuilder accum = new StringBuilder();
        parentNode.outerHtml(accum);

        assertEquals(expected, accum.toString());
    }
}