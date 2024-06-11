package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        assertEquals("#root", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();

        assertTrue(head.children().isEmpty());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();

        assertTrue(body.children().isEmpty());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");

        assertNull(doc.title());

        doc.title("Test Title");

        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");

        assertEquals("p", p.tagName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Element p1 = doc.createElement("p").text("First");
        Element p2 = doc.createElement("p").text("Second");
        doc.body().appendChild(p1);
        doc.body().appendChild(p2);

        doc.normalise();

        Element body = doc.body();
        List<Node> children = body.children();

        assertTrue(children.size() == 2);
        assertTrue(children.get(0) instanceof TextNode);
        assertEquals("First", ((TextNode) children.get(0)).text());
        assertTrue(children.get(1) instanceof Element);
        assertEquals("p", ((Element) children.get(1)).tagName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Element p1 = doc.createElement("p").text("First");
        Element p2 = doc.createElement("p").text("Second");
        doc.body().appendChild(p1);
        doc.body().appendChild(p2);

        doc.normalise();

        assertEquals("<html><head><title>Test Title</title></head><body>FirstSecond</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        doc.body().text("Test Body");

        assertEquals("Test Body", doc.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        Document doc = new Document("http://example.com");
        doc.title("");
    }
}