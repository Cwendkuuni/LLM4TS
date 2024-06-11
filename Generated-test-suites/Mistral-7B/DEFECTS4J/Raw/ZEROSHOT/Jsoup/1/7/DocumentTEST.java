package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 *
 * @author [Your Name]
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        Element html = document.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testGetHeadElement() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        Element head = document.head();
        assertNotNull(head);
        Element title = head.getElementsByTag("title").first();
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testGetBodyElement() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        Element body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        Element newElement = document.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title><body><div>Text 1</div></html>", "http://example.com");
        document.normalise();
        List<Element> bodyElements = document.select("body > *");
        assertEquals(1, bodyElements.size());
        Element bodyElement = bodyElements.get(0);
        List<Node> textNodes = bodyElement.childNodes();
        assertEquals(2, textNodes.size());
        TextNode firstTextNode = (TextNode) textNodes.get(0);
        assertEquals("Text 1", firstTextNode.text());
        TextNode secondTextNode = (TextNode) textNodes.get(1);
        assertEquals("", secondTextNode.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Jsoup.parse("<html><head></head><body><div>Text 1</div></body></html>", "http://example.com");
        assertEquals("<html><head></head><body><div>Text 1</div></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
}