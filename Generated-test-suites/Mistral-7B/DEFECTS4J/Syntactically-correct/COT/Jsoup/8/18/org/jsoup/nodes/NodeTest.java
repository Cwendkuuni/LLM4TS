package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Node.
 */
public class NodeTest {

    @Test
    public void testNodeName() {
        Node node = new Element("div"); // abstract Node cannot be instantiated directly
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", "", attributes);

        assertEquals("test", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));

        // Test abs: prefix
        assertEquals(node.absUrl("href", "http://example.com"), "http://example.com");
        assertEquals(node.absUrl("abs:href", "http://example.com"), "http://example.com");
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", "", attributes);

        List<String> attributesList = new ArrayList<>(Arrays.asList(node.attributes().keySet().toArray(new String[0])));
        assertEquals(1, attributesList.size());
        assertEquals("class", attributesList.get(0));
    }

    @Test
    public void testSetAttr() {
        Attributes attributes = new Attributes();
        Node node = new Element("div", "", attributes);
        node.attr("class", "test");

        List<String> attributesList = new ArrayList<>(Arrays.asList(node.attributes().keySet().toArray(new String[0])));
        assertEquals(1, attributesList.size());
        assertEquals("class", attributesList.get(0));
        assertEquals("test", node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        Attributes attributes = new Attributes();
        Node node = new Element("div", "", attributes);
        node.attr("class", "test");

        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        Attributes attributes = new Attributes();
        Node node = new Element("div", "", attributes);
        node.attr("class", "test");
        node.removeAttr("class");

        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri, new Attributes());

        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        Node node = new Element("div", "", new Attributes());
        node.setBaseUri("http://example.com");

        assertEquals("http://example.com", node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriNull() {
        Node node = new Element("div", "", new Attributes());
        node.setBaseUri(null);
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Element("a", baseUri, new Attributes().put("href", "relative.html"));

        assertEquals("http://example.com/relative.html", node.absUrl("href"));
        assertEquals("http://example.com", node.absUrl(""));
        assertEquals("", node.absUrl(null));
        assertEquals("", node.absUrl("nonExistentAttr"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedURL() {
        String baseUri = "http://example.com";
        Node node = new Element("a", baseUri, new Attributes().put("href", "malformed:url"));
        node.absUrl("href");
    }

    @Test
    public void testChildNode() {
        Node parent = new Element("div");
        Node child = new Element("span");

        parent.appendChild(child);
        Node retrievedChild = parent.childNode(0);

        assertEquals(child, retrievedChild);
    }

    @Test
    public void testChildNodes() {
        Node parent = new Element("div");
        Node child1 = new Element("span");
        Node child2 = new Element("span");

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> childNodes = parent.childNodes();

        assertEquals(2, childNodes.size());
        assertTrue(childNodes.contains(child1));
        assertTrue(childNodes.contains(child2));
    }

    @Test
    public void testChildNodesArray() {
        Node parent = new Element("div");
        Node child1 = new Element("span");
        Node child2 = new Element("span");

        parent.appendChild(child1);
        parent.appendChild(child2);

        Node[] childNodes = parent.childNodesAsArray();

        assertEquals(2, childNodes.length);
        assertTrue(Arrays.asList(childNodes).contains(child1));
        assertTrue(Arrays.asList(childNodes).contains(child2));
    }

    @Test
    public void testParent() {
        Node child = new Element("span");
        Node parent = new Element("div");

        child.setParentNode(parent);

        assertEquals(parent, child.parent());
        assertEquals(null, child.parent()); // default constructor
    }

    @Test
    public void testOwnerDocument() {
        Node node = new Element("div");

        assertEquals(null, node.ownerDocument());
        Document document = new Document();
        node.setParentNode(document.getRootElement());

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Element("div");
        Node child = new Element("span");

        parent.appendChild(child);

        parent.removeChild(child);

        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Element("div");
        Node oldChild = new Element("span");
        Node newChild = new Element("span");

        parent.appendChild(oldChild);

        parent.replaceChild(oldChild, newChild);

        assertEquals(1, parent.childNodes().size());
        assertNotEquals(oldChild, parent.childNodes().get(0));
        assertEquals(newChild, parent.childNodes().get(0));
    }

    @Test
    public void testSetParentNode() {
        Node child = new Element("span");
        Node parent = new Element("div");

        child.setParentNode(parent);

        assertEquals(parent, child.parentNode);
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Element("div");
        Node out = new Element("span");
        Node in = new Element("span");

        parent.appendChild(out);

        parent.replaceChild(out, in);

        assertEquals(1, parent.childNodes().size());
        assertEquals(in, parent.childNodes().get(0));
    }

    @Test
    public void testRemoveChild() {
        Node parent = new Element("div");
        Node child = new Element("span");

        parent.appendChild(child);

        parent.removeChild(child);

        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testAddChildren() {
        Node parent = new Element("div");
        Node child1 = new Element("span");
        Node child2 = new Element("span");

        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertTrue(parent.childNodes().contains(child1));
        assertTrue(parent.childNodes().contains(child2));
    }

    @Test
    public void testAddChildrenIndex() {
        Node parent = new Element("div");
        Node child1 = new Element("span");
        Node child2 = new Element("span");

        parent.addChildren(1, child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertTrue(parent.childNodes().get(0) instanceof Element);
        assertTrue(parent.childNodes().get(1) instanceof Element);
        assertEquals(child1, parent.childNodes().get(1));
        assertEquals(child2, parent.childNodes().get(0));
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new Element("div");
        Node sibling1 = new Element("span");
        Node current = new Element("span");
        Node sibling2 = new Element("span");

        parent.appendChild(sibling1);
        parent.appendChild(current);
        parent.appendChild(sibling2);

        List<Node> siblings = current.siblingNodes();

        assertEquals(2, siblings.size());
        assertTrue(siblings.contains(sibling1));
        assertTrue(siblings.contains(sibling2));
    }

    @Test
    public void testNextSibling() {
        Node parent = new Element("div");
        Node sibling1 = new Element("span");
        Node current = new Element("span");
        Node sibling2 = new Element("span");

        parent.appendChild(sibling1);
        parent.appendChild(current);
        parent.appendChild(sibling2);

        Node nextSibling = current.nextSibling();

        assertEquals(sibling2, nextSibling);
        assertEquals(null, current.nextSibling()); // first child
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new Element("div");
        Node sibling1 = new Element("span");
        Node current = new Element("span");
        Node sibling2 = new Element("span");

        parent.appendChild(sibling1);
        parent.appendChild(current);
        parent.appendChild(sibling2);

        Node previousSibling = current.previousSibling();

        assertEquals(sibling1, previousSibling);
        assertEquals(null, current.previousSibling()); // last child
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Element("div");
        Node sibling1 = new Element("span");
        Node current = new Element("span");
        Node sibling2 = new Element("span");

        parent.appendChild(sibling1);
        parent.appendChild(current);
        parent.appendChild(sibling2);

        int index = current.siblingIndex();

        assertEquals(1, index);
    }

    @Test
    public void testSetSiblingIndex() {
        Node parent = new Element("div");
        Node sibling1 = new Element("span");
        Node current = new Element("span");
        Node sibling2 = new Element("span");

        parent.appendChild(sibling1);
        parent.appendChild(current);
        parent.appendChild(sibling2);

        current.setSiblingIndex(0);

        Node firstSibling = parent.childNodes().get(0);
        assertEquals(current, firstSibling);

        assertEquals(1, sibling1.siblingIndex());
        assertEquals(2, sibling2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        Node node = new Element("div").attr("id", "test");
        node.outerHtml("test"); // no implementation in abstract Node class

        assertEquals("<div id=\"test\"></div>", node.outerHtml());
    }
}