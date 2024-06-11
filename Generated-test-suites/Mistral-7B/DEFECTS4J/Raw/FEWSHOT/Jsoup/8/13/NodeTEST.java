package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testNewNode() {
        Node node = new Node("", new Attributes());
        assertEquals("", node.nodeName());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertSame(node.baseUri(), "");
        assertEquals(-1, node.siblingIndex());
    }

    @Test
    public void testNewNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Node(baseUri, attributes);
        assertEquals(baseUri, node.baseUri());
        assertEquals("id", node.attr("id"));
        assertSame(node.parentNode, null);
        assertTrue(node.childNodes().isEmpty());
        assertSame(node.attributes(), attributes);
    }

    @Test
    public void testNewNodeWithEmptyAttributes() {
        Node node = new Node("");
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNewNodeWithNullAttributes() {
        Node node = new Node("", null);
        assertNull(node.attributes());
    }

    @Test
    public void testNewNodeWithNullBaseUri() {
        Node node = new Node(null, new Attributes());
        assertNull(node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNewNodeWithNullAttributesAndNullBaseUri() {
        new Node(null, null);
    }

    @Test
    public void testNodeName() {
        Node node = new Elements.TextNode("Test");
        assertEquals("text", node.nodeName());

        Node element = new Element("div", new Attributes());
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testAttr() {
        Node node = new Element("div", new Attributes().put("class", "test"));
        assertEquals("test", node.attr("class"));
        assertEquals("", node.attr("unknown"));
    }

    @Test
    public void testHasAttr() {
        Node node = new Element("div", new Attributes().put("class", "test"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("unknown"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("div", new Attributes().put("class", "test"));
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a", new Attributes().put("href", "test.html"));
        assertEquals("test.html", node.absUrl("href"));
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        assertEquals("http://example.com/test.html", node.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Node parent = new Element("ul", new Attributes());
        Node child1 = new Element("li", new Attributes().put("class", "child1"));
        Node child2 = new Element("li", new Attributes().put("class", "child2"));

        parent.addChildren(child1, child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));

        List<Node> siblings = Arrays.asList(child1, child2);
        assertEquals(siblings, parent.siblingNodes());
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Element("ul", new Attributes());
        Node child1 = new Element("li", new Attributes().put("class", "child1"));
        Node child2 = new Element("li", new Attributes().put("class", "child2"));

        parent.addChildren(child1, child2);
        parent.removeChild(child1);

        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertSame(child2, children.get(0));

        List<Node> siblings = Arrays.asList(child2);
        assertEquals(siblings, parent.siblingNodes());
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Element("ul", new Attributes());
        Node child1 = new Element("li", new Attributes().put("class", "child1"));
        Node child2 = new Element("li", new Attributes().put("class", "child2"));

        parent.addChildren(child1);
        Node replacedChild = new Element("li", new Attributes().put("class", "replaced"));
        parent.replaceChild(child1, replacedChild);

        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertSame(replacedChild, children.get(0));

        List<Node> siblings = Arrays.asList(replacedChild);
        assertEquals(siblings, parent.siblingNodes());
    }

    @Test
    public void testSetParentNode() {
        Node parent = new Element("ul", new Attributes());
        Node child = new Element("li", new Attributes());

        parent.addChildren(child);
        child.setParentNode(parent);

        assertEquals(parent, child.parentNode);
    }

    @Test
    public void testReplaceChildWithNull() {
        Node parent = new Element("ul", new Attributes());
        Node child = new Element("li", new Attributes());

        parent.addChildren(child);

        parent.replaceChild(child, null);

        List<Node> children = parent.childNodes();
        assertTrue(children.isEmpty());
        assertNull(child.parentNode);
    }

    @Test
    public void testRemoveWithNullParent() {
        Node child = new Element("li", new Attributes());

        child.remove();

        assertNull(child.parentNode);
    }

    @Test
    public void testRemoveWithEmptyChildrenList() {
        Node parent = new Element("ul", new Attributes());
        Node child = new Element("li", new Attributes());

        parent.addChildren(child);
        parent.removeChild(child);

        List<Node> children = parent.childNodes();
        assertTrue(children.isEmpty());
    }
}