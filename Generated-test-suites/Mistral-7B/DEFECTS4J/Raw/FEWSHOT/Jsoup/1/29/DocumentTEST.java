package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertFalse(doc.hasText());
        assertFalse(doc.hasChildren());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertTrue(doc.hasText());
        assertTrue(doc.hasChildren());
    }

    @Test
    public void testHeadElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertEquals(baseUri, head.baseUri());
        assertFalse(head.hasText());
        assertFalse(head.hasChildren());
    }

    @Test
    public void testBodyElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(baseUri, body.baseUri());
        assertFalse(body.hasText());
        assertFalse(body.hasChildren());
    }

    @Test
    public void testTitleElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
        Element titleEl = doc.getElementsByTag("title").first();
        assertEquals("title", titleEl.tagName());
        assertEquals(baseUri, titleEl.baseUri());
        assertEquals("Test Title", titleEl.text().trim());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element link = doc.createElement("a");
        assertEquals("a", link.tagName());
        assertEquals(baseUri, link.baseUri());
        assertNull(link.text());
        assertFalse(link.hasChildren());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        doc.appendElement("html").appendElement("head").appendElement("body")
            .appendText("Hello from head").appendText("Hello from body");
        doc.normalise();
        assertEquals(3, doc.childNodes().size());
        assertEquals("html", doc.getElementsByTag("html").first().tagName());
        assertEquals("head", doc.getElementsByTag("head").first().tagName());
        assertEquals("body", doc.getElementsByTag("body").first().tagName());
        assertTrue(doc.getElementsByTag("title").isEmpty());
        assertEquals("Test Title", doc.title());
        assertEquals("Hello from body", doc.body().text().trim());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        doc.title("Test Title");
        body.appendText("Hello from body");
        doc.normalise();
        assertEquals("<html><head></head><body>Hello from body</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        doc.body().text("Hello from body");
        doc.normalise();
        assertEquals("Hello from body", doc.text());
    }

}