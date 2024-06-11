package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";
        String attributeKey = "test";
        String attributeValue = "value";

        Node node = new Element(baseUri, new Attributes());
        node.attr(attributeKey, attributeValue);
        assertEquals(attributeValue, node.attr(attributeKey));
        assertNull(node.attr("nonExistentAttribute"));
        assertNull(node.absUrl("nonExistentAttribute"));
        assertNotNull(node.baseUri());
        assertEquals(baseUri, node.baseUri());
        assertFalse(node.hasAttr(attributeKey));
        node.removeAttr(attributeKey);
        assertFalse(node.hasAttr(attributeKey));

        Node nodeWithBaseUri = new TextNode("Test Text", baseUri);
        assertEquals(baseUri, nodeWithBaseUri.baseUri());
        assertEquals("Test Text", nodeWithBaseUri.outerHtml());

        Node nodeWithoutBaseUri = new TextNode("Test Text");
        assertNull(nodeWithoutBaseUri.baseUri());
        assertEquals("Test Text", nodeWithoutBaseUri.outerHtml());

        Node nodeWithChildren = new Element(baseUri, new Attributes());
        Node child1 = new Element(baseUri, new Attributes());
        Node child2 = new Element(baseUri, new Attributes());
        nodeWithChildren.addChildren(child1, child2);
        List<Node> children = nodeWithChildren.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child1, nodeWithChildren.childNode(0));
        assertEquals(child2, nodeWithChildren.childNode(1));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Node node = new Element(baseUri, attributes);
        assertEquals("value", node.attr("test"));
        assertEquals("value", node.attributes().get("test"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
        assertEquals("", node.attr("test"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "relative.html";
        Node node = new Element(baseUri, new Attributes());
        node.attr("href", relativeUrl);
        String absoluteUrl = node.absUrl("href");
        assertEquals("http://example.com/relative.html", absoluteUrl);

        baseUri = "http://example.com/test.html";
        relativeUrl = "../index.html";
        node = new Element(baseUri, new Attributes());
        absoluteUrl = node.absUrl(relativeUrl);
        assertEquals("http://example.com/index.html", absoluteUrl);

        baseUri = "http://example.com/test.html";
        relativeUrl = "/test2.html";
        node = new Element(baseUri, new Attributes());
        absoluteUrl = node.absUrl(relativeUrl);
        assertEquals("http://example.com/test2.html", absoluteUrl);

        baseUri = "http://example.com/test.html";
        relativeUrl = "test.html";
        node = new Element(baseUri, new Attributes());
        assertNull(node.absUrl(relativeUrl));

        baseUri = "http://example.com/test.html";
        relativeUrl = "http://example2.com/test.html";
        node = new Element(baseUri, new Attributes());
        assertNull(node.absUrl(relativeUrl));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Node parentNode = new Element(baseUri, new Attributes());
        Node child1 = new Element(baseUri, new Attributes());
        Node child2 = new Element(baseUri, new Attributes());
        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Node firstChild = parentNode.childNode(0);
        assertEquals(child1, firstChild);

        Node lastChild = parentNode.childNode(1);
        assertEquals(child2, lastChild);

        List<Node> childArray = parentNode.childNodesAsArray();
        assertEquals(2, childArray.length);
        assertEquals(child1, childArray[0]);
        assertEquals(child2, childArray[1]);
    }

    @Test
    public void testParentNode() {
        String baseUri = "http://example.com";
        Node child1 = new Element(baseUri, new Attributes());
        Node child2 = new Element(baseUri, new Attributes());
        Node parent = new Element(baseUri, new Attributes());
        parent.setParentNode(child1);
        child1.addChildren(child2);

        assertEquals(child1, child2.parent());
        assertEquals(null, child1.parent());
        assertEquals(null, parent.parent());

        child1.remove();
        assertEquals(null, child2.parent());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Node parentNode = new Element(baseUri, new Attributes());
        Node child1 = new Element(baseUri, new Attributes());
        Node child2 = new Element(baseUri, new Attributes());
        parentNode.addChildren(child1, child2);

        List<Node> childrenBeforeRemoval = parentNode.childNodes();
        assertEquals(2, childrenBeforeRemoval.size());
        assertEquals(child1, childrenBeforeRemoval.get(0));
        assertEquals(child2, childrenBeforeRemoval.get(1));

        parentNode.removeChild(child1);
        List<Node> childrenAfterRemoval = parentNode.childNodes();
        assertEquals(1, childrenAfterRemoval.size());
        assertEquals(child2, childrenAfterRemoval.get(0));

        parentNode.remove();
        assertEquals(null, child2.parent());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Node parentNode = new Element(baseUri, new Attributes());
        Node oldChild1 = new Element(baseUri, new Attributes());
        Node oldChild2 = new Element(baseUri, new Attributes());
        Node newChild = new Element(baseUri, new Attributes());

        parentNode.addChildren(oldChild1, oldChild2);
        parentNode.replaceChild(oldChild1, newChild);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(newChild, children.get(0));
        assertEquals(oldChild2, children.get(1));

        assertEquals(newChild, parentNode.childNode(0));
        assertEquals(oldChild2, parentNode.childNode(1));
    }
}