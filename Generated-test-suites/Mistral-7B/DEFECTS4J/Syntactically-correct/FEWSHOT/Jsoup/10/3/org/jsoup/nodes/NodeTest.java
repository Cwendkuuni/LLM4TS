package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testNewNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element(baseUri, "div", attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
    }

    @Test
    public void testNewNodeWithAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("class", "foo").put("id", "bar");
        Node node = new Element(baseUri, "div", attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertTrue(node.hasAttr("class"));
        assertEquals("foo", node.attr("class"));
        assertTrue(node.hasAttr("id"));
        assertEquals("bar", node.attr("id"));
    }

    @Test
    public void testNewNodeWithEmptyList() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element(baseUri, "div", attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.emptyList(), node.childNodes());
    }

    @Test
    public void testNewNodeWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child1 = new TextNode("Child 1");
        Node child2 = new Element(baseUri, "p", attributes);
        child2.appendText("Child 2");
        parent.appendChild(child1);
        parent.appendChild(child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        Node firstChild = children.get(0);
        assertEquals("Child 1", firstChild.outerHtml());
        assertTrue(firstChild instanceof TextNode);
        Node secondChild = children.get(1);
        assertEquals("p", secondChild.nodeName());
        assertEquals("Child 2", secondChild.outerHtml());
        assertTrue(secondChild instanceof Element);
        assertEquals(parent, child1.parentNode());
        assertEquals(parent, child2.parentNode());
    }

    @Test
    public void testNewNodeWithParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child = new Element(baseUri, "p", attributes);
        child.setAttribute("class", "foo");
        parent.appendChild(child);
        assertEquals(1, parent.childNodes().size());
        Node childNode = parent.childNodes().get(0);
        assertEquals("p", childNode.nodeName());
        assertEquals("foo", childNode.attr("class"));
        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testEmptyList() {
        Node node = new Element("", "div");
        assertEquals(Collections.emptyList(), node.childNodes());
    }

    @Test
    public void testGetChildNodeByIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child1 = new TextNode("Child 1");
        Node child2 = new Element(baseUri, "p", attributes);
        child2.appendText("Child 2");
        parent.appendChild(child1);
        parent.appendChild(child2);
        Node child = parent.childNode(1);
        assertEquals("p", child.nodeName());
        assertEquals("Child 2", child.outerHtml());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildNodeByInvalidIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child1 = new TextNode("Child 1");
        Node child2 = new Element(baseUri, "p", attributes);
        child2.appendText("Child 2");
        parent.appendChild(child1);
        parent.appendChild(child2);
        Node child = parent.childNode(2);
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child1 = new TextNode("Child 1");
        Node child2 = new Element(baseUri, "p", attributes);
        child2.appendText("Child 2");
        parent.appendChild(child1);
        parent.appendChild(child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        Node firstChild = children.get(0);
        assertEquals("Child 1", firstChild.outerHtml());
        assertTrue(firstChild instanceof TextNode);
        Node secondChild = children.get(1);
        assertEquals("p", secondChild.nodeName());
        assertEquals("Child 2", secondChild.outerHtml());
        assertTrue(secondChild instanceof Element);
    }

    @Test
    public void testGetChildrenAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child1 = new TextNode("Child 1");
        Node child2 = new Element(baseUri, "p", attributes);
        child2.appendText("Child 2");
        parent.appendChild(child1);
        parent.appendChild(child2);
        Node[] childrenArray = parent.childNodesAsArray();
        assertEquals(2, childrenArray.length);
        Node firstChild = childrenArray[0];
        assertEquals("Child 1", firstChild.outerHtml());
        assertTrue(firstChild instanceof TextNode);
        Node secondChild = childrenArray[1];
        assertEquals("p", secondChild.nodeName());
        assertEquals("Child 2", secondChild.outerHtml());
        assertTrue(secondChild instanceof Element);
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child = new TextNode("Child 1");
        child.setParentNode(parent);
        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testGetParentNodeNull() {
        Node child = new TextNode("Child 1");
        assertNull(child.parentNode());
    }

    @Test
    public void testGetDocumentOwner() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child = new TextNode("Child 1");
        child.setParentNode(parent);
        Document document = new Document(baseUri);
        assertEquals(document, child.ownerDocument());
    }

    @Test
    public void testRemoveNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child1 = new TextNode("Child 1");
        Node child2 = new Element(baseUri, "p", attributes);
        child2.appendText("Child 2");
        parent.appendChild(child1);
        parent.appendChild(child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        parent.removeChild(child1);
        children = parent.childNodes();
        assertEquals(1, children.size());
        Node remainingChild = children.get(0);
        assertEquals("p", remainingChild.nodeName());
        assertEquals("Child 2", remainingChild.outerHtml());
        assertTrue(remainingChild instanceof Element);
    }

    @Test
    public void testReplaceChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node oldChild = new TextNode("Old Child");
        Node newChild = new Element(baseUri, "p", attributes);
        newChild.appendText("New Child");
        parent.appendChild(oldChild);
        parent.replaceChild(newChild, oldChild);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        Node replacedChild = children.get(0);
        assertEquals("p", replacedChild.nodeName());
        assertEquals("New Child", replacedChild.outerHtml());
        assertTrue(replacedChild instanceof Element);
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child = new TextNode("Child 1");
        child.setParentNode(parent);
        parent.setParentNode(null);
        assertEquals(null, child.parentNode());
    }

    @Test
    public void testSetParentNodeNull() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child = new TextNode("Child 1");
        child.setParentNode(parent);
        child.setParentNode(null);
        assertEquals(null, child.parentNode());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node olderSibling = new TextNode("Older Sibling");
        Node child = new TextNode("Child 1");
        Node youngerSibling = new TextNode("Younger Sibling");
        parent.appendChild(olderSibling);
        parent.appendChild(child);
        parent.appendChild(youngerSibling);
        child.setSiblingIndex(1);
        assertEquals(1, child.siblingIndex());
        Node olderSiblingNode = parent.childNodes().get(0);
        Node childNode = parent.childNodes().get(1);
        Node youngerSiblingNode = parent.childNodes().get(2);
        assertEquals("Older Sibling", olderSiblingNode.outerHtml());
        assertEquals("Child 1", childNode.outerHtml());
        assertEquals("Younger Sibling", youngerSiblingNode.outerHtml());
        assertTrue(olderSiblingNode instanceof TextNode);
        assertTrue(childNode instanceof TextNode);
        assertTrue(youngerSiblingNode instanceof TextNode);
    }

    @Test
    public void testToString() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element(baseUri, "div", attributes);
        assertEquals(node.outerHtml(), node.toString());
    }

    @Test
    public void testCloneNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element(baseUri, "div", attributes);
        Node child = new Element(baseUri, "p", attributes).setText("Child 1");
        parent.appendChild(child);
        Node clonedChild = (Node) child.clone();
        Node clonedParent = (Node) parent.clone();
        List<Node> clonedChildren = clonedParent.childNodes();
        assertEquals(1, clonedChildren.size());
        Node clonedChildNode = clonedChildren.get(0);
        assertEquals("p", clonedChildNode.nodeName());
        assertEquals("Child 1", clonedChildNode.outerHtml());
        assertEquals("Child 1", clonedChild.outerHtml());
        assertTrue(clonedChild instanceof Element);
        assertEquals(clonedParent, clonedChild.parentNode());
        assertEquals(parent, child.parentNode());
        assertNotSame(clonedParent, parent);
        assertNotSame(clonedChild, child);
    }
}