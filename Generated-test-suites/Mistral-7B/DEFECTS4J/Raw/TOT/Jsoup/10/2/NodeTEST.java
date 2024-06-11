package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    private Node root;
    private Node parent;
    private Node child1;
    private Node child2;
    private Node child3;

    @Before
    public void setUp() {
        root = new Document();
        parent = new Element("parent", new Attributes());
        child1 = new Element("child1", new Attributes().put("class", "child1"));
        child2 = new Element("child2", new Attributes().put("class", "child2"));
        child3 = new Element("child3", new Attributes().put("class", "child3"));

        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.appendChild(child3);
        root.appendChild(parent);
    }

    @Test
    public void testNodeName() {
        assertEquals("parent", parent.nodeName());
        assertEquals("child1", child1.nodeName());
        assertNotNull(child3.nodeName()); // null check for empty nodes
    }

    @Test
    public void testAttr() {
        parent.attr("testKey", "testValue");
        assertEquals("testValue", parent.attr("testKey"));
        assertEquals("", parent.attr("nonExistentKey"));

        parent.attr("abs:testKey", "absTestValue");
        assertEquals("absTestValue", parent.absUrl("abs:testKey"));

        parent.attr("href", "http://example.com");
        assertEquals("http://example.com", parent.absUrl("href"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = parent.attributes();
        assertEquals(3, attributes.size());

        assertTrue(attributes.hasKey("class"));
        assertEquals("child1", attributes.get("class").get(0));
        assertEquals("child2", attributes.get("class").get(1));
        assertEquals("child3", attributes.get("class").get(2));

        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(parent.hasAttr("class"));
        assertFalse(parent.hasAttr("nonExistentKey"));
    }

    @Test
    public void testRemoveAttr() {
        parent.removeAttr("class");
        assertEquals(0, parent.attributes().size());
    }

    @Test
    public void testBaseUri() {
        parent.setBaseUri("http://example.com");
        assertEquals("http://example.com", parent.baseUri());

        child1.setBaseUri("http://sub.example.com");
        assertEquals("http://sub.example.com", child1.baseUri());

        assertEquals("http://example.com/child1", child1.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node child = parent.childNode(0);
        assertEquals(child1, child);
    }

    @Test
    public void testChildNodes() {
        List<Node> children = parent.childNodes();
        assertEquals(3, children.size());

        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
    }

    @Test
    public void testChildNodesAsArray() {
        Node[] children = parent.childNodesAsArray();
        assertArrayEquals(new Node[]{child1, child2, child3}, children);
    }

    @Test
    public void testParentNode() {
        assertEquals(parent, child1.parent());
        assertNull(child1.parent()); // for root node
    }

    @Test
    public void testOwnerDocument() {
        assertEquals(root, parent.ownerDocument());
        assertNull(child1.ownerDocument()); // orphan nodes have no document
    }

    @Test
    public void testRemove() {
        List<Node> childrenBefore = new ArrayList<>(parent.childNodes());

        parent.remove();
        List<Node> childrenAfter = new ArrayList<>(root.childNodes());

        assertEquals(1, childrenBefore.size());
        assertEquals(0, childrenAfter.size());
    }

    @Test
    public void testReplaceWith() {
        Node replacement = new Element("replacement");

        parent.replaceWith(replacement);
        List<Node> children = root.childNodes();

        assertEquals(1, children.size());
        assertEquals(replacement, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        Node newParent = new Element("newParent");
        child1.setParentNode(newParent);

        List<Node> children = newParent.childNodes();
        assertEquals(1, children.size());
        assertEquals(child1, children.get(0));
    }

    @Test
    public void testReplaceChild() {
        Node newChild = new Element("newChild");
        child1.replaceChild(child2, newChild);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(newChild, children.get(1));
    }

    @Test
    public void testRemoveChild() {
        Node childToRemove = child1;
        parent.removeChild(childToRemove);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertNotEquals(childToRemove, children.get(0));
    }

    @Test
    public void testAddChildren() {
        Node newChild1 = new Element("newChild1");
        Node newChild2 = new Element("newChild2");

        parent.addChildren(newChild1, newChild2);

        List<Node> children = parent.childNodes();
        assertEquals(5, children.size());
        assertEquals(newChild1, children.get(3));
        assertEquals(newChild2, children.get(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenWithNull() {
        parent.addChildren(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenWithEmptyArray() {
        parent.addChildren(new Node[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenWithNullElement() {
        Node newChild = new Node();
        parent.addChildren(newChild);
    }

    @Test
    public void testAddChildrenWithExistingChild() {
        Node newChild = child1;
        parent.addChildren(newChild);

        List<Node> children = parent.childNodes();
        assertEquals(5, children.size());
        assertEquals(newChild, children.get(4));
    }

    @Test
    public void testSetSiblingIndex() {
        child1.setSiblingIndex(2);
        List<Node> children = parent.childNodes();

        assertEquals(child2, children.get(1));
        assertEquals(child1, children.get(2));
        assertEquals(child3, children.get(3));
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder(32 * 1024);
        parent.outerHtml(accum);

        String expected = "<parent>\n" +
                " <child1 class=\"child1\"></child1>\n" +
                " <child2 class=\"child2\"></child2>\n" +
                " <child3 class=\"child3\"></child3>\n" +
                "</parent>";

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testEquals() {
        Node node1 = new Element("node1", new Attributes());
        Node node2 = new Element("node1", new Attributes());

        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));

        Node node3 = new Element("node2", new Attributes());
        assertFalse(node1.equals(node3));
        assertFalse(node3.equals(node1));

        Node node4 = new TextNode("text");
        assertFalse(node1.equals(node4));
        assertFalse(node4.equals(node1));
    }

    @Test
    public void testHashCode() {
        Node node1 = new Element("node1", new Attributes());
        Node node2 = new Element("node1", new Attributes());
        Node node3 = new Element("node2", new Attributes());
        Node node4 = new TextNode("text");

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();
        int hashCode4 = node4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode2, hashCode1);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode3, hashCode1);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode4, hashCode1);
    }
}