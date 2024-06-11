package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class NodeTest {

    private Node rootNode;
    private Document doc;

    @Before
    public void setup() {
        doc = new Document();
        rootNode = doc.createElement("html");
        doc.setRootElement(rootNode);
    }

    @Test
    public void testNodeConstructor() {
        Node node = new Node("http://example.com", new AttributesImpl());
        assertEquals("http://example.com", node.baseUri());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeAbstractConstructor() {
        Node node = new Node();
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeName() {
        Element elem = doc.createElement("div");
        assertEquals("div", elem.nodeName());
    }

    @Test
    public void testAttr() {
        Element link = doc.createElement("a");
        link.attr("href", "http://example.com");
        assertEquals("http://example.com", link.attr("href"));
        assertEquals("", link.attr("nonexistent"));
        link.attr("abs:href", "http://another-example.com");
        assertEquals("http://another-example.com", link.attr("abs:href"));
    }

    @Test
    public void testAttributes() {
        Element element = doc.createElement("div");
        Attributes attributes = new AttributesImpl();
        attributes.put("class", "myclass");
        element.setAttributes(attributes);
        assertEquals("class", element.attrName(0));
        assertEquals("myclass", element.attr("class"));
        assertEquals(1, element.attributes().size());
    }

    @Test
    public void testHasAttr() {
        Element element = doc.createElement("div");
        element.attr("class", "myclass");
        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = doc.createElement("div");
        element.attr("class", "myclass");
        element.removeAttr("class");
        assertFalse(element.hasAttr("class"));
        assertNull(element.attr("class"));
    }

    @Test
    public void testBaseUri() {
        Element link = doc.createElement("a");
        link.setBaseUri("http://example.com");
        assertEquals("http://example.com", link.baseUri());
        link.setBaseUri("http://another-example.com");
        assertEquals("http://another-example.com", link.baseUri());
        Element textNode = doc.createTextNode("Text node");
        textNode.setBaseUri(null);
        assertNull(textNode.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        Element link = doc.createElement("a");
        link.setBaseUri(null);
        link.setBaseUri("http://example.com");
        assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Element link = doc.createElement("a");
        link.attr("href", "/relative/path");
        assertEquals("", link.absUrl("href"));
        link.setBaseUri("http://example.com");
        assertEquals("http://example.com/relative/path", link.absUrl("href"));
        link.setBaseUri("http://example.com/relative");
        assertEquals("http://example.com/relative/path", link.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Element parent = doc.createElement("div");
        Element child = doc.createElement("p");
        parent.add(child);
        Element childFromParent = parent.childNode(0);
        assertEquals(child, childFromParent);
    }

    @Test
    public void testChildren() {
        Element parent = doc.createElement("div");
        Element child1 = doc.createElement("p");
        Element child2 = doc.createElement("p");
        parent.add(child1);
        parent.add(child2);
        Element firstChild = parent.childNodes().get(0);
        assertEquals(child1, firstChild);
        Element lastChild = parent.childNodes().get(parent.childNodes().size() - 1);
        assertEquals(child2, lastChild);
    }

    @Test
    public void testChildNodeArray() {
        Element parent = doc.createElement("div");
        Element child1 = doc.createElement("p");
        Element child2 = doc.createElement("p");
        parent.add(child1);
        parent.add(child2);
        Node[] children = parent.childNodesAsArray();
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testParent() {
        Element parent = doc.createElement("div");
        Element child = doc.createElement("p");
        parent.add(child);
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
        child.setParentNode(null);
        assertNull(child.parent());
    }

    @Test
    public void testOwnerDocument() {
        Element element = doc.createElement("div");
        assertEquals(doc, element.ownerDocument());
    }

    @Test
    public void testRemove() {
        Element parent = doc.createElement("div");
        Element child = doc.createElement("p");
        parent.add(child);
        parent.removeChild(child);
        assertTrue(parent.childNodes().isEmpty());
        assertNull(child.parentNode());
    }

    @Test
    public void testReplaceWith() {
        Element parent = doc.createElement("div");
        Element oldChild = doc.createElement("p");
        Element newChild = doc.createElement("p");
        parent.add(oldChild);
        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
        assertNull(oldChild.parentNode());
    }

    @Test
    public void testSetParentNode() {
        Element parent = doc.createElement("div");
        Element child = doc.createElement("p");
        child.setParentNode(parent);
        parent.removeChild(child);
        child.setParentNode(null);
        assertNull(child.parentNode());
        parent.add(child);
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testReplaceChild() {
        Element parent = doc.createElement("div");
        Element oldChild = doc.createElement("p");
        Element newChild = doc.createElement("p");
        parent.add(oldChild);
        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
        assertNull(oldChild.parentNode());
    }

    @Test
    public void testAddChildren() {
        Element parent = doc.createElement("div");
        Element child1 = doc.createElement("p");
        Element child2 = doc.createElement("p");
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Element parent = doc.createElement("div");
        Element child1 = doc.createElement("p");
        Element child2 = doc.createElement("p");
        parent.addChildren(1, child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(1));
        assertEquals(child2, parent.childNodes().get(0));
    }

    @Test
    public void testSiblingNodes() {
        Element parent = doc.createElement("div");
        Element sibling1 = doc.createElement("p");
        Element sibling2 = doc.createElement("p");
        Element sibling3 = doc.createElement("p");
        parent.add(sibling1);
        parent.add(sibling2);
        parent.add(sibling3);
        List<Node> siblings = parent.siblingNodes();
        assertEquals(3, siblings.size());
        assertEquals(sibling1, siblings.get(0));
        assertEquals(sibling2, siblings.get(1));
        assertEquals(sibling3, siblings.get(2));
    }

    @Test
    public void testNextSibling() {
        Element parent = doc.createElement("div");
        Element sibling1 = doc.createElement("p");
        Element sibling2 = doc.createElement("p");
        parent.add(sibling1);
        parent.add(sibling2);
        Element nextSibling = sibling1.nextSibling();
        assertNull(nextSibling);
        nextSibling = sibling2.nextSibling();
        assertNull(nextSibling);
    }

    @Test
    public void testPreviousSibling() {
        Element parent = doc.createElement("div");
        Element sibling1 = doc.createElement("p");
        Element sibling2 = doc.createElement("p");
        parent.add(sibling1);
        parent.add(sibling2);
        Element previousSibling = sibling2.previousSibling();
        assertNull(previousSibling);
        previousSibling = sibling1.previousSibling();
        assertNull(previousSibling);
    }

    @Test
    public void testSiblingIndex() {
        Element parent = doc.createElement("div");
        Element sibling1 = doc.createElement("p");
        Element sibling2 = doc.createElement("p");
        parent.add(sibling1);
        parent.add(sibling2);
        assertEquals(0, sibling1.siblingIndex());
        assertEquals(1, sibling2.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        Element parent = doc.createElement("div");
        Element sibling1 = doc.createElement("p");
        Element sibling2 = doc.createElement("p");
        parent.add(sibling1);
        parent.add(sibling2);
        sibling1.setSiblingIndex(1);
        sibling2.setSiblingIndex(0);
        assertEquals(0, sibling2.siblingIndex());
        assertEquals(1, sibling1.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        Element element = doc.createElement("div");
        element.outerHtml("test");
        assertEquals("<div></div>", element.outerHtml());
        Element withText = doc.createElement("p");
        withText.text("Test text");
        assertEquals("<p>Test text</p>", withText.outerHtml());
        Element withAttributes = doc.createElement("a");
        withAttributes.attr("href", "http://example.com");
        withAttributes.attr("class", "myclass");
        assertEquals("<a href=\"http://example.com\" class=\"myclass\"></a>", withAttributes.outerHtml());
    }
}