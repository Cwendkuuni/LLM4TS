package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

/**
 * Node Test File
 */
public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        String attributeKey = "test";
        String attributeValue = "testValue";
        Node node = new Node(baseUri, new Attributes().put(attributeKey, attributeValue));

        assertEquals(baseUri, node.baseUri());
        assertEquals(attributeValue, node.attr(attributeKey));
        assertEquals(Collections.singletonList(node), node.childNodes());
        assertNull(node.parentNode());
        assertEquals("", node.outerHtml());
        assertEquals(attributeValue, node.attr("test"));
        assertTrue(node.hasAttr(attributeKey));
    }

    @Test
    public void testNodeConstructorWithoutAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertNull(node.parentNode());
        assertEquals("", node.outerHtml());
    }

    @Test
    public void testDefaultConstructor() {
        Node node = new Node();

        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.parentNode());
        assertEquals("", node.outerHtml());
    }

    @Test
    public void testAbstractMethods() {
        // Abstract methods cannot be tested directly, so we can only test the presence of them.
        assertTrue(Node.class.getDeclaredMethod("nodeName").isAbstract());
        assertTrue(Node.class.getDeclaredMethod("outerHtmlHead").isAbstract());
        assertTrue(Node.class.getDeclaredMethod("outerHtmlTail").isAbstract());
    }

    @Test
    public void testGetNodeName() {
        Class<? extends Node> nodeClass = Element.class; // An example of a subclass of Node
        Node node = Mockito.mock(nodeClass);
        when(node.nodeName()).thenReturn("testName");

        assertEquals("testName", node.nodeName());
    }

    @Test
    public void testSetAndGetAttributes() {
        String baseUri = "http://example.com";
        String attributeKey = "test";
        String attributeValue = "testValue";

        Node node = new Node(baseUri, new Attributes().put(attributeKey, attributeValue));

        assertEquals(attributeValue, node.attr(attributeKey));
        assertEquals(Collections.singletonMap(attributeKey, attributeValue), node.attributes());

        node.attr(attributeKey, "updatedValue");
        assertEquals("updatedValue", node.attr(attributeKey));
        assertEquals(Collections.singletonMap(attributeKey, "updatedValue"), node.attributes());
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        String attributeKey = "test";
        String attributeValue = "testValue";

        Node node = new Node(baseUri, new Attributes().put(attributeKey, attributeValue));

        assertTrue(node.hasAttr(attributeKey));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        String attributeKey = "test";
        String attributeValue = "testValue";

        Node node = new Node(baseUri, new Attributes().put(attributeKey, attributeValue));

        assertEquals(attributeValue, node.attr(attributeKey));
        node.removeAttr(attributeKey);
        assertEquals("", node.attr(attributeKey));
        assertFalse(node.hasAttr(attributeKey));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String attributeKey = "href";
        String attributeValue = "/test/page.html";

        Node node = new Node(baseUri, new Attributes().put(attributeKey, attributeValue));

        assertEquals("", node.absUrl(attributeKey)); // Relative URL, no base URI
        assertEquals("http://example.com/test/page.html", node.absUrl(attributeKey + "abs")); // Absolute URL with "abs:" prefix
        assertEquals("http://example.com/test/page.html", node.absUrl(attributeKey)); // Absolute URL, base URI is already present
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);

        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);

        parentNode.addChildren(child1, child2);

        List<Node> childNodes = parentNode.childNodes();

        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
        assertEquals(Collections.singletonList(parentNode), child1.parentNodes());
        assertEquals(Collections.singletonList(parentNode), child2.parentNodes());
    }
}