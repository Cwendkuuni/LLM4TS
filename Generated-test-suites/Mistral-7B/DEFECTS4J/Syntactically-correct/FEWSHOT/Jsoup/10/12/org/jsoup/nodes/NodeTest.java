package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeName() {
        Element element = new Element("div");
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testAttr() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Element element = new Element("div", "", attributes);
        assertEquals("test", element.attr("class"));
        assertEquals("", element.attr("non-existent-attribute"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Element element = new Element("div", "", attributes);
        assertEquals(attributes, element.attributes());
    }

    @Test
    public void testHasAttr() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Element element = new Element("div", "", attributes);
        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Element element = new Element("div", "", attributes);
        element.removeAttr("class");
        assertFalse(element.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");

        Element link = new Element("a", "", attributes);
        Document document = new Document("http://example.com");
        link.setBaseUri(document.baseUri());

        assertEquals("http://example.com/relative.html", link.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Element parent = new Element("ul");

        Element child1 = new Element("li", "", null);
        Element child2 = new Element("li", "", null);

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testParent() {
        Element child = new Element("li");
        Element parent = new Element("ul");

        parent.addChild(child);

        assertEquals(parent, child.parent());
        assertEquals(null, child.parent()); // for the root element
    }

    @Test
    public void testOwnerDocument() {
        Element child = new Element("li");
        Document document = new Document();
        document.addChild(child);

        assertEquals(document, child.ownerDocument());
        assertEquals(null, child.ownerDocument()); // for the root document
    }

    @Test
    public void testRemove() {
        Element parent = new Element("ul");
        Element child = new Element("li");

        parent.addChild(child);

        parent.removeChild(child);

        List<Node> children = parent.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWith() {
        Element parent = new Element("ul");
        Element oldChild = new Element("li");
        Element newChild = new Element("li");

        parent.addChild(oldChild);

        parent.replaceChild(oldChild, newChild);

        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChild, children.get(0));
    }

    @Test
    public void testReindexChildren() {
        Element parent = new Element("ul");
        Element child1 = new Element("li");
        Element child2 = new Element("li");

        parent.addChildren(child1, child2);

        parent.reindexChildren();

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testNextSibling() {
        Element parent = new Element("ul");
        Element child1 = new Element("li");
        Element child2 = new Element("li");

        parent.addChildren(child1, child2);

        assertEquals(child2, child1.nextSibling());
        assertEquals(null, child2.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Element parent = new Element("ul");
        Element child1 = new Element("li");
        Element child2 = new Element("li");

        parent.addChildren(child1, child2);

        assertEquals(child1, child2.previousSibling());
        assertEquals(null, child1.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Element parent = new Element("ul");
        Element child1 = new Element("li");
        Element child2 = new Element("li");

        parent.addChildren(child1, child2);

        assertEquals(0, child1.siblingIndex());
        assertEquals(1, child2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        Element element = new Element("div");
        element.attr("class", "test");
        element.appendChild(new TextNode("Hello World!"));

        StringBuilder accum = new StringBuilder();
        element.outerHtml(accum);

        assertEquals("<div class=\"test\">Hello World!</div>", accum.toString());
    }
}