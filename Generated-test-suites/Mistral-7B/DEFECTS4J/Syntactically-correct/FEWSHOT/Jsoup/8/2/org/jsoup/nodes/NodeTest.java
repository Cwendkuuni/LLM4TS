package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertEquals("example", node.attr("class"));
    }

    @Test
    public void testNodeCreationWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeCreationWithNullAttributes() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri, null);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeCreationWithNullBaseUri() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", null, attributes);
        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertNotNull(node.attributes());
        assertEquals("example", node.attr("class"));
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("example", node.attr("class"));
        assertEquals("", node.attr("non-existent-attribute"));
        assertEquals("http://example.com", node.absUrl("href"));
        assertEquals("http://example.com/example", node.absUrl("abs:href"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        assertEquals(attributes, node.attributes());
        assertEquals("example", node.attributes().get("class"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testSetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri);
        node.attr("class", "example");
        assertTrue(node.hasAttr("class"));
        assertEquals("example", node.attr("class"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        List<Node> children = parent.childNodesAsArray();
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri);
        Node child = new Element("p", baseUri, attributes);
        parent.addChild(child);
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testGetParentNode() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Attributes attributes = new Attributes();
        Node child = new Element("p", baseUri1, attributes);
        Node parent = new Element("div", baseUri2, attributes);
        parent.addChild(child);
        assertEquals(parent, child.parent());
        assertNull(child.parent());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri);
        Node child = new Element("p", baseUri, attributes);
        parent.addChild(child);
        parent.removeChild(child);
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri);
        Node oldChild = new Element("p", baseUri, attributes);
        Node newChild = new Element("p", baseUri, attributes);
        parent.addChild(oldChild);
        parent.replaceChild(newChild, oldChild);
        assertEquals(newChild, parent.childNode(0));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        List<Node> children = parent.childNodesAsArray();
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testGetDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("p", baseUri, attributes);
        Node document = new Document(baseUri);
        document.addChild(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        StringBuilder expected = new StringBuilder();
        expected.append("<div>");
        expected.append("<p class=\"example\">Example text</p>");
        expected.append("<p class=\"example\">Another example</p>");
        expected.append("</div>");
        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri, attributes).text("Example text");
        Node child2 = new Element("p", baseUri, attributes).text("Another example");
        parent.addChildren(child1, child2);
        StringBuilder actual = new StringBuilder();
        parent.outerHtml(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlWithNullAccumulator() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri, attributes).text("Example text");
        Node child2 = new Element("p", baseUri, attributes).text("Another example");
        parent.addChildren(child1, child2);
        parent.outerHtml(null);
    }
}