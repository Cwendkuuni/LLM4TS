package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        assertEquals(2, doc.head().siblingIndex());
        assertEquals(3, doc.body().siblingIndex());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);
        doc.head().appendElement("head");
        head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element title = head.appendElement("title");
        title.text("Title");
        assertEquals("Title", doc.title());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);
        doc.body().appendElement("body");
        body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.title());
        doc.title("Title");
        assertEquals("Title", doc.title());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
        Element div = doc.body().appendElement("div");
        div.text("Text");
        doc.normalise();
        assertEquals(1, doc.body().childNodeSize());
        assertEquals(" ", doc.body().childNode(0).outerHtml());
        assertEquals("Text", doc.body().childNode(1).outerHtml());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());
        Element html = doc.appendElement("html");
        html.appendElement("body");
        assertEquals("<html>\n <body></body>\n</html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Text");
        assertEquals("Text", doc.body().text());
    }
}