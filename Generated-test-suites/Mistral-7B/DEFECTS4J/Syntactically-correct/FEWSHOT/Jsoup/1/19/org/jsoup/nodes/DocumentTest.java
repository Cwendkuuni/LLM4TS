package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
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
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        assertEquals("#html", html.nodeName());
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        assertEquals("#head", head.nodeName());
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals("#body", body.nodeName());
        assertEquals("", doc.title());
    }

    @Test
    public void testHeadElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertEquals("#head", head.nodeName());
        Element title = head.getElementsByTag("title").first();
        assertNull(title);
        doc.title("Test Title");
        title = head.getElementsByTag("title").first();
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testBodyElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertEquals("#body", body.nodeName());
        assertEquals("", body.text().trim());
        doc.text("Test Body");
        assertEquals("Test Body", body.text().trim());
    }

    @Test
    public void testTitleElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element link = doc.createElement("a");
        assertEquals("#a", link.nodeName());
        assertEquals(baseUri, link.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.head().appendText("Test Head Text");
        doc.body().appendText("Test Body Text 1");
        doc.body().appendElement(new TextNode("Test Body Text 2 "));
        doc.normalise();
        Element body = doc.body();
        assertEquals("Test Body Text 1Test Body Text 2 ", body.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Document doc = new Document(null);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.appendElement("html").appendElement("head").appendElement("body").text("Test");
        assertEquals("<html><head></head><body>Test</body></html>", doc.outerHtml());
    }

    @Test
    public void testTextElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.body().text("Test Body Text");
        assertEquals("Test Body Text", doc.body().text().trim());
        doc.text("Test");
        assertEquals("Test", doc.text().trim());
    }
}