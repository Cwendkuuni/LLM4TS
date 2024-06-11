package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.head() == null);
        assertTrue(doc.body() == null);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertTrue(doc.head().nodeName().equals("#head"));
        assertTrue(doc.body().nodeName().equals("#body"));
    }

    @Test
    public void testGetAndSetTitle() {
        Document doc = new Document("http://example.com");
        Element title = doc.head().appendElement("title");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", title.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element link = doc.createElement("a");
        assertEquals("#element-a", link.nodeName());
        assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title", head);
        title.text("Test Title");
        TextNode textNode = doc.appendTextNode("Test Text", body);

        doc.normalise();

        assertEquals("Test Title", doc.title());
        assertEquals("Test Text", body.text().trim());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element headElement = doc.findFirstElementByTagName("head", doc);
        assertEquals(head, headElement);

        Element bodyElement = doc.findFirstElementByTagName("body", doc);
        assertEquals(body, bodyElement);

        Element nonExistentElement = doc.findFirstElementByTagName("nonExistent", doc);
        assertNull(nonExistentElement);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        head.appendElement("title").text("Test Title");
        body.appendElement("p").text("Test Paragraph");

        doc.head().appendElement("meta").attr("charset", "UTF-8");

        String expected = "<html><head><meta charset=\"UTF-8\"><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");
        body.text("Test Text");

        String expected = "Test Text";
        assertEquals(expected, doc.text().toString());
    }
}