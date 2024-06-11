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
    public void testBasicConstructor() {
        String baseUri = "http://example.com/";
        Node node = new Node(baseUri);
        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode);
        assertEquals(Collections.emptyList(), node.childNodes());
    }

    @Test
    public void testAttributesConstructor() {
        String baseUri = "http://example.com/";
        String attrKey = "test";
        String attrValue = "value";
        Attributes attributes = new Attributes();
        attributes.put(attrKey, attrValue);

        Node node = new Node(baseUri, attributes);
        assertEquals(attrKey, node.attr(attrKey));
        assertEquals(attrValue, node.attr(attrKey));
        assertEquals(attrValue, node.attr("test"));
        assertEquals(Collections.singletonMap(attrKey, attrValue), node.attributes());
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com/";
        String attrKey = "test";
        Attributes attributes = new Attributes();
        attributes.put(attrKey, "value");

        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr(attrKey));
        assertFalse(node.hasAttr("other"));
    }

    @Test
    public void testSetAttr() {
        String baseUri = "http://example.com/";
        String attrKey = "test";
        String attrValue = "value";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr(attrKey, attrValue);
        assertEquals(attrValue, node.attr(attrKey));
        assertEquals(attrValue, node.attr("test"));
        assertEquals(Collections.singletonMap(attrKey, attrValue), node.attributes());
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com/";
        String attrKey = "test";
        Attributes attributes = new Attributes();
        attributes.put(attrKey, "value");
        Node node = new Node(baseUri, attributes);
        node.removeAttr(attrKey);
        assertNull(node.attr(attrKey));
        assertNull(node.attr("test"));
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/";
        String relativeUrl = "relative.html";
        Node node = new Node(baseUri, new Attributes());
        node.attr("abs:href", relativeUrl);
        String expected = "http://example.com/relative.html";
        assertEquals(expected, node.absUrl("href"));

        try {
            new URL(node.absUrl("nonexistent"));
            fail();
        } catch (MalformedURLException e) {
            // expected
        }

        try {
            new URL(baseUri + "/" + relativeUrl);
            assertEquals(expected, node.absUrl("href"));
        } catch (MalformedURLException e) {
            fail();
        }
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode = new Element("div", new Attributes());
        parentNode.addChildren(childNode);
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testChildren() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Element("div", new Attributes());
        Node childNode2 = new Element("p", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testParent() {
        String baseUri1 = "http://example1.com/";
        String baseUri2 = "http://example2.com/";
        Node childNode = new Element("div", new Attributes());
        Node parentNode1 = new Element("body", new Attributes());
        Node parentNode2 = new Element("html", new Attributes());
        parentNode1.setBaseUri(baseUri1);
        parentNode2.setBaseUri(baseUri2);
        childNode.setParentNode(parentNode1);
        assertEquals(parentNode1, childNode.parent());
        assertEquals(parentNode1, childNode.ownerDocument().getElementsByTag("body").get(0).parent());
        assertEquals(parentNode2, parentNode1.parent());
        assertEquals(parentNode2, parentNode1.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode = new Element("div", new Attributes());
        parentNode.addChildren(childNode);
        parentNode.removeChild(childNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplace() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node oldChildNode = new Element("div", new Attributes());
        Node newChildNode = new Element("p", new Attributes());
        parentNode.addChildren(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri1 = "http://example1.com/";
        String baseUri2 = "http://example2.com/";
        Node childNode = new Element("div", new Attributes());
        Node parentNode1 = new Element("body", new Attributes());
        Node parentNode2 = new Element("html", new Attributes());
        parentNode1.setBaseUri(baseUri1);
        parentNode2.setBaseUri(baseUri2);
        childNode.setParentNode(parentNode1);
        parentNode1.addChildren(childNode);
        assertEquals(parentNode1, childNode.parent());
        childNode.setParentNode(parentNode2);
        assertEquals(parentNode2, childNode.parent());
        assertEquals(parentNode2, childNode.ownerDocument());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node oldChildNode = new Element("div", new Attributes());
        Node newChildNode = new Element("p", new Attributes());
        parentNode.addChildren(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
        assertEquals(oldChildNode, oldChildNode.parent());
        assertEquals(parentNode, newChildNode.parent());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode = new Element("div", new Attributes());
        parentNode.addChildren(childNode);
        parentNode.removeChild(childNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parent());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Element("div", new Attributes());
        Node childNode2 = new Element("p", new Attributes());
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testAddChildrenArray() {
        String baseUri = "http://example.com/";
        Node parentNode = new Node(baseUri, new Attributes());
        Node childNode1 = new Element("div", new Attributes());
        Node childNode2 = new Element("p", new Attributes());
        parentNode.addChildren(new Node[] {childNode1, childNode2});
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com/";
        Node node = new TextNode("Hello, world!");
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        assertEquals("<textarea>\nHello, world!\n</textarea>", accum.toString());

        node = new Element("div", new Attributes());
        accum.setLength(0);
        node.outerHtml(accum);
        assertEquals("<div></div>", accum.toString());

        node.attr("class", "test");
        node.outerHtml(accum);
        assertEquals("<div class=\"test\"></div>", accum.toString());

        node.outerHtmlHead(accum, 0, null);
        assertEquals("", accum.toString());

        node.outerHtmlTail(accum, 0, null);
        assertEquals("</div>", accum.toString());
    }
}