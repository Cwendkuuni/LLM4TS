package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParserUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testBasicNodeCreation() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testEmptyNodeCreation() {
        Node node = new Node();

        assertNull(node.nodeName());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        Node node = new Element("a", "http://example.com/", new Attributes());
        node.attr("href", "http://example.org/");

        assertEquals("http://example.org/", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithRelativeUrl() {
        Node node = new Element("a", "http://example.com/", new Attributes());
        node.attr("href", "foo.html");

        assertEquals("http://example.com/foo.html", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithEmptyAttribute() {
        Node node = new Element("a", "http://example.com/", new Attributes());

        assertEquals("", node.absUrl(""));
    }

    @Test
    public void testAbsUrlWithNullAttribute() {
        Node node = new Element("a", "http://example.com/", new Attributes());

        assertEquals("", node.absUrl(null));
    }

    @Test
    public void testAbsUrlWithNonExistentAttribute() {
        Node node = new Element("a", "http://example.com/", new Attributes());

        assertEquals("", node.absUrl("nonExistentAttribute"));
    }

    @Test
    public void testGetChildNodeByIndex() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parentNode = new Element("div", baseUri, attributes);
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new TextNode("Child 1"));
        childNodes.add(new Element("p", baseUri, new Attributes()));
        childNodes.add(new TextNode("Child 3"));

        parentNode.setChildNodes(childNodes);

        Node childNode = parentNode.childNode(1);

        assertEquals("p", childNode.nodeName());
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parentNode = new Element("div", baseUri, attributes);
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new TextNode("Child 1"));
        childNodes.add(new Element("p", baseUri, new Attributes()));
        childNodes.add(new TextNode("Child 3"));
        childNodes.add(new Element("ul", baseUri, new Attributes()));
        childNodes.add(new Element("li", baseUri, new Attributes()));
        childNodes.add(new TextNode("Child 6"));
        childNodes.add(new Element("li", baseUri, new Attributes()));

        parentNode.setChildNodes(childNodes);

        List<Node> children = parentNode.childNodes();

        assertEquals(6, children.size());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parentNode = new Element("div", baseUri, attributes);
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new TextNode("Child 1"));
        childNodes.add(new Element("p", baseUri, new Attributes()));

        parentNode.setChildNodes(childNodes);

        Node childToReplace = parentNode.childNode(1);
        Node newChild = new TextNode("New Child");

        parentNode.replaceChild(childToReplace, newChild);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals("New Child", children.get(1).outerHtml());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parentNode = new Element("div", baseUri, attributes);
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new TextNode("Child 1"));
        childNodes.add(new Element("p", baseUri, new Attributes()));

        parentNode.setChildNodes(childNodes);

        Node childToRemove = parentNode.childNode(1);

        parentNode.removeChild(childToRemove);

        List<Node> children = parentNode.childNodes();

        assertEquals(1, children.size());
        assertFalse(children.contains(childToRemove));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parentNode = new Element("div", baseUri, attributes);

        Node child1 = new TextNode("Child 1");
        Node child2 = new Element("p", baseUri, new Attributes());

        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals("Child 1", children.get(0).outerHtml());
        assertEquals("p", children.get(1).nodeName());
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node childNode = new Element("p", baseUri, attributes);
        Node parentNode = new Element("div", baseUri, new Attributes());
        parentNode.setChildNodes(Arrays.asList(childNode));

        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testGetOwnerDocument() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node childNode = new Element("p", baseUri, attributes);
        Document document = new Document(baseUri);
        document.setRootElement(new Element("html", baseUri, new Attributes()));
        document.getRootElement().setChildNodes(Arrays.asList(childNode));

        assertEquals(document, childNode.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parentNode = new Element("div", baseUri, attributes);
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new TextNode("Child 1"));
        childNodes.add(new Element("p", baseUri, new Attributes()));

        parentNode.setChildNodes(childNodes);

        Node childToRemove = parentNode.childNode(1);

        childToRemove.remove();

        List<Node> children = parentNode.childNodes();

        assertEquals(1, children.size());
        assertFalse(children.contains(childToRemove));
    }
}