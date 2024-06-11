package org.jsoup.nodes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAttr() {
        Node node = new Element("test");
        node.attr("key", "value");

        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testAttributes() {
        Node node = new Element("test");
        node.attr("key1", "value1");
        node.attr("key2", "value2");

        Attributes attributes = node.attributes();

        assertNotNull(attributes);
        assertEquals(2, attributes.size());
    }

    @Test
    public void testHasAttr() {
        Node node = new Element("test");
        node.attr("key", "value");

        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("notExist"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("test");
        node.attr("key1", "value1");
        node.attr("key2", "value2");

        node.removeAttr("key1");

        assertFalse(node.hasAttr("key1"));
        assertTrue(node.hasAttr("key2"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("test");
        node.setBaseUri("http://example.com");

        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() throws MalformedURLException {
        Node node = new Element("a");
        node.attr("href", "/about");

        node.setBaseUri("http://example.com");

        assertEquals("http://example.com/about", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node parentNode = new Element("parent");
        Node childNode = new Element("child");
        parentNode.addChildren(childNode);

        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Node parentNode = new Element("parent");
        List<Node> children = new ArrayList<>();
        children.add(new Element("child1"));
        children.add(new Element("child2"));
        children.add(new Element("child3"));

        parentNode.addChildren(children.toArray(new Node[0]));

        assertEquals(children, parentNode.childNodes());
    }

    @Test
    public void testParent() {
        Node parentNode = new Element("parent");
        Node childNode = new Element("child");
        parentNode.addChildren(childNode);

        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testSiblingNodes() {
        Node parentNode = new Element("parent");
        List<Node> children = new ArrayList<>();
        children.add(new Element("child1"));
        children.add(new Element("child2"));
        children.add(new Element("child3"));

        parentNode.addChildren(children.toArray(new Node[0]));

        assertEquals(children, children.get(0).siblingNodes());
    }

    @Test
    public void testNextSibling() {
        Node parentNode = new Element("parent");
        List<Node> children = new ArrayList<>();
        children.add(new Element("child1"));
        children.add(new Element("child2"));
        children.add(new Element("child3"));

        parentNode.addChildren(children.toArray(new Node[0]));

        assertEquals(children.get(1), children.get(0).nextSibling());
        assertNull(children.get(2).nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parentNode = new Element("parent");
        List<Node> children = new ArrayList<>();
        children.add(new Element("child1"));
        children.add(new Element("child2"));
        children.add(new Element("child3"));

        parentNode.addChildren(children.toArray(new Node[0]));

        assertEquals(children.get(0), children.get(1).previousSibling());
        assertNull(children.get(0).previousSibling());
    }

    @Test
    public void testOuterHtml() {
        Node node = new Element("a");
        node.attr("href", "http://example.com");
        node.attr("target", "_blank");
        node.attr("class", "link");

        String expectedOutput = "<a href=\"http://example.com\" target=\"_blank\" class=\"link\"></a>";
        assertEquals(expectedOutput, node.outerHtml());
    }
}