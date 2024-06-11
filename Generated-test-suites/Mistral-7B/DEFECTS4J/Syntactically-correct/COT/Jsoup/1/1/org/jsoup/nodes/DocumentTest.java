package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        Element html = doc.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHeadGetter() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testHeadSetter() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Element head = doc.head();
        Element title = head.getElementsByTag("title").first();
        assertNotNull(title);
        assertEquals("Test Title", title.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleSetterNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleSetterEmptyTitle() {
        Document doc = new Document("http://example.com");
        doc.title("");
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.head().appendText("Test Heading");
        doc.body().appendText("Test Content");
        doc.body().appendElement("p").text("Another Paragraph");
        doc.title("Test Title");

        doc.normalise();

        Element body = doc.body();
        assertEquals("Test ContentTest HeadingAnother Paragraph", body.text());
        assertNull(doc.head().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseNullElement() {
        Document doc = new Document("http://example.com");
        doc.normalise(null);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.head().appendText("Test Heading");
        doc.body().appendText("Test Content");
        doc.title("Test Title");

        assertEquals("<html><head><title>Test Title</title></head><body>Test Content</body></html>", doc.outerHtml());
    }

    @Test
    public void testTextSetterNullText() {
        Document doc = new Document("http://example.com");
        assertThrows(NullPointerException.class, () -> doc.text(null));
    }

    @Test
    public void testTextSetterEmptyText() {
        Document doc = new Document("http://example.com");
        doc.text("");
        assertEquals("", doc.outerHtml());
    }
}