package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertNull(doc.body());
        assertNull(doc.head());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitleTwice() {
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
        assertEquals("p", p.nodeName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.head().appendElement("title").text("Test Title");
        doc.body().appendElement("p").text("Test Paragraph");
        doc.appendElement("div").text("Test Div");

        doc.normalise();

        Element title = doc.getElementsByTag("title").first();
        assertSame(title, doc.head().getElementsByTag("title").first());
        assertEquals("Test Title", title.text());

        Element body = doc.body();
        List<Node> bodyChildren = new ArrayList<>();
        for (Node child : body.childNodes()) {
            bodyChildren.add(child);
        }
        assertEquals(2, bodyChildren.size());
        assertEquals("p", bodyChildren.get(0).nodeName());
        assertEquals("Test Paragraph", bodyChildren.get(0).text());
        assertEquals("TextNode", bodyChildren.get(1).nodeName());
        assertEquals("", bodyChildren.get(1).text());

        Element div = doc.getElementsByTag("div").first();
        assertNull(div);
    }

    @Test
    public void testNormaliseNoRootElements() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        doc.title("Test Title 2");

        doc.normalise();

        Element title = doc.getElementsByTag("title").first();
        assertSame(title, doc.head().getElementsByTag("title").first());
        assertEquals("Test Title 2", title.text());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.head().appendElement("title").text("Test Title");
        doc.body().appendElement("p").text("Test Paragraph");

        String html = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", html);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.body().appendElement("p").text("Test Paragraph");

        doc.text("Test Text");

        assertEquals("Test Text", doc.outerHtml());
    }
}