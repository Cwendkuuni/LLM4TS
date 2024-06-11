package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    Node node;
    Attributes attributes;

    @Before
    public void setUp() {
        node = new Element("div");
        attributes = new Attributes();
        attributes.put("id", "test");
        node.setAttributes(attributes);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("test", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttribute"));
    }

    @Test
    public void testAttributes() {
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testBaseUri() {
        node.setBaseUri("http://test.com");
        assertEquals("http://test.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        node.setAttribute("href", "http://test.com/test.html");
        assertEquals("http://test.com/test.html", node.absUrl("href"));
        node.setBaseUri("http://other.com");
        assertEquals("http://other.com/test.html", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node child = new TextNode("Child");
        node.appendChild(child);
        assertEquals(child, node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Node child1 = new TextNode("Child 1");
        Node child2 = new TextNode("Child 2");
        node.appendChild(child1);
        node.appendChild(child2);
        List<Node> childNodes = node.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
    }

    @Test
    public void testParent() {
        Document document = new Document();
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());
        assertEquals(node, document.getElementsByTag("div").get(0));
    }

    @Test
    public void testRemove() {
        Document document = new Document();
        document.appendChild(node);
        node.remove();
        assertEquals(0, document.getElementsByTag("div").size());
    }

    @Test
    public void testReplaceWith() {
        Document document = new Document();
        Node oldNode = new Element("div");
        Node newNode = new Element("p");
        document.appendChild(oldNode);
        oldNode.replaceWith(newNode);
        assertEquals(newNode, document.getElementsByTag("div").get(0));
        assertEquals(newNode, document.getElementsByTag("p").get(0));
    }

    @Test
    public void testSetParentNode() {
        Document document1 = new Document();
        Document document2 = new Document();
        Node node1 = new Element("div");
        Node node2 = new Element("p");
        document1.appendChild(node1);
        document2.appendChild(node2);
        node1.setParentNode(document2);
        assertEquals(document2, node1.ownerDocument());
        assertEquals(node2, node1.parent());
    }

    @Test
    public void testReplaceChild() {
        Document document = new Document();
        Node oldNode = new Element("div");
        Node newNode = new Element("p");
        document.appendChild(oldNode);
        oldNode.replaceChild(newNode, oldNode.childNode(0));
        assertEquals(newNode, oldNode.childNode(0));
    }

    @Test
    public void testRemoveChild() {
        Document document = new Document();
        Node parent = new Element("div");
        document.appendChild(parent);
        Node child = new Element("p");
        parent.appendChild(child);
        parent.removeChild(child);
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testAddChildren() {
        Document document = new Document();
        Node parent = new Element("div");
        document.appendChild(parent);
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Document document = new Document();
        Node parent = new Element("div");
        document.appendChild(parent);
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(1, child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document();
        Node node = new Element("div");
        document.appendChild(node);
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        assertEquals("<div></div>", accum.toString());
    }

    @Test
    public void testToString() {
        Document document = new Document();
        Node node = new Element("div");
        document.appendChild(node);
        assertEquals("<div></div>", node.toString());
    }

    @Test
    public void testEquals() {
        Node node1 = new Element("div");
        Node node2 = new Element("div");
        Node node3 = node1;
        assertEquals(node3, node3);
        assertEquals(node1, node1);
        assertFalse(node1.equals(null));
        assertFalse(node1.equals(""));
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Node node1 = new Element("div");
        Node node2 = new Element("div");
        Node node3 = node1;
        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();
        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() {
        Node node = new Element("div");
        Node clonedNode = node.clone();
        assertEquals(node, clonedNode);
        assertEquals(node.attributes(), clonedNode.attributes());
        assertFalse(node.equals(clonedNode));
        assertFalse(node.hashCode() == clonedNode.hashCode());
        Node child = new Element("p");
        node.appendChild(child);
        Node clonedChild = clonedNode.childNodes().get(0);
        assertFalse(node.equals(clonedChild));
        assertFalse(node.hashCode() == clonedChild.hashCode());
        assertEquals(child.toString(), clonedChild.toString());
    }
}