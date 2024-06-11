package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testNode() {
        Node node = new Element("div");
        assertNull(node.parentNode());
        assertFalse(node.hasChildren());
        assertEquals(0, node.childNodes().size());
        assertNull(node.attributes());
        assertNull(node.baseUri());
        assertEquals(-1, node.siblingIndex());
        assertNotNull(node.nodeName());
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testNodeWithAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", baseUri, attributes);

        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertFalse(node.hasChildren());
        assertEquals(0, node.childNodes().size());
        assertEquals("div", node.nodeName());
        assertTrue(node.hasAttr("class"));
        assertEquals("myClass", node.attr("class"));
        assertNull(node.attr("nonexistent"));
        assertEquals("https://example.com/", node.absUrl(""));
        assertEquals("https://example.com/myClass", node.absUrl("class"));
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new Element("p", baseUri, new Attributes());
        Node child2 = new Element("span", baseUri, new Attributes());

        parent.appendChild(child1);
        parent.appendChild(child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
        assertSame(parent, child1.parentNode());
        assertSame(parent, child2.parentNode());
        assertNull(child1.parent());
        assertNull(child2.parent());
        assertEquals("div", parent.nodeName());
        assertEquals("p", child1.nodeName());
        assertEquals("span", child2.nodeName());
    }

    @Test
    public void testNodeWithBaseUri() throws MalformedURLException {
        String baseUri = "https://example.com/index.html";
        Node node = new Element("a", baseUri, new Attributes());
        node.attr("href", "relative.html");
        String absUrl = node.absUrl("href");
        URL url = new URL(absUrl);
        assertEquals("https://example.com/index.html/relative.html", absUrl);
        assertEquals("https://example.com/relative.html", url.toExternalForm());
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidBaseUri() throws MalformedURLException {
        String baseUri = "https://example.com";
        Node node = new Element("a", baseUri, new Attributes());
        node.attr("href", "://example.com/relative.html");
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new Element("p", baseUri, new Attributes());
        Node child2 = new Element("span", baseUri, new Attributes());

        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.removeChild(child1);

        assertEquals(1, parent.childNodes().size());
        assertSame(child2, parent.childNode(0));
        assertSame(child2, parent.childNodes().get(0));
        assertNull(child1.parentNode());
        assertNull(child1.parent());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, new Attributes());
        Node newChild = new Element("span", baseUri, new Attributes());

        parent.appendChild(oldChild);
        parent.replaceChild(newChild, oldChild);

        assertEquals(1, parent.childNodes().size());
        assertSame(newChild, parent.childNode(0));
        assertSame(newChild, parent.childNodes().get(0));
        assertNull(oldChild.parentNode());
        assertNull(oldChild.parent());
        assertSame(newChild, parent.childNode(0));
        assertSame(newChild, parent.childNodes().get(0));
        assertNull(oldChild.siblingNext());
        assertNull(oldChild.siblingPrevious());
        assertNull(newChild.siblingPrevious());
        assertSame(newChild, newChild.siblingNext());
    }
}