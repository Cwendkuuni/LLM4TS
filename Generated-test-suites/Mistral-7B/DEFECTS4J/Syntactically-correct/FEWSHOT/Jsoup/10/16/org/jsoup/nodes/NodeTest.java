package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNewNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
    }

    @Test
    public void testNewNodeWithoutAttributes() {
        String baseUri = "https://example.com";
        Node node = new Element("div", baseUri);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
    }

    @Test
    public void testNewNodeWithoutBaseUri() {
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");
        Node node = new Element("div", attributes);

        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.childNodes());
        assertNull(node.parentNode());
    }

    @Test
    public void testNewNodeWithChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node child1 = new TextNode("child1");
        Node child2 = new TextNode("child2");

        Node parent = new Element("div", baseUri, attributes);
        parent.addChildren(child1, child2);

        assertEquals("div", parent.nodeName());
        assertEquals(baseUri, parent.baseUri());
        assertEquals(attributes, parent.attributes());
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
        assertNull(parent.parentNode());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new TextNode("child1");
        Node child2 = new TextNode("child2");
        parent.addChildren(child1, child2);

        parent.removeChild(child1);

        assertEquals(1, parent.childNodes().size());
        assertSame(child2, parent.childNodes().get(0));
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new TextNode("child1");
        Node child2 = new TextNode("child2");
        parent.addChildren(child1, child2);

        Node newChild = new TextNode("newChild");
        parent.replaceChild(child1, newChild);

        assertEquals(1, parent.childNodes().size());
        assertSame(newChild, parent.childNodes().get(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri1 = "https://example1.com";
        String baseUri2 = "https://example2.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node child = new Element("div", baseUri1, attributes);
        Node parent = new Element("body", baseUri2, attributes);

        child.setParentNode(parent);

        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testGetChildrenAsArray() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new TextNode("child1");
        Node child2 = new TextNode("child2");
        parent.addChildren(child1, child2);

        Node[] children = parent.childNodesAsArray();

        assertEquals(2, children.length);
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testGetBaseUri() {
        String baseUri = "https://example.com";
        String expectedBaseUri = "https://example.com/child";

        Node node = new Element("div", baseUri);
        node.setBaseUri(expectedBaseUri);

        assertEquals(expectedBaseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "https://example.com";
        Node node = new Element("div", baseUri);

        String newBaseUri = "https://example.net";
        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "https://example.com";
        Node node = new Element("a", baseUri);
        node.attr("href", "/child.html");

        String expectedAbsUrl = "https://example.com/child.html";

        assertEquals(expectedAbsUrl, node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        String baseUri = "https://example.com";
        Node node = new Element("a", baseUri);
        node.attr("href", "https://example.net/child.html");

        String expectedAbsUrl = "https://example.net/child.html";

        assertEquals(expectedAbsUrl, node.absUrl("href"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");
        attributes.put("class", "bar");

        Node node = new Element("div", baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node node = new Element("div", baseUri, attributes);
        node.attr("class", "bar");

        assertEquals("bar", node.attr("class"));
    }

    @Test
    public void testAttrWithAbsoluteUrl() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node node = new Element("a", baseUri, attributes);
        node.attr("href", "/child.html");

        String expectedAbsUrl = "https://example.com/child.html";

        assertEquals(expectedAbsUrl, node.absUrl("href"));
        assertEquals(expectedAbsUrl, node.attr("abs:href"));
    }

    @Test
    public void testGetAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");
        attributes.put("class", "bar");

        Node node = new Element("div", baseUri, attributes);

        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testSetAttributes() {
        String baseUri = "https://example.com";
        Node node = new Element("div", baseUri);

        Attributes attributes = new Attributes();
        attributes.put("id", "foo");
        attributes.put("class", "bar");

        node.attributes(attributes);

        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");
        attributes.put("class", "bar");

        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("class");

        assertEquals(1, node.attributes().size());
        assertFalse(node.hasAttr("class"));
        assertTrue(node.hasAttr("id"));
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parent = new Element("div", baseUri, attributes);
        Node child = new TextNode("child");
        parent.addChildren(child);

        Node childFromParent = parent.childNode(0);

        assertEquals(child, childFromParent);
    }

    @Test
    public void testGetChildNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new TextNode("child1");
        Node child2 = new TextNode("child2");
        parent.addChildren(child1, child2);

        List<Node> childNodes = parent.childNodes();

        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
    }

    @Test
    public void testGetParentNode() {
        String baseUri1 = "https://example1.com";
        String baseUri2 = "https://example2.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node child = new Element("div", baseUri1, attributes);
        Node parent = new Element("body", baseUri2, attributes);
        child.setParentNode(parent);

        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testRemoveNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new TextNode("child1");
        Node child2 = new TextNode("child2");
        parent.addChildren(child1, child2);

        parent.remove();

        assertEquals(0, child1.childNodes().size());
        assertEquals(0, child2.childNodes().size());
        assertNull(child1.parentNode());
        assertNull(child2.parentNode());
    }

    @Test
    public void testReplaceNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "foo");

        Node parent = new Element("div", baseUri, attributes);
        Node oldChild = new TextNode("oldChild");
        Node newChild = new TextNode("newChild");
        parent.addChildren(oldChild);
        parent.replaceWith(newChild);

        assertEquals(0, oldChild.childNodes().size());
        assertEquals(0, oldChild.parentNode().childNodes().size());
        assertEquals(1, newChild.childNodes().size());
        assertEquals(1, newChild.parentNode().childNodes().size());
    }
}