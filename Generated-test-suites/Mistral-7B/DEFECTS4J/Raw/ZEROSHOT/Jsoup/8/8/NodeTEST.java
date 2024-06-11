package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        assertEquals("node1", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.hasAttr("id"));
        assertEquals("node1", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttr"));
        assertEquals(0, node.childNodes().size());
        assertEquals(null, node.parent());
    }

    @Test
    public void testNodeConstructorWithNoArguments() {
        Node node = new Node();
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
        assertNull(node.baseUri());
    }

    @Test
    public void testGetNodeName() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testGetAttributeValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Element("div", baseUri, attributes);
        node.attr("class", "class1");
        assertEquals("class1", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));

        attributes.put("href", "http://example.com/link.html");
        String linkUrl = node.absUrl("href");
        assertEquals("http://example.com/link.html", linkUrl);
    }

    @Test
    public void testGetAllAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "class1");
        attributes.put("href", "http://example.com/link.html");
        Node node = new Element("a", baseUri, attributes);
        List<String> attributesList = new ArrayList<>(node.attributes().keySet());
        List<String> expectedAttributesList = new ArrayList<>(3) {{
            add("id");
            add("class");
            add("href");
        }};
        assertEquals(expectedAttributesList, attributesList);
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        node.attr("id", "node1");
        assertEquals("node1", node.attr("id"));
        node.attr("class", "class1");
        assertEquals("class1", node.attr("class"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        node.attr("id", "node1");
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "class1");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testGetBaseUri() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        String newBaseUri = "http://example2.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(child1, child2);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));

        List<Node> childNodesAsArray = parentNode.childNodesAsArray();
        Node[] expectedChildNodesArray = new Node[]{child1, child2};
        assertArrayEquals(expectedChildNodesArray, childNodesAsArray);
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node child = new Element("p", baseUri, attributes);
        child.setParentNode(parentNode);
        assertEquals(parentNode, child.parent());
        assertEquals(null, child.parent());
    }

    @Test
    public void testGetOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);
        Document ownerDocument = new Document(baseUri);
        ownerDocument.setBody(node);
        assertEquals(ownerDocument, node.ownerDocument());
    }

    @Test
    public void testRemoveNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node child = new Element("p", baseUri, attributes);
        parentNode.addChildren(child);
        parentNode.removeChild(child);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testReplaceNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, attributes);
        Node newChild = new Element("p", baseUri, attributes);
        parentNode.addChildren(oldChild);
        parentNode.replaceChild(oldChild, newChild);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(newChild, childNodes.get(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node child = new Element("p", baseUri, attributes);
        Node parent = new Element("div", baseUri, attributes);
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testGetNextSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        assertEquals(sibling2, node.nextSibling());
        assertEquals(null, node.nextSibling(parentNode.childNodes().size()));
    }

    @Test
    public void testGetPreviousSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        assertEquals(sibling1, node.previousSibling());
        assertEquals(null, node.previousSibling(parentNode.childNodes().size()));
    }

    @Test
    public void testGetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        assertEquals(1, node.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(sibling1, node, sibling2);
        node.setSiblingIndex(0);
        assertEquals(sibling2, node.previousSibling());
        assertEquals(sibling1, node.nextSibling());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "element");
        Node node = new Element("div", baseUri, attributes);
        node.attr("id", "node1");
        TextNode textNode = new TextNode("Text content");
        node.addChildren(textNode);

        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        String expectedHtml = "<div id=\"node1\" class=\"element\">Text content</div>";
        assertEquals(expectedHtml, accum.toString());
    }
}