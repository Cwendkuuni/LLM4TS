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

/**
 * Test class for Document class.
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        Element html = document.select("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("http://example.com");
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleMethodNullArgument() {
        Document document = new Document("http://example.com");
        try {
            document.title(null);
            fail("Expected ValidateNestedException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getCause() instanceof ValidateNestedException);
        }
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("a");
        assertNotNull(element);
    }

    @Test
    public void testNormaliseMethod() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head></html>");
        document.title("Test2");
        document.appendElement("body").text("Body Text");
        document.normalise();
        Element body = document.body();
        TextNode titleNode = document.getElementsByTag("title").first().child(0);
        TextNode bodyTextNode = body.child(0);
        assertEquals("Test2", titleNode.text());
        assertEquals("Body Text", bodyTextNode.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testNormaliseMethodWithNoRootElement() {
        Document document = new Document("http://example.com");
        document.getElementsByTag("html").remove(0);
        document.normalise();
    }

    @Test
    public void testNormaliseMethodWithEmptyDocument() {
        Document document = new Document("http://example.com");
        document.normalise();
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        String html = document.outerHtml();
        assertEquals("<html><head><title>Test</title></head><body></body></html>", html);
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("http://example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

}