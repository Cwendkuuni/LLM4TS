package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test class for Node.java
 */
public class NodeTest {

    /**
     * Test constructor with baseUri and attributes.
     */
    @Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals("testNode", node.attr("id"));
        assertTrue(node.hasAttr("id"));
    }

    /**
     * Test constructor with baseUri only.
     */
    @Test
    public void testNodeWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    /**
     * Test constructor with no arguments.
     */
    @Test
    public void testNodeWithNoArguments() {
        Node node = new Node();

        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertNull(node.baseUri());
    }

    /**
     * Test getNodeName.
     */
    @Test
    public void testgetNodeName() {
        Element element = new Element("div");
        assertEquals("div", element.nodeName());

        TextNode textNode = new TextNode("Test text");
        assertEquals("#text", textNode.nodeName());
    }

    /**
     * Test getAttribute.
     */
    @Test
    public void testgetAttribute() {
        Element element = new Element("a").attr("href", "http://example.com/test");
        String attribute = element.attr("href");
        assertEquals("http://example.com/test", attribute);

        String attributeEmpty = element.attr("nonExistentAttribute");
        assertEquals("", attributeEmpty);
    }

    /**
     * Test getAttributes.
     */
    @Test
    public void testgetAttributes() {
        Element element = new Element("a").attr("href", "http://example.com/test").attr("id", "test123");
        Attributes attributes = element.attributes();

        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("href"));
        assertTrue(attributes.hasKey("id"));
        assertEquals("http://example.com/test", attributes.get("href"));
        assertEquals("test123", attributes.get("id"));
    }

    /**
     * Test setting an attribute.
     */
    @Test
    public void testsetAttribute() {
        Element element = new Element("a");
        element.attr("href", "http://example.com/test");

        assertEquals("http://example.com/test", element.attr("href"));

        element.attr("id", "test123");
        assertEquals("test123", element.attr("id"));
    }

    /**
     * Test removing an attribute.
     */
    @Test
    public void testremoveAttribute() {
        Element element = new Element("a").attr("href", "http://example.com/test").attr("id", "test123");

        assertEquals("test123", element.attr("id"));
        element.removeAttr("id");
        assertFalse(element.hasAttr("id"));
    }

    /**
     * Test getBaseUri.
     */
    @Test
    public void testgetBaseUri() {
        Element element = new Element("a").attr("href", "http://example.com/test").attr("baseUri", "http://example.org");

        assertEquals("http://example.org", element.baseUri());
    }

    /**
     * Test setting the baseUri.
     */
    @Test
    public void testsetBaseUri() {
        Element element = new Element("a").attr("href", "http://example.com/test");
        element.setBaseUri("http://example.org");

        assertEquals("http://example.org", element.baseUri());

        element.setBaseUri(null);
        assertNull(element.baseUri());
    }

    /**
     * Test getAbsoluteUrl.
     */
    @Test
    public void testgetAbsoluteUrl() {
        Element element = new Element("a").attr("href", "/images/image.png");
        Document document = new Document();
        document.setBaseUri("http://example.com");
        element.setOwnerDocument(document);

        String absoluteUrl = element.absUrl("href");
        assertEquals("http://example.com/images/image.png", absoluteUrl);
    }

    /**
     * Test getChildNode.
     */
    @Test
    public void testgetChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.appendChild(child);

        Element retrievedChild = parent.childNode(0);
        assertEquals(child, retrievedChild);
    }

    /**
     * Test getChildren.
     */
    @Test
    public void testgetChildren() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    /**
     * Test setParentNode.
     */
    @Test
    public void testsetParentNode() {
        Element child = new Element("p");
        Element parent = new Element("div");

        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    /**
     * Test removeChild.
     */
    @Test
    public void testremoveChild() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.appendChild(child1);
        parent.appendChild(child2);

        parent.removeChild(child1);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertFalse(children.contains(child1));
        assertTrue(children.contains(child2));

        parent.removeChild(child2);
        children = parent.childNodes();
        assertEquals(0, children.size());
    }

    /**
     * Test replaceChild.
     */
    @Test
    public void testreplaceChild() {
        Element parent = new Element("div");
        Element oldChild = new Element("p");
        Element newChild = new Element("p");
        parent.appendChild(oldChild);

        parent.replaceChild(newChild, oldChild);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertTrue(children.contains(newChild));
        assertFalse(children.contains(oldChild));
    }

    /**
     * Test addChildren.
     */
    @Test
    public void testaddChildren() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    /**
     * Test addChildren with index.
     */
    @Test
    public void testaddChildrenWithIndex() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.addChildren(1, child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.get(0).equals(child1));
        assertTrue(children.get(1).equals(child2));
    }

    /**
     * Test clone.
     */
    @Test
    public void testclone() throws CloneNotSupportedException {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.appendChild(child);

        Node clonedNode = parent.clone();
        List<Node> children = clonedNode.childNodes();
        assertEquals(1, children.size());
        assertTrue(children.get(0).equals(child.clone()));
    }
}