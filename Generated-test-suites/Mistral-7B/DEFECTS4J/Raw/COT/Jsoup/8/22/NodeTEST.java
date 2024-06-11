package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertNull(node.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testAbstractMethod() {
        node.nodeName(); // abstract method, should throw an AssertionError in JUnit 4
    }

    @Test
    public void testAttr() {
        assertEquals("value", node.attr("key"));
        assertEquals("", node.attr("nonExistentKey"));
    }

    @Test
    public void testAttributes() {
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testAttrSet() {
        node.attr("key", "newValue");
        assertEquals("newValue", node.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("nonExistentKey"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://example.org");
        assertEquals("http://example.org", node.baseUri());
    }

    @Test(expected = AssertionError.class)
    public void testNullBaseUri() {
        node.baseUri(null); // constructor should throw an AssertionError in JUnit 4
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", node.absUrl(""));
        assertEquals("http://example.com/path/relUrl", node.absUrl("path/relUrl"));
        assertEquals("http://example.com/path/relUrl", node.absUrl("path/relUrl", "http://example.net"));
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrl() {
        node.absUrl("invalidUrl"); // constructor should throw a MalformedURLException in JUnit 4
    }

    @Test
    public void testChildNode() {
        Node child = new Node();
        node.addChild(child);

        Node retrievedChild = node.childNode(0);
        assertSame(child, retrievedChild);
    }

    @Test
    public void testChildren() {
        Node child1 = new Node();
        Node child2 = new Node();

        node.addChildren(child1, child2);

        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    @Test
    public void testParent() {
        Node parent = new Node();
        parent.addChild(node);

        Node retrievedParent = node.parent();
        assertSame(parent, retrievedParent);
    }

    @Test
    public void testOwnerDocument() {
        Node document = new Document();
        document.addChild(node);

        Document retrievedDocument = node.ownerDocument();
        assertSame(document, retrievedDocument);
    }

    @Test
    public void testRemove() {
        Node parent = new Node();
        parent.addChild(node);

        node.remove();
        assertFalse(parent.hasChildNodes());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Node();
        Node child1 = new Node();
        Node child2 = new Node();

        parent.addChild(child1);
        parent.addChild(node);
        parent.addChild(child2);

        node.replaceWith(child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
        assertFalse(children.contains(node));
    }
}