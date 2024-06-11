package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertFalse(node.hasAttr("class"));
        assertTrue(node.hasAttr("class", "test"));
        assertEquals("test", node.attr("class"));
        assertFalse(node.hasAttr("non-existent"));
        assertNull(node.attr("non-existent"));
        assertEquals(Collections.emptyList(), node.childNodes());
        assertNull(node.parent());
        assertNull(node.ownerDocument());

        node.setBaseUri("https://example.org");
        assertEquals("https://example.org", node.baseUri());

        attributes.put("new-attr", "new-value");
        node.attr("new-attr", "new-value");
        assertEquals("new-value", node.attr("new-attr"));
        assertTrue(node.hasAttr("new-attr"));
        assertFalse(node.hasAttr("class"));
        assertEquals(1, node.childNodes().size());
        assertEquals(node, node.childNode(0));
        Node parent = new Element("body", baseUri, new Attributes());
        parent.addChildren(node);
        node.setParentNode(parent);
        assertEquals(parent, node.parent());
        assertEquals(parent, node.ownerDocument());

        assertNull(node.absUrl("non-existent"));
        String href = "https://example.com/page.html";
        node.attr("href", href);
        assertEquals(href, node.absUrl("href"));
        assertEquals(href, node.absUrl("abs:href"));

        Node child = new Element("p", baseUri, new Attributes());
        node.appendChild(child);
        assertEquals(child, node.childNodes().get(0));
        assertEquals(1, node.childNodes().size());
        assertEquals(child, node.childNode(0));
        assertEquals(2, node.childNodesAsArray().length);

        Node grandchild = new Element("a", baseUri, new Attributes().put("href", "https://example.com/grandchild.html"));
        child.appendChild(grandchild);
        assertEquals(grandchild, node.childNodes().get(0).childNodes().get(0));
        assertEquals(1, node.childNodes().get(0).childNodes().size());
        assertEquals(grandchild, node.childNodes().get(0).childNodes().get(0));
        assertEquals(3, node.childNodesAsArray().length);

        Element textNode = new TextNode("Hello, world!");
        node.appendChild(textNode);
        assertEquals(4, node.childNodes().size());
        assertEquals(textNode, node.childNodes().get(3));
        assertEquals("Hello, world!", textNode.outerHtml());
        assertEquals("Hello, world!\n", node.outerHtml());
    }

    @Test
    public void testDocument() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Document doc = new Document(baseUri, attributes);
        assertEquals("document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.attributes());
        assertFalse(doc.hasAttr("class"));
        assertNull(doc.parent());
        assertEquals(doc, doc.ownerDocument());
    }

    @Test
    public void testTextNode() {
        String text = "Hello, world!";
        Node node = new TextNode(text);
        assertEquals("text", node.nodeName());
        assertEquals("", node.baseUri());
        assertNull(node.attributes());
        assertEquals(text, node.outerHtml());
        assertEquals(text, node.toString());
        assertEquals(text, node.toString(0));
        assertEquals(text, node.toString(1));
        assertEquals(1, node.childNodes().size());
        assertEquals(node, node.childNode(0));
        assertNull(node.parent());
        assertNull(node.ownerDocument());
    }

    @Test
    public void testElement() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Element node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertFalse(node.hasAttr("class"));
        assertTrue(node.hasAttr(""));
        assertEquals("", node.attr(""));
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
        assertNull(node.absUrl(""));

        node.attr("class", "test");
        assertTrue(node.hasAttr("class"));
        assertEquals("test", node.attr("class"));
        assertEquals("test", node.attr("class", "new-value"));
        assertEquals("test", node.attr("class"));
        assertFalse(node.hasAttr("non-existent"));
        assertNull(node.attr("non-existent"));
        assertNull(node.absUrl("non-existent"));
        assertEquals(Collections.emptyList(), node.childNodes());
        assertNull(node.parent());
        assertNull(node.ownerDocument());

        node.setAttributeValue("class", "test test2");
        assertEquals("test test2", node.attr("class"));
        assertEquals("test test2", node.attr("class"));

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
        assertNull(node.absUrl("class"));
        assertEquals(Collections.emptyList(), node.childNodes());
        assertNull(node.parent());
        assertNull(node.ownerDocument());

        Node child = new Element("p", baseUri, new Attributes());
        node.appendChild(child);
        assertEquals(1, node.childNodes().size());
        assertEquals(child, node.childNodes().get(0));
        assertEquals(1, node.childNodesAsArray().length);
        assertEquals(child, node.childNodesAsArray()[0]);
        assertEquals(2, node.childNodesAsArray().length);
        assertEquals(child, node.childNode(0));
        assertNull(node.prevSibling());
        assertNull(node.nextSibling());
        assertEquals(node, child.parentNode());
        assertEquals(node, child.ownerDocument());

        node.remove();
        assertNull(node.parent());
        assertNull(node.ownerDocument());

        node.setParentNode(new Document(baseUri, new Attributes()));
        assertEquals(node, node.parent().childNodes().get(0));
    }

    @Test
    public void testNodeTraversor() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Document doc = new Document(baseUri, attributes);
        Element root = new Element("html", baseUri, attributes);
        doc.appendChild(root);
        Element body = new Element("body", baseUri, new Attributes());
        root.appendChild(body);
        Element div = new Element("div", baseUri, new Attributes());
        body.appendChild(div);
        Element p1 = new Element("p", baseUri, new Attributes());
        div.appendChild(p1);
        TextNode text1 = new TextNode("Hello, ");
        p1.appendChild(text1);
        Element p2 = new Element("p", baseUri, new Attributes());
        div.appendChild(p2);
        TextNode text2 = new TextNode("world!");
        p2.appendChild(text2);

        Elements selectAll = new Elements(doc, "p");
        assertEquals(2, selectAll.size());
        Elements selectByClass = new Elements(doc, "p.test");
        assertEquals(0, selectByClass.size());
        Elements selectById = new Elements(doc, "#div");
        assertEquals(1, selectById.size());
        Element selected = selectById.first();
        assertEquals(1, selected.childNodes().size());
        assertEquals(1, selected.childNodesAsArray().length);
        assertEquals(text1, selected.childNodes().get(0));
        assertEquals("Hello, ", text1.outerHtml());
        assertEquals("Hello, ", text1.toString());
        assertEquals("Hello, ", text1.toString(0));
        assertEquals("Hello, ", text1.toString(1));
        assertEquals(1, selected.childNodesAsArray()[0].childNodes().size());
        assertEquals(text2, selected.childNodesAsArray()[0].childNodes().get(0));
        assertEquals("world!", text2.outerHtml());
        assertEquals("world!", text2.toString());
        assertEquals("world!", text2.toString(0));
        assertEquals("world!", text2.toString(1));

        NodeVisitor visitor = new NodeVisitor() {
            int count = 0;

            @Override
            public void head(Node node, int depth) {
                count++;
            }

            @Override
            public void tail(Node node, int depth) {
                count++;
            }
        };
        new NodeTraversor(visitor).traverse(doc);
        assertEquals(12, count);
    }

    @Test
    public void testNodeVisitor() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Document doc = new Document(baseUri, attributes);
        Element root = new Element("html", baseUri, attributes);
        doc.appendChild(root);
        Element body = new Element("body", baseUri, new Attributes());
        root.appendChild(body);
        Element div = new Element("div", baseUri, new Attributes());
        body.appendChild(div);
        Element p1 = new Element("p", baseUri, new Attributes());
        div.appendChild(p1);
        TextNode text1 = new TextNode("Hello, ");
        p1.appendChild(text1);
        Element p2 = new Element("p", baseUri, new Attributes());
        div.appendChild(p2);
        TextNode text2 = new TextNode("world!");
        p2.appendChild(text2);

        int count = 0;
        new NodeTraversor(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    if (textNode.toString().equals("Hello, ")) {
                        count++;
                    }
                }
            }

            @Override
            public void tail(Node node, int depth) {
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    if (textNode.toString().equals("world!")) {
                        count++;
                    }
                }
            }
        }).traverse(doc);
        assertEquals(2, count);
    }
}