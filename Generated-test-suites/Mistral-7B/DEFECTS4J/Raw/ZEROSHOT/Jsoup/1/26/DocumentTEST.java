package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for Document.java
 *
 * @author John Doe
 */
public class DocumentTest {

    @Test
    public void testCreateNewDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
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
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testGetBodyElement() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertTrue(body.children().isEmpty());
    }

    @Test
    public void testGetEmptyTitle() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        assertEquals("", document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullTitle() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        document.title(null);
    }

    @Test
    public void testSetTitle() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title>Test</html>", "http://example.com");
        document.normalise();
        Element body = document.body();
        List<Node> bodyChildren = body.children();
        assertEquals(2, bodyChildren.size());
        assertTrue(bodyChildren.get(0) instanceof TextNode);
        TextNode titleNode = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleNode.text());
    }

    @Test
    public void testCreateDocumentWithEmptyHtmlTag() {
        Document document = Jsoup.parse("<html></html>", "http://example.com");
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateDocumentWithEmptyHeadAndBody() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
    }
}