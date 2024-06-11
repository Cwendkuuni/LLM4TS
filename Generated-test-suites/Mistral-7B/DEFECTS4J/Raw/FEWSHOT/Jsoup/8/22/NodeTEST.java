package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeName() {
        Element element = new Element("div");
        assertEquals("div", element.nodeName());

        Document document = new Document();
        assertEquals("document", document.nodeName());
    }

    @Test
    public void testAttr() {
        Element element = new Element("a").attr("href", "http://example.com");
        assertEquals("http://example.com", element.attr("href"));

        assertEquals("", element.attr("non-existent"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("id", "bar");

        Element element = new Element("div").attributes(attributes);
        assertEquals("foo", element.attr("class"));
        assertEquals("bar", element.attr("id"));
        assertEquals("", element.attr("non-existent"));
    }

    @Test
    public void testHasAttr() {
        Element element = new Element("a").attr("href", "http://example.com");
        assertTrue(element.hasAttr("href"));
        assertFalse(element.hasAttr("non-existent"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = new Element("a").attr("href", "http://example.com");
        element.removeAttr("href");
        assertEquals("", element.attr("href"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "https://example.com";
        Element element = new Element("a", baseUri).attr("href", "about.html");

        assertEquals(baseUri, element.baseUri());
        assertEquals("about.html", element.absUrl("href"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "https://example.com";
        Element element = new Element("a").attr("href", "relative.html");

        assertEquals("", element.absUrl("href"));
        assertEquals(baseUri + "/relative.html", element.absUrl("href", baseUri));
    }

    @Test
    public void testChildNode() {
        Element parent = new Element("ul");
        Element child = new Element("li").text("Item 1");
        parent.appendChild(child);

        Node node = parent.childNode(0);
        assertEquals(child, node);
    }

    @Test
    public void testChildNodes() {
        Element parent = new Element("ul");
        Element child1 = new Element("li").text("Item 1");
        Element child2 = new Element("li").text("Item 2");
        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> nodes = parent.childNodes();
        assertEquals(2, nodes.size());
        assertEquals(child1, nodes.get(0));
        assertEquals(child2, nodes.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        Element parent = new Element("ul");
        Element child1 = new Element("li").text("Item 1");
        Element child2 = new Element("li").text("Item 2");
        parent.appendChild(child1);
        parent.appendChild(child2);

        Node[] nodes = parent.childNodesAsArray();
        assertEquals(2, nodes.length);
        assertEquals(child1, nodes[0]);
        assertEquals(child2, nodes[1]);
    }

    @Test
    public void testParent() {
        Element parent = new Element("ul");
        Element child = new Element("li").text("Item 1");
        parent.appendChild(child);

        assertEquals(parent, child.parent());
        assertEquals(null, new TextNode(" ").parent());
    }

    @Test
    public void testOwnerDocument() {
        Element parent = new Element("ul");
        Element child = new Element("li").text("Item 1");
        parent.appendChild(child);

        assertEquals(parent, child.ownerDocument());
        assertTrue(child.ownerDocument() instanceof Document);
    }

    @Test
    public void testRemove() {
        Element parent = new Element("ul");
        Element child = new Element("li").text("Item 1");
        parent.appendChild(child);

        parent.removeChild(child);
        assertTrue(child.parentNode == null);
    }

    @Test
    public void testReplaceWith() {
        Element parent = new Element("ul");
        Element oldChild = new Element("li").text("Item 1");
        Element newChild = new Element("li").text("Item 2");
        parent.appendChild(oldChild);

        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
        assertEquals(null, oldChild.parentNode);
    }

    @Test
    public void testSetParentNode() {
        Element child = new Element("li").text("Item 1");
        Element parent = new Element("ul");

        child.setParentNode(parent);
        assertEquals(parent, child.parentNode);
    }

    @Test
    public void testReplaceChild() {
        Element parent = new Element("ul");
        Element oldChild = new Element("li").text("Item 1");
        Element newChild = new Element("li").text("Item 2");

        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
        assertEquals(null, oldChild.parentNode);
    }

    @Test
    public void testRemoveChild() {
        Element parent = new Element("ul");
        Element child = new Element("li").text("Item 1");
        parent.appendChild(child);

        parent.removeChild(child);
        assertTrue(child.parentNode == null);
    }

    @Test
    public void testAddChildren() {
        Element parent = new Element("ul");
        Element child1 = new Element("li").text("Item 1");
        Element child2 = new Element("li").text("Item 2");

        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Element parent = new Element("ul");
        Element child1 = new Element("li").text("Item 1");
        Element child2 = new Element("li").text("Item 2");

        parent.addChildren(1, child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testOuterHtml() {
        Element element = new Element("a").attr("href", "http://example.com").text("Link");

        StringBuilder accum = new StringBuilder();
        element.outerHtml(accum);

        assertEquals("<a href=\"http://example.com\">Link</a>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithIndent() {
        Element element = new Element("div").text("Div");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.indentSize(4);

        StringBuilder accum = new StringBuilder();
        element.outerHtml(accum, 1, settings);

        assertEquals("<div>\n\tDiv\n</div>", accum.toString());
    }
}