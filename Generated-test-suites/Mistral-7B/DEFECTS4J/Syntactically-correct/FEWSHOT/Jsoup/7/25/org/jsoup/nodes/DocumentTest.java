package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.List;

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
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        assertEquals("#html", html.nodeName());
        assertEquals(baseUri, html.baseUri());
        assertNull(html.getElementsByTag("head").first());
        assertNull(html.getElementsByTag("body").first());

        html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        assertEquals("#html", html.nodeName());
        assertEquals(baseUri, html.baseUri());
        assertNotNull(html.getElementsByTag("head").first());
        assertNotNull(html.getElementsByTag("body").first());
    }

    @Test
    public void testHead() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.head();
        assertNull(doc.head());

        doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertEquals("#head", head.nodeName());
        assertEquals(baseUri, head.baseUri());
    }

    @Test
    public void testBody() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.body();
        assertNull(doc.body());

        doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertEquals("#body", body.nodeName());
        assertEquals(baseUri, body.baseUri());
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("", doc.title());

        doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        Element titleEl = doc.getElementsByTag("title").first();
        assertEquals("Test Title", titleEl.text());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element elem = doc.createElement("a");
        assertEquals("#document", elem.parentNode().nodeName());
        assertEquals(baseUri, elem.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.body().appendText(" Text before normalise");
        doc.head().appendText(" Text before normalise");
        doc.text(" Text before normalise");
        doc.normalise();

        Element body = doc.body();
        TextNode textNode1 = (TextNode) body.getElementsByTag("text").first();
        assertEquals(" Text before normalise", textNode1.text());

        TextNode textNode2 = (TextNode) body.getElementsByTag("text").first();
        assertEquals("", textNode2.previousSibling().nodeName());
        assertEquals(" Text before normalise", textNode2.text());

        TextNode textNode3 = (TextNode) doc.getElementsByTag("text").first();
        assertNull(textNode3.previousSibling());
        assertEquals(" Text before normalise", textNode3.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");

        Element found = Document.findFirstElementByTagName("html", doc);
        assertEquals(html, found);

        found = Document.findFirstElementByTagName("head", body);
        assertNull(found);

        found = Document.findFirstElementByTagName("title", head);
        assertEquals(title, found);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.head().appendElement("meta").text("Test Meta Tag");
        doc.title("Test Title");
        doc.body().appendElement("p").text("Test Paragraph");

        String html = doc.outerHtml();
        assertEquals("<html lang=\"en\"><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<meta charset=\"UTF-8\"><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", html);
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }
}