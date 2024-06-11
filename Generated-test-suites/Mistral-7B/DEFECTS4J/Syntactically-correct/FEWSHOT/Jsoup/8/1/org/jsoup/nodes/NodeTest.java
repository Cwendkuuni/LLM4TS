package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("example", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertSame(attributes, node1.attributes());

        assertEquals("", node2.nodeName());
        assertNull(node2.baseUri());
        assertNull(node2.attributes());

        assertEquals("", node3.nodeName());
        assertNull(node3.baseUri());
        assertNull(node3.attributes());
    }

    @Test
    public void testAttributeGetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node = new Element("div", baseUri, attributes);

        assertEquals("class", node.attr("class"));
        assertEquals("example", node.attr("class"));
        assertNull(node.attr("non-existent"));

        String absUrl = node.absUrl("href");
        assertEquals("", absUrl);

        List<Node> children = new ArrayList<>();
        children.add(new TextNode("Child 1"));
        children.add(node);
        Node parent = new Element("body", baseUri, attributes).addChildren(children);

        assertEquals(baseUri, parent.baseUri());
        assertEquals(children, parent.childNodes());

        Node child = parent.childNode(0);
        assertEquals("Child 1", child.outerHtml());
        assertEquals("text", child.nodeName());

        child = parent.childNode(1);
        assertEquals("div", child.nodeName());
        assertEquals("example", child.attr("class"));

        List<Node> siblings = parent.siblingNodes();
        assertTrue(siblings.isEmpty());

        Node previousSibling = parent.previousSibling();
        assertNull(previousSibling);

        Node nextSibling = parent.nextSibling();
        assertNull(nextSibling);
    }

    @Test
    public void testAttributeSetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("div", baseUri, attributes);

        node.attr("class", "new-example");
        assertEquals("new-example", node.attr("class"));
        assertEquals("new-example", node.attributes().get("class"));

        node.removeAttr("class");
        assertNull(node.attr("class"));
        assertEquals(Collections.emptyMap(), node.attributes());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Element("a", baseUri, new Attributes().put("href", "relative.html"));
        assertEquals("", node.absUrl("href"));

        String absUrl = node.absUrl("href");
        assertEquals("http://example.com/relative.html", absUrl);

        node = new Element("img", baseUri, new Attributes().put("src", "relative.png"));
        absUrl = node.absUrl("src");
        assertEquals("http://example.com/relative.png", absUrl);

        node = new Element("a", null, new Attributes().put("href", "absolute.html"));
        absUrl = node.absUrl("href");
        assertEquals("absolute.html", absUrl);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        Node child1 = new Element("div", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();
        List<Node> expectedChildren = Arrays.asList(child1, child2, parent);
        assertEquals(expectedChildren, children);

        Node firstChild = parent.childNode(0);
        assertEquals(child1, firstChild);

        Node lastChild = parent.childNode(children.size() - 1);
        assertEquals(parent, lastChild);

        parent.removeChild(child1);
        children = parent.childNodes();
        List<Node> expectedChildrenWithoutChild1 = Arrays.asList(child2, parent);
        assertEquals(expectedChildrenWithoutChild1, children);
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        Node child1 = new Element("div", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.addChildren(child1, child2);

        Node newChild = new Element("ul", baseUri, new Attributes());
        parent.replaceChild(child1, newChild);

        List<Node> children = parent.childNodes();
        List<Node> expectedChildren = Arrays.asList(newChild, child2, parent);
        assertEquals(expectedChildren, children);
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        Node child1 = new Element("div", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.addChildren(child1, child2);

        parent.removeChild(child1);

        List<Node> children = parent.childNodes();
        List<Node> expectedChildren = Arrays.asList(child2, parent);
        assertEquals(expectedChildren, children);

        assertFalse(child1.parentNode.equals(parent));
        assertNull(child1.parentNode);
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);

        Node child1 = new Element("div", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);
        Node child3 = new Element("ul", baseUri, new Attributes().put("class", "list"));

        parent.addChildren(child1, child2, child3);

        List<Node> children = parent.childNodes();
        List<Node> expectedChildren = Arrays.asList(child1, child2, child3, parent);
        assertEquals(expectedChildren, children);
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node child = new Element("div", baseUri, attributes);
        Node parent = new Element("body", baseUri, attributes);

        child.setParentNode(parent);

        assertEquals(parent, child.parentNode);
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        Node olderSibling = new Element("p", baseUri, attributes);
        Node currentNode = new Element("div", baseUri, attributes);
        Node youngerSibling = new Element("p", baseUri, attributes);

        parent.addChildren(olderSibling, currentNode, youngerSibling);

        currentNode.setSiblingIndex(1);

        assertEquals(1, currentNode.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("div", baseUri, attributes);

        StringBuilder accum = new StringBuilder(32 * 1024);
        node.outerHtml(accum);

        String expectedHtml = "<div></div>";
        assertEquals(expectedHtml, accum.toString());

        attributes.put("class", "example");
        node.outerHtmlHead(accum, 0, null);
        node.outerHtmlTail(accum, 0, null);

        expectedHtml = "<div class=\"example\"></div>";
        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testOuterHtmlWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        Node child1 = new Element("div", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.addChildren(child1, child2);

        StringBuilder accum = new StringBuilder(32 * 1024);
        parent.outerHtml(accum);

        String expectedHtml = "<body>\n<div></div>\n<p></p>\n</body>";
        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testOuterHtmlWithChildrenAndCustomSettings() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        Node child1 = new Element("div", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.addChildren(child1, child2);

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setPrettify(true);

        StringBuilder accum = new StringBuilder(32 * 1024);
        parent.outerHtml(accum, outputSettings);

        String expectedHtml = "<body>\n<div>\n</div>\n<p>\n</p>\n</body>";
        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testOuterHtmlWithTextNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        TextNode textNode = new TextNode("Test");

        parent.addChildren(textNode);

        StringBuilder accum = new StringBuilder(32 * 1024);
        parent.outerHtml(accum);

        String expectedHtml = "<body>\nTest\n</body>";
        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testOuterHtmlWithEmptyTextNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        TextNode textNode = new TextNode("");

        parent.addChildren(textNode);

        StringBuilder accum = new StringBuilder(32 * 1024);
        parent.outerHtml(accum);

        String expectedHtml = "<body>\n\n</body>";
        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testOuterHtmlWithAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "example");

        Node node = new Element("div", baseUri, attributes);
        attributes.put("data-test", "foo");

        StringBuilder accum = new StringBuilder(32 * 1024);
        node.outerHtml(accum);

        String expectedHtml = "<div id=\"example\" data-test=\"foo\"></div>";
        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testOuterHtmlWithNestedElements() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("body", baseUri, attributes);
        Node child1 = new Element("div", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.addChildren(child1);
        child1.addChildren(child2);

        StringBuilder accum = new StringBuilder(32 * 1024);
        parent.outerHtml(accum);

        String expectedHtml = "<body>\n<div>\n<p>\n</p>\n</div>\n</body>";
        assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes().put("class", "example");
        Attributes attributes2 = new Attributes().put("class", "example");

        Node node1 = new Element("div", baseUri, attributes1);
        Node node2 = new Element("div", baseUri, attributes2);

        assertEquals(node1, node2);
        assertEquals(node1.hashCode(), node2.hashCode());

        node2.removeAttr("class");
        assertFalse(node1.equals(node2));
        assertFalse(node1.hashCode() == node2.hashCode());

        Node node3 = new Element("p", baseUri, attributes1);
        assertFalse(node1.equals(node3));
        assertFalse(node1.hashCode() == node3.hashCode());

        Node node4 = new Element("div", baseUri, attributes2);
        assertEquals(node1, node4);
        assertEquals(node1.hashCode(), node4.hashCode());
    }
}