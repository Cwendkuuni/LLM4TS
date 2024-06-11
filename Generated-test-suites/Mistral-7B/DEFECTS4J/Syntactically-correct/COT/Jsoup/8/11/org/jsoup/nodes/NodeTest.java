package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class NodeTest {

    private Node node;
    private Attributes attributes;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        attributes = new Attributes();
        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        // Abstract class, cannot be instantiated to test nodeName() directly
        // You can create test cases for specific Node subclasses instead
    }

    @Test
    public void testConstructor() {
        String baseUri = "http://example.com";
        String attributeKey = "test";
        String attributeValue = "value";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());

        assertEquals(baseUri.trim(), node1.baseUri());
        assertEquals(Collections.emptyList(), node2.childNodes());
        assertNull(node3.childNodes());
    }

    @Test
    public void testAttr() {
        String attributeKey = "test";
        String attributeValue = "value";

        node.attr(attributeKey, attributeValue);

        assertEquals(attributeValue, node.attr(attributeKey));
        assertFalse(node.hasAttr("otherKey"));

        String absUrl = node.absUrl(attributeKey);
        assertEquals(absUrl, node.attr("abs:" + attributeKey));
        assertEquals("", absUrl);
    }

    @Test
    public void testAttributes() {
        String attributeKey = "test";
        String attributeValue = "value";

        node.attr(attributeKey, attributeValue);

        List<String> keys = new ArrayList<>(Arrays.asList("test", "otherKey"));
        List<String> values = new ArrayList<>(Arrays.asList("value", "otherValue"));

        attributes.putAll(keys, values);

        assertEquals(attributes, node.attributes());
        assertEquals(keys, node.attributes().keySet());
        assertEquals(values, node.attributes().values());
        assertEquals(attributeValue, node.attr("test"));
        assertFalse(node.hasAttr("otherKey"));
        assertEquals("", node.attr("otherKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Node(null, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        new Node("http://example.com", null);
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedBaseUri() {
        new Node("http://example.com/", attributes);
        new Node("http://example.com", null);
    }

    @Test
    public void testChildNodes() {
        Node child1 = new Node();
        Node child2 = new Node();

        node.addChildren(child1, child2);

        Node[] children = node.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);

        List<Node> childList = node.childNodes();
        assertEquals(2, childList.size());
        assertEquals(child1, childList.get(0));
        assertEquals(child2, childList.get(1));

        List<Node> siblings = node.siblingNodes();
        assertTrue(siblings.isEmpty());

        Node sibling = new Node();
        node.parentNode.addChildren(sibling);
        node.parentNode.addChild(sibling);
        sibling.addSibling(node);

        siblings = node.siblingNodes();
        assertEquals(sibling, siblings.get(0));

        Node previousSibling = new Node();
        node.parentNode.addChild(previousSibling);
        previousSibling.addSibling(node);

        siblings = node.siblingNodes();
        assertEquals(previousSibling, siblings.get(siblings.size()-1));

        node.remove();
        siblings = node.siblingNodes();
        assertTrue(siblings.isEmpty());
    }

    @Test
    public void testAbsUrl() {
        String attributeKey = "test";
        String relativeUrl = "test.html";

        node.attr(attributeKey, relativeUrl);

        String baseUrl = "http://example.com/base";
        node.setBaseUri(baseUrl);

        String absUrl = node.absUrl(attributeKey);
        assertEquals("http://example.com/base/test.html", absUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidUrl() {
        node.attr("test", "javascript:alert('Hello')");
        String absUrl = node.absUrl("test");
    }
}