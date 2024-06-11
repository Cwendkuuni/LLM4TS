package org.jsoup.nodes; // update the package name if necessary

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import static org.jsoup.nodes.Node.OutputSettings.indentAmount;

public class NodeTest {
    

    @Test
    public void testNodeConstructor() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        Assert.assertNotNull(node);
        Assert.assertNotNull(node.baseUri());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNotNull(node.attributes());
    }

    @Test
    public void testAbstractNodeConstructor() {
        Node node = new Node();

        Assert.assertNull(node.baseUri());
        Assert.assertNull(node.childNodes());
        Assert.assertNull(node.attributes());
    }

    @Test
    public void testNodeName() {
        Element element = new Element("div");

        Assert.assertEquals("div", element.nodeName());
    }

    @Test
    public void testAttr() {
        Element element = new Element("a");
        element.attr("href", "https://example.com");

        Assert.assertEquals("https://example.com", element.attr("href"));
        Assert.assertEquals("", element.attr("non-existent-attribute"));
    }

    @Test
    public void testAttributes() {
        Element element = new Element("a");
        element.attr("href", "https://example.com");
        element.attr("class", "example");

        Attributes attributes = element.attributes();

        Assert.assertEquals("href=\"https://example.com\" class=\"example\"", attributes.toString());
    }

    @Test
    public void testHasAttr() {
        Element element = new Element("a");
        element.attr("href", "https://example.com");

        Assert.assertTrue(element.hasAttr("href"));
        Assert.assertFalse(element.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = new Element("a");
        element.attr("href", "https://example.com");

        element.removeAttr("href");

        Assert.assertFalse(element.hasAttr("href"));
    }

    @Test
    public void testAbsUrl() {
        Element link = new Element("a");
        link.attr("abs:href", "https://test.com/page/1");

        Assert.assertEquals("https://test.com/page/1", link.absUrl("abs:href"));
        Assert.assertEquals("", link.absUrl("non-existent-attribute"));
    }

    @Test
    public void testChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");

        parent.addChild(child);

        Node node = parent.childNode(0);

        Assert.assertEquals(child, node);
    }

    @Test
    public void testChildNodes() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.addChildren(child1, child2);

        List<Node> childNodes = parent.childNodes();

        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(child1, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));
    }

    @Test
    public void testParent() {
        Element child = new Element("p");
        Element parent = new Element("div");

        child.setParentNode(parent);

        Assert.assertEquals(parent, child.parent());
    }

    @Test(expected = IllegalStateException.class)
    public void testParentWhenNoParent() {
        Element child = new Element("p");

        child.parent();
    }

    @Test
    public void testOwnerDocument() {
        Element element = new Element("p");

        Document document = new Document();
        document.appendChild(element);

        Assert.assertEquals(document, element.ownerDocument());
        Assert.assertEquals(null, element.ownerDocument()); // when no document
    }

    @Test
    public void testRemove() {
        Element parent = new Element("div");
        Element child = new Element("p");

        parent.addChild(child);

        parent.removeChild(child);

        Assert.assertFalse(parent.hasChild(child));
    }

    @Test
    public void testReplaceWith() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.appendChild(child1);

        parent.replaceChild(child1, child2);

        Assert.assertEquals(child2, parent.childNode(0));
    }

    @Test
    public void testSetParentNode() {
        Element child = new Element("p");
        Element parent1 = new Element("div");
        Element parent2 = new Element("div");

        child.setParentNode(parent1);
        child.setParentNode(parent2);

        Assert.assertEquals(parent2, child.parent());
    }

    @Test
    public void testSetSiblingIndex() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element sibling2 = new Element("p");
        Element sibling3 = new Element("p");

        parent.addChildren(sibling1, sibling2, sibling3);
        sibling1.setSiblingIndex(1);

        Assert.assertEquals(1, sibling1.siblingIndex());
        Assert.assertEquals(2, sibling2.siblingIndex());
        Assert.assertEquals(3, sibling3.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        Element element = new Element("p");
        element.outerHtml("test");

        StringBuilder accum = new StringBuilder();
        element.outerHtml(accum);

        Assert.assertEquals("<p></p>", accum.toString());
    }

    @Test
    public void testOuterHtmlHead() {
        Element element = new TextNode("Hello World!");

        StringBuilder accum = new StringBuilder();
        element.outerHtmlHead(accum, 0, null);

        Assert.assertEquals("<p>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Element element = new TextNode("Hello World!");

        StringBuilder accum = new StringBuilder();
        element.outerHtmlTail(accum, 0, null);

        Assert.assertEquals("</p>", accum.toString());
    }

    @Test
    public void testIndent() {
        StringBuilder accum = new StringBuilder();
        indent(accum, 2, null);

        Assert.assertEquals("\n  ", accum.toString());
    }

    @Test
    public void testEquals() {
        Element element1 = new Element("p");
        Element element2 = new Element("p");

        Assert.assertFalse(element1.equals(element2));
    }

    @Test
    public void testHashCode() {
        Element element1 = new Element("p");
        Element element2 = new Element("p");

        Assert.assertEquals(element1.hashCode(), element2.hashCode());

        Node node = new Node();
        Attributes attributes = new Attributes();
        Element element3 = new Element("p", node, attributes);

        Assert.assertNotEquals(element1.hashCode(), element3.hashCode());
    }

    
}