package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element pElement = doc.createElement("p");
        assertEquals("p", pElement.tagName());
        assertEquals(baseUri, pElement.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.head().appendElement("title").text("Test Title");
        doc.body().appendElement("p").text("Test Paragraph");
        doc.appendElement("div").text("Test Div");

        doc.normalise();

        List<Element> bodyChildren = doc.body().children();
        assertEquals(2, bodyChildren.size());
        assertEquals("p", bodyChildren.get(0).tagName());
        assertEquals("Test Paragraph", bodyChildren.get(0).text());
        assertEquals("div", bodyChildren.get(1).tagName());
        assertNull(doc.getElementsByTag("title").first());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.head().appendElement("title").text("Test Title");
        doc.body().appendElement("p").text("Test Paragraph");

        assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testTextDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.body().text("Test Paragraph");

        assertEquals("Test Paragraph", doc.text().trim());
    }

    @Test
    public void testNodeName() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
    }
}