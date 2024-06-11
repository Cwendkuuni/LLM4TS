package org.jsoup.nodes; // adjust the package name if necessary

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    private Node node;
    private Document document;

    @Before
    public void setup() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        document = new Document(baseUri, attributes);
        node = new Element("div", baseUri, attributes);
        document.appendChild(node);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("example", node.attr("class"));
        assertFalse(node.hasAttr("non-existent-attribute"));
        assertNull(node.attr("non-existent-attribute"));

        node.attr("new-attribute", "new-value");
        assertEquals("new-value", node.attr("new-attribute"));
    }

    @Test
    public void testAttributes() {
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://other-example.com");
        assertEquals("http://other-example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String href = "about:blank";
        node.attr("href", href);

        String absUrl = node.absUrl("href");
        assertEquals("", absUrl); // relative URL, no base URI

        node.setBaseUri("http://example.com");
        absUrl = node.absUrl("href");
        assertEquals("http://example.com" + href, absUrl); // absolute URL from base URI

        try {
            new URL(absUrl);
        } catch (MalformedURLException e) {
            fail("Invalid absolute URL: " + absUrl);
        }
    }

    @Test
    public void testChildNode() {
        Element child = new Element("p", baseUri, new Attributes());
        node.appendChild(child);

        Node childNode = node.childNode(0);
        assertSame(child, childNode);
    }

    @Test
    public void testChildren() {
        Element child1 = new Element("p", baseUri, new Attributes());
        Element child2 = new Element("p", baseUri, new Attributes());

        node.appendChild(child1);
        node.appendChild(child2);

        List<Node> children = node.childNodes();
        List<Node> expectedChildren = Arrays.asList(child1, child2);

        assertEquals(expectedChildren, children);
    }

    @Test
    public void testEmptyChildren() {
        List<Node> children = node.childNodes();
        assertTrue(children.isEmpty());
    }

    @Test
    public void testParent() {
        Document parentDocument = new Document(baseUri, new Attributes());
        Element parent = new Element("body", baseUri, new Attributes());
        parentDocument.appendChild(parent);
        parent.appendChild(node);

        Node parentNode = node.parent();
        assertSame(parent, parentNode);
    }

    @Test
    public void testOwnerDocument() {
        Document ownerDocument = document;
        assertSame(ownerDocument, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        document.remove(node);
        assertFalse(document.contains(node));
    }

    @Test
    public void testReplaceWith() {
        Element replacement = new Element("span", baseUri, new Attributes());
        node.replaceWith(replacement);

        List<Node> children = document.childNodes();
        int indexOfNode = children.indexOf(node);
        int indexOfReplacement = children.indexOf(replacement);

        assertEquals(indexOfNode, indexOfReplacement);
        assertFalse(document.contains(node));
        assertTrue(document.contains(replacement));
    }

    @Test
    public void testSetParentNode() {
        Element newParent = new Element("ul", baseUri, new Attributes());
        node.setParentNode(newParent);

        assertEquals(newParent, node.parentNode);
    }

    @Test
    public void testReplaceChild() {
        Element replacement = new Element("span", baseUri, new Attributes());

        node.replaceChild(node.childNode(0), replacement);

        List<Node> children = document.childNodes();
        int indexOfNode = children.indexOf(node);
        int indexOfReplacement = children.indexOf(replacement);

        assertEquals(indexOfNode, indexOfReplacement);
        assertFalse(document.contains(node.childNode(0)));
        assertTrue(document.contains(replacement));
    }

    @Test
    public void testRemoveChild() {
        Element child = node.childNode(0);
        node.removeChild(child);

        List<Node> children = document.childNodes();
        int indexOfChild = children.indexOf(child);
        int indexOfNode = children.indexOf(node);

        assertFalse(document.contains(child));
        assertEquals(indexOfNode, indexOfChild + 1);
    }

    @Test
    public void testAddChildren() {
        Element child1 = new Element("p", baseUri, new Attributes());
        Element child2 = new Element("p", baseUri, new Attributes());

        node.addChildren(child1, child2);

        List<Node> children = document.childNodes();
        List<Node> expectedChildren = new ArrayList<>(Arrays.asList(child1, child2));
        expectedChildren.add(node);

        assertEquals(expectedChildren, children);
    }

    @Test
    public void testAddChildrenWithIndex() {
        Element child1 = new Element("p", baseUri, new Attributes());
        Element child2 = new Element("p", baseUri, new Attributes());

        node.addChildren(1, child1, child2);

        List<Node> children = document.childNodes();
        List<Node> expectedChildren = new ArrayList<>(Arrays.asList(child1, node, child2));

        assertEquals(expectedChildren, children);
    }

    @Test
    public void testSiblingIndex() {
        Element sibling1 = new Element("p", baseUri, new Attributes());
        Element sibling2 = new Element("p", baseUri, new Attributes());

        node.addSibling(sibling1);
        node.addSibling(sibling2);

        int nodeIndex = node.siblingIndex();
        int sibling1Index = sibling1.siblingIndex();
        int sibling2Index = sibling2.siblingIndex();

        assertEquals(sibling1Index, nodeIndex + 1);
        assertEquals(sibling2Index, sibling1Index + 1);
    }

    @Test
    public void testOuterHtml() {
        StringBuilder expectedOuterHtml = new StringBuilder();
        expectedOuterHtml.append("<div class=\"example\">");
        expectedOuterHtml.append("<p></p>");
        expectedOuterHtml.append("</div>");

        StringBuilder actualOuterHtml = new StringBuilder();
        node.outerHtml(actualOuterHtml);

        assertEquals(expectedOuterHtml.toString(), actualOuterHtml.toString());
    }

    @Test
    public void testClone() {
        Node clonedNode = node.clone();

        assertEquals(node, clonedNode);
        assertFalse(node == clonedNode);

        List<Node> children = document.childNodes();
        int indexOfNode = children.indexOf(node);
        int indexOfClonedNode = children.indexOf(clonedNode);

        assertFalse(node.equals(clonedNode));
        assertEquals(indexOfNode, indexOfClonedNode);
        assertEquals(node.hashCode(), clonedNode.hashCode());
    }
}