package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        assertNotNull(doc);
        assertEquals("html", doc.nodeName());
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("https://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("https://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("https://example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("https://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://example.com");
        doc.head().appendElement("title").text("Test Title");
        doc.normalise();
        assertEquals("Test Title", doc.title());
        // Test other normalisation scenarios
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("https://example.com");
        Element body = doc.body();
        body.appendElement("div").text("Test Div");
        String html = doc.outerHtml();
        assertEquals("<div>Test Div</div>", html);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("https://example.com");
        doc.text("Test Body Text");
        assertEquals("Test Body Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("https://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://example.com");
        Document.OutputSettings settings = doc.outputSettings();
        assertNotNull(settings);
        // Test output settings methods
    }
}