package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertTrue(doc.outerHtml().isEmpty());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        Element html = doc.head();
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertEquals("body", body.tagName());
        assertTrue(doc.outerHtml().startsWith("<html><head><body>"));
    }

    @Test
    public void testGetHeadAndBodyElements() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        assertSame(head, doc.head());
        assertSame(body, doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = Document.createShell("http://example.com");
        String title = "Test Title";
        doc.title(title);
        assertEquals(title, doc.title());
        assertEquals(title, doc.head().child(0).text());
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("http://example.com");
        String title1 = "Title 1";
        String title2 = "Title 2";

        doc.title(title1);
        assertEquals(title1, doc.title());

        doc.title(title2);
        assertEquals(title2, doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p");
        assertEquals("p", p.tagName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalizeDocument() {
        String html = "<html><head><title>Test Title</title></head>"
                + "<body><p>Test</p><p>Text</p></body></html>";
        Document doc = Document.parse(html);
        doc.normalise();
        assertTrue(doc.outerHtml().startsWith("<html><head><title>Test Title</title></head><body>"));
    }

    @Test
    public void testNormalizeDocumentWithEmptyDocument() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        assertTrue(doc.outerHtml().isEmpty());
    }

    @Test
    public void testNormalizeDocumentWithEmptyHeadAndBody() {
        String html = "<html><head></head><body></body></html>";
        Document doc = Document.parse(html);
        doc.normalise();
        assertTrue(doc.outerHtml().startsWith("<html><head></head><body></body>"));
    }

    @Test
    public void testNormalizeDocumentWithTextNodesOutsideBody() {
        String html = "<html><head><title>Test Title</title></head>"
                + "<p>Test</p><html><body></body></html>";
        Document doc = Document.parse(html);
        doc.normalise();
        assertTrue(doc.outerHtml().startsWith("<html><head><title>Test Title</title></head><body>Test</body>"));
    }

    @Test
    public void testNormalizeDocumentWithTextNodesInRoot() {
        String html = "<html>Test<body></body></html>";
        Document doc = Document.parse(html);
        doc.normalise();
        assertTrue(doc.outerHtml().startsWith("<html><body>Test</body>"));
    }

    @Test
    public void testNormalizeDocumentWithTextNodesInHead() {
        String html = "<html><head>Test</head><body></body></html>";
        Document doc = Document.parse(html);
        doc.normalise();
        assertTrue(doc.outerHtml().startsWith("<html><head></head><body></body>"));
    }
}