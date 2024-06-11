package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        assertTrue(doc.head() != null);
        assertTrue(doc.body() != null);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testHeadMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleMethodWithNoTitle() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleSetter() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        doc.title("Test New Title");
        String title = doc.title();
        assertEquals("Test New Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleSetterNullTitle() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        doc.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.tagName());
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Text 1</p><head><title>Test New Title</title></head></body></html>", "http://example.com");
        doc.normalise();
        Element body = doc.body();
        List<Node> bodyChildren = body.children();
        assertFalse(bodyChildren.isEmpty());
        TextNode textNode1 = (TextNode) bodyChildren.get(0);
        assertEquals("Text 1", textNode1.text());
        Element titleElement = doc.getElementsByTag("title").first();
        assertEquals("Test New Title", titleElement.text());
    }

    @Test
    public void testOuterHtmlMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        String html = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body></body></html>", html);
    }

    @Test
    public void testTextMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        doc.text("Test Text");
        String bodyText = doc.body().text();
        assertEquals("Test Text", bodyText);
    }

}