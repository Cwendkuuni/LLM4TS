package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test file for Document class.
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testDocumentTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testDocumentTitleNull() {
        Document doc = new Document("http://example.com");
        assertNull(doc.title());
    }

    @Test
    public void testDocumentTitleSetTwice() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
    }

    @Test
    public void testDocumentNormalize() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        doc.normalise();
        Element body = doc.body();
        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testDocumentOuterHtml() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        assertEquals("<html><head></head><body><p>Test</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testDocumentText() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        doc.text("New Text");
        assertEquals("New Text", doc.body().text());
    }
}