package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    // Basic test cases for constructors

    @Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1");
        Node node = new Node(baseUri, attributes);
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testDefaultConstructor() {
        Node node = new Node();
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    // Basic test cases for getters and setters

    @Test
    public void testNodeName() {
        // Abstract method, cannot be tested directly
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1");
        Node node = new Node(baseUri, attributes);
        assertEquals("element1", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttr"));

        node.attr("class", "example");
        assertEquals("example", node.attr("class"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1").put("class", "example");
        Node node = new Node(baseUri, attributes);
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1").put("class", "example");
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1").put("class", "example");
        Node node = new Node(baseUri, attributes);
        assertEquals("element1", node.attr("id"));
        node.removeAttr("id");
        assertEquals("", node.attr("id"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://another-example.com");
        assertEquals("http://another-example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String href = "relative.html";
        Node node = new Node(baseUri, new Attributes());
        node.attr("href", href);
        assertEquals("", node.absUrl(""));
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
        assertEquals("http://example.com/absolute.html", node.absUrl("abs:absolute.html"));
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriConstructor() {
        new Node(null, new Attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributesConstructor() {
        new Node("http://example.com", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributeValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1");
        Node node = new Node(baseUri, attributes);
        node.attr("id", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasAttrWithNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        assertFalse(node.hasAttr(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAttrWithNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1");
        Node node = new Node(baseUri, attributes);
        node.removeAttr(null);
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidBaseUri() {
        new Node("http://example.com/invalid", new Attributes());
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidUrlAttributeValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr("abs:invalid", "invalid");
    }
}