package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testNewDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertTrue(doc.head().childNodes().size() > 0);
        assertTrue(doc.body().childNodes().size() == 0);
    }

    @Test
    public void testHeadElement() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertEquals("#head", head.nodeName());
        assertNull(head.text());
    }

    @Test
    public void testBodyElement() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertEquals("#body", body.nodeName());
        assertNull(body.text());
    }

    @Test
    public void testTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        Element title = doc.getElementsByTag("title").first();
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p");
        assertEquals("p", p.nodeName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.body().appendElement("title", "Test Title");
        doc.head().appendElement("p", "Test Text");

        doc.normalise();

        assertNull(doc.getElementsByTag("p").first());
        Element body = doc.body();
        assertTrue(body.childNodes().size() > 0);
        TextNode title = (TextNode) doc.getElementsByTag("title").first().childNodes().get(0);
        assertEquals("Test Title", title.text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title", "Test Title");
        doc.body().appendElement("p", "Test Text");

        assertEquals("<html><head><title>Test Title</title></head><body><p>Test Text</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.body().text("Test Text");

        assertEquals("Test Text", doc.text().toString());
    }
}