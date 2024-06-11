package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument_constructor() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);

        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testDocument_createShell() {
        String baseUri = "http://www.example.com";
        Document doc = Document.createShell(baseUri);

        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.head().parent().nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("html", doc.body().parent().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void testDocument_head() {
        Document doc = Document.createShell("http://www.example.com");

        assertNull(doc.head());

        Element head = doc.createElement("head");
        doc.appendChild(head);

        assertEquals(head, doc.head());
    }

    @Test
    public void testDocument_body() {
        Document doc = Document.createShell("http://www.example.com");

        assertNull(doc.body());

        Element body = doc.createElement("body");
        doc.appendChild(body);

        assertEquals(body, doc.body());
    }

    @Test
    public void testDocument_title() {
        Document doc = Document.createShell("http://www.example.com");

        assertEquals("", doc.title());

        String title = "Example Title";
        doc.title(title);

        assertEquals(title, doc.title());

        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals(title, titleEl.text());
    }

    @Test
    public void testDocument_createElement() {
        Document doc = Document.createShell("http://www.example.com");

        String tagName = "a";
        Element element = doc.createElement(tagName);

        assertNotNull(element);
        assertEquals(tagName, element.nodeName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testDocument_normalise() {
        Document doc = Document.createShell("http://www.example.com");

        doc.normalise();

        assertNotNull(doc.select("html"));
        assertNotNull(doc.head());
        assertNotNull(doc.body());

        Element html = doc.select("html").first();
        assertNotNull(html);

        assertTrue(html.parent().nodeName().equals("#root"));
        assertTrue(html.children().contains(doc.head()));
        assertTrue(html.children().contains(doc.body()));
    }

    @Test
    public void testDocument_outerHtml() {
        Document doc = Document.createShell("http://www.example.com");

        String html = doc.outerHtml();

        assertNotNull(html);
        assertEquals("", html);
    }

    @Test
    public void testDocument_text() {
        Document doc = Document.createShell("http://www.example.com");

        String text = "Example Text";

        Element body = doc.body().text(text);
        assertEquals(text, body.text());

        // Ensure that doc structure is not nuked
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
}