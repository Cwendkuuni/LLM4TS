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
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("elementId", node1.attr("id"));
        assertEquals(baseUri, node1.baseUri());
        assertEquals(null, node2.attributes());
        assertEquals("", node3.attr("id"));
        assertEquals(null, node3.baseUri());
    }

    @Test
    public void testNodeAbstractMethods() {
        throw new RuntimeException("Cannot test abstract methods in Node class.");
    }

    @Test
    public void testAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node = new Element("div", baseUri, attributes);

        node.attr("class", "exampleClass");
        assertEquals("exampleClass", node.attr("class"));

        node.attr("href", "https://example.com/new-url");
        assertEquals("https://example.com/new-url", node.absUrl("href"));

        assertEquals(null, node.attr("nonexistentattr"));
        assertEquals("", node.attr(""));
    }

    @Test
    public void testAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        attributes.put("class", "exampleClass");

        Node node = new Element("div", baseUri, attributes);

        assertEquals("id=\"elementId\" class=\"exampleClass\"", node.toString());
        assertEquals("id", node.attributes().keySet().iterator().next());
        assertEquals("elementId", node.attributes().get("id"));
        assertEquals("exampleClass", node.attributes().get("class"));
        assertEquals(2, node.attributes().size());
    }

    @Test
    public void testHasAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node = new Element("div", baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistentattr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        attributes.put("class", "exampleClass");

        Node node = new Element("div", baseUri, attributes);

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertEquals("id=\"elementId\"", node.toString());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative-url");

        Node node = new Element("a", baseUri, attributes);

        assertEquals("https://example.com/relative-url", node.absUrl("href"));
        assertEquals("", node.absUrl("nonexistentattr"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parentNode.appendChild(child1);
        parentNode.appendChild(child2);

        Elements children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetChildNodeByIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parentNode.appendChild(child1);
        parentNode.appendChild(child2);

        Node child = parentNode.childNode(1);

        assertEquals(child2, child);
    }

    @Test
    public void testGetParentNode() {
        String parentBaseUri = "https://parent.example.com";
        String childBaseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", parentBaseUri, attributes);
        Node childNode = new Element("p", childBaseUri, attributes);

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
        assertEquals(null, childNode.parent()
                .childNodes().get(0).parent());
    }

    @Test
    public void testRemove() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        parentNode.appendChild(childNode);

        parentNode.removeChild(childNode);

        assertEquals(0, parentNode.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode = new Element("p", baseUri, attributes);
        Node newChildNode = new Element("span", baseUri, attributes);

        parentNode.appendChild(oldChildNode);

        parentNode.replaceChild(oldChildNode, newChildNode);

        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newChildNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testSetParentNode() {
        String parentBaseUri = "https://parent.example.com";
        String childBaseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", parentBaseUri, attributes);
        Node childNode = new Element("p", childBaseUri, attributes);

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
        assertEquals(null, childNode.parent().childNodes().get(0).parent());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);
        Node child3 = new Element("p", baseUri, attributes);

        parentNode.addChildren(child1, child2, child3);

        assertEquals(3, parentNode.childNodes().size());
        assertEquals(child1, parentNode.childNodes().get(0));
        assertEquals(child2, parentNode.childNodes().get(1));
        assertEquals(child3, parentNode.childNodes().get(2));
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);
        Node child3 = new Element("p", baseUri, attributes);

        List<Node> children = new ArrayList<>(Collections.nCopies(3, null));

        parentNode.addChildren(2, child1, child2, child3);

        assertEquals(3, parentNode.childNodes().size());
        assertEquals(child1, parentNode.childNodes().get(1));
        assertEquals(child2, parentNode.childNodes().get(2));
        assertEquals(child3, parentNode.childNodes().get(0));
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "exampleClass");

        Node node = new Element("div", baseUri, attributes);

        node.outerHtmlHead(new StringBuilder(), 0, null);
        node.outerHtmlTail(new StringBuilder(), 0, null);
        node.outerHtml(new StringBuilder());

        StringBuilder expected = new StringBuilder("<div class=\"exampleClass\">");
        expected.append(node.toString());
        expected.append("</div>");

        assertEquals(expected.toString(), node.outerHtml());
    }
}