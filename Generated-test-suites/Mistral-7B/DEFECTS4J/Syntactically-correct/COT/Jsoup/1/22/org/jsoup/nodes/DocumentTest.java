package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link Document} class
 */
public class DocumentTest {

    @Test
    public void testDocumentConstruction() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
        assertEquals("baseUri", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertNotNull(document);
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTitleWithNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @Test
    public void testDocumentTitleGetAndSet() {
        Document document = new Document("baseUri");
        String title = "title";
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        assertNotNull(element);
        assertEquals("tagName", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testDocumentNormalise() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head></html>");
        document.normalise();
        List<Element> bodyElements = document.getElementsByTag("body").first().children();
        assertTrue(bodyElements.isEmpty());
    }

    @Test
    public void testDocumentNormaliseWithTextNodes() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body>Test</body></html>");
        document.normalise();
        List<Element> bodyElements = document.getElementsByTag("body").first().children();
        Element textElement = bodyElements.get(0);
        assertEquals("Test", textElement.text());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentNormaliseWithNullElement() {
        Document document = new Document("baseUri");
        document.normalise(null);
    }

    @Test
    public void testDocumentTitleWithEmptyString() {
        Document document = new Document("baseUri");
        document.title("");
        assertEquals("", document.title());
    }

    @Test
    public void testDocumentOuterHtml() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body>Test</body></html>");
        assertEquals("<html><head><title>Test</title></head><body>Test</body></html>", document.outerHtml());
    }

    @Test
    public void testDocumentText() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body>Test</body></html>");
        document.text("NewText");
        assertEquals("NewText", document.body().text());
    }

    @Test
    public void testDocumentNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }
}