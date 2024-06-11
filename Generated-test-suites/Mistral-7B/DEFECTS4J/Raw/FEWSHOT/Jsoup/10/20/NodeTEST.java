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
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeName() {
        Element element = new Element("div");
        assertEquals("div", element.nodeName());

        TextNode textNode = new TextNode("Hello World");
        assertEquals("#text", textNode.nodeName());

        Document document = new Document();
        assertEquals("document", document.nodeName());
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("id", "myId");
        attributes.put("class", "myClass");

        Element element = new Element("div").attrs(attributes);

        assertEquals("myId", element.attr("id"));
        assertEquals("myClass", element.attr("class"));
        assertEquals("", element.attr("nonExistent"));

        assertEquals(attributes, element.attributes());
        assertEquals(2, element.attributes().size());
    }

    @Test
    public void testHasAttr() {
        Attributes attributes = new Attributes();
        attributes.put("id", "myId");

        Element element = new Element("div").attrs(attributes);

        assertTrue(element.hasAttr("id"));
        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr("nonExistent"));
    }

    @Test
    public void testRemoveAttr() {
        Attributes attributes = new Attributes();
        attributes.put("id", "myId");

        Element element = new Element("div").attrs(attributes);

        assertEquals("myId", element.attr("id"));
        assertEquals(1, element.attributes().size());

        element.removeAttr("id");

        assertEquals("", element.attr("id"));
        assertEquals(0, element.attributes().size());
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://www.example.com";
        Element element = new Element("div").baseUri(baseUri);

        assertEquals(baseUri, element.baseUri());

        // testing setting baseUri to null
        element.setBaseUri(null);
        assertNull(element.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "/test.html");

        Element element = new Element("a").baseUri(baseUri).attrs(attributes);

        assertEquals("http://www.example.com/test.html", element.absUrl("href"));

        // testing absUrl with no baseUri
        element.setBaseUri(null);
        assertEquals("", element.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Element parent = new Element("div");

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));

        Node[] children = parent.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testParent() {
        Element parent = new Element("div");
        Element child = new Element("p").parent(parent);

        assertEquals(parent, child.parent());
        assertNull(child.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        Element element = new Element("div").ownerDocument(document);

        assertEquals(document, element.ownerDocument());
        assertEquals(null, document.ownerDocument());
    }

    @Test
    public void testRemove() {
        Element parent = new Element("div");
        Element child = new Element("p").parent(parent);

        parent.addChildren(child);
        parent.removeChild(child);

        assertEquals(0, parent.childNodes().size());
        assertNull(child.parent());
    }

    @Test
    public void testReplaceWith() {
        Element parent = new Element("div");
        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.addChildren(child1);

        parent.replaceChild(child1, child2);

        assertEquals(1, parent.childNodes().size());
        assertEquals(child2, parent.childNode(0));
        assertEquals(child1, child2.parent());
    }

    @Test
    public void testSetParentNode() {
        Element parent1 = new Element("div1");
        Element parent2 = new Element("div2");
        Element child = new Element("p");

        child.setParentNode(parent1);
        assertEquals(parent1, child.parent());

        child.setParentNode(parent2);
        assertEquals(parent2, child.parent());

        // test setting parentNode to null
        child.setParentNode(null);
        assertNull(child.parent());
    }

    @Test
    public void testReplaceChild() {
        Element parent = new Element("div");
        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.addChildren(child1);

        parent.replaceChild(child1, child2);

        assertEquals(1, parent.childNodes().size());
        assertEquals(child2, parent.childNode(0));
        assertEquals(child1, child2.previousSibling());
    }

    @Test
    public void testAddChildren() {
        Element parent = new Element("div");
        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));

        assertEquals(child1, child2.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p").text("Sibling 1");
        Element element = new Element("p").text("Element");
        Element sibling2 = new Element("p").text("Sibling 2");

        parent.addChildren(sibling1, element, sibling2);

        assertEquals(1, element.siblingIndex());
        assertEquals(sibling1, element.previousSibling());
        assertEquals(sibling2, element.nextSibling());
    }

    @Test
    public void testOuterHtml() {
        Element element = new Element("p").text("Hello World");

        StringBuilder accum = new StringBuilder(32 * 1024);
        element.outerHtml(accum);

        assertEquals("<p>Hello World</p>", accum.toString());
    }

    @Test
    public void testEquals() {
        Element element1 = new Element("p").text("Hello World");
        Element element2 = new Element("p").text("Hello World");

        assertEquals(element1, element2);
    }

    @Test
    public void testHashCode() {
        Element element1 = new Element("p").text("Hello World");
        Element element2 = new Element("p").text("Hello World");

        assertEquals(element1.hashCode(), element2.hashCode());
    }
}