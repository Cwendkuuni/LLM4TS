package org.jsoup.nodes;

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

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeVisitor;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test class for Node.
 */
public class NodeTest {

    @Test
    public void testConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertEquals("test", node.attr("id"));
        assertEquals(baseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBaseUri() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullAttributes() {
        new Node("http://example.com", null);
    }

    @Test
    public void testDefaultConstructor() {
        Node node = new Node();
        assertNull(node.baseUri());
        assertNull(node.childNodes());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeName() {
        // Abstract method, can't test directly.
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertEquals("test", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttribute"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertEquals(attributes, node.attributes());
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertEquals("test", node.attr("id"));
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node();
        node.setBaseUri(baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "/test.html");
        Node node = new Node(baseUri, attributes);

        assertEquals("http://example.com/test.html", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistentAttribute"));
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrlInAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "invalid:url");
        Node node = new Node(baseUri, attributes);

        node.absUrl("href");
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);

        parentNode.childNodes.add(childNode);
        assertSame(parentNode, childNode.parent());
        assertEquals(childNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(childNode1);
        parentNode.childNodes.add(childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    @Test
    public void testChildNodesAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(childNode1);
        parentNode.childNodes.add(childNode2);

        Node[] children = parentNode.childNodesAsArray();

        assertEquals(2, children.length);
        assertTrue(Arrays.asList(children).contains(childNode1));
        assertTrue(Arrays.asList(children).contains(childNode2));
    }

    @Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);

        childNode.setParentNode(parentNode);
        assertSame(parentNode, childNode.parent());
        assertNull(parentNode.childNodes().get(0).parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);

        childNode.setParentNode(parentNode);
        Document doc = new Document();
        doc.setBody(parentNode);
        assertEquals(doc, childNode.ownerDocument());
        assertEquals(doc.getBody(), childNode.parent());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);

        parentNode.childNodes.add(childNode);
        parentNode.remove();
        assertTrue(parentNode.childNodes().isEmpty());
        assertNull(childNode.parent());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes1);
        Node childNode2 = new Node(baseUri, attributes2);

        parentNode.childNodes.add(childNode1);
        parentNode.replaceChild(childNode1, childNode2);
        assertEquals(childNode2, parentNode.childNodes().get(0));
        assertSame(parentNode, childNode2.parent());
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode1 = new Node(baseUri);
        Node parentNode2 = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);

        childNode.setParentNode(parentNode1);
        parentNode1.removeChild(childNode);
        childNode.setParentNode(parentNode2);
        parentNode2.addChildren(childNode);

        assertEquals(childNode, parentNode2.childNodes().get(0));
        assertSame(parentNode2, childNode.parent());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes1);
        Node childNode2 = new Node(baseUri, attributes2);

        parentNode.childNodes.add(childNode1);
        parentNode.replaceChild(childNode1, childNode2);
        assertEquals(childNode2, parentNode.childNodes().get(0));
        assertSame(parentNode, childNode2.parent());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);

        parentNode.childNodes.add(childNode);
        parentNode.removeChild(childNode);
        assertTrue(parentNode.childNodes().isEmpty());
        assertNull(childNode.parent());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().contains(childNode1));
        assertTrue(parentNode.childNodes().contains(childNode2));
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(1, childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().get(0).equals(childNode2));
        assertTrue(parentNode.childNodes().get(1).equals(childNode1));
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        String html = "<div id='test'><p>Test</p></div>";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new TextNode("Test", baseUri, attributes);
        Node divNode = new Node("div", baseUri, attributes);
        divNode.outerHtmlHead(new StringBuilder(), 0, null);
        divNode.outerHtmlTail(new StringBuilder(), 0, null);
        divNode.addChildren(node);

        StringBuilder accum = new StringBuilder(html.length());
        divNode.outerHtml(accum);

        assertEquals(html, accum.toString());
    }

    @Test
    public void testToString() {
        String baseUri = "http://example.com";
        String html = "<div id='test'></div>";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node("div", baseUri, attributes);

        assertEquals(html, node.toString());
    }

    @Test
    public void testEquals() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node node1 = new Node("div", baseUri, attributes1);
        Node node2 = new Node("div", baseUri, attributes2);

        assertFalse(node1.equals(node2));

        attributes1.putAll(attributes2);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node node1 = new Node("div", baseUri, attributes1);
        Node node2 = new Node("div", baseUri, attributes2);

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();

        assertFalse(hashCode1 == hashCode2);

        attributes1.putAll(attributes2);
        assertTrue(node1.hashCode() == node2.hashCode());
    }
}