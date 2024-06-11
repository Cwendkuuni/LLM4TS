package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    private Node root;

    @Before
    public void setUp() {
        String html = "<html><head></head><body><div id=\"div1\" class=\"div1\"><a href=\"http://example.com\">Link</a><img src=\"image.jpg\"></div></body></html>";
        root = Parser.parseBodyFragment(html);
    }

    @Test
    public void testAbstractNode() {
        Node node = new Node("", new Attributes());
        assertEquals("", node.nodeName());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertEquals("", node.baseUri());
        assertNull(node.attributes().get("test"));
        assertEquals(0, node.siblingIndex());
    }

    @Test
    public void testNodeWithBaseUri() {
        Node node = new Element("div", new Attributes(), "");
        node.setBaseUri("http://test.com");
        assertEquals("http://test.com", node.baseUri());
        assertEquals("div", node.nodeName());
        Node child = new TextNode("Test");
        node.appendChild(child);
        assertEquals(child, node.getChildNodes().get(0));
        assertEquals("Test", child.text());
        assertEquals("Test", node.outerHtml());
    }

    @Test
    public void testNodeWithAttributes() {
        Element div = (Element) root.child(0);
        assertEquals("div", div.nodeName());
        assertEquals(2, div.childNodes().size());

        Element link = (Element) div.child(0);
        assertEquals("a", link.nodeName());
        assertEquals("Link", link.text());

        List<String> attributes = Arrays.asList("id", "class");
        for (String attr : attributes) {
            assertEquals(attr, link.attr(attr));
            assertFalse(link.hasAttr("nonexistent"));
            assertEquals("", link.attr("nonexistent"));
        }
        assertEquals("http://example.com", link.absUrl("href"));

        String baseUri = "http://testbase.com";
        link.setBaseUri(baseUri);
        assertEquals("http://testbase.com/link.html", link.absUrl("href"));

        Attributes attrs = link.attributes();
        assertEquals("id=\"div1\" class=\"div1\" href=\"http://testbase.com/link.html\"", attrs.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeWithNullBaseUri() {
        Node node = new Node("", null);
        throw new AssertionError("Should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeWithNullAttributes() {
        Node node = new Node("", null);
        throw new AssertionError("Should not reach this point");
    }

    @Test
    public void testNodeWithEmptyList() {
        Node node = new Node("", new Attributes());
        assertTrue(node.childNodes().isEmpty());
        assertFalse(node.hasChildNodes());
    }

    @Test
    public void testNodeWithChildren() {
        Element div = (Element) root.child(0);
        assertEquals(2, div.childNodes().size());

        Element link = (Element) div.child(0);
        Element img = (Element) div.child(1);

        Node[] children = div.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(link, children[0]);
        assertEquals(img, children[1]);

        TextNode textNode = new TextNode("Test");
        div.appendChild(textNode);
        assertEquals(3, div.childNodes().size());
        assertEquals(textNode, div.child(2));
    }

    @Test
    public void testNodeWithNullChild() {
        Element div = (Element) root.child(0);
        Node nullNode = null;
        try {
            div.appendChild(nullNode);
            throw new AssertionError("Should not reach this point");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testNodeWithCustomRemove() {
        Element div = (Element) root.child(0);
        Element link = (Element) div.child(0);

        div.removeChild(link);
        assertEquals(0, div.childNodes().size());
        assertNull(link.parentNode());

        link.setParentNode(div);
        div.appendChild(link);
        assertEquals(1, div.childNodes().size());

        TextNode textNode = new TextNode("Test");
        div.appendChild(textNode);
        assertEquals(2, div.childNodes().size());

        div.removeChild(textNode);
        assertEquals(1, div.childNodes().size());
    }

    @Test
    public void testNodeWithCustomReplace() {
        Element div = (Element) root.child(0);
        Element link = (Element) div.child(0);

        Element newLink = new Element("a", new Attributes(), "New Link");
        div.replaceChild(link, newLink);
        assertEquals(1, div.childNodes().size());
        assertEquals("New Link", div.child(0).text());

        TextNode textNode = new TextNode("Test");
        div.appendChild(textNode);
        assertEquals(2, div.childNodes().size());

        div.replaceChild(textNode, link);
        assertEquals(1, div.childNodes().size());
        assertEquals("Link", div.child(0).text());
    }

    @Test
    public void testNodeWithParent() {
        Element div = (Element) root.child(0);
        TextNode textNode = new TextNode("Test");
        div.appendChild(textNode);
        div.setParentNode(null);
        assertNull(div.parentNode());

        textNode.parentNode = new Element("p", new Attributes(), "");
        assertEquals(null, textNode.parentNode());
    }

    @Test
    public void testNodeWithDocument() {
        Document doc = new Document();
        Element div = new Element("div", new Attributes(), "");
        doc.appendChild(div);
        TextNode textNode = new TextNode("Test");
        div.appendChild(textNode);
        assertEquals(doc, div.ownerDocument());
        assertEquals(div, textNode.parentNode());
    }

    @Test
    public void testNodeWithCustomRemoveFromParent() {
        Element div = (Element) root.child(0);
        Element link = (Element) div.child(0);

        div.removeChild(link);
        assertEquals(0, div.childNodes().size());
        assertNull(link.parentNode());

        TextNode textNode = new TextNode("Test");
        div.appendChild(textNode);
        div.setParentNode(null);
        div.removeChild(textNode);
        assertEquals(0, div.childNodes().size());

        div.setParentNode(root);
        div.appendChild(link);
        assertEquals(1, div.childNodes().size());
        assertEquals("Link", div.child(0).text());
    }

    @Test
    public void testNodeWithCustomAddChildren() {
        Element div = (Element) root.child(0);
        Node[] children = new Node[3];

        for (int i = 0; i < 3; i++) {
            Element node = new Element("p", new Attributes(), "Test " + i);
            div.addChildren(node);
            children[i] = node;
        }

        assertEquals(3, div.childNodes().size());
        assertEquals(children[0], div.child(0));
        assertEquals(children[1], div.child(1));
        assertEquals(children[2], div.child(2));
    }

    @Test
    public void testNodeWithCustomAddChildrenAtIndex() {
        Element div = (Element) root.child(0);
        Node[] children = new Node[3];

        for (int i = 0; i < 3; i++) {
            Element node = new Element("p", new Attributes(), "Test " + i);
            div.addChildren(1, node);
            children[i] = node;
        }

        assertEquals(3, div.childNodes().size());
        assertEquals(children[2], div.child(0));
        assertEquals(children[0], div.child(1));
        assertEquals(children[1], div.child(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeWithNegativeIndexForAddChildren() {
        Element div = (Element) root.child(0);
        div.addChildren(-1, new Element("p", new Attributes(), "Test"));
        throw new AssertionError("Should not reach this point");
    }

    @Test
    public void testNodeWithCustomAddChildrenOverCapacity() {
        Element div = (Element) root.child(0);
        Node[] children = new Node[100];

        for (int i = 0; i < 100; i++) {
            Element node = new Element("p", new Attributes(), "Test " + i);
            div.addChildren(node);
            children[i] = node;
        }

        assertEquals(100, div.childNodes().size());
        for (Node node : children) {
            assertEquals(node, div.child(node.siblingIndex()));
        }
    }
}