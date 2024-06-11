package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        Element html = doc.select("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        Document docWithHead = Document.createShell("http://example.com");
        head = docWithHead.head();
        assertNotNull(head);
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        Document docWithBody = Document.createShell("http://example.com");
        body = docWithBody.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        Document docWithTitle = Document.createShell("http://example.com");
        docWithTitle.title("Test Title");
        title = docWithTitle.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.tagName());
        assertEquals("http://example.com", newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.head().text("Test Header");
        doc.body().text("Test Body");
        doc.appendElement("div").text("Test Div");

        doc.normalise();

        Element body = doc.body();
        assertEquals("Test Body", body.text());
        assertTrue(body.childNodes().isEmpty());

        assertEquals("Test Header", doc.head().text());
        assertTrue(doc.getElementsByTag("div").isEmpty());

        Element div = doc.createElement("div");
        body.appendChild(div);
        body.text("New Test Body");

        doc.normalise();

        assertEquals("New Test Body", body.text());
        assertTrue(body.childNodes().isEmpty());
        assertTrue(div.parentNode() instanceof Element);
        assertEquals("div", div.tagName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.head().text("Test Header");
        doc.body().text("Test Body");

        String html = doc.outerHtml();
        String expectedHtml = "<html><head>Test Header</head><body>Test Body</body></html>";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.body().text("Test Body");

        doc.text("Test Document");
        String documentText = doc.text();
        assertEquals("Test DocumentTest Body", documentText);
    }
}