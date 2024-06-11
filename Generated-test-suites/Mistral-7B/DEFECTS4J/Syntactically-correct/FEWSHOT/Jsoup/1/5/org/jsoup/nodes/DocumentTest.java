package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        assertEquals("#document", doc.nodeName());
        assertEquals(3, doc.childElementCount());
        Element html = doc.select("html").first();
        assertEquals("html", html.tagName());
        Element head = html.getFirstElementSibling();
        assertEquals("head", head.tagName());
        Element body = html.nextElementSibling();
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadElement() {
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        assertNull(head.text());
    }

    @Test
    public void testBodyElement() {
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertNull(body.text());
    }

    @Test
    public void testTitleElement() {
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        Element title = doc.select("title").first();
        assertNotNull(title);
        assertEquals("title", title.tagName());
        assertEquals("Test Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Element link = doc.createElement("a");
        assertEquals("a", link.tagName());
        assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testNormalise() {
        doc.title("Test Title");
        doc.body().appendText("Test Body");
        doc.head().appendText("Test Head");
        doc.appendText("Test Root");

        doc.normalise();

        assertEquals("Test Title", doc.title());
        assertEquals("Test BodyTest Body", doc.body().text());
        assertNull(doc.head().text());
        assertNull(doc.text());
    }

    @Test
    public void testOuterHtml() {
        doc.title("Test Title");
        doc.body().appendText("Test Body");
        doc.head().appendText("Test Head");
        doc.appendText("Test Root");

        String html = doc.outerHtml();
        assertTrue(html.contains("<html><head><title>Test Title</title>"));
        assertTrue(html.contains("<body>Test Body</body>"));
        assertTrue(html.contains("</html>"));
    }

    @Test
    public void testText() {
        doc.title("Test Title");
        doc.body().appendText("Test Body");
        doc.head().appendText("Test Head");
        doc.appendText("Test Root");

        doc.text("Test Text");

        assertEquals("Test TextTest BodyTest Body", doc.outerHtml());
    }
}