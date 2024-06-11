package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {
    @Test
    public void testDocument() {
        Document doc = new Document("https://www.example.com");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertEquals("html", doc.getElementsByTag("html").get(0).tagName());
        assertEquals("head", doc.getElementsByTag("head").get(0).tagName());
        assertEquals("body", doc.getElementsByTag("body").get(0).tagName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        assertNull(head);

        doc.appendElement("html");
        doc.getElementsByTag("html").first().appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body= doc.body();
        assertNull(body);

        doc.appendElement("html");
        doc.getElementsByTag("html").first().appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        assertEquals("", doc.title());

        doc.appendElement("html");
        doc.getElementsByTag("html").first().appendElement("head");
        doc.head().appendElement("title");
        doc.title("Example Title");
        assertEquals("Example Title", doc.title());

        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("https://www.example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("https://www.example.com");
        doc.appendElement("html");
        doc.normalise();
        assertNotNull(doc.getElementsByTag("html"));
        assertNotNull(doc.head());
        assertNotNull(doc.body());

        doc.head().appendElement("title").text("Test Title");
        doc.normalise();
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://www.example.com");
        doc.appendElement("html");
        doc.appendElement("head");
        doc.appendElement("body");

        assertEquals("<head></head><body></body>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("https://www.example.com");
        doc.appendElement("html");
        doc.appendElement("head");
        doc.appendElement("body");

        doc.body().text("Example Text");
        assertEquals("Example Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#document", doc.nodeName());
    }
}