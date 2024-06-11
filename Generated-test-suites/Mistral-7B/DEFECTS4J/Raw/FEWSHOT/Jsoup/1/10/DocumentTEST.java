package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.W3C.html;
import static org.jsoup.helper.W3C.head;
import static org.jsoup.helper.W3C.body;
import static org.jsoup.helper.W3C.title;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    private Document doc;

    @Before
    public void setup() {
        doc = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.select("html").first();
        assertEquals("#html", html.nodeName());
        Element head = html.getElementsByTag("head").first();
        assertEquals("#head", head.nodeName());
        Element body = html.getElementsByTag("body").first();
        assertEquals("#body", body.nodeName());
        assertNull(doc.title());
    }

    @Test
    public void testHead() {
        Element head = doc.head();
        assertTrue(head.getElementsByTag("title").isEmpty());
    }

    @Test
    public void testGetTitle() {
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", doc.getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitle() {
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
        assertEquals("Test Title 2", doc.getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        Element p = doc.createElement("p");
        assertEquals("#p", p.nodeName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        doc.title("Test Title");
        Element p = doc.createElement("p");
        p.text("Test Text");
        doc.body().appendChild(p);

        doc.normalise();

        assertEquals("Test Title", doc.title());
        Element body = doc.body();
        assertEquals(1, body.childNodes().size());
        assertEquals("Test Text", body.childNodes().get(0).text());
    }

    @Test
    public void testOuterHtml() {
        doc.title("Test Title");
        Element p = doc.createElement("p");
        p.text("Test Text");
        doc.body().appendChild(p);

        doc.normalise();

        String html = doc.outerHtml();
        Document parsed = Jsoup.parse(html);
        assertEquals("Test Title", parsed.title());
        assertEquals("Test Text", parsed.body().text());
    }

    @Test
    public void testTextNode() {
        doc.title("Test Title");
        doc.body().text("Test Text");

        doc.normalise();

        assertEquals("Test Title", doc.title());
        assertEquals("Test Text", doc.body().text());
    }
}