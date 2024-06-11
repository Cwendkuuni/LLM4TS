package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
        assertTrue(doc.head() != null);
        assertTrue(doc.body() != null);
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("#head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element root = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.appendElement("title").text("Test Title");
        Element body = doc.appendElement("body");
        body.appendText("Test ");

        doc.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals("Test Title", title.text());
        assertEquals(1, bodyChildren.size());
        assertTrue(bodyChildren.get(0) instanceof TextNode);
        assertEquals("Test ", ((TextNode) bodyChildren.get(0)).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        doc.body().appendText("Test ");

        assertEquals("<html><head><title>Test Title</title></head><body>Test </body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.body().text("Test");

        assertEquals("Test", doc.text());
    }
}