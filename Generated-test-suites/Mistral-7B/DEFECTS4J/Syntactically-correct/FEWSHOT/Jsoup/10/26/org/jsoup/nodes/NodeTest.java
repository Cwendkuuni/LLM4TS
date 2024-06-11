package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertEquals("test", node.attr("id"));
        assertEquals("", node.attr("nonExistent"));
        assertFalse(node.hasAttr("nonExistent"));
        assertEquals("", node.absUrl("nonExistent"));
        assertNull(node.parent());
        assertEmpty(node.childNodes());
    }

    @Test
    public void testNodeCreationWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertFalse(node.hasAttr("nonExistent"));
        assertEquals("", node.attr("nonExistent"));
        assertEquals("", node.absUrl("nonExistent"));
        assertNull(node.parent());
        assertEmpty(node.childNodes());
    }

    @Test
    public void testNodeCreationWithNullBaseUri() {
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        try {
            Node node = new Element("div", null, attributes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNodeCreationWithNullAttributes() {
        String baseUri = "http://example.com";
        try {
            Node node = new Node(baseUri, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/path";
        Node node = new Element("a", baseUri, new Attributes().put("href", "/relative/path"));
        assertEquals("http://example.com/relative/path", node.absUrl("href"));
        node.setBaseUri("http://another-example.com");
        assertEquals("http://another-example.com/relative/path", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistent"));
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("test", node.attr("id"));
        assertEquals("test", node.attr("class"));
        assertEquals("", node.attr("nonExistent"));
        assertFalse(node.hasAttr("nonExistent"));
    }

    @Test
    public void testAttrWithAbsoluteUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("abs:href", "http://another-example.com");
        Node node = new Element("a", baseUri, attributes);
        assertEquals("http://another-example.com", node.absUrl("href"));
        assertEquals("http://another-example.com", node.attr("abs:href"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistent"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
        assertFalse(node.hasAttr("id"));
        assertEquals("test", node.attr("id"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, new Attributes()).text("Child 1");
        Node childNode2 = new Element("p", baseUri, new Attributes()).text("Child 2");
        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));
    }

    @Test
    public void testEmptyChildNodes() {
        Node parentNode = new Element("div");
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testGetParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node childNode = new Element("p", baseUri, attributes).text("Child");
        Node parentNode = new Element("div", baseUri, attributes);
        parentNode.addChild(childNode);
        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        Document doc = new Document(baseUri);
        doc.setRootElement(node);
        assertEquals(doc, node.ownerDocument());
        assertNull(new Element("p", baseUri).ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, new Attributes()).text("Child");
        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());
        assertNull(childNode.parent());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, new Attributes()).text("Old Child");
        Node newChild = new Element("p", baseUri, new Attributes()).text("New Child");
        parentNode.addChild(oldChild);
        parentNode.replaceChild(oldChild, newChild);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(newChild, childNodes.get(0));
        assertEquals(newChild, parentNode.childNode(0));
        assertEquals(newChild, parentNode.getElementsByTag("p").get(0));
        assertEquals(oldChild, parentNode.removeChild(newChild));
        assertNull(newChild.parent());
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node childNode1 = new Element("p", baseUri, attributes).text("Child 1");
        Node childNode2 = new Element("p", baseUri, attributes).text("Child 2");
        Node parentNode = new Element("div", baseUri, attributes);
        childNode1.setParentNode(parentNode);
        childNode2.setParentNode(parentNode);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
        assertEquals(parentNode, childNode1.parent());
        assertEquals(parentNode, childNode2.parent());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node node1 = new Element("p", baseUri, attributes).text("Node 1");
        Node node2 = new Element("p", baseUri, attributes).text("Node 2");
        Node node3 = new Element("p", baseUri, attributes).text("Node 3");
        Node parentNode = new Element("div", baseUri, attributes);
        parentNode.addChildren(node1, node2, node3);
        node1.setSiblingIndex(1);
        node2.setSiblingIndex(2);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(3, childNodes.size());
        assertEquals(node2, childNodes.get(0));
        assertEquals(node1, childNodes.get(1));
        assertEquals(node3, childNodes.get(2));
        assertEquals(1, node1.siblingIndex());
        assertEquals(2, node2.siblingIndex());
        assertEquals(3, node3.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        StringBuilder accum = new StringBuilder(32 * 1024);
        node.outerHtml(accum);
        String expected = "<div></div>";
        assertEquals(expected, accum.toString());
        attributes.put("class", "test");
        node = new Element("div", baseUri, attributes);
        node.outerHtmlHead(accum, 0, null);
        node.outerHtmlTail(accum, 0, null);
        expected = "<div></div>";
        assertEquals(expected, accum.toString());
        node.outerHtmlHead(accum, 1, null);
        node.outerHtmlTail(accum, 1, null);
        expected = "<div></div>";
        assertEquals(expected, accum.toString());
        TextNode textNode = new TextNode("Text");
        node.appendChild(textNode);
        node.outerHtmlHead(accum, 1, null);
        node.appendChild(new Element("p", baseUri, new Attributes()).text("Paragraph"));
        node.outerHtmlTail(accum, 1, null);
        expected = "<div><p></p><text></text></div>";
        assertEquals(expected, accum.toString());
    }
}