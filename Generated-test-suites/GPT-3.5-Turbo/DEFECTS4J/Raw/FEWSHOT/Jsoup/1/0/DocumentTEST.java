package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.childNodes.get(0).nodeName());
        assertEquals("head", doc.childNodes.get(0).childNodes.get(0).nodeName());
        assertEquals("body", doc.childNodes.get(0).childNodes.get(1).nodeName());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("head");

        assertNotNull(head);
        assertEquals("head", doc.head().nodeName());
        assertEquals(head, doc.head());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");

        assertNotNull(body);
        assertEquals("body", doc.body().nodeName());
        assertEquals(body, doc.body());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title").text("Test Title");

        assertEquals("Test Title", doc.title());
        assertEquals(title, doc.getElementsByTag("title").first());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("head");

        doc.title("New Title");

        assertEquals("New Title", doc.title());
        assertNotNull(head.getElementsByTag("title").first());
        assertEquals("New Title", head.getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitle_existingTitle() {
        Document doc = new Document("http://example.com");
        Element head = doc.appendElement("head");
        Element title = head.appendElement("title").text("Old Title");

        doc.title("New Title");

        assertEquals("New Title", doc.title());
        assertEquals("New Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element div = body.appendElement("div").text("Test");

        doc.normalise();

        assertEquals("html", doc.childNodes.get(0).nodeName());
        assertEquals("head", doc.childNodes.get(0).childNodes.get(0).nodeName());
        assertEquals("body", doc.childNodes.get(0).childNodes.get(1).nodeName());
        assertEquals("div", doc.childNodes.get(0).childNodes.get(1).childNodes.get(1).nodeName());
        assertEquals("Test", doc.childNodes.get(0).childNodes.get(1).childNodes.get(1).childNodes.get(0).toString());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element body = html.appendElement("body");
        Element div = body.appendElement("div").text("Test");

        assertEquals("<html><body><div>Test</div></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");

        doc.text("Test");

        assertEquals("Test", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");

        assertEquals("#document", doc.nodeName());
    }
}